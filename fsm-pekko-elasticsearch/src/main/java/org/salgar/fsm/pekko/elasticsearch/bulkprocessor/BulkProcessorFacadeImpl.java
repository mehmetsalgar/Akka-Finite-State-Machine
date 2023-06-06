package org.salgar.fsm.pekko.elasticsearch.bulkprocessor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.VersionType;
import org.salgar.fsm.pekko.elasticsearch.OffsetFacade;
import org.salgar.fsm.pekko.elasticsearch.OffsetState;
import org.salgar.fsm.pekko.elasticsearch.OffsetStateImpl;
import org.salgar.fsm.pekko.elasticsearch.config.BehaviorOnMalformedDoc;
import org.salgar.fsm.pekko.elasticsearch.config.ElasticsearchProperties;
import org.salgar.fsm.pekko.elasticsearch.model.ElasticsearchOffset;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
@Slf4j
public class BulkProcessorFacadeImpl implements BulkProcessorFacade {
    private static final long CLOSE_WAIT_TIME_MS = 5_000;
    private static final long MAX_RETRY_TIME_MS = TimeUnit.HOURS.toMillis(24);
    private final ElasticsearchProperties elasticsearchProperties;
    private final RestHighLevelClient client;
    private final ExecutorService bulkExecutorService;
    private final OffsetFacade offsetFacade;

    private final ConcurrentMap<DocWriteRequest<?>, SinkRecordAndOffset> requestToSinkRecord = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, List<SinkRecordAndOffset>> inFlightRequests =  new ConcurrentHashMap<>();
    //private final Runnable afterBulkCallback = () -> log.info("handle offsets");
    private final Lock inFlightRequestLock = new ReentrantLock();
    private final Condition inFlightRequestsUpdated = inFlightRequestLock.newCondition();
    protected final AtomicInteger numBufferedRecords = new AtomicInteger(0);
    private final AtomicReference<IllegalStateException> error = new AtomicReference<>();

    private static final String VERSION_CONFLICT_EXCEPTION = "version_conflict_engine_exception";
    private static final String BEHAVIOR_ON_MALFORMED_DOCS_CONFIG = "behavior.on.malformed.documents";
    private static final Set<String> MALFORMED_DOC_ERRORS = new HashSet<>(
            Arrays.asList(
                    "strict_dynamic_mapping_exception",
                    "mapper_parsing_exception",
                    "illegal_argument_exception",
                    "action_request_validation_exception"
            )
    );

    private BulkProcessor bulkProcessor;

    @Override
    public void index(String offset,
                      String persistenceId,
                      Long sequenceNr,
                      String persistenceKey,
                      String persistenceName,
                      IndexRequest indexRequest) {
        requestToSinkRecord.put(
                indexRequest,
                new SinkRecordAndOffset(
                        offset,
                        persistenceId,
                        sequenceNr,
                        persistenceKey,
                        persistenceName,
                        new OffsetStateImpl(offset)));
        numBufferedRecords.incrementAndGet();
        this.bulkProcessor.add(indexRequest);
    }

    @PostConstruct
    private void preparebulkProcessor() {
        this.bulkProcessor = BulkProcessor
                .builder(buildConsumer(), buildListener(), "fsm-pekko-bulkprocessor")
                .setBulkActions(elasticsearchProperties.getBatchSize())
                .setBulkSize(new ByteSizeValue(elasticsearchProperties.getBulkSize(), ByteSizeUnit.KB))
                .setConcurrentRequests(elasticsearchProperties.getMaxInFlightRequests() - 1) // 0 = no concurrent requests
                .setFlushInterval(TimeValue.timeValueMillis(elasticsearchProperties.getLingerMs()))
                // (see https://github.com/elastic/elasticsearch/issues/71159)
                .setBackoffPolicy(BackoffPolicy.noBackoff())
                .build();
    }

    private BulkProcessor.Listener buildListener() {
        return new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                if (inFlightRequests != null) {
                    List<SinkRecordAndOffset> sinkRecords = request.requests().stream()
                            .map(requestToSinkRecord::get)
                            .collect(toList());

                    inFlightRequests.put(executionId, sinkRecords);
                }
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                List<DocWriteRequest<?>> requests = request.requests();

                int idx = 0;
                for (BulkItemResponse bulkItemResponse : response) {
                    DocWriteRequest<?> req = idx < requests.size() ? requests.get(idx) : null;
                    boolean failed = handleResponse(bulkItemResponse, req, executionId);
                    if (!failed && req != null) {
                        requestToSinkRecord.get(req).offsetState.markProcessed();
                        SinkRecordAndOffset sinkRecordAndOffset = requestToSinkRecord.get(req);
                        log.info("Bulkprocessor handle the offset");
                        offsetFacade.saveOffset(
                                sinkRecordAndOffset.getProjectionName(),
                                sinkRecordAndOffset.projectionKey,
                                new ElasticsearchOffset(
                                        sinkRecordAndOffset.getProjectionName()
                                                + "_" + sinkRecordAndOffset.projectionKey,
                                        sinkRecordAndOffset.getProjectionName(),
                                        sinkRecordAndOffset.projectionKey,
                                        sinkRecordAndOffset.getOffset()
                                ));
                    }
                    idx++;
                }


                //afterBulkCallback.run();

                bulkFinished(executionId, request);
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                log.warn("Bulk request {} failed", executionId, failure);
                error.compareAndSet(null, new IllegalStateException("Bulk request failed", failure));
                bulkFinished(executionId, request);
            }

            private void bulkFinished(long executionId, BulkRequest request) {
                request.requests().forEach(requestToSinkRecord::remove);
                removeFromInFlightRequests(executionId);
                inFlightRequestLock.lock();
                try {
                    numBufferedRecords.addAndGet(-request.requests().size());
                    inFlightRequestsUpdated.signalAll();
                } finally {
                    inFlightRequestLock.unlock();
                }
            }
        };
    }

    private BiConsumer<BulkRequest, ActionListener<BulkResponse>> buildConsumer() {
        return (req, lis) ->
                // Executes a synchronous bulk request in a background thread, with synchronous retries.
                // We don't use bulkAsync because we can't retry from its callback (see
                // https://github.com/confluentinc/kafka-connect-elasticsearch/pull/575)
                // BulkProcessor is the one guaranteeing that no more than maxInFlightRequests batches
                // are started at the same time (a new consumer is not called until all others are finished),
                // which means we don't need to limit the executor pending task queue.

                // Result is ignored because everything is reported via the corresponding ActionListener.
                bulkExecutorService.submit(() -> {
                    try {
                        BulkResponse bulkResponse = callWithRetries(
                                "execute bulk request",
                                () -> client.bulk(req, RequestOptions.DEFAULT)
                        );
                        lis.onResponse(bulkResponse);
                    } catch (Exception ex) {
                        lis.onFailure(ex);
                    } catch (Throwable ex) {
                        lis.onFailure(new IllegalStateException("Bulk request failed", ex));
                    }
                });
    }

    private <T> T callWithRetries(String description, Callable<T> function) {
        return callWithRetries(
                description,
                function,
                elasticsearchProperties.getMaxRetries() + 1,
                elasticsearchProperties.getRetryBackoffMs()
        );
    }

    /**
     * Call the supplied function up to the {@code maxTotalAttempts}.
     *
     * <p>The description of the function should be a succinct, human-readable present tense phrase
     * that summarizes the function, such as "read tables" or "connect to database" or
     * "make remote request". This description will be used within exception and log messages.
     *
     * @param description      present tense description of the action, used to create the error
     *                         message; may not be null
     * @param function         the function to call; may not be null
     * @param maxTotalAttempts maximum number of attempts
     * @param initialBackoff   the initial backoff in ms before retrying
     * @param <T>              the return type of the function to retry
     * @return the function's return value
     * @throws IllegalStateException        if the function failed after retries
     */
    @SneakyThrows
    protected static <T> T callWithRetries(
            String description,
            Callable<T> function,
            int maxTotalAttempts,
            long initialBackoff
    ) {
        assert description != null;
        assert function != null;
        int attempt = 0;
        while (true) {
            ++attempt;
            try {
                log.trace(
                        "Try {} (attempt {} of {})",
                        description,
                        attempt,
                        maxTotalAttempts
                );
                T call = function.call();
                return call;
            } catch (Exception e) {
                if (attempt >= maxTotalAttempts) {
                    String msg = String.format("Failed to %s due to '%s' after %d attempt(s)",
                            description, e, attempt);
                    log.error(msg, e);
                    throw new IllegalStateException(msg, e);
                }

                // Otherwise it is retriable and we should retry
                long backoff = computeRandomRetryWaitTimeInMillis(attempt, initialBackoff);

                log.warn("Failed to {} due to {}. Retrying attempt ({}/{}) after backoff of {} ms",
                        description, e.getCause(), attempt, maxTotalAttempts, backoff);
                Thread.sleep(backoff);
            }
        }
    }

    public static long computeRandomRetryWaitTimeInMillis(int retryAttempts,
                                                          long initialRetryBackoffMs) {
        if (initialRetryBackoffMs < 0) {
            return 0;
        }
        if (retryAttempts < 0) {
            return initialRetryBackoffMs;
        }
        long maxRetryTime = computeRetryWaitTimeInMillis(retryAttempts, initialRetryBackoffMs);
        return ThreadLocalRandom.current().nextLong(0, maxRetryTime);
    }

    public static long computeRetryWaitTimeInMillis(int retryAttempts,
                                                    long initialRetryBackoffMs) {
        if (initialRetryBackoffMs < 0) {
            return 0;
        }
        if (retryAttempts <= 0) {
            return initialRetryBackoffMs;
        }
        if (retryAttempts > 32) {
            // This would overflow the exponential algorithm ...
            return MAX_RETRY_TIME_MS;
        }
        long result = initialRetryBackoffMs << retryAttempts;
        return result < 0L ? MAX_RETRY_TIME_MS : Math.min(MAX_RETRY_TIME_MS, result);
    }

    protected boolean handleResponse(BulkItemResponse response,
                                     DocWriteRequest<?> request,
                                     long executionId) {
        if (response.isFailed()) {
            for (String error : MALFORMED_DOC_ERRORS) {
                if (response.getFailureMessage().contains(error)) {
                    boolean failed = handleMalformedDocResponse(response);
                    if (!failed) {
                        reportBadRecord(response, executionId);
                    }
                    return failed;
                }
            }
            if (response.getFailureMessage().contains(VERSION_CONFLICT_EXCEPTION)) {
                // Now check if this version conflict is caused by external version number
                // which was set by us (set explicitly to the topic's offset), in which case
                // the version conflict is due to a repeated or out-of-order message offset
                // and thus can be ignored, since the newer value (higher offset) should
                // remain the key's value in any case.
                if (request == null || request.versionType() != VersionType.EXTERNAL) {
                    log.warn("{} version conflict for operation {} on document '{}' version {}"
                                    + " in index '{}'.",
                            request != null ? request.versionType() : "UNKNOWN",
                            response.getOpType(),
                            response.getId(),
                            response.getVersion(),
                            response.getIndex()
                    );
                    // Maybe this was a race condition?  Put it in the DLQ in case someone
                    // wishes to investigate.
                    reportBadRecord(response, executionId);
                } else {
                    // This is an out-of-order or (more likely) repeated topic offset.  Allow the
                    // higher offset's value for this key to remain.
                    //
                    // Note: For external version conflicts, response.getVersion() will be returned as -1,
                    // but we have the actual version number for this record because we set it in
                    // the request.
                    log.debug("Ignoring EXTERNAL version conflict for operation {} on"
                                    + " document '{}' version {} in index '{}'.",
                            response.getOpType(),
                            response.getId(),
                            request.version(),
                            response.getIndex()
                    );
                }
                return false;
            }

            error.compareAndSet(
                    null,
                    new IllegalStateException("Indexing record failed.", response.getFailure().getCause())
            );
            return true;
        }
        return false;
    }

    private boolean handleMalformedDocResponse(BulkItemResponse response) {
        String errorMsg = String.format(
                "Encountered an illegal document error '%s'. Ignoring and will not index record.",
                response.getFailureMessage()
        );
        switch (elasticsearchProperties.getBehaviorOnMalformedDoc()) {
            case IGNORE:
                log.debug(errorMsg);
                return false;
            case WARN:
                log.warn(errorMsg);
                return false;
            case FAIL:
            default:
                log.error(
                        "Encountered an illegal document error '{}'. To ignore future records like this,"
                                + " change the configuration '{}' to '{}'.",
                        response.getFailureMessage(),
                        BEHAVIOR_ON_MALFORMED_DOCS_CONFIG,
                        BehaviorOnMalformedDoc.IGNORE
                );
                error.compareAndSet(
                        null,
                        new IllegalStateException("Indexing record failed.", response.getFailure().getCause())
                );
                return true;
        }
    }

    private synchronized void reportBadRecord(BulkItemResponse response,
                                              long executionId) {
        /*if (reporter != null) {
            List<SinkRecordAndOffset> sinkRecords =
                    inFlightRequests.getOrDefault(executionId, new ArrayList<>());
            SinkRecordAndOffset original = sinkRecords.size() > response.getItemId()
                    ? sinkRecords.get(response.getItemId())
                    : null;
            if (original != null) {
                reporter.report(
                        original.sinkRecord,
                        new IllegalStateException("Indexing failed: " + response.getFailureMessage())
                );
            }
        }*/
    }

    /**
     * Removes the mapping for bulk request id to records being written.
     *
     * @param executionId the execution id of the bulk request
     */
    private void removeFromInFlightRequests(long executionId) {
        if (inFlightRequests != null) {
            inFlightRequests.remove(executionId);
        }
    }

    @Getter
    private static class SinkRecordAndOffset {
        private final String offset;
        private final String persistenceId;
        private final Long sequenceNr;
        private final String projectionName;
        private final String projectionKey;
        private final OffsetState offsetState;

        public SinkRecordAndOffset(String offset,
                                   String persistenceId,
                                   Long sequenceNr,
                                   String projectionName,
                                   String  projectionKey,
                                   OffsetState offsetState) {
            this.offset = offset;
            this.persistenceId = persistenceId;
            this.sequenceNr = sequenceNr;
            this.projectionName = projectionName;
            this.projectionKey = projectionKey;
            this.offsetState = offsetState;
        }
    }

    public void closeResources() {
        bulkExecutorService.shutdown();
        try {
            if (!bulkExecutorService.awaitTermination(CLOSE_WAIT_TIME_MS, TimeUnit.MILLISECONDS)) {
                bulkExecutorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            bulkExecutorService.shutdownNow();
            Thread.currentThread().interrupt();
            log.warn("Interrupted while awaiting for executor service shutdown.", e);
        }
        try {
            client.close();
        } catch (IOException e) {
            log.warn("Failed to close Elasticsearch client.", e);
        }
    }

    @PreDestroy
    private void close() {
        try {
            if (!bulkProcessor.awaitClose(elasticsearchProperties.getFlushTimeoutMs(), TimeUnit.MILLISECONDS)) {
                throw new IllegalStateException(
                        "Failed to process outstanding requests in time while closing the ElasticsearchClient."
                );
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                    "Interrupted while processing all in-flight requests on ElasticsearchClient close.", e
            );
        } finally {
            closeResources();
        }
    }
}
package org.salgar.fsm.pekko.elasticsearch.bulkprocessor;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._helpers.bulk.BulkIngester;
import co.elastic.clients.elasticsearch._helpers.bulk.BulkListener;
import co.elastic.clients.elasticsearch._types.VersionType;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.transport.BackoffPolicy;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.salgar.fsm.pekko.elasticsearch.OffsetFacade;
import org.salgar.fsm.pekko.elasticsearch.OffsetState;
import org.salgar.fsm.pekko.elasticsearch.OffsetStateImpl;
import org.salgar.fsm.pekko.elasticsearch.config.BehaviorOnMalformedDoc;
import org.salgar.fsm.pekko.elasticsearch.config.ElasticsearchProperties;
import org.salgar.fsm.pekko.elasticsearch.model.ElasticsearchOffset;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
@Slf4j
public class BulkProcessorFacadeImpl implements BulkProcessorFacade {
    private static final long CLOSE_WAIT_TIME_MS = 5_000;
    private static final long MAX_RETRY_TIME_MS = TimeUnit.HOURS.toMillis(24);
    private final ElasticsearchClient elasticsearchClient;
    private final ElasticsearchProperties elasticsearchProperties;
    private final OffsetFacade offsetFacade;
    private ScheduledExecutorService scheduler;
    private ExecutorService retryExecutor;


    private final ConcurrentMap<String, SinkRecordAndOffset> requestToSinkRecord = new ConcurrentHashMap<>();
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

    private BulkIngester<RetryContext<?>> ingester;

    record SinkRecordAndOffset(String offset, String persistenceId, Long sequenceNr, String projectionName,
                               String projectionKey, OffsetState offsetState) {
    }

    private static class RetryContext<T> {
        final String index;
        final String id;
        final T doc;
        final int attempts;

        RetryContext(String index, String id, T doc, int attempts) {
            this.index = index;
            this.id = id;
            this.doc = doc;
            this.attempts = attempts;
        }
    }

    @Override
    public void index(String offset,
                      String persistenceId,
                      Long sequenceNr,
                      String persistenceKey,
                      String persistenceName,
                      String id,
                      String index,
                      Object document) {
        requestToSinkRecord.put(
                id,
                new SinkRecordAndOffset(
                        offset,
                        persistenceId,
                        sequenceNr,
                        persistenceKey,
                        persistenceName,
                        new OffsetStateImpl(offset)));
        this.ingester.add(
                BulkOperation.of(
                        b ->
                                b.index(
                                        i -> i
                                                .index(index)
                                                .id(id)
                                                .document(document)
                                )
                ),
                new RetryContext<>(index, id, document, 1)
        );
    }

    @PostConstruct
    private void prepareBulkProcessor() {
        // Thread pools
        this.scheduler = Executors
                .newScheduledThreadPool(
                        1,
                        r -> new Thread(
                                r,
                                "bulk-retry-scheduler"));
        this.retryExecutor = Executors
                .newFixedThreadPool(
                        Runtime.getRuntime().availableProcessors(),
                        r -> new Thread(
                                r,
                                "bulk-retry-executor"));

        this.ingester = BulkIngester.of(b -> b
                .client(this.elasticsearchClient)
                .maxOperations(elasticsearchProperties.getBatchSize()) // batch size
                .maxSize(elasticsearchProperties.getBulkSize()) // 1 MB
                .flushInterval(elasticsearchProperties.getLingerMs(), TimeUnit.MILLISECONDS)
                .maxConcurrentRequests(elasticsearchProperties.getMaxInFlightRequests() - 1)
                .backoffPolicy(BackoffPolicy.exponentialBackoff())
                .listener(new BulkListener<RetryContext<?>>() {
                    @Override
                    public void beforeBulk(long executionId, BulkRequest request, List<RetryContext<?>> retryContexts) {
                        log.debug("Executing bulk of {} requests", request.operations().size());
                    }

                    @Override
                    public void afterBulk(long executionId, BulkRequest request, List<RetryContext<?>> retryContexts, BulkResponse response) {
                        List<BulkOperation> requests = request.operations();

                        int idx = 0;
                        for (BulkResponseItem bulkItemResponse : response.items()) {
                            BulkOperation req = idx < requests.size() ? requests.get(idx) : null;
                            boolean failed = handleResponse(bulkItemResponse, req, executionId);
                            if (!failed && req != null) {
                                SinkRecordAndOffset sinkRecordAndOffset = requestToSinkRecord.get(bulkItemResponse.id());
                                sinkRecordAndOffset.offsetState.markProcessed();
                                log.info("BulkProcessor handle the offset");
                                offsetFacade.saveOffset(
                                        sinkRecordAndOffset.projectionName(),
                                        sinkRecordAndOffset.projectionKey,
                                        new ElasticsearchOffset(
                                                sinkRecordAndOffset.projectionName()
                                                        + "_" + sinkRecordAndOffset.projectionKey,
                                                sinkRecordAndOffset.projectionName(),
                                                sinkRecordAndOffset.projectionKey,
                                                sinkRecordAndOffset.offset()
                                        ));
                            }
                            idx++;
                        }


                        if (response.errors()) {
                            for (int i = 0; i < response.items().size(); i++) {
                                var item = response.items().get(i);
                                if (item.error() != null) {
                                    scheduleRetry(retryContexts.get(i));
                                }
                            }
                        }
                    }

                    @Override
                    public void afterBulk(long executionId, BulkRequest request, List<RetryContext<?>> retryContexts, Throwable failure) {
                        log.warn("Bulk request {} failed", executionId, failure);
                        error.compareAndSet(null, new IllegalStateException("Bulk request failed", failure));
                        // retry all items
                        retryContexts.forEach(ctx -> scheduleRetry(ctx));
                    }
                })
        );
    }

    protected boolean handleResponse(BulkResponseItem response,
                                     BulkOperation request,
                                     long executionId) {
        if (response.error() != null) {
            for (String error : MALFORMED_DOC_ERRORS) {
                if (response.error().reason().contains(error)) {
                    boolean failed = handleMalformedDocResponse(response);
                    if (!failed) {
                        reportBadRecord(response, executionId);
                    }
                    return failed;
                }
            }
            if (response.error().reason().contains(VERSION_CONFLICT_EXCEPTION)) {
                // Now check if this version conflict is caused by external version number
                // which was set by us (set explicitly to the topic's offset), in which case
                // the version conflict is due to a repeated or out-of-order message offset
                // and thus can be ignored, since the newer value (higher offset) should
                // remain the key's value in any case.
                if (request == null || request.index().versionType() != VersionType.External) {
                    log.warn("{} version conflict for operation {} on document '{}' version {}"
                                    + " in index '{}'.",
                            request != null ? request.index().versionType() : "UNKNOWN",
                            request._kind(),
                            request.index().id(),
                            request.index().version(),
                            request.index().index()
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
                            request._kind(),
                            request.index().id(),
                            request.index().version(),
                            request.index().index()
                    );
                }
                return false;
            }

            error.compareAndSet(
                    null,
                    new IllegalStateException(String.format("Indexing record failed. %s", response.error().reason()))
            );
            return true;
        }
        return false;
    }

    private boolean handleMalformedDocResponse(BulkResponseItem response) {
        String errorMsg = String.format(
                "Encountered an illegal document error '%s'. Ignoring and will not index record.",
                response.error().reason()
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
                        response.error().reason(),
                        BEHAVIOR_ON_MALFORMED_DOCS_CONFIG,
                        BehaviorOnMalformedDoc.IGNORE
                );
                error.compareAndSet(
                        null,
                        new IllegalStateException(String.format("Indexing record failed. %s", response.error().causedBy().reason()))
                );
                return true;
        }
    }

    private synchronized void reportBadRecord(BulkResponseItem response,
                                              long executionId) {
    }

    private void scheduleRetry(RetryContext<?> ctx) {
        if (ctx.attempts >= elasticsearchProperties.getMaxRetries()) {
            log.error("Max retries reached for document id={} index={}", ctx.id, ctx.index);
            return;
        }

        long delay = (long) (elasticsearchProperties.getLingerMs() * Math.pow(elasticsearchProperties.getRetryBackoffMs(), ctx.attempts));
        RetryContext<?> nextCtx = new RetryContext<>(ctx.index, ctx.id, ctx.doc, ctx.attempts + 1);

        scheduler.schedule(
                () -> retryExecutor.submit(
                        () -> ingester
                                .add(
                                        BulkOperation.of(
                                                b ->
                                                        b.index(
                                                                i -> i
                                                                        .index(ctx.index)
                                                                        .id(ctx.id)
                                                                        .document(ctx.doc)
                                                        )
                                        ),
                                        nextCtx
                                )
                ),
                delay,
                TimeUnit.MILLISECONDS);
    }

    @PreDestroy
    @SneakyThrows
    private void destroy() {
        this.elasticsearchClient.close();
    }
}
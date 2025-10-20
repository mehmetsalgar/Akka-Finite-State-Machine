package org.salgar.fsm.pekko.elasticsearch.bulkprocessor;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._helpers.bulk.BulkIngester;
import co.elastic.clients.elasticsearch._helpers.bulk.BulkListener;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Bulk ingester wrapper implementing maxOperations, maxSize, flush interval,
 * max in-flight requests, and exponential backoff retries.
 */
@Service
public class ElasticsearchBulkService<T> {

    private final Logger log = LoggerFactory.getLogger(ElasticsearchBulkService.class);

    private final BulkIngester<RetryContext<T>> ingester;
    private final ScheduledExecutorService scheduler;
    private final ExecutorService retryExecutor;

    private final int maxRetries = 5;
    private final long initialDelayMs = 100;
    private final double backoffFactor = 2.0;

    public ElasticsearchBulkService(ElasticsearchClient client) {
        // Thread pools
        this.scheduler = Executors.newScheduledThreadPool(1, r -> new Thread(r, "bulk-retry-scheduler"));
        this.retryExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), r -> new Thread(r, "bulk-retry-executor"));

        // BulkIngester
        this.ingester = BulkIngester.of(b -> b
                .client(client)
                .maxOperations(1000) // batch size
                .maxSize(1024 * 1024L) // 1 MB
                .flushInterval(50, TimeUnit.SECONDS)
                .maxConcurrentRequests(1)
                .listener(new BulkListener<RetryContext<T>>() {
                    @Override
                    public void beforeBulk(long executionId, BulkRequest request, List<RetryContext<T>> retryContexts) {
                        log.debug("Executing bulk of {} requests", request.operations().size());
                    }

                    @Override
                    public void afterBulk(long executionId, BulkRequest request, List<RetryContext<T>> retryContexts, BulkResponse response) {
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
                    public void afterBulk(long executionId, BulkRequest request, List<RetryContext<T>> retryContexts, Throwable failure) {
                        log.error("Bulk request failed", failure);
                        // retry all items
                        retryContexts.forEach(ElasticsearchBulkService.this::scheduleRetry);
                    }
                })
        );
    }

    /** Add a document to bulk ingestion */
    public void add(String index, String id, T doc) {
        RetryContext<T> ctx = new RetryContext<>(index, id, doc, 0);
        ingester.add(BulkOperation.of(builder -> {
                    builder.index()
        }),
                ctx);
    }

    /** Flush manually */
    public void flush() {
        ingester.flush();
    }

    /** Close the ingester and thread pools */
    @PreDestroy
    public void close() throws IOException {
        ingester.close();
        scheduler.shutdown();
        retryExecutor.shutdown();
    }

    /** Retry with exponential backoff */
    private void scheduleRetry(RetryContext<T> ctx) {
        if (ctx.attempts >= maxRetries) {
            log.error("Max retries reached for document id={} index={}", ctx.id, ctx.index);
            return;
        }

        long delay = (long) (initialDelayMs * Math.pow(backoffFactor, ctx.attempts));
        RetryContext<T> nextCtx = new RetryContext<>(ctx.index, ctx.id, ctx.doc, ctx.attempts + 1);

        scheduler.schedule(() -> retryExecutor.submit(() -> ingester.add(nextCtx, nextCtx)), delay, TimeUnit.MILLISECONDS);
    }

    /** Context to track retries */
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
}


package org.salgar.fsm.pekko.elasticsearch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;
import org.salgar.fsm.pekko.elasticsearch.bulkprocessor.BulkProcessorFacade;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ElasticsearchRepositoryImpl implements ElasticsearchRepository {
    private final BulkProcessorFacade bulkProcessorFacade;
    private final ElasticsearchConverter elasticsearchConverter;

    private final ExecutorService executor
            = Executors.newSingleThreadExecutor();

    @Override
    public CompletableFuture<Void> index(
            final String offset,
            final String persistenceId,
            final Long sequenceId,
            final String projectionName,
            final String projectionKey,
            final String id,
            final String index,
            final String persistEvent,
            final Map<String, Object> payload) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        executor.submit(() -> {
                indexIntern(
                        offset,
                        persistenceId,
                        sequenceId,
                        projectionName,
                        projectionKey,
                        id,
                        index,
                        persistEvent,
                        payload);
            completableFuture.complete(null);
            return null;
        });

        return completableFuture;
    }

    public void indexIntern(final String offset,
                            final String persistenceId,
                            final Long sequenceId,
                            final String projectionName,
                            final String projectionKey,
                            final String id,
                            final String index,
                            final String persistEvent,
                            final Map<String, Object> payload) {
        log.debug("Processing Index Request for Id: {}", id);
        try {
            Map<String, Object> indexPayload = new HashMap<>(payload);
            indexPayload.put("state", persistEvent);

            IndexRequest indexRequest = new IndexRequest(index)
                    .id(id)
                    .source(elasticsearchConverter.mapObject(indexPayload).toJson(), Requests.INDEX_CONTENT_TYPE);
            this.bulkProcessorFacade.index(
                    offset,
                    persistenceId,
                    sequenceId,
                    projectionName,
                    projectionKey,
                    indexRequest);
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
        }
    }
}
package org.salgar.fsm.pekko.elasticsearch;

import java.util.Map;
import java.util.concurrent.CompletableFuture;


public interface ElasticsearchRepository {
    CompletableFuture<Void> index(
            String offset,
            String persistenceId,
            Long sequenceId,
            String projectionName,
            String projectionKey,
            String id,
            String index,
            String persistEvent,
            Map<String, Object> payload);
}
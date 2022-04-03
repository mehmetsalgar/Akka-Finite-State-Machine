package org.salgar.fsm.akka.elasticsearch;

import org.salgar.fsm.akka.elasticsearch.model.ElasticsearchOffset;

import java.util.concurrent.CompletableFuture;

public interface OffsetFacade {
    CompletableFuture<ElasticsearchOffset> readOffset(String projectionName, String projectionKey);
    CompletableFuture<Void> saveOffset(String projectionName, String projectionKey, ElasticsearchOffset offset);
    CompletableFuture<Void> clearOffset(String projectionName, String projectionKey);
    CompletableFuture<Boolean> readManagementState(String projectionName, String projectionKey);
    CompletableFuture<Void> savePaused(String projectionName, String projectionKey, Boolean paused);
}


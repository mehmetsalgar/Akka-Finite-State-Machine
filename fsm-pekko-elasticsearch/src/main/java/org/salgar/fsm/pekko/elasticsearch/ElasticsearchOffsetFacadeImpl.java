package org.salgar.fsm.pekko.elasticsearch;

import lombok.RequiredArgsConstructor;
import org.salgar.fsm.pekko.elasticsearch.model.ElasticsearchManagement;
import org.salgar.fsm.pekko.elasticsearch.model.ElasticsearchOffset;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@ConditionalOnProperty(name = "org.salgar.fsm.pekko.projections.offsetStore", havingValue = "elasticsearch")
@RequiredArgsConstructor
public class ElasticsearchOffsetFacadeImpl implements OffsetFacade{
    private final OffsetRepository offsetRepository;
    private final ManagementRepository managementRepository;
    private final ExecutorService executor
            = Executors.newSingleThreadExecutor();

    @Override
    public CompletableFuture<ElasticsearchOffset> readOffset(String projectionName, String projectionKey) {
        final CompletableFuture<ElasticsearchOffset> completableFuture = new CompletableFuture<>();

        executor.submit(() -> {
                    final Optional<ElasticsearchOffset> elasticsearchOffset = offsetRepository
                            .findById(projectionName + "_" + projectionKey);
                    completableFuture.complete(elasticsearchOffset.orElse(null));
                    return null;
                }
        );

        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> saveOffset(String projectionName, String projectionKey, ElasticsearchOffset offset) {
        final CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        executor.submit(() -> {
            offsetRepository.save(new ElasticsearchOffset(
                projectionName + "_" + projectionKey,
                projectionName,
                projectionKey,
                offset.getOffset()));
            completableFuture.complete(null);
            return null;
        });
        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> clearOffset(String projectionName, String projectionKey) {
        final CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        executor.submit(() -> {
            offsetRepository.deleteById(projectionName + "_" + projectionKey);
            completableFuture.complete(null);
            return null;
        });
        return completableFuture;
    }

    @Override
    public CompletableFuture<Boolean> readManagementState(String projectionName, String projectionKey) {
        final CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();

        executor.submit(() -> {
           final Optional<ElasticsearchManagement> elasticsearchManagement =
                    managementRepository.findById(projectionName + "_" + projectionKey);
           completableFuture.complete(
                   elasticsearchManagement.isPresent() ? elasticsearchManagement.get().getPaused(): Boolean.FALSE);
           return null;

        });
        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> savePaused(String projectionName, String projectionKey, Boolean paused) {
        final CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        executor.submit(() -> {
            managementRepository.save(new ElasticsearchManagement(
                    projectionName + "_" + projectionKey,
                    projectionName,
                    projectionKey,
                    paused));
            return null;
        });

        return completableFuture;
    }
}
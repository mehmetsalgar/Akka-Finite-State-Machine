package org.salgar.fsm.pekko.elasticsearch.bulkprocessor;

import org.elasticsearch.action.index.IndexRequest;

public interface BulkProcessorFacade {
    void index(String offset,
               String persistenceId,
               Long sequenceId,
               String persistenceKey,
               String persistenceName,
               IndexRequest indexRequest);
}
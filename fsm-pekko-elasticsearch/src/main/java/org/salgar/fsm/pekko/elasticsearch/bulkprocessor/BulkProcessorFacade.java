package org.salgar.fsm.pekko.elasticsearch.bulkprocessor;


public interface BulkProcessorFacade {
    void index(String offset,
               String persistenceId,
               Long sequenceId,
               String persistenceKey,
               String persistenceName,
               String id,
               String index,
               String document);
}
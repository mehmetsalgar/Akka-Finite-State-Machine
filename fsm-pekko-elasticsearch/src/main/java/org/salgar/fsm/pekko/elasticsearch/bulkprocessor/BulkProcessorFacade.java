package org.salgar.fsm.pekko.elasticsearch.bulkprocessor;


import org.springframework.data.elasticsearch.core.document.Document;

public interface BulkProcessorFacade {
    void index(String offset,
               String persistenceId,
               Long sequenceId,
               String persistenceKey,
               String persistenceName,
               String id,
               String index,
               Document py);
}
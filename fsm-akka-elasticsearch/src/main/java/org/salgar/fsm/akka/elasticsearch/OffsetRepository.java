package org.salgar.fsm.akka.elasticsearch;

import org.salgar.fsm.akka.elasticsearch.model.ElasticsearchOffset;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffsetRepository extends CrudRepository<ElasticsearchOffset, String> {
}
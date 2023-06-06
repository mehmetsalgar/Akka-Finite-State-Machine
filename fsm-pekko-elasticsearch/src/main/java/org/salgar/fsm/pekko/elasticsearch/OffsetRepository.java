package org.salgar.fsm.pekko.elasticsearch;

import org.salgar.fsm.pekko.elasticsearch.model.ElasticsearchOffset;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffsetRepository extends CrudRepository<ElasticsearchOffset, String> {
}
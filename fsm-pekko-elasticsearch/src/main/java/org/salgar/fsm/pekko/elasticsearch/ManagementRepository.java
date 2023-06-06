package org.salgar.fsm.pekko.elasticsearch;

import org.salgar.fsm.pekko.elasticsearch.model.ElasticsearchManagement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagementRepository extends CrudRepository<ElasticsearchManagement, String> {
}
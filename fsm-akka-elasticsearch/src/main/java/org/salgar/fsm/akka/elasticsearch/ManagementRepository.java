package org.salgar.fsm.akka.elasticsearch;

import org.salgar.fsm.akka.elasticsearch.model.ElasticsearchManagement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagementRepository extends CrudRepository<ElasticsearchManagement, String> {
}
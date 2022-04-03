package org.salgar.fsm.akka.foureyes.elasticsearch;

import org.salgar.fsm.akka.foureyes.elasticsearch.model.CreditSmEs;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditSMRepository extends CrudRepository<CreditSmEs, String> {
}
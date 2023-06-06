package org.salgar.fsm.pekko.foureyes.elasticsearch;

import org.salgar.fsm.pekko.foureyes.model.CreditSmEs;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditSMRepository extends CrudRepository<CreditSmEs, String> {
}
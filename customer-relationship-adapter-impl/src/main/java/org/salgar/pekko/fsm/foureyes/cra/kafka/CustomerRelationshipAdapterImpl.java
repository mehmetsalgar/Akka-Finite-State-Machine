package org.salgar.pekko.fsm.foureyes.cra.kafka;

import lombok.extern.slf4j.Slf4j;
import org.salgar.pekko.fsm.foureyes.cra.model.CRMCustomer;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerRelationshipAdapterImpl implements CustomerRelationshipAdapter {
    @Override
    public void transferCustomerCreation(CRMCustomer customer) {
        log.info("Real Kafka implementation is not there at the moment!");
    }
}
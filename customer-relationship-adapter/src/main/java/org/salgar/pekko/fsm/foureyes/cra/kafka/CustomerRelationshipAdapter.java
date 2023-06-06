package org.salgar.pekko.fsm.foureyes.cra.kafka;

import org.salgar.pekko.fsm.foureyes.cra.model.CRMCustomer;

public interface CustomerRelationshipAdapter {
    void transferCustomerCreation(CRMCustomer customer);
}
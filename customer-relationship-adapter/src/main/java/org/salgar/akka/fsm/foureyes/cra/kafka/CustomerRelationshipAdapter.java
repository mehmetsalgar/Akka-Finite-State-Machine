package org.salgar.akka.fsm.foureyes.cra.kafka;

import org.salgar.akka.fsm.foureyes.cra.model.CRMCustomer;

public interface CustomerRelationshipAdapter {
    void transferCustomerCreation(CRMCustomer customer);
}
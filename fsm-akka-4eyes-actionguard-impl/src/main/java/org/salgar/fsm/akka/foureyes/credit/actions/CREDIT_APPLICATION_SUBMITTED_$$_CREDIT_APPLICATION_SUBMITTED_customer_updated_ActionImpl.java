package org.salgar.fsm.akka.foureyes.credit.actions;

import akka.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.akka.foureyes.credit.CreditSM;
import org.salgar.fsm.akka.foureyes.credit.model.CustomerV2;
import org.salgar.fsm.akka.foureyes.variables.PayloadVariableConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_APPLICATION_SUBMITTED_customer_updated_ActionImpl
    extends CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_APPLICATION_SUBMITTED_customer_updated_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        List<CustomerV2> creditTenants = (List<CustomerV2>) controlObject.get(PayloadVariableConstants.CREDIT_TENANTS);
        CustomerV2 updatedCustomer = (CustomerV2) payload.get(PayloadVariableConstants.CUSTOMER);

        for(int i = 0, n = creditTenants.size(); i < n; i++) {
            CustomerV2 customer = creditTenants.get(i);
            if(customer.getCustomerId().equals(updatedCustomer.getCustomerId())) {
                creditTenants.remove(i);
                break;
            }
        }
        creditTenants.add(updatedCustomer);
        Map<String, Object> stateUpdate = new HashMap<>();
        stateUpdate.put(PayloadVariableConstants.CREDIT_TENANTS, creditTenants);

        return stateUpdate;
    }
}
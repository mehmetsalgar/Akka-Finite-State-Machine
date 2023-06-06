package org.salgar.fsm.pekko.foureyes.credit.actions;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;
import org.salgar.fsm.pekko.foureyes.credit.model.Customer;
import org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_APPLICATION_SUBMITTED_customer_updated_ActionImpl
    extends CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_APPLICATION_SUBMITTED_customer_updated_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        List<Customer> creditTenants = (List<Customer>) controlObject.get(PayloadVariableConstants.CREDIT_TENANTS);
        Customer updatedCustomer = (Customer) payload.get(PayloadVariableConstants.CUSTOMER);

        for(int i = 0, n = creditTenants.size(); i < n; i++) {
            Customer customer = creditTenants.get(i);
            if(customer.getPersonalId().equals(updatedCustomer.getPersonalId())) {
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
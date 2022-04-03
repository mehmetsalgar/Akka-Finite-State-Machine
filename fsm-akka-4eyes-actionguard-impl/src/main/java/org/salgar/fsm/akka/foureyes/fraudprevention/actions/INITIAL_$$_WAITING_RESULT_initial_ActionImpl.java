package org.salgar.fsm.akka.foureyes.fraudprevention.actions;

import akka.actor.typed.scaladsl.ActorContext;
import lombok.RequiredArgsConstructor;
import org.salgar.akka.fsm.foureyes.faudprevention.FraudPreventionService;
import org.salgar.fsm.akka.foureyes.credit.model.Customer;
import org.salgar.fsm.akka.foureyes.fraudprevention.FraudPreventionSM;
import org.salgar.fsm.akka.foureyes.variables.PayloadVariableConstants;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class INITIAL_$$_WAITING_RESULT_initial_ActionImpl
    extends INITIAL_$$_WAITING_RESULT_initial_Action {
    private final FraudPreventionService fraudPreventionService;

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<FraudPreventionSM.FraudPreventionSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        if(payload != null) {
            List<Customer> creditTenants = (List<Customer>) payload.get(PayloadVariableConstants.CREDIT_TENANTS);

            //We should actually do Multi Tenant Fraud Prevention, but living that as exercise
            Customer customer = creditTenants.get(0);
            fraudPreventionService.reportFraudPrevention(
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getPersonalId());
        }
        return payload;
    }
}

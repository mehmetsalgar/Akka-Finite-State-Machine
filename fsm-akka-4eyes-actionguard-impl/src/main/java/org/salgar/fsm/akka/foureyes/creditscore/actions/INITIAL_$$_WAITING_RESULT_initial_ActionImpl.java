package org.salgar.fsm.akka.foureyes.creditscore.actions;

import akka.actor.typed.scaladsl.ActorContext;
import lombok.RequiredArgsConstructor;
import org.salgar.akka.fsm.foureyes.creditscore.CreditScoreService;
import org.salgar.fsm.akka.foureyes.credit.model.Customer;
import org.salgar.fsm.akka.foureyes.creditscore.CreditScoreSM;
import org.salgar.fsm.akka.foureyes.variables.PayloadVariableConstants;

import java.util.Map;

@RequiredArgsConstructor
public class INITIAL_$$_WAITING_RESULT_initial_ActionImpl
    extends INITIAL_$$_WAITING_RESULT_initial_Action {
    private final CreditScoreService creditScoreService;

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditScoreSM.CreditScoreSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        if(payload != null) {
            Customer customer = (Customer) payload.get(PayloadVariableConstants.CUSTOMER);
            creditScoreService.calculateCreditScore(
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getPersonalId());
        }

        return payload;
    }
}

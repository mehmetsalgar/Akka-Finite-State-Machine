package org.salgar.fsm.akka.foureyes.credit.guards;

import akka.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.akka.foureyes.credit.CreditSM;

import java.util.Map;

import static org.salgar.fsm.akka.foureyes.variables.PayloadVariableConstants.CREDIT_AMOUNT;

public class WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_relationshipManagerCreditAmountCriticalGuard_GuardImpl
    implements WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_relationshipManagerCreditAmountCriticalGuard_Guard {
    @Override
    public boolean evaluate(
            ActorContext<CreditSM.CreditSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload) {
        actorContext.log().debug("Evaluating WAITING_MANAGER_APPROVAL waitingManagerApproval_onRelationshipManagerApproved Guard");

        Double creditAmount = (Double) controlObject.get(CREDIT_AMOUNT);
        if(creditAmount >= 100000000.0) {
            return true;
        }

        return false;
    }
}

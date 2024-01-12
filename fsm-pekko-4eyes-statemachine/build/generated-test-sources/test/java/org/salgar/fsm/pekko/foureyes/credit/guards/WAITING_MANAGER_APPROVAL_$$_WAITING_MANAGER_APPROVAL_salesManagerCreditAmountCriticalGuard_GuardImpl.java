package org.salgar.fsm.pekko.foureyes.credit.guards;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.Map;

public class WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_salesManagerCreditAmountCriticalGuard_GuardImpl
    implements WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_salesManagerCreditAmountCriticalGuard_Guard {
    @Override
    public boolean evaluate(
            ActorContext<CreditSM.CreditSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload) {
        actorContext.log().debug("Evaluating WAITING_MANAGER_APPROVAL waitingForApproval_onSalesManagerApproved Guard");

        return true;
    }
}

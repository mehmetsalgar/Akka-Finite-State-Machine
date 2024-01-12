package org.salgar.fsm.pekko.foureyes.credit.guards;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.Map;

public class SALES_MANAGER_APPROVED_$$_WAITING_APPROVAL_FROM_SENIOR_MANAGER_isCreditScoreNotSufficientGuard_GuardImpl
    implements SALES_MANAGER_APPROVED_$$_WAITING_APPROVAL_FROM_SENIOR_MANAGER_isCreditScoreNotSufficientGuard_Guard {
    @Override
    public boolean evaluate(
            ActorContext<CreditSM.CreditSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload) {
        actorContext.log().debug("Evaluating SALES_MANAGER_APPROVED salesManagerApproved_onCreditScoreReceived Guard");

        return true;
    }
}

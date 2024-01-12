package org.salgar.fsm.pekko.foureyes.credit.guards;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.Map;

public class WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_creditAnalystCreditAmountCritical_GuardImpl
    implements WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_creditAnalystCreditAmountCritical_Guard {
    @Override
    public boolean evaluate(
            ActorContext<CreditSM.CreditSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload) {
        actorContext.log().debug("Evaluating WAITING_ANAYLIST_APPROVAL waitingAnalystApproval_onCreditAccepted Guard");

        return true;
    }
}

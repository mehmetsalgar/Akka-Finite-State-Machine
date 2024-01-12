package org.salgar.fsm.pekko.foureyes.credit.actions;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.Map;

public class WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_waitingAnalystApproval_onCreditAccepted_ActionImpl
    extends WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_waitingAnalystApproval_onCreditAccepted_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        return payload;
    }
}

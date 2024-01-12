package org.salgar.fsm.pekko.foureyes.credit.actions;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.Map;

public class WAITING_CREDIT_ANALYST_APPROVAL_$$_CREDIT_ACCEPTED_creditAnalyst_onAccepted_ActionImpl
    extends WAITING_CREDIT_ANALYST_APPROVAL_$$_CREDIT_ACCEPTED_creditAnalyst_onAccepted_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        return payload;
    }
}

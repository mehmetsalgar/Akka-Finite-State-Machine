package org.salgar.fsm.pekko.foureyes.credit.actions;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.Map;

public class ADRRESCHECK_RESULT_RECEIVED_$$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED_addressCheck_CredfitScore_onResultReceived_ActionImpl
    extends ADRRESCHECK_RESULT_RECEIVED_$$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED_addressCheck_CredfitScore_onResultReceived_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        return payload;
    }
}

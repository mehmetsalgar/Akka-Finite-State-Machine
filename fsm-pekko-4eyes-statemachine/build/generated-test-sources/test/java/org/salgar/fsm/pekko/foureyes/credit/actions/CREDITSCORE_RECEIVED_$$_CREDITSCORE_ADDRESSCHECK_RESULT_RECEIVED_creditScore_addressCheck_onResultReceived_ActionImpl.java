package org.salgar.fsm.pekko.foureyes.credit.actions;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.Map;

public class CREDITSCORE_RECEIVED_$$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED_creditScore_addressCheck_onResultReceived_ActionImpl
    extends CREDITSCORE_RECEIVED_$$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED_creditScore_addressCheck_onResultReceived_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        return payload;
    }
}

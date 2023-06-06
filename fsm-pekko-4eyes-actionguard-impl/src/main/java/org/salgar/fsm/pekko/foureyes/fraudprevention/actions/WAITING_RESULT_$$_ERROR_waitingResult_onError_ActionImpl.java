package org.salgar.fsm.pekko.foureyes.fraudprevention.actions;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.fraudprevention.FraudPreventionSM;

import java.util.Map;

public class WAITING_RESULT_$$_ERROR_waitingResult_onError_ActionImpl
    extends WAITING_RESULT_$$_ERROR_waitingResult_onError_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<FraudPreventionSM.FraudPreventionSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        return payload;
    }
}

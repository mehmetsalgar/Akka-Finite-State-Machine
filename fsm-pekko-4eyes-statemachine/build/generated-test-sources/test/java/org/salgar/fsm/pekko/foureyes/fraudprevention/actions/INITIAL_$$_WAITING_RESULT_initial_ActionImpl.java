package org.salgar.fsm.pekko.foureyes.fraudprevention.actions;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.fraudprevention.FraudPreventionSM;

import java.util.Map;

public class INITIAL_$$_WAITING_RESULT_initial_ActionImpl
    extends INITIAL_$$_WAITING_RESULT_initial_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<FraudPreventionSM.FraudPreventionSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        return payload;
    }
}

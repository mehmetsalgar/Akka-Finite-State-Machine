package org.salgar.fsm.akka.foureyes.fraudprevention.actions;

import akka.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.akka.foureyes.fraudprevention.FraudPreventionSM;

import java.util.Map;

public class ERROR_$$_WAITING_RESULT_error_onRetry_ActionImpl
    extends ERROR_$$_WAITING_RESULT_error_onRetry_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<FraudPreventionSM.FraudPreventionSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        return payload;
    }
}

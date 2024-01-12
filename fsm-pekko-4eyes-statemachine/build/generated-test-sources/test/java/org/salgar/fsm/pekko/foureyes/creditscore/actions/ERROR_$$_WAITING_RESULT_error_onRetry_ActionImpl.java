package org.salgar.fsm.pekko.foureyes.creditscore.actions;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.creditscore.CreditScoreSM;

import java.util.Map;

public class ERROR_$$_WAITING_RESULT_error_onRetry_ActionImpl
    extends ERROR_$$_WAITING_RESULT_error_onRetry_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditScoreSM.CreditScoreSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        return payload;
    }
}

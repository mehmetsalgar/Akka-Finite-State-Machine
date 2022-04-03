package org.salgar.fsm.akka.foureyes.creditscore.actions;

import akka.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.akka.foureyes.creditscore.CreditScoreSM;

import java.util.Map;

public class WAITING_RESULT_$$_ERROR_waitingResult_onError_ActionImpl
    extends WAITING_RESULT_$$_ERROR_waitingResult_onError_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditScoreSM.CreditScoreSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        return payload;
    }
}

package org.salgar.fsm.pekko.foureyes.addresscheck.actions;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.addresscheck.AdressCheckSM;

import java.util.Map;

public class WAITING_RESULT_$$_ERROR_waiting_onError_ActionImpl
    extends WAITING_RESULT_$$_ERROR_waiting_onError_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<AdressCheckSM.AdressCheckSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        return payload;
    }
}

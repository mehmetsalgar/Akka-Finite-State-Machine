package org.salgar.fsm.pekko.foureyes.credit.actions;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.Map;

public class ADRRESCHECK_RESULT_RECEIVED_$$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED_addressCheckResultReceived_fraudPreventionResultReceived_ActionImpl
    extends ADRRESCHECK_RESULT_RECEIVED_$$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED_addressCheckResultReceived_fraudPreventionResultReceived_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        return payload;
    }
}

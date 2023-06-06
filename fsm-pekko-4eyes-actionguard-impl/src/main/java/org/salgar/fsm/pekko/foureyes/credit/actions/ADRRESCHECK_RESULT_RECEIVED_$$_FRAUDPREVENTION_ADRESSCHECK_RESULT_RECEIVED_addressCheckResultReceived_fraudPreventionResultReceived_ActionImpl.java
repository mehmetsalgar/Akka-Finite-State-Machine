package org.salgar.fsm.pekko.foureyes.credit.actions;


import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.HashMap;
import java.util.Map;

import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.FRAUD_PREVENTION_RESULT;

public class ADRRESCHECK_RESULT_RECEIVED_$$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED_addressCheckResultReceived_fraudPreventionResultReceived_ActionImpl
    extends ADRRESCHECK_RESULT_RECEIVED_$$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED_addressCheckResultReceived_fraudPreventionResultReceived_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                      Map<String, Object> controlObject,
                                                      Map<String, Object> payload) {
        Map<String, Object> modifiedPayload = new HashMap<>();
        Boolean fraudPreventionResult = (Boolean) payload.get(FRAUD_PREVENTION_RESULT);
        modifiedPayload.put(FRAUD_PREVENTION_RESULT, fraudPreventionResult);

        return modifiedPayload;
    }
}

package org.salgar.fsm.pekko.foureyes.credit.actions;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;
import org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants;

import java.util.HashMap;
import java.util.Map;

public class INITIAL_CSC_$$_FRAUDPREVENTION_RESULT_RECEIVED_initial_FraudPreventionResultReceived_ActionImpl
    extends INITIAL_CSC_$$_FRAUDPREVENTION_RESULT_RECEIVED_initial_FraudPreventionResultReceived_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        Map<String, Object> modifiedPayload = new HashMap<>();
        boolean result = (boolean) payload.get(PayloadVariableConstants.FRAUD_PREVENTION_RESULT);
        modifiedPayload.put(PayloadVariableConstants.FRAUD_PREVENTION_RESULT, result);

        return modifiedPayload;
    }
}
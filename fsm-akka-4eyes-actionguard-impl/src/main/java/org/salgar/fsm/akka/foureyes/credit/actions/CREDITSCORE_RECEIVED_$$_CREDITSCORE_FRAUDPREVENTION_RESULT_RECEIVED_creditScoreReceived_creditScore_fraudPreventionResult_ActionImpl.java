package org.salgar.fsm.akka.foureyes.credit.actions;

import akka.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.akka.foureyes.credit.CreditSM;
import org.salgar.fsm.akka.foureyes.variables.PayloadVariableConstants;

import java.util.HashMap;
import java.util.Map;

public class CREDITSCORE_RECEIVED_$$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED_creditScoreReceived_creditScore_fraudPreventionResult_ActionImpl
    extends CREDITSCORE_RECEIVED_$$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED_creditScoreReceived_creditScore_fraudPreventionResult_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {

        Map<String, Object> modifiedPayload = new HashMap<>();
        Boolean fraudPreventionResult = (Boolean) payload.get(PayloadVariableConstants.FRAUD_PREVENTION_RESULT);
        modifiedPayload.put(PayloadVariableConstants.FRAUD_PREVENTION_RESULT, fraudPreventionResult);

        return modifiedPayload;
    }
}
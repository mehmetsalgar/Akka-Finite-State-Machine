package org.salgar.fsm.akka.foureyes.credit.actions;

import akka.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.akka.foureyes.credit.CreditSM;

import java.util.HashMap;
import java.util.Map;

import static org.salgar.fsm.akka.foureyes.slaves.SlaveStatemachineConstants.*;
import static org.salgar.fsm.akka.foureyes.variables.PayloadVariableConstants.*;

public class CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_lowScore_onResultReceived_ActionImpl
        extends CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_lowScore_onResultReceived_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        Map<String, Object> modifiedPayload = new HashMap<>();
        String slaveSM = (String) payload.get(SOURCE_SLAVE_SM_TAG);
        if(CUSTOMER_SCORE_SM.equals(slaveSM)) {
            Double creditScoreResult = (Double) payload.get(CREDIT_SCORE_RESULT);
            modifiedPayload.put(CREDIT_SCORE_RESULT, creditScoreResult);
        } else if(FRAUD_PREVENTION_SM.equals(slaveSM)) {
            Boolean fraudPreventionResult = (Boolean) payload.get(FRAUD_PREVENTION_RESULT);
            modifiedPayload.put(FRAUD_PREVENTION_RESULT, fraudPreventionResult);
        } else if(ADDRESS_CHECK_SM.equals(slaveSM)) {
            Boolean addressCheckResult = (Boolean) payload.get(ADDRESS_CHECK_RESULT);
            modifiedPayload.put(ADDRESS_CHECK_RESULT, addressCheckResult);
        }
        return modifiedPayload;
    }
}

package org.salgar.fsm.pekko.foureyes.credit.actions;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.HashMap;
import java.util.Map;

import static org.salgar.fsm.pekko.foureyes.slaves.SlaveStatemachineConstants.ADDRESS_CHECK_SM;
import static org.salgar.fsm.pekko.foureyes.slaves.SlaveStatemachineConstants.CUSTOMER_SCORE_SM;
import static org.salgar.fsm.pekko.foureyes.slaves.SlaveStatemachineConstants.FRAUD_PREVENTION_SM;
import static org.salgar.fsm.pekko.foureyes.slaves.SlaveStatemachineConstants.SOURCE_SLAVE_SM_TAG;
import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.ADDRESS_CHECK_RESULT;
import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.CREDIT_SCORE_RESULT;
import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.FRAUD_PREVENTION_RESULT;

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

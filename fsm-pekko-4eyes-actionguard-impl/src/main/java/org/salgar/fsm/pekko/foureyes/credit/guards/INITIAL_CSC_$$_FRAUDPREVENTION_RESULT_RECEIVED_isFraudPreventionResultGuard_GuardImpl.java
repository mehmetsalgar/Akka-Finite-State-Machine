package org.salgar.fsm.pekko.foureyes.credit.guards;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.Map;

import static org.salgar.fsm.pekko.foureyes.slaves.SlaveStatemachineConstants.FRAUD_PREVENTION_SM;
import static org.salgar.fsm.pekko.foureyes.slaves.SlaveStatemachineConstants.SOURCE_SLAVE_SM_TAG;

public class INITIAL_CSC_$$_FRAUDPREVENTION_RESULT_RECEIVED_isFraudPreventionResultGuard_GuardImpl
    implements INITIAL_CSC_$$_FRAUDPREVENTION_RESULT_RECEIVED_isFraudPreventionResultGuard_Guard {
    @Override
    public boolean evaluate(
            ActorContext<CreditSM.CreditSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload) {
        actorContext.log().debug("Evaluating INITIAL_CSC initial_FraudPreventionResultReceived Guard");

        String slaveTag = (String) payload.get(SOURCE_SLAVE_SM_TAG);
        if(FRAUD_PREVENTION_SM.equals(slaveTag)) {
            return true;
        }
        return false;
    }
}

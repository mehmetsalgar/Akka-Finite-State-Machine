package org.salgar.fsm.pekko.foureyes.credit.guards;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.Map;

public class ADRRESCHECK_RESULT_RECEIVED_$$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED_isFraudPreventionResultGaurd_GuardImpl
    implements ADRRESCHECK_RESULT_RECEIVED_$$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED_isFraudPreventionResultGaurd_Guard {
    @Override
    public boolean evaluate(
            ActorContext<CreditSM.CreditSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload) {
        actorContext.log().debug("Evaluating ADRRESCHECK_RESULT_RECEIVED addressCheckResultReceived_fraudPreventionResultReceived Guard");

        return true;
    }
}

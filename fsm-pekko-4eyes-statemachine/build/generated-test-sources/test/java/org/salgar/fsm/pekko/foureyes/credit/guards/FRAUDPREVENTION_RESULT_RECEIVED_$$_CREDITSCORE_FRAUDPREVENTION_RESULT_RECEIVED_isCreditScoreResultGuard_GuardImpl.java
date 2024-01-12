package org.salgar.fsm.pekko.foureyes.credit.guards;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.Map;

public class FRAUDPREVENTION_RESULT_RECEIVED_$$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED_isCreditScoreResultGuard_GuardImpl
    implements FRAUDPREVENTION_RESULT_RECEIVED_$$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED_isCreditScoreResultGuard_Guard {
    @Override
    public boolean evaluate(
            ActorContext<CreditSM.CreditSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload) {
        actorContext.log().debug("Evaluating FRAUDPREVENTION_RESULT_RECEIVED fraudPreventionResultReceived_creditScoreFraudPreventionResultReceived Guard");

        return true;
    }
}

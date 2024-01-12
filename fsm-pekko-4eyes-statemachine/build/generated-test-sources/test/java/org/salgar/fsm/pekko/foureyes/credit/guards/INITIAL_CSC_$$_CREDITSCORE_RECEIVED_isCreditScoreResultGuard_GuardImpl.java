package org.salgar.fsm.pekko.foureyes.credit.guards;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.Map;

public class INITIAL_CSC_$$_CREDITSCORE_RECEIVED_isCreditScoreResultGuard_GuardImpl
    implements INITIAL_CSC_$$_CREDITSCORE_RECEIVED_isCreditScoreResultGuard_Guard {
    @Override
    public boolean evaluate(
            ActorContext<CreditSM.CreditSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload) {
        actorContext.log().debug("Evaluating INITIAL_CSC initial_creditScoreReceived Guard");

        return true;
    }
}

package org.salgar.fsm.pekko.foureyes.credit.guards;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.Map;

public class CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_isScoreTooLowGuard_GuardImpl
    implements CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_isScoreTooLowGuard_Guard {
    @Override
    public boolean evaluate(
            ActorContext<CreditSM.CreditSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload) {
        actorContext.log().debug("Evaluating CREDIT_APPLICATION_SUBMITTED lowScore_onResultReceived Guard");

        return true;
    }
}

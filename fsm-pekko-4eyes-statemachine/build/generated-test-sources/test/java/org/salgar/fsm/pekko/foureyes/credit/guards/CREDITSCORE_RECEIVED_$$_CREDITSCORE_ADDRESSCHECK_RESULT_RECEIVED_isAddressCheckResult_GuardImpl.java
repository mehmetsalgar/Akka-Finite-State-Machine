package org.salgar.fsm.pekko.foureyes.credit.guards;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.Map;

public class CREDITSCORE_RECEIVED_$$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED_isAddressCheckResult_GuardImpl
    implements CREDITSCORE_RECEIVED_$$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED_isAddressCheckResult_Guard {
    @Override
    public boolean evaluate(
            ActorContext<CreditSM.CreditSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload) {
        actorContext.log().debug("Evaluating CREDITSCORE_RECEIVED creditScore_addressCheck_onResultReceived Guard");

        return true;
    }
}

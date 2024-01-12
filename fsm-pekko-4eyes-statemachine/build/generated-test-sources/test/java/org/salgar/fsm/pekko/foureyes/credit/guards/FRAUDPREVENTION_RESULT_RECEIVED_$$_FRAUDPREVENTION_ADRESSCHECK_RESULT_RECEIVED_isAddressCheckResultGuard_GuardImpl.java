package org.salgar.fsm.pekko.foureyes.credit.guards;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.Map;

public class FRAUDPREVENTION_RESULT_RECEIVED_$$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED_isAddressCheckResultGuard_GuardImpl
    implements FRAUDPREVENTION_RESULT_RECEIVED_$$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED_isAddressCheckResultGuard_Guard {
    @Override
    public boolean evaluate(
            ActorContext<CreditSM.CreditSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload) {
        actorContext.log().debug("Evaluating FRAUDPREVENTION_RESULT_RECEIVED fraudPReventionResultReceived_fraudPReventionResultReceived Guard");

        return true;
    }
}

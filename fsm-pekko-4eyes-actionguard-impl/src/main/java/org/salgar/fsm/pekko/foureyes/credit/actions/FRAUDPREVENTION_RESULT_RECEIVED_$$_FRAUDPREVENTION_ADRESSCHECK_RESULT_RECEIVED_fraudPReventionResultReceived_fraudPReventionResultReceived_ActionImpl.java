package org.salgar.fsm.pekko.foureyes.credit.actions;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.HashMap;
import java.util.Map;

import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.ADDRESS_CHECK_RESULT;

public class FRAUDPREVENTION_RESULT_RECEIVED_$$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED_fraudPReventionResultReceived_fraudPReventionResultReceived_ActionImpl
    extends FRAUDPREVENTION_RESULT_RECEIVED_$$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED_fraudPReventionResultReceived_fraudPReventionResultReceived_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        Map<String, Object> modifiedPayload = new HashMap<>();
        Boolean addressCheckResult = (Boolean) payload.get(ADDRESS_CHECK_RESULT);
        modifiedPayload.put(ADDRESS_CHECK_RESULT, addressCheckResult);

        return modifiedPayload;
    }
}

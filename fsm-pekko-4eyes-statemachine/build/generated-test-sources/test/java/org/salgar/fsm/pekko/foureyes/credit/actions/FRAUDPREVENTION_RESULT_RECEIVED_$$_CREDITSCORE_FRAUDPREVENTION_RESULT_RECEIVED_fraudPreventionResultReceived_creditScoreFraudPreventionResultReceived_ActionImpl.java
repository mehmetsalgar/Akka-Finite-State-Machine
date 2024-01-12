package org.salgar.fsm.pekko.foureyes.credit.actions;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.Map;

public class FRAUDPREVENTION_RESULT_RECEIVED_$$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED_fraudPreventionResultReceived_creditScoreFraudPreventionResultReceived_ActionImpl
    extends FRAUDPREVENTION_RESULT_RECEIVED_$$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED_fraudPreventionResultReceived_creditScoreFraudPreventionResultReceived_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        return payload;
    }
}

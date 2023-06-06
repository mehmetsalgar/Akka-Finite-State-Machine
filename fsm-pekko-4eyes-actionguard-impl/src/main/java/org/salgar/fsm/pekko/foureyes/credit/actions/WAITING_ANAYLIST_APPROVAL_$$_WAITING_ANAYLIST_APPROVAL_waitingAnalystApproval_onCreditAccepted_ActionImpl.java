package org.salgar.fsm.pekko.foureyes.credit.actions;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.NUMBER_OF_CREDIT_ANALYST_APPROVED;

public class WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_waitingAnalystApproval_onCreditAccepted_ActionImpl
    extends WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_waitingAnalystApproval_onCreditAccepted_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        Optional<Integer> count = Optional.ofNullable((Integer)controlObject.get(NUMBER_OF_CREDIT_ANALYST_APPROVED));

        controlObject.put(NUMBER_OF_CREDIT_ANALYST_APPROVED, count.orElse(0) + 1);

        return Collections.emptyMap();
    }
}

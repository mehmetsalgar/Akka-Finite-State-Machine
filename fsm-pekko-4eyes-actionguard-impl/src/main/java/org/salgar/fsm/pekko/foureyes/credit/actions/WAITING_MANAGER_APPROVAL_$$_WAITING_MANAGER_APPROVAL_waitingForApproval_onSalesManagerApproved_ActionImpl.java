package org.salgar.fsm.pekko.foureyes.credit.actions;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.NUMBER_OF_SALES_MANAGERS_APPROVED;

public class WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_waitingForApproval_onSalesManagerApproved_ActionImpl
    extends WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_waitingForApproval_onSalesManagerApproved_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        Optional<Integer> count = Optional.ofNullable((Integer)controlObject.get(NUMBER_OF_SALES_MANAGERS_APPROVED));

        controlObject.put(NUMBER_OF_SALES_MANAGERS_APPROVED, count.orElse(0) + 1);

        return Collections.emptyMap();
    }
}

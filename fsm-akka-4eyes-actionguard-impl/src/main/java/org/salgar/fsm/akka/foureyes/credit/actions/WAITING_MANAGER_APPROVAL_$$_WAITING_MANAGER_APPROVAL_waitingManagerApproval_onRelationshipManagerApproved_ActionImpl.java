package org.salgar.fsm.akka.foureyes.credit.actions;

import akka.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.akka.foureyes.credit.CreditSM;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.salgar.fsm.akka.foureyes.variables.PayloadVariableConstants.NUMBER_OF_RELATIONSHIP_MANAGERS_APPROVED;

public class WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_waitingManagerApproval_onRelationshipManagerApproved_ActionImpl
    extends WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_waitingManagerApproval_onRelationshipManagerApproved_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        Optional<Integer> count = Optional.ofNullable((Integer)controlObject.get(NUMBER_OF_RELATIONSHIP_MANAGERS_APPROVED));

        controlObject.put(NUMBER_OF_RELATIONSHIP_MANAGERS_APPROVED, count.orElse(0) + 1);

        return Collections.emptyMap();
    }
}
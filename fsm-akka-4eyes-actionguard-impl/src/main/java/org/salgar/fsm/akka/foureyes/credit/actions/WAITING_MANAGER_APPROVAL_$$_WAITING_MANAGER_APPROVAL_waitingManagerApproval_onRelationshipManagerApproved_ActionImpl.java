package org.salgar.fsm.akka.foureyes.credit.actions;

import akka.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.akka.foureyes.credit.CreditSM;

import java.util.Map;

public class WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_waitingManagerApproval_onRelationshipManagerApproved_ActionImpl
    extends WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_waitingManagerApproval_onRelationshipManagerApproved_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        return payload;
    }
}

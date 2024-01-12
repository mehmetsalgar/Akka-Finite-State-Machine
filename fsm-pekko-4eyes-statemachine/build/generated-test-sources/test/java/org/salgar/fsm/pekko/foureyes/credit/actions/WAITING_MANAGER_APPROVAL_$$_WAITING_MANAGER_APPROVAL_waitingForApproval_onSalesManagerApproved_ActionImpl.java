package org.salgar.fsm.pekko.foureyes.credit.actions;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.Map;

public class WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_waitingForApproval_onSalesManagerApproved_ActionImpl
    extends WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_waitingForApproval_onSalesManagerApproved_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        return payload;
    }
}

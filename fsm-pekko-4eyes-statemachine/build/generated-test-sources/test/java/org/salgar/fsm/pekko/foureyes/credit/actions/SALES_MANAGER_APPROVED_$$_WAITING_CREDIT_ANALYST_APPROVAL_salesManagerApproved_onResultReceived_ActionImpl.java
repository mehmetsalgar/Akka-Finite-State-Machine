package org.salgar.fsm.pekko.foureyes.credit.actions;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.Map;

public class SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_salesManagerApproved_onResultReceived_ActionImpl
    extends SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_salesManagerApproved_onResultReceived_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        return payload;
    }
}

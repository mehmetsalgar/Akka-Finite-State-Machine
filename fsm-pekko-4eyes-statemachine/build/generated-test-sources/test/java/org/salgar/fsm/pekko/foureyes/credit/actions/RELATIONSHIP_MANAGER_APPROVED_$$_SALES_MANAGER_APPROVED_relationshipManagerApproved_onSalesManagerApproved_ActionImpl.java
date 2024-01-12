package org.salgar.fsm.pekko.foureyes.credit.actions;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.Map;

public class RELATIONSHIP_MANAGER_APPROVED_$$_SALES_MANAGER_APPROVED_relationshipManagerApproved_onSalesManagerApproved_ActionImpl
    extends RELATIONSHIP_MANAGER_APPROVED_$$_SALES_MANAGER_APPROVED_relationshipManagerApproved_onSalesManagerApproved_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        return payload;
    }
}

package org.salgar.fsm.pekko.foureyes.credit.actions;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.Map;

public class WAITING_APPROVAL_FROM_SENIOR_MANAGER_$$_WAITING_CREDIT_ANALYST_APPROVAL_waitingApprovalFromSeniorOfficier_onAcceptableScore_ActionImpl
    extends WAITING_APPROVAL_FROM_SENIOR_MANAGER_$$_WAITING_CREDIT_ANALYST_APPROVAL_waitingApprovalFromSeniorOfficier_onAcceptableScore_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        return payload;
    }
}

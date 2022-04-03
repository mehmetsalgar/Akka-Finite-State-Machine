package org.salgar.fsm.akka.foureyes.credit.actions;

import akka.actor.typed.scaladsl.ActorContext;
import lombok.RequiredArgsConstructor;
import org.salgar.akka.fsm.foureyes.notifier.NotifierService;
import org.salgar.fsm.akka.foureyes.credit.CreditSM;

import java.util.List;
import java.util.Map;

import static org.salgar.akka.fsm.foureyes.notifier.NotificationHelper.CREDIT_ANALYST_NOTIFICATION_LIST;

@RequiredArgsConstructor
public class WAITING_APPROVAL_FROM_SENIOR_MANAGER_$$_WAITING_CREDIT_ANALYST_APPROVAL_waitingApprovalFromSeniorOfficier_onAcceptableScore_ActionImpl
    extends WAITING_APPROVAL_FROM_SENIOR_MANAGER_$$_WAITING_CREDIT_ANALYST_APPROVAL_waitingApprovalFromSeniorOfficier_onAcceptableScore_Action {
    private final NotifierService notifierService;

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        List<String> notificationList = notifierService.calculateRecipientList(CREDIT_ANALYST_NOTIFICATION_LIST);
        notifierService
                .notify(notificationList, "Sales Manager Approved the Credit and Partner Systems delivered results"
                        + " you should proceed. Please check!");

        return payload;
    }
}

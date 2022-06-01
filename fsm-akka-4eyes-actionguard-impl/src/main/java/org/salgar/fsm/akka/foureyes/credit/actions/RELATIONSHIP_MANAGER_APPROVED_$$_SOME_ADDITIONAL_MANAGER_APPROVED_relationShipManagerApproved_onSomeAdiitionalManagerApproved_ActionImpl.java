package org.salgar.fsm.akka.foureyes.credit.actions;

import akka.actor.typed.scaladsl.ActorContext;
import lombok.RequiredArgsConstructor;
import org.salgar.akka.fsm.foureyes.notifier.NotifierService;
import org.salgar.fsm.akka.foureyes.credit.CreditSM;

import java.util.List;
import java.util.Map;

import static org.salgar.akka.fsm.foureyes.notifier.NotificationHelper.SALES_MANAGER_NOTIFICATION_LIST;
import static org.salgar.fsm.akka.foureyes.variables.PayloadVariableConstants.SALES_MANAGERS;

@RequiredArgsConstructor
public class RELATIONSHIP_MANAGER_APPROVED_$$_SOME_ADDITIONAL_MANAGER_APPROVED_relationShipManagerApproved_onSomeAdiitionalManagerApproved_ActionImpl
    extends RELATIONSHIP_MANAGER_APPROVED_$$_SOME_ADDITIONAL_MANAGER_APPROVED_relationshipManagerApproved_someAdditionalManagerApproved_Action {
    private final NotifierService notifierService;

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                      Map<String, Object> controlObject,
                                                      Map<String, Object> payload) {
        List<String> notificationList = notifierService.calculateRecipientList(SALES_MANAGER_NOTIFICATION_LIST);
        notifierService
                .notify(notificationList, "Some Other Manager Approved, Sales Manager must proceed please check. Please check!");
        controlObject.put(SALES_MANAGERS, notificationList);

        return payload;
    }
}

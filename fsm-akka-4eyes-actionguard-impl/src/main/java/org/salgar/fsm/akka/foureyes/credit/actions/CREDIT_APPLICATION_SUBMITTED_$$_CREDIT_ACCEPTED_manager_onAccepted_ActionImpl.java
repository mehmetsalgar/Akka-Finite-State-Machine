package org.salgar.fsm.akka.foureyes.credit.actions;

import akka.actor.typed.scaladsl.ActorContext;
import lombok.RequiredArgsConstructor;
import org.salgar.akka.fsm.foureyes.notifier.NotifierService;
import org.salgar.fsm.akka.foureyes.credit.CreditSM;
import org.salgar.fsm.akka.foureyes.credit.model.CustomerV2;
import org.salgar.fsm.akka.foureyes.variables.PayloadVariableConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_ACCEPTED_manager_onAccepted_ActionImpl
        extends CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_ACCEPTED_manager_onAccepted_Action {
    private final NotifierService notifierService;

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        List<CustomerV2> customers = (List<CustomerV2>) controlObject.get(PayloadVariableConstants.CREDIT_TENANTS);
        List<String> emails = new ArrayList<>();
        for (CustomerV2 customer : customers) {
            emails.add(customer.getEmail());
        }
        notifierService.notify(emails, "Your credit application is accepted!");
        return payload;
    }
}
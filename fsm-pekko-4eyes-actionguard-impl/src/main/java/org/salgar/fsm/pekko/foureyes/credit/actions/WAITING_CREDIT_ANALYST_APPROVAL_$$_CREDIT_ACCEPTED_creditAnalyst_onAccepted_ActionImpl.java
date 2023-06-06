package org.salgar.fsm.pekko.foureyes.credit.actions;

import lombok.RequiredArgsConstructor;
import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;
import org.salgar.fsm.pekko.foureyes.credit.model.Customer;
import org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants;
import org.salgar.pekko.fsm.foureyes.notifier.NotifierService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class WAITING_CREDIT_ANALYST_APPROVAL_$$_CREDIT_ACCEPTED_creditAnalyst_onAccepted_ActionImpl
    extends WAITING_CREDIT_ANALYST_APPROVAL_$$_CREDIT_ACCEPTED_creditAnalyst_onAccepted_Action {
    private final NotifierService notifierService;

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        List<Customer> customers = (List<Customer>) controlObject.get(PayloadVariableConstants.CREDIT_TENANTS);
        List<String> emails = new ArrayList<>();
        for (Customer customer : customers) {

            emails.add(customer.getEmail());
        }
        notifierService.notify(emails, "Your credit application is accepted!");
        return payload;
    }
}
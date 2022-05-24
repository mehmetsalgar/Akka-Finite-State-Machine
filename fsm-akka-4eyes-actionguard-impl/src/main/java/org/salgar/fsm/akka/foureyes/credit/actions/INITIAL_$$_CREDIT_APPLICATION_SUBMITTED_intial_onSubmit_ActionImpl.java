package org.salgar.fsm.akka.foureyes.credit.actions;

import akka.actor.typed.scaladsl.ActorContext;
import lombok.RequiredArgsConstructor;
import org.salgar.akka.fsm.foureyes.cra.kafka.CustomerRelationshipAdapter;
import org.salgar.akka.fsm.foureyes.cra.model.CRMCustomer;
import org.salgar.akka.fsm.foureyes.notifier.NotifierService;
import org.salgar.fsm.akka.foureyes.credit.CreditSM;
import org.salgar.fsm.akka.foureyes.credit.model.CreditApplication;
import org.salgar.fsm.akka.foureyes.credit.model.Customer;
import org.salgar.fsm.akka.foureyes.usecasekey.CreditUseCaseKeyStrategy;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.salgar.akka.fsm.foureyes.notifier.NotificationHelper.RELATIONSHIP_MANAGER_NOTIFICATION_LIST;
import static org.salgar.fsm.akka.foureyes.variables.PayloadVariableConstants.*;

@RequiredArgsConstructor
public class INITIAL_$$_CREDIT_APPLICATION_SUBMITTED_intial_onSubmit_ActionImpl
    extends INITIAL_$$_CREDIT_APPLICATION_SUBMITTED_intial_onSubmit_Action {
    private final CustomerRelationshipAdapter customerRelationshipAdapter;
    private final NotifierService notifierService;

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {

        List<String> notificationList = notifierService.calculateRecipientList(RELATIONSHIP_MANAGER_NOTIFICATION_LIST);
        notifierService.notify(notificationList, "Credit Tenants applied for Credit. Please check!");
        controlObject.put(RELATIONSHIP_MANAGERS, notificationList);

        String creditId = (String) payload.get(CreditUseCaseKeyStrategy.CREDIT_UUID);
        CreditApplication creditApplication = (CreditApplication) payload.get(CREDIT_APPLICATION);

        controlObject.put(CreditUseCaseKeyStrategy.CREDIT_UUID, creditId);
        controlObject.put(CREDIT_AMOUNT, creditApplication.getCreditAmount());
        controlObject.put(CREDIT_TENANTS, creditApplication.getCreditTenants().getCreditTenants());

        for (Customer customer: creditApplication.getCreditTenants().getCreditTenants()) {
            CRMCustomer crmCustomer =
                    new CRMCustomer(
                            customer.getFirstName(),
                            customer.getLastName());
            customerRelationshipAdapter.transferCustomerCreation(crmCustomer);
        }

        return Collections.emptyMap();
    }
}

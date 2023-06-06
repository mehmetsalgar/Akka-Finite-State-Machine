package org.salgar.fsm.pekko.foureyes.credit.actions;

import lombok.RequiredArgsConstructor;
import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;
import org.salgar.fsm.pekko.foureyes.credit.model.CreditApplication;
import org.salgar.fsm.pekko.foureyes.credit.model.Customer;
import org.salgar.fsm.pekko.foureyes.usecasekey.CreditUseCaseKeyStrategy;
import org.salgar.pekko.fsm.foureyes.cra.kafka.CustomerRelationshipAdapter;
import org.salgar.pekko.fsm.foureyes.cra.model.CRMCustomer;
import org.salgar.pekko.fsm.foureyes.notifier.NotifierService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.CREDIT_AMOUNT;
import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.CREDIT_APPLICATION;
import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.CREDIT_TENANTS;
import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.RELATIONSHIP_MANAGERS;
import static org.salgar.pekko.fsm.foureyes.notifier.NotificationHelper.RELATIONSHIP_MANAGER_NOTIFICATION_LIST;

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

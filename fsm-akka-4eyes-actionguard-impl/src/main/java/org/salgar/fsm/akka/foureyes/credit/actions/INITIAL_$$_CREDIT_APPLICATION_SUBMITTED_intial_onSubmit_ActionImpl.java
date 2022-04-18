package org.salgar.fsm.akka.foureyes.credit.actions;

import akka.actor.typed.scaladsl.ActorContext;
import lombok.RequiredArgsConstructor;
import org.salgar.akka.fsm.foureyes.cra.kafka.CustomerRelationshipAdapter;
import org.salgar.akka.fsm.foureyes.cra.model.CRMCustomer;
import org.salgar.akka.fsm.foureyes.notifier.NotifierService;
import org.salgar.fsm.akka.foureyes.credit.CreditSM;
import org.salgar.fsm.akka.foureyes.credit.model.CustomerV2;
import org.salgar.fsm.akka.foureyes.usecasekey.CreditUseCaseKeyStrategy;

import java.util.List;
import java.util.Map;

import static org.salgar.akka.fsm.foureyes.notifier.NotificationHelper.RELATIONSHIP_MANAGER_NOTIFICATION_LIST;
import static org.salgar.fsm.akka.foureyes.variables.PayloadVariableConstants.CREDIT_TENANTS;

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

        String creditId = (String) payload.get(CreditUseCaseKeyStrategy.CREDIT_UUID);
        List<CustomerV2> customers = (List<CustomerV2>) payload.get(CREDIT_TENANTS);

        controlObject.put(CreditUseCaseKeyStrategy.CREDIT_UUID, creditId);
        controlObject.put(CREDIT_TENANTS, customers);

        for (CustomerV2 customer: customers) {
            CRMCustomer crmCustomer =
                    new CRMCustomer(
                            customer.getFirstname(),
                            customer.getLastname());
            customerRelationshipAdapter.transferCustomerCreation(crmCustomer);
        }

        return payload;
    }
}

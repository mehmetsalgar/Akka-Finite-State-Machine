package org.salgar.fsm.akka.foureyes.creditscore.actions;

import akka.actor.typed.scaladsl.ActorContext;
import lombok.RequiredArgsConstructor;
import org.salgar.fsm.akka.foureyes.credit.model.Customer;
import org.salgar.fsm.akka.foureyes.creditscore.MultiTenantCreditScoreSM;
import org.salgar.fsm.akka.foureyes.creditscore.facade.CreditScoreSMFacade;
import org.salgar.fsm.akka.foureyes.usecasekey.CreditUseCaseKeyStrategy;
import org.salgar.fsm.akka.foureyes.variables.PayloadVariableConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class INITIAL_MTCS_$$_WAITING_MULTI_TENANT_RESULTS_initial_WaitingMultiTenantResult_ActionImpl
    extends INITIAL_MTCS_$$_WAITING_MULTI_TENANT_RESULTS_initial_WaitingMultiTenantResult_Action {
    private final CreditScoreSMFacade creditScoreSMFacade;

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<MultiTenantCreditScoreSM.MultiTenantCreditScoreSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        String creditUuid = (String) payload.get(CreditUseCaseKeyStrategy.CREDIT_UUID);
        List<Customer> creditTenants = (List<Customer>) payload.get(PayloadVariableConstants.CREDIT_TENANTS);

        for(Customer customer : creditTenants) {
            Map<String, Object> creditScorePayload = new HashMap<>();
            creditScorePayload.put(CreditUseCaseKeyStrategy.CREDIT_UUID, creditUuid);
            creditScorePayload.put(PayloadVariableConstants.CUSTOMER, customer);

            creditScoreSMFacade.startCreditScoreResearch(
                    () -> creditUuid + "_" + customer.getPersonalId(),
                    creditScorePayload);
        }
        return payload;
    }
}
package org.salgar.fsm.akka.foureyes.creditscore.guards;

import akka.actor.typed.scaladsl.ActorContext;
import org.junit.jupiter.api.Test;
import org.salgar.fsm.akka.foureyes.credit.model.CreditTenantScoreResult;
import org.salgar.fsm.akka.foureyes.credit.model.CustomerV2;
import org.salgar.fsm.akka.foureyes.creditscore.MultiTenantCreditScoreSM;
import org.salgar.fsm.akka.foureyes.usecasekey.CreditUseCaseKeyStrategy;
import org.salgar.fsm.akka.foureyes.variables.PayloadVariableConstants;
import org.slf4j.Logger;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WAITING_MULTI_TENANT_RESULTS_WAITING_MULTI_TENANT_RESULTS_isNotAllTenantsResponded_GuardImplTest
        extends TestBase {
    @Test
    public void tenantSizeOneResultReceivedTest() {
        final WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_isNotAllTenantsResponded_GuardImpl guard =
                new WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_isNotAllTenantsResponded_GuardImpl();

        final ActorContext<MultiTenantCreditScoreSM.MultiTenantCreditScoreSMEvent> actorContext =
                mock(ActorContext.class);
        final Logger log = mock(Logger.class);

        when(actorContext.log()).thenReturn(log);

        final List<CustomerV2> creditTenants = new ArrayList<>();
        final CustomerV2 customer = prepareCustomerV2(
                "123456789X",
                "John",
                "Doe",
                "max@muster.com");

        creditTenants.add(customer);

        Map<String, Object> controlObjects = prepareControlObject(
                UUID.randomUUID().toString(),
                creditTenants
        );

        Map<String, Object> payload = new HashMap<>();
        payload.put(PayloadVariableConstants.CUSTOMER_ID, "123456789X");
        payload.put(PayloadVariableConstants.CREDIT_SCORE_RESULT, 84.51);
        boolean result = guard.evaluate(
                actorContext,
                controlObjects,
                payload);

        assertTrue(result);
    }

    @Test
    public void tenantSizeFiveOneResultReceivedTest() {
        final WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_isNotAllTenantsResponded_GuardImpl guard =
                new WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_isNotAllTenantsResponded_GuardImpl();


        final ActorContext<MultiTenantCreditScoreSM.MultiTenantCreditScoreSMEvent> actorContext =
                mock(ActorContext.class);
        final Logger log = mock(Logger.class);

        when(actorContext.log()).thenReturn(log);

        final List<CustomerV2> creditTenants = prepareCustomers();

        Map<String, Object> controlObjects = prepareControlObject(
                UUID.randomUUID().toString(),
                creditTenants
        );

        Map<String, Object> payload = new HashMap<>();
        payload.put(PayloadVariableConstants.CUSTOMER_ID, "K659453WE456t");
        payload.put(PayloadVariableConstants.CREDIT_SCORE_RESULT, 66.83);
        boolean result = guard.evaluate(
                actorContext,
                controlObjects,
                payload);

        assertTrue(result);
    }

    @Test
    public void tenantSizeFiveThreeResultExistOneResultReceivedTest() {
        final WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_isNotAllTenantsResponded_GuardImpl guard =
                new WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_isNotAllTenantsResponded_GuardImpl();


        final ActorContext<MultiTenantCreditScoreSM.MultiTenantCreditScoreSMEvent> actorContext =
                mock(ActorContext.class);
        final Logger log = mock(Logger.class);

        when(actorContext.log()).thenReturn(log);

        final List<CustomerV2> creditTenants = prepareCustomers();

        Map<String, Object> controlObject = prepareControlObject(
                UUID.randomUUID().toString(),
                creditTenants
        );
        Map<String, CreditTenantScoreResult> creditTenantScoreResultMap = new HashMap<>();
        creditTenantScoreResultMap.put(
                creditTenants.get(0).getCustomerId(), new CreditTenantScoreResult(creditTenants.get(0).getCustomerId(), 91.54));
        creditTenantScoreResultMap.put(
                creditTenants.get(1).getCustomerId(), new CreditTenantScoreResult(creditTenants.get(1).getCustomerId(), 11.69));
        creditTenantScoreResultMap.put(
                creditTenants.get(2).getCustomerId(), new CreditTenantScoreResult(creditTenants.get(2).getCustomerId(), 62.72));

        controlObject.put(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS, creditTenantScoreResultMap);

        Map<String, Object> payload = new HashMap<>();
        payload.put(PayloadVariableConstants.CUSTOMER_ID, creditTenants.get(3).getCustomerId());
        payload.put(PayloadVariableConstants.CREDIT_SCORE_RESULT, 66.83);
        boolean result = guard.evaluate(
                actorContext,
                controlObject,
                payload);

        assertTrue(result);
    }

    @Test
    public void tenantSizeFiveFourResultExistOneResultReceivedTest() {
        final WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_isNotAllTenantsResponded_GuardImpl guard =
                new WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_isNotAllTenantsResponded_GuardImpl();


        final ActorContext<MultiTenantCreditScoreSM.MultiTenantCreditScoreSMEvent> actorContext =
                mock(ActorContext.class);
        final Logger log = mock(Logger.class);

        when(actorContext.log()).thenReturn(log);

        final List<CustomerV2> creditTenants = prepareCustomers();

        Map<String, Object> controlObject = prepareControlObject(
                UUID.randomUUID().toString(),
                creditTenants
        );
        Map<String, CreditTenantScoreResult> creditTenantScoreResultMap = new HashMap<>();
        creditTenantScoreResultMap.put(
                creditTenants.get(0).getCustomerId(), new CreditTenantScoreResult(creditTenants.get(0).getCustomerId(), 91.54));
        creditTenantScoreResultMap.put(
                creditTenants.get(1).getCustomerId(), new CreditTenantScoreResult(creditTenants.get(1).getCustomerId(), 11.69));
        creditTenantScoreResultMap.put(
                creditTenants.get(2).getCustomerId(), new CreditTenantScoreResult(creditTenants.get(2).getCustomerId(), 62.72));
        creditTenantScoreResultMap.put(
                creditTenants.get(4).getCustomerId(), new CreditTenantScoreResult(creditTenants.get(4).getCustomerId(), 62.72));

        controlObject.put(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS, creditTenantScoreResultMap);

        Map<String, Object> payload = new HashMap<>();
        payload.put(PayloadVariableConstants.CUSTOMER_ID, creditTenants.get(3).getCustomerId());
        payload.put(PayloadVariableConstants.CREDIT_SCORE_RESULT, 66.83);
        boolean result = guard.evaluate(
                actorContext,
                controlObject,
                payload);

        assertFalse(result);
    }

    private Map<String, Object> prepareControlObject(
            String creditUuid,
            List<CustomerV2> creditTenants) {

        final Map<String, Object> payload = new HashMap<>();
        payload.put(CreditUseCaseKeyStrategy.CREDIT_UUID, creditUuid);
        payload.put(PayloadVariableConstants.CREDIT_TENANTS, creditTenants);

        return payload;
    }
}
package org.salgar.fsm.pekko.foureyes.credit.guards;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;
import org.salgar.fsm.pekko.foureyes.credit.model.CreditTenantScoreResult;
import org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.salgar.fsm.pekko.foureyes.slaves.SlaveStatemachineConstants.ADDRESS_CHECK_SM;
import static org.salgar.fsm.pekko.foureyes.slaves.SlaveStatemachineConstants.CUSTOMER_SCORE_SM;
import static org.salgar.fsm.pekko.foureyes.slaves.SlaveStatemachineConstants.FRAUD_PREVENTION_SM;
import static org.salgar.fsm.pekko.foureyes.slaves.SlaveStatemachineConstants.SOURCE_SLAVE_SM_TAG;

@ExtendWith(MockitoExtension.class)
public class SALES_MANAGER_APPROVED_WAITING_CREDIT_ANALYST_APPROVAL_isResultSufficientGuard_GuardImplTest {
    @Mock
    private ActorContext<CreditSM.CreditSMEvent> actorContext;

    @Mock
    private Logger log;

    @Test
    public void evaluationCreditScorePositiveTest() {
        SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_isResultSufficientGuard_GuardImpl guard =
                new SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_isResultSufficientGuard_GuardImpl();

        when(actorContext.log()).thenReturn(log);
        Map<String, Object> controlObject = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        payload.put(SOURCE_SLAVE_SM_TAG, CUSTOMER_SCORE_SM);
        Map<String, CreditTenantScoreResult> creditTenantResults = new HashMap<>();
        creditTenantResults.put("1234567", new CreditTenantScoreResult("1234567", 81.00));
        payload.put(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS, creditTenantResults);

        controlObject.put(PayloadVariableConstants.FRAUD_PREVENTION_RESULT, true);
        controlObject.put(PayloadVariableConstants.ADDRESS_CHECK_RESULT, true);

        boolean result = guard.evaluate(actorContext, controlObject, payload);

        Assertions.assertTrue(result);
    }

    @Test
    public void evaluationCreditScoreNegativeTest() {
        SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_isResultSufficientGuard_GuardImpl guard =
                new SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_isResultSufficientGuard_GuardImpl();

        when(actorContext.log()).thenReturn(log);
        Map<String, Object> controlObject = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        payload.put(SOURCE_SLAVE_SM_TAG, CUSTOMER_SCORE_SM);
        Map<String, CreditTenantScoreResult> creditTenantResults = new HashMap<>();
        creditTenantResults.put("1234567", new CreditTenantScoreResult("1234567", 81.00));
        payload.put(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS, creditTenantResults);

        controlObject.put(PayloadVariableConstants.FRAUD_PREVENTION_RESULT, false);
        controlObject.put(PayloadVariableConstants.ADDRESS_CHECK_RESULT, false);

        boolean result = guard.evaluate(actorContext, controlObject, payload);

        Assertions.assertFalse(result);
    }

    @Test
    public void evaluationCreditScoreNegativeMissingTest() {
        SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_isResultSufficientGuard_GuardImpl guard =
                new SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_isResultSufficientGuard_GuardImpl();

        when(actorContext.log()).thenReturn(log);
        Map<String, Object> controlObject = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        payload.put(SOURCE_SLAVE_SM_TAG, CUSTOMER_SCORE_SM);
        Map<String, CreditTenantScoreResult> creditTenantResults = new HashMap<>();
        creditTenantResults.put("1234567", new CreditTenantScoreResult("1234567", 81.00));
        payload.put(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS, creditTenantResults);

        boolean result = guard.evaluate(actorContext, controlObject, payload);

        Assertions.assertFalse(result);
    }

    @Test
    public void evaluationCreditScoreNegativeScoreIsNotEnoughTest() {
        SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_isResultSufficientGuard_GuardImpl guard =
                new SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_isResultSufficientGuard_GuardImpl();

        when(actorContext.log()).thenReturn(log);
        Map<String, Object> controlObject = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        payload.put(SOURCE_SLAVE_SM_TAG, CUSTOMER_SCORE_SM);
        Map<String, CreditTenantScoreResult> creditTenantResults = new HashMap<>();
        creditTenantResults.put("1234567", new CreditTenantScoreResult("1234567", 50.00));
        payload.put(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS, creditTenantResults);

        controlObject.put(PayloadVariableConstants.FRAUD_PREVENTION_RESULT, true);
        controlObject.put(PayloadVariableConstants.ADDRESS_CHECK_RESULT, true);

        boolean result = guard.evaluate(actorContext, controlObject, payload);

        Assertions.assertFalse(result);
    }

    @Test
    public void evaluationAddressCheckPositiveTest() {
        SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_isResultSufficientGuard_GuardImpl guard =
                new SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_isResultSufficientGuard_GuardImpl();

        when(actorContext.log()).thenReturn(log);
        Map<String, Object> controlObject = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        payload.put(SOURCE_SLAVE_SM_TAG, ADDRESS_CHECK_SM);

        payload.put(PayloadVariableConstants.ADDRESS_CHECK_RESULT, true);

        Map<String, CreditTenantScoreResult> creditTenantResults = new HashMap<>();
        creditTenantResults.put("1234567", new CreditTenantScoreResult("1234567", 81.00));
        controlObject.put(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS, creditTenantResults);

        controlObject.put(PayloadVariableConstants.FRAUD_PREVENTION_RESULT, true);

        boolean result = guard.evaluate(actorContext, controlObject, payload);

        Assertions.assertTrue(result);
    }

    @Test
    public void evaluationAddressCheckNegativeTest() {
        SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_isResultSufficientGuard_GuardImpl guard =
                new SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_isResultSufficientGuard_GuardImpl();

        when(actorContext.log()).thenReturn(log);
        Map<String, Object> controlObject = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        payload.put(SOURCE_SLAVE_SM_TAG, ADDRESS_CHECK_SM);

        payload.put(PayloadVariableConstants.ADDRESS_CHECK_RESULT, true);

        Map<String, CreditTenantScoreResult> creditTenantResults = new HashMap<>();
        creditTenantResults.put("1234567", new CreditTenantScoreResult("1234567", 81.00));
        controlObject.put(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS, creditTenantResults);

        controlObject.put(PayloadVariableConstants.FRAUD_PREVENTION_RESULT, false);

        boolean result = guard.evaluate(actorContext, controlObject, payload);

        Assertions.assertFalse(result);
    }

    @Test
    public void evaluationAddressCheckNegativeMissingTest() {
        SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_isResultSufficientGuard_GuardImpl guard =
                new SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_isResultSufficientGuard_GuardImpl();

        when(actorContext.log()).thenReturn(log);
        Map<String, Object> controlObject = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        payload.put(SOURCE_SLAVE_SM_TAG, ADDRESS_CHECK_SM);

        payload.put(PayloadVariableConstants.ADDRESS_CHECK_RESULT, true);

        boolean result = guard.evaluate(actorContext, controlObject, payload);

        Assertions.assertFalse(result);
    }

    @Test
    public void evaluationFraudPreventionPositiveTest() {
        SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_isResultSufficientGuard_GuardImpl guard =
                new SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_isResultSufficientGuard_GuardImpl();

        when(actorContext.log()).thenReturn(log);
        Map<String, Object> controlObject = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        payload.put(SOURCE_SLAVE_SM_TAG, FRAUD_PREVENTION_SM);

        payload.put(PayloadVariableConstants.FRAUD_PREVENTION_RESULT, true);

        Map<String, CreditTenantScoreResult> creditTenantResults = new HashMap<>();
        creditTenantResults.put("1234567", new CreditTenantScoreResult("1234567", 81.00));
        controlObject.put(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS, creditTenantResults);

        controlObject.put(PayloadVariableConstants.ADDRESS_CHECK_RESULT, true);

        boolean result = guard.evaluate(actorContext, controlObject, payload);

        Assertions.assertTrue(result);
    }

    @Test
    public void evaluationFraudPreventionNegativeTest() {
        SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_isResultSufficientGuard_GuardImpl guard =
                new SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_isResultSufficientGuard_GuardImpl();

        when(actorContext.log()).thenReturn(log);
        Map<String, Object> controlObject = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        payload.put(SOURCE_SLAVE_SM_TAG, FRAUD_PREVENTION_SM);

        payload.put(PayloadVariableConstants.FRAUD_PREVENTION_RESULT, false);

        Map<String, CreditTenantScoreResult> creditTenantResults = new HashMap<>();
        creditTenantResults.put("1234567", new CreditTenantScoreResult("1234567", 81.00));
        controlObject.put(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS, creditTenantResults);

        controlObject.put(PayloadVariableConstants.ADDRESS_CHECK_RESULT, true);

        boolean result = guard.evaluate(actorContext, controlObject, payload);

        Assertions.assertFalse(result);
    }

    @Test
    public void evaluationFraudPreventionNegativeMissingTest() {
        SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_isResultSufficientGuard_GuardImpl guard =
                new SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_isResultSufficientGuard_GuardImpl();

        when(actorContext.log()).thenReturn(log);
        Map<String, Object> controlObject = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        payload.put(SOURCE_SLAVE_SM_TAG, FRAUD_PREVENTION_SM);

        payload.put(PayloadVariableConstants.FRAUD_PREVENTION_RESULT, false);

        boolean result = guard.evaluate(actorContext, controlObject, payload);

        Assertions.assertFalse(result);
    }
}
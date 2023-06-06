package org.salgar.fsm.pekko.foureyes.credit.guards;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;
import org.salgar.fsm.pekko.foureyes.credit.model.CreditTenantScoreResult;
import org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants;

import java.util.Map;

import static org.salgar.fsm.pekko.foureyes.slaves.SlaveStatemachineConstants.ADDRESS_CHECK_SM;
import static org.salgar.fsm.pekko.foureyes.slaves.SlaveStatemachineConstants.CUSTOMER_SCORE_SM;
import static org.salgar.fsm.pekko.foureyes.slaves.SlaveStatemachineConstants.FRAUD_PREVENTION_SM;
import static org.salgar.fsm.pekko.foureyes.slaves.SlaveStatemachineConstants.SOURCE_SLAVE_SM_TAG;

public class SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_isResultSufficientGuard_GuardImpl
    implements SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_isResultSufficientGuard_Guard {
    @Override
    public boolean evaluate(
            ActorContext<CreditSM.CreditSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload) {
        actorContext.log().debug("Evaluating SALES_MANAGER_APPROVED salesManagerApproved_onResultReceived Guard");

        String slaveSM = (String) payload.get(SOURCE_SLAVE_SM_TAG);

        if(CUSTOMER_SCORE_SM.equals(slaveSM)) {
            return checkResultCreditScore(
                    actorContext,
                    controlObject,
                    payload
            );
        } else if(FRAUD_PREVENTION_SM.equals(slaveSM)) {
            return checkResultFraudPrevention(
                    actorContext,
                    controlObject,
                    payload
            );
        } else if(ADDRESS_CHECK_SM.equals(slaveSM)) {
            return checkResultFromAddressCheck(
                    actorContext,
                    controlObject,
                    payload
            );
        }

        return false;
    }

    private boolean checkResultCreditScore(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                               Map<String, Object> controlObject,
                                               Map<String, Object> payload) {
        Object csr = payload.get(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS);
        if(csr == null) {
            return false;
        }
        Map<String, CreditTenantScoreResult> creditTenantScoreResultMap =
                (Map<String, CreditTenantScoreResult>) csr;

        Object fpr = controlObject.get(PayloadVariableConstants.FRAUD_PREVENTION_RESULT);
        if(fpr == null) {
            return false;
        }
        boolean fraudPreventionResult = (boolean) fpr;

        Object acr = controlObject.get(PayloadVariableConstants.ADDRESS_CHECK_RESULT);
        if(acr == null) {
            return false;
        }
        boolean addressCheckResult = (boolean) acr;

        return checkCondition(creditTenantScoreResultMap, fraudPreventionResult, addressCheckResult);
    }

    private boolean checkResultFraudPrevention(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                Map<String, Object> controlObject,
                                                Map<String, Object> payload) {
        Object csr = controlObject.get(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS);
        if(csr == null) {
            return false;
        }
        Map<String, CreditTenantScoreResult> creditTenantScoreResultMap =
                (Map<String, CreditTenantScoreResult>) csr;

        Object fpr = payload.get(PayloadVariableConstants.FRAUD_PREVENTION_RESULT);
        if(fpr == null) {
            return false;
        }
        boolean fraudPreventionResult = (boolean) fpr;

        Object acr = controlObject.get(PayloadVariableConstants.ADDRESS_CHECK_RESULT);
        if(acr == null) {
            return false;
        }
        boolean addressCheckResult = (boolean) acr;

        return checkCondition(creditTenantScoreResultMap, fraudPreventionResult, addressCheckResult);
    }

    private boolean checkResultFromAddressCheck(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                Map<String, Object> controlObject,
                                                Map<String, Object> payload) {
        Object csr = controlObject.get(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS);
        if(csr == null) {
            return false;
        }
        Map<String, CreditTenantScoreResult> creditTenantScoreResultMap =
                (Map<String, CreditTenantScoreResult>) csr;

        Object fpr = controlObject.get(PayloadVariableConstants.FRAUD_PREVENTION_RESULT);
        if(fpr == null) {
            return false;
        }
        boolean fraudPreventionResult = (boolean) fpr;

        Object acr = payload.get(PayloadVariableConstants.ADDRESS_CHECK_RESULT);
        if(acr == null) {
            return false;
        }
        boolean addressCheckResult = (boolean) acr;

        return checkCondition(creditTenantScoreResultMap, fraudPreventionResult, addressCheckResult);
    }

    private boolean checkCondition(
            Map<String, CreditTenantScoreResult> creditScores,
            boolean fraudPreventionResult,
            boolean addressCheckResult) {
        if(CreditScoreGaurdUtilities.checkCreditScore(creditScores, 70.00, 100.00)
                && fraudPreventionResult
                && addressCheckResult) {
            return true;
        }
        return false;
    }
}
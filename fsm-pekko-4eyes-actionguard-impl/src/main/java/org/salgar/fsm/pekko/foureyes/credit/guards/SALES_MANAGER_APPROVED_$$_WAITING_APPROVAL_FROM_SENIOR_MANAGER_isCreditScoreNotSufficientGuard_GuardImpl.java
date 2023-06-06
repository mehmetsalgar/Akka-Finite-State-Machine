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
import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.CREDIT_AMOUNT;

public class SALES_MANAGER_APPROVED_$$_WAITING_APPROVAL_FROM_SENIOR_MANAGER_isCreditScoreNotSufficientGuard_GuardImpl
    implements SALES_MANAGER_APPROVED_$$_WAITING_APPROVAL_FROM_SENIOR_MANAGER_isCreditScoreNotSufficientGuard_Guard {
    @Override
    public boolean evaluate(
            ActorContext<CreditSM.CreditSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload) {
        actorContext.log().debug("Evaluating SALES_MANAGER_APPROVED salesManagerApproved_onCreditScoreReceived Guard");

        String slaveSM = (String) payload.get(SOURCE_SLAVE_SM_TAG);
        Double creditAmount = (Double) controlObject.get(CREDIT_AMOUNT);

        if(CUSTOMER_SCORE_SM.equals(slaveSM)) {
            Object csr = payload.get(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS);
            Object fpr = controlObject.get(PayloadVariableConstants.FRAUD_PREVENTION_RESULT);
            Object acr = controlObject.get(PayloadVariableConstants.ADDRESS_CHECK_RESULT);
            if(isAllResponsesCompleteCreditScore(csr, fpr, acr)) {
                if(creditAmount >= 10000000.0) {
                    return true;
                } else {
                    return checkCondition(csr, fpr, acr);
                }
            }
        } else if(FRAUD_PREVENTION_SM.equals(slaveSM)) {
            Object csr = controlObject.get(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS);
            Object fpr = payload.get(PayloadVariableConstants.FRAUD_PREVENTION_RESULT);
            Object acr = controlObject.get(PayloadVariableConstants.ADDRESS_CHECK_RESULT);
            if(isAllResponsesFP(csr, fpr, acr)) {
                if(creditAmount >= 10000000.0) {
                    return true;
                } else {
                    return checkCondition(csr, fpr, acr);
                }
            }
        } else if(ADDRESS_CHECK_SM.equals(slaveSM)) {
            Object csr = controlObject.get(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS);
            Object fpr = controlObject.get(PayloadVariableConstants.FRAUD_PREVENTION_RESULT);
            Object acr = payload.get(PayloadVariableConstants.ADDRESS_CHECK_RESULT);
            if(isAllResponsesAC(csr, fpr, acr)) {
                if(creditAmount >= 10000000.0) {
                    return true;
                } else {
                    return checkCondition(csr, fpr, acr);
                }
            }
        }

        return false;
    }

    private boolean isAllResponsesCompleteCreditScore(Object csr,
                                                      Object fpr,
                                                      Object acr) {
        if(csr == null) {
            return false;
        }
        if(fpr == null) {
            return false;
        }
        return acr != null;
    }

    private boolean isAllResponsesFP(Object csr,
                                     Object fpr,
                                     Object acr) {
        if(csr == null) {
            return false;
        }
        if(fpr == null) {
            return false;
        }
        return acr != null;
    }

    private boolean isAllResponsesAC(Object csr,
                                     Object fpr,
                                     Object acr) {
        if(csr == null) {
            return false;
        }
        if(fpr == null) {
            return false;
        }
        return acr != null;
    }

    private boolean checkCondition(
            Object csr,
            Object fpr,
            Object acr) {
        Map<String, CreditTenantScoreResult> creditTenantScoreResultMap =
                (Map<String, CreditTenantScoreResult>) csr;
        boolean fraudPreventionResult = (boolean) fpr;
        boolean addressCheckResult = (boolean) acr;
        return checkCondition(creditTenantScoreResultMap, fraudPreventionResult, addressCheckResult);
    }

    private boolean checkCondition(
            Map<String, CreditTenantScoreResult> creditTenantScoreResultMap,
            boolean fraudPreventionResult,
            boolean addressCheckResult
    ) {
        if(CreditScoreGaurdUtilities.checkCreditScore(creditTenantScoreResultMap, 60.00, 70.00)
                && fraudPreventionResult
                && addressCheckResult) {
            return true;
        }
        return false;
    }
}
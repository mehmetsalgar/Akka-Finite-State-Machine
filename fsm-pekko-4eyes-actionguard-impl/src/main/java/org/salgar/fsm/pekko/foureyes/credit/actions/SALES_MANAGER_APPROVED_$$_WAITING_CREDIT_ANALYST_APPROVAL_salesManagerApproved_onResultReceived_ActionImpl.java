package org.salgar.fsm.pekko.foureyes.credit.actions;

import lombok.RequiredArgsConstructor;
import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;
import org.salgar.fsm.pekko.foureyes.credit.model.CreditTenantScoreResult;
import org.salgar.pekko.fsm.foureyes.notifier.NotifierService;

import java.util.List;
import java.util.Map;

import static org.salgar.fsm.pekko.foureyes.slaves.SlaveStatemachineConstants.ADDRESS_CHECK_SM;
import static org.salgar.fsm.pekko.foureyes.slaves.SlaveStatemachineConstants.CUSTOMER_SCORE_SM;
import static org.salgar.fsm.pekko.foureyes.slaves.SlaveStatemachineConstants.FRAUD_PREVENTION_SM;
import static org.salgar.fsm.pekko.foureyes.slaves.SlaveStatemachineConstants.SOURCE_SLAVE_SM_TAG;
import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.ADDRESS_CHECK_RESULT;
import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.CREDIT_ANALYSTS;
import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS;
import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.FRAUD_PREVENTION_RESULT;
import static org.salgar.pekko.fsm.foureyes.notifier.NotificationHelper.CREDIT_ANALYST_NOTIFICATION_LIST;

@RequiredArgsConstructor
public class SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_salesManagerApproved_onResultReceived_ActionImpl
    extends SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_salesManagerApproved_onResultReceived_Action {
    private final NotifierService notifierService;

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {

        String slaveSM = (String) payload.get(SOURCE_SLAVE_SM_TAG);
        if(CUSTOMER_SCORE_SM.equals(slaveSM)) {
            processCreditScoreResult(
                    actorContext,
                    controlObject,
                    payload
            );
        } else if(FRAUD_PREVENTION_SM.equals(slaveSM)) {
            processFraudPreventionResult(
                    actorContext,
                    controlObject,
                    payload
            );
        } else if(ADDRESS_CHECK_SM.equals(slaveSM)) {
            processAddressCheckResult(
                    actorContext,
                    controlObject,
                    payload
            );
        }
        List<String> notificationList = notifierService.calculateRecipientList(CREDIT_ANALYST_NOTIFICATION_LIST);
        notifierService
                .notify(notificationList, "Sales Manager Approved the Credit and Partner Systems delivered results"
                + " you should proceed. Please check!");

        controlObject.put(CREDIT_ANALYSTS, notificationList);

        return payload;
    }

    private void processCreditScoreResult(
            ActorContext<CreditSM.CreditSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload) {
        Map<String, CreditTenantScoreResult> creditTenantScoreResultMap =
                (Map<String, CreditTenantScoreResult>) payload.get(CREDIT_SCORE_TENANT_RESULTS);
        controlObject.put(CREDIT_SCORE_TENANT_RESULTS, creditTenantScoreResultMap);
    }

    private void processFraudPreventionResult(
            ActorContext<CreditSM.CreditSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload) {
        Boolean fraudPreventionkResult = (Boolean) payload.get(FRAUD_PREVENTION_RESULT);
        controlObject.put(FRAUD_PREVENTION_RESULT, fraudPreventionkResult);
    }

    private void processAddressCheckResult(
            ActorContext<CreditSM.CreditSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload) {
        Boolean addressCheckResult = (Boolean) payload.get(ADDRESS_CHECK_RESULT);
        controlObject.put(ADDRESS_CHECK_RESULT, addressCheckResult);
    }
}
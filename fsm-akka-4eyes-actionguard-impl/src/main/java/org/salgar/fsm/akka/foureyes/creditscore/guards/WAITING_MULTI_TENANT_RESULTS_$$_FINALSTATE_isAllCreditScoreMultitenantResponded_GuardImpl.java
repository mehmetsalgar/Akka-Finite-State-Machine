package org.salgar.fsm.akka.foureyes.creditscore.guards;

import akka.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.akka.foureyes.credit.model.CreditTenantScoreResult;
import org.salgar.fsm.akka.foureyes.credit.model.CustomerV2;
import org.salgar.fsm.akka.foureyes.creditscore.MultiTenantCreditScoreSM;
import org.salgar.fsm.akka.foureyes.variables.PayloadVariableConstants;

import java.util.List;
import java.util.Map;

public class WAITING_MULTI_TENANT_RESULTS_$$_FINALSTATE_isAllCreditScoreMultitenantResponded_GuardImpl
    implements WAITING_MULTI_TENANT_RESULTS_$$_FINALSTATE_isAllCreditScoreMultitenantResponded_Guard {
    @Override
    public boolean evaluate(
            ActorContext<MultiTenantCreditScoreSM.MultiTenantCreditScoreSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload) {
        actorContext.log().debug("Evaluating WAITING_MULTI_TENANT_RESULTS waitingMultiTenantResult_finalState Guard");

        List<CustomerV2> creditTenants = (List<CustomerV2>) controlObject.get(PayloadVariableConstants.CREDIT_TENANTS);

        String customerId = (String) payload.get(PayloadVariableConstants.CUSTOMER_ID);
        Double creditScoreResult = (Double) payload.get(PayloadVariableConstants.CREDIT_SCORE_RESULT);

        Map<String, CreditTenantScoreResult> creditTenantScoreResultMap =
                (Map<String, CreditTenantScoreResult>) controlObject.get(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS);

        if(creditScoreResult == null) {
            return false;
        }

        if(creditTenantScoreResultMap == null) {
            if(creditTenants.size() == 1) {
                return true;
            }
        } else  {
            if(creditTenantScoreResultMap.get(customerId) != null) {
                //We received the Event twice?
                return false;
            } else if(creditTenantScoreResultMap.size() + 1  == creditTenants.size()) {
                return true;
            }
        }

        return false;
    }
}
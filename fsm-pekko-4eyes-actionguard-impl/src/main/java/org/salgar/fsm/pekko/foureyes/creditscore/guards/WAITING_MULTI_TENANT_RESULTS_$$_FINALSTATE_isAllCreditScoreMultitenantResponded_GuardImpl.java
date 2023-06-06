package org.salgar.fsm.pekko.foureyes.creditscore.guards;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.model.CreditTenantScoreResult;
import org.salgar.fsm.pekko.foureyes.credit.model.Customer;
import org.salgar.fsm.pekko.foureyes.creditscore.MultiTenantCreditScoreSM;
import org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants;

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

        List<Customer> creditTenants = (List<Customer>) controlObject.get(PayloadVariableConstants.CREDIT_TENANTS);

        String personalId = (String) payload.get(PayloadVariableConstants.PERSONAL_ID);
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
            if(creditTenantScoreResultMap.get(personalId) != null) {
                //We received the Event twice?
                return false;
            } else if(creditTenantScoreResultMap.size() + 1  == creditTenants.size()) {
                return true;
            }
        }

        return false;
    }
}
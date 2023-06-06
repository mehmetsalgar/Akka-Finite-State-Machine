package org.salgar.fsm.pekko.foureyes.creditscore.guards;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.model.CreditTenantScoreResult;
import org.salgar.fsm.pekko.foureyes.credit.model.Customer;
import org.salgar.fsm.pekko.foureyes.creditscore.MultiTenantCreditScoreSM;
import org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants;

import java.util.List;
import java.util.Map;

public class WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_isNotAllTenantsResponded_GuardImpl
    implements WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_isNotAllTenantsResponded_Guard {
    @Override
    public boolean evaluate(
            ActorContext<MultiTenantCreditScoreSM.MultiTenantCreditScoreSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload) {
        actorContext.log().debug("Evaluating WAITING_MULTI_TENANT_RESULTS waitingMultiTenantResults_waitingMultiTenantResults Guard");

        List<Customer> creditTenants = (List<Customer>) controlObject.get(PayloadVariableConstants.CREDIT_TENANTS);

        String personalId = (String) payload.get(PayloadVariableConstants.PERSONAL_ID);
        Double creditScoreResult = (Double) payload.get(PayloadVariableConstants.CREDIT_SCORE_RESULT);

        Map<String, CreditTenantScoreResult> creditTenantScoreResultMap =
                (Map<String, CreditTenantScoreResult>) controlObject.get(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS);

        if(creditTenantScoreResultMap != null && creditTenantScoreResultMap.get(personalId) != null) {
            return false;
        }

        if(creditTenantScoreResultMap == null && creditScoreResult != null) {
            return true;
        } else {
            if(creditTenantScoreResultMap.size()  + 1 < creditTenants.size()) {
                return true;
            }
        }

        return false;
    }
}

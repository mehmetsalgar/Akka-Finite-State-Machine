package org.salgar.fsm.akka.foureyes.creditscore.guards;

import akka.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.akka.foureyes.credit.model.CreditTenantScoreResult;
import org.salgar.fsm.akka.foureyes.credit.model.CustomerV2;
import org.salgar.fsm.akka.foureyes.creditscore.MultiTenantCreditScoreSM;
import org.salgar.fsm.akka.foureyes.variables.PayloadVariableConstants;

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

        List<CustomerV2> creditTenants = (List<CustomerV2>) controlObject.get(PayloadVariableConstants.CREDIT_TENANTS);

        String customerId = (String) payload.get(PayloadVariableConstants.CUSTOMER_ID);
        Double creditScoreResult = (Double) payload.get(PayloadVariableConstants.CREDIT_SCORE_RESULT);

        Map<String, CreditTenantScoreResult> creditTenantScoreResultMap =
                (Map<String, CreditTenantScoreResult>) controlObject.get(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS);

        if(creditTenantScoreResultMap != null && creditTenantScoreResultMap.get(customerId) != null) {
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

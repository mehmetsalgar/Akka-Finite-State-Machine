package org.salgar.fsm.pekko.foureyes.creditscore.actions;

import lombok.extern.slf4j.Slf4j;
import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.model.CreditTenantScoreResult;
import org.salgar.fsm.pekko.foureyes.creditscore.MultiTenantCreditScoreSM;
import org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_waitingMultiTenantResults_waitingMultiTenantResults_ActionImpl
    extends WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_waitingMultiTenantResults_waitingMultiTenantResults_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<MultiTenantCreditScoreSM.MultiTenantCreditScoreSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {

        Double creditScoreResult = (Double) payload.get(PayloadVariableConstants.CREDIT_SCORE_RESULT);
        String personalId = (String) payload.get(PayloadVariableConstants.PERSONAL_ID);

        Map<String, CreditTenantScoreResult> creditTenantScoreResultMap =
                (Map<String, CreditTenantScoreResult>) controlObject.get(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS);

        if(creditTenantScoreResultMap == null) {
            creditTenantScoreResultMap = new HashMap<>();
        }

        if(creditTenantScoreResultMap.get(personalId) != null) {
            log.warn("We actually processed this customer for Credit Score: {}", creditTenantScoreResultMap.get(personalId) );
        }

        creditTenantScoreResultMap.put(
                personalId,
                new CreditTenantScoreResult(personalId, creditScoreResult));

        controlObject.put(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS, creditTenantScoreResultMap);

        return payload;
    }
}
package org.salgar.fsm.pekko.foureyes.creditscore.actions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.facade.CreditSMFacade;
import org.salgar.fsm.pekko.foureyes.credit.model.CreditTenantScoreResult;
import org.salgar.fsm.pekko.foureyes.creditscore.MultiTenantCreditScoreSM;
import org.salgar.fsm.pekko.foureyes.usecasekey.CreditUseCaseKeyStrategy;
import org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants;

import java.util.HashMap;
import java.util.Map;

import static org.salgar.fsm.pekko.foureyes.slaves.SlaveStatemachineConstants.CUSTOMER_SCORE_SM;
import static org.salgar.fsm.pekko.foureyes.slaves.SlaveStatemachineConstants.SOURCE_SLAVE_SM_TAG;

@RequiredArgsConstructor
@Slf4j
public class WAITING_MULTI_TENANT_RESULTS_$$_FINALSTATE_waitingMultiTenantResult_finalState_ActionImpl
    extends WAITING_MULTI_TENANT_RESULTS_$$_FINALSTATE_waitingMultiTenantResult_finalState_Action {
    private final CreditSMFacade creditSMFacade;

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
            log.warn("We actually processed this customer for Credit Score: {}", creditTenantScoreResultMap.get(personalId));
        }

        creditTenantScoreResultMap.put(
                personalId,
                new CreditTenantScoreResult(personalId, creditScoreResult));
        controlObject.put(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS, creditTenantScoreResultMap);

        //Don't forget creditUUID
        String creditUuid = (String) controlObject.get(CreditUseCaseKeyStrategy.CREDIT_UUID);
        Map<String, Object> creditSmPayload = new HashMap<>();
        creditSmPayload.put(CreditUseCaseKeyStrategy.CREDIT_UUID, creditUuid);
        creditSmPayload.put(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS, creditTenantScoreResultMap);
        creditSmPayload.put(SOURCE_SLAVE_SM_TAG, CUSTOMER_SCORE_SM);
        creditSMFacade.resultReceived(creditSmPayload);

        return payload;
    }
}
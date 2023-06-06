package org.salgar.fsm.pekko.foureyes.creditscore.actions;

import lombok.RequiredArgsConstructor;
import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.model.Customer;
import org.salgar.fsm.pekko.foureyes.creditscore.CreditScoreSM;
import org.salgar.fsm.pekko.foureyes.creditscore.facade.MultiTenantCreditScoreSMFacade;
import org.salgar.fsm.pekko.foureyes.usecasekey.CreditUseCaseKeyStrategy;
import org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class WAITING_RESULT_$$_RESULTRECEIVED_waitingResult_onResultReceived_ActionImpl
    extends WAITING_RESULT_$$_RESULTRECEIVED_waitingResult_onResultReceived_Action {
    private final MultiTenantCreditScoreSMFacade multiTenantCreditScoreSMFacade;

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditScoreSM.CreditScoreSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        Double creditScoreResult = (Double) payload.get(PayloadVariableConstants.CREDIT_SCORE_RESULT);

        Map<String, Object> modifiedPayload = new HashMap<>();
        modifiedPayload.put(PayloadVariableConstants.CREDIT_SCORE_RESULT, creditScoreResult);

        //Don't forget creditUUID
        String creditUuid = (String) controlObject.get(CreditUseCaseKeyStrategy.CREDIT_UUID);
        Customer customer = (Customer) controlObject.get(PayloadVariableConstants.CUSTOMER);
        Map<String, Object> multiTenantCreditScoreSmPayload = new HashMap<>();
        multiTenantCreditScoreSmPayload.put(CreditUseCaseKeyStrategy.CREDIT_UUID, creditUuid);
        multiTenantCreditScoreSmPayload.put(PayloadVariableConstants.PERSONAL_ID, customer.getPersonalId());
        multiTenantCreditScoreSmPayload.put(PayloadVariableConstants.CREDIT_SCORE_RESULT, creditScoreResult);
        multiTenantCreditScoreSMFacade.creditScoreReceived(() -> creditUuid,  multiTenantCreditScoreSmPayload);

        return modifiedPayload;
    }
}
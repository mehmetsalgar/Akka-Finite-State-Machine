package org.salgar.fsm.pekko.foureyes.fraudprevention.actions;

import lombok.RequiredArgsConstructor;
import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.facade.CreditSMFacade;
import org.salgar.fsm.pekko.foureyes.fraudprevention.FraudPreventionSM;
import org.salgar.fsm.pekko.foureyes.usecasekey.CreditUseCaseKeyStrategy;
import org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants;

import java.util.HashMap;
import java.util.Map;

import static org.salgar.fsm.pekko.foureyes.slaves.SlaveStatemachineConstants.FRAUD_PREVENTION_SM;
import static org.salgar.fsm.pekko.foureyes.slaves.SlaveStatemachineConstants.SOURCE_SLAVE_SM_TAG;

@RequiredArgsConstructor
public class WAITING_RESULT_$$_RESULTRECEIVED_waitingResult_onResult_ActionImpl
    extends WAITING_RESULT_$$_RESULTRECEIVED_waitingResult_onResult_Action {
    private final CreditSMFacade creditSMFacade;

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<FraudPreventionSM.FraudPreventionSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        boolean fraudPreventionResult = (boolean) payload.get(PayloadVariableConstants.FRAUD_PREVENTION_RESULT);

        Map<String, Object> modifiedPayload = new HashMap<>();
        modifiedPayload.put(PayloadVariableConstants.FRAUD_PREVENTION_RESULT, fraudPreventionResult);

        //Don't forget creditUUID
        String creditUuid = (String) controlObject.get(CreditUseCaseKeyStrategy.CREDIT_UUID);
        Map<String, Object> creditSmPayload = new HashMap<>();
        creditSmPayload.put(CreditUseCaseKeyStrategy.CREDIT_UUID, creditUuid);
        creditSmPayload.put(PayloadVariableConstants.FRAUD_PREVENTION_RESULT, fraudPreventionResult);
        creditSmPayload.put(SOURCE_SLAVE_SM_TAG, FRAUD_PREVENTION_SM);
        creditSMFacade.resultReceived(creditSmPayload);

        return modifiedPayload;
    }
}
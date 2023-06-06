package org.salgar.fsm.pekko.foureyes.addresscheck.actions;

import lombok.RequiredArgsConstructor;
import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.addresscheck.AdressCheckSM;
import org.salgar.fsm.pekko.foureyes.credit.facade.CreditSMFacade;
import org.salgar.fsm.pekko.foureyes.usecasekey.CreditUseCaseKeyStrategy;
import org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants;

import java.util.HashMap;
import java.util.Map;

import static org.salgar.fsm.pekko.foureyes.slaves.SlaveStatemachineConstants.ADDRESS_CHECK_SM;
import static org.salgar.fsm.pekko.foureyes.slaves.SlaveStatemachineConstants.SOURCE_SLAVE_SM_TAG;

@RequiredArgsConstructor
public class WAITING_RESULT_$$_RESULTRECEIVED_waiting_onResult_ActionImpl
    extends WAITING_RESULT_$$_RESULTRECEIVED_waiting_onResult_Action {
    private final CreditSMFacade creditSMFacade;

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<AdressCheckSM.AdressCheckSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {

        boolean addressCheckResult = (boolean) payload.get(PayloadVariableConstants.ADDRESS_CHECK_RESULT);
        Map<String, Object> modifiedPayload = new HashMap<>();
        modifiedPayload.put(PayloadVariableConstants.ADDRESS_CHECK_RESULT, addressCheckResult);

        //Don't forget creditUUID
        String creditUuid = (String) controlObject.get(CreditUseCaseKeyStrategy.CREDIT_UUID);
        Map<String, Object> creditSmPayload = new HashMap<>();
        creditSmPayload.put(CreditUseCaseKeyStrategy.CREDIT_UUID, creditUuid);
        creditSmPayload.put(PayloadVariableConstants.ADDRESS_CHECK_RESULT, addressCheckResult);
        creditSmPayload.put(SOURCE_SLAVE_SM_TAG, ADDRESS_CHECK_SM);
        creditSMFacade.resultReceived(creditSmPayload);

        return modifiedPayload;
    }
}
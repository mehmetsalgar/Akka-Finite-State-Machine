package org.salgar.fsm.pekko.foureyes.fraudprevention.actions.config;

import org.salgar.fsm.pekko.foureyes.credit.facade.CreditSMFacade;
import org.salgar.fsm.pekko.foureyes.fraudprevention.actions.ERROR_$$_WAITING_RESULT_error_onRetry_Action;
import org.salgar.fsm.pekko.foureyes.fraudprevention.actions.ERROR_$$_WAITING_RESULT_error_onRetry_ActionImpl;
import org.salgar.fsm.pekko.foureyes.fraudprevention.actions.INITIAL_$$_WAITING_RESULT_initial_Action;
import org.salgar.fsm.pekko.foureyes.fraudprevention.actions.INITIAL_$$_WAITING_RESULT_initial_ActionImpl;
import org.salgar.fsm.pekko.foureyes.fraudprevention.actions.WAITING_RESULT_$$_ERROR_waitingResult_onError_Action;
import org.salgar.fsm.pekko.foureyes.fraudprevention.actions.WAITING_RESULT_$$_ERROR_waitingResult_onError_ActionImpl;
import org.salgar.fsm.pekko.foureyes.fraudprevention.actions.WAITING_RESULT_$$_RESULTRECEIVED_waitingResult_onResult_Action;
import org.salgar.fsm.pekko.foureyes.fraudprevention.actions.WAITING_RESULT_$$_RESULTRECEIVED_waitingResult_onResult_ActionImpl;
import org.salgar.pekko.fsm.foureyes.faudprevention.FraudPreventionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FraudPreventionSMActionConfiguration {
    @Bean
    public ERROR_$$_WAITING_RESULT_error_onRetry_Action fraudpreventionsm_error_WAITING_RESULT_error_onRetryAction() {
        return new ERROR_$$_WAITING_RESULT_error_onRetry_ActionImpl();
    }
    @Bean
    public INITIAL_$$_WAITING_RESULT_initial_Action fraudpreventionsm_initial_WAITING_RESULT_initialAction(
            FraudPreventionService fraudPreventionService
    ) {
        return new INITIAL_$$_WAITING_RESULT_initial_ActionImpl(fraudPreventionService);
    }
    @Bean
    public WAITING_RESULT_$$_ERROR_waitingResult_onError_Action fraudpreventionsm_waiting_result_ERROR_waitingResult_onErrorAction() {
        return new WAITING_RESULT_$$_ERROR_waitingResult_onError_ActionImpl();
    }
    @Bean
    public WAITING_RESULT_$$_RESULTRECEIVED_waitingResult_onResult_Action
        fraudpreventionsm_waiting_result_ResultReceived_waitingResult_onResultAction(CreditSMFacade creditSMFacade) {
        return new WAITING_RESULT_$$_RESULTRECEIVED_waitingResult_onResult_ActionImpl(creditSMFacade);
    }
}

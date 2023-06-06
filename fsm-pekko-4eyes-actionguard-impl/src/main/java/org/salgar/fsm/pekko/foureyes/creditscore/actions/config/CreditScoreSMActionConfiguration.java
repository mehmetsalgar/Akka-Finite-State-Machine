package org.salgar.fsm.pekko.foureyes.creditscore.actions.config;

import org.salgar.fsm.pekko.foureyes.creditscore.actions.ERROR_$$_WAITING_RESULT_error_onRetry_Action;
import org.salgar.fsm.pekko.foureyes.creditscore.actions.ERROR_$$_WAITING_RESULT_error_onRetry_ActionImpl;
import org.salgar.fsm.pekko.foureyes.creditscore.actions.INITIAL_$$_WAITING_RESULT_initial_Action;
import org.salgar.fsm.pekko.foureyes.creditscore.actions.INITIAL_$$_WAITING_RESULT_initial_ActionImpl;
import org.salgar.fsm.pekko.foureyes.creditscore.actions.WAITING_RESULT_$$_ERROR_waitingResult_onError_Action;
import org.salgar.fsm.pekko.foureyes.creditscore.actions.WAITING_RESULT_$$_ERROR_waitingResult_onError_ActionImpl;
import org.salgar.fsm.pekko.foureyes.creditscore.actions.WAITING_RESULT_$$_RESULTRECEIVED_waitingResult_onResultReceived_Action;
import org.salgar.fsm.pekko.foureyes.creditscore.actions.WAITING_RESULT_$$_RESULTRECEIVED_waitingResult_onResultReceived_ActionImpl;
import org.salgar.fsm.pekko.foureyes.creditscore.facade.MultiTenantCreditScoreSMFacade;
import org.salgar.pekko.fsm.foureyes.creditscore.CreditScoreService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreditScoreSMActionConfiguration {
    @Bean
    public ERROR_$$_WAITING_RESULT_error_onRetry_Action creditscoresm_error_WAiTING_RESULT_error_onRetryAction() {
        return new ERROR_$$_WAITING_RESULT_error_onRetry_ActionImpl();
    }
    @Bean
    public INITIAL_$$_WAITING_RESULT_initial_Action creditscoresm_initial_WAiTING_RESULT_initialAction(
            CreditScoreService creditScoreService
    ) {
        return new INITIAL_$$_WAITING_RESULT_initial_ActionImpl(creditScoreService);
    }
    @Bean
    public WAITING_RESULT_$$_ERROR_waitingResult_onError_Action creditscoresm_waiting_result_ERROR_waitingResult_onErrorAction() {
        return new WAITING_RESULT_$$_ERROR_waitingResult_onError_ActionImpl();
    }
    @Bean
    public WAITING_RESULT_$$_RESULTRECEIVED_waitingResult_onResultReceived_Action
        creditscoresm_waiting_result_ResultReceived_waitingResult_onResultReceivedAction(MultiTenantCreditScoreSMFacade multiTenantCreditScoreSMFacade) {
        return new WAITING_RESULT_$$_RESULTRECEIVED_waitingResult_onResultReceived_ActionImpl(multiTenantCreditScoreSMFacade);
    }
}

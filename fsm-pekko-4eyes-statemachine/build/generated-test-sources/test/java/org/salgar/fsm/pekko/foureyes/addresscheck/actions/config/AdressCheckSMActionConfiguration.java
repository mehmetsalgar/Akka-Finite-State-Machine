package org.salgar.fsm.pekko.foureyes.addresscheck.actions.config;

import org.salgar.fsm.pekko.foureyes.addresscheck.actions.ERROR_$$_WAITING_RESULT_error_onRetry_Action;
import org.salgar.fsm.pekko.foureyes.addresscheck.actions.ERROR_$$_WAITING_RESULT_error_onRetry_ActionImpl;
import org.salgar.fsm.pekko.foureyes.addresscheck.actions.INITIAL_$$_WAITING_RESULT_initial_Action;
import org.salgar.fsm.pekko.foureyes.addresscheck.actions.INITIAL_$$_WAITING_RESULT_initial_ActionImpl;
import org.salgar.fsm.pekko.foureyes.addresscheck.actions.WAITING_RESULT_$$_ERROR_waiting_onError_Action;
import org.salgar.fsm.pekko.foureyes.addresscheck.actions.WAITING_RESULT_$$_ERROR_waiting_onError_ActionImpl;
import org.salgar.fsm.pekko.foureyes.addresscheck.actions.WAITING_RESULT_$$_RESULTRECEIVED_waiting_onResult_Action;
import org.salgar.fsm.pekko.foureyes.addresscheck.actions.WAITING_RESULT_$$_RESULTRECEIVED_waiting_onResult_ActionImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class AdressCheckSMActionConfiguration {
    @Bean
    public ERROR_$$_WAITING_RESULT_error_onRetry_Action adresschecksm_error_WAITING_RESULT_error_onRetryAction() {
        return new ERROR_$$_WAITING_RESULT_error_onRetry_ActionImpl();
    }
    @Bean
    public INITIAL_$$_WAITING_RESULT_initial_Action adresschecksm_initial_WAITING_RESULT_initialAction() {
        return new INITIAL_$$_WAITING_RESULT_initial_ActionImpl();
    }
    @Bean
    public WAITING_RESULT_$$_ERROR_waiting_onError_Action adresschecksm_waiting_result_ERROR_waiting_onErrorAction() {
        return new WAITING_RESULT_$$_ERROR_waiting_onError_ActionImpl();
    }
    @Bean
    public WAITING_RESULT_$$_RESULTRECEIVED_waiting_onResult_Action adresschecksm_waiting_result_ResultReceived_waiting_onResultAction() {
        return new WAITING_RESULT_$$_RESULTRECEIVED_waiting_onResult_ActionImpl();
    }
}

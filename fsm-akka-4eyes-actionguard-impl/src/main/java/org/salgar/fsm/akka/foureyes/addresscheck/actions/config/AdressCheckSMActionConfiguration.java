package org.salgar.fsm.akka.foureyes.addresscheck.actions.config;

import org.salgar.akka.fsm.foureyes.addresscheck.AddressCheckService;
import org.salgar.fsm.akka.foureyes.addresscheck.actions.*;
import org.salgar.fsm.akka.foureyes.credit.facade.CreditSMFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdressCheckSMActionConfiguration {
    @Bean
    public ERROR_$$_WAITING_RESULT_error_onRetry_Action adresschecksm_error_WAITING_RESULT_error_onRetryAction() {
        return new ERROR_$$_WAITING_RESULT_error_onRetry_ActionImpl();
    }
    @Bean
    public INITIAL_$$_WAITING_RESULT_initial_Action adresschecksm_initial_WAITING_RESULT_initialAction(
            AddressCheckService addressCheckService
    ) {
        return new INITIAL_$$_WAITING_RESULT_initial_ActionImpl(addressCheckService);
    }
    @Bean
    public WAITING_RESULT_$$_ERROR_waiting_onError_Action adresschecksm_waiting_result_ERROR_waiting_onErrorAction() {
        return new WAITING_RESULT_$$_ERROR_waiting_onError_ActionImpl();
    }
    @Bean
    public WAITING_RESULT_$$_RESULTRECEIVED_waiting_onResult_Action
        adresschecksm_waiting_result_ResultReceived_waiting_onResultAction(CreditSMFacade creditSMFacade) {
        return new WAITING_RESULT_$$_RESULTRECEIVED_waiting_onResult_ActionImpl(creditSMFacade);
    }
}

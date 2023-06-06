package org.salgar.fsm.pekko.foureyes.creditscore.actions.config;

import org.salgar.fsm.pekko.foureyes.credit.facade.CreditSMFacade;
import org.salgar.fsm.pekko.foureyes.creditscore.actions.INITIAL_MTCS_$$_WAITING_MULTI_TENANT_RESULTS_initial_WaitingMultiTenantResult_Action;
import org.salgar.fsm.pekko.foureyes.creditscore.actions.INITIAL_MTCS_$$_WAITING_MULTI_TENANT_RESULTS_initial_WaitingMultiTenantResult_ActionImpl;
import org.salgar.fsm.pekko.foureyes.creditscore.actions.WAITING_MULTI_TENANT_RESULTS_$$_FINALSTATE_waitingMultiTenantResult_finalState_Action;
import org.salgar.fsm.pekko.foureyes.creditscore.actions.WAITING_MULTI_TENANT_RESULTS_$$_FINALSTATE_waitingMultiTenantResult_finalState_ActionImpl;
import org.salgar.fsm.pekko.foureyes.creditscore.actions.WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_waitingMultiTenantResults_waitingMultiTenantResults_Action;
import org.salgar.fsm.pekko.foureyes.creditscore.actions.WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_waitingMultiTenantResults_waitingMultiTenantResults_ActionImpl;
import org.salgar.fsm.pekko.foureyes.creditscore.facade.CreditScoreSMFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultiTenantScreditScoreSMActionConfiguration {
    @Bean
    public INITIAL_MTCS_$$_WAITING_MULTI_TENANT_RESULTS_initial_WaitingMultiTenantResult_Action
        multitenantscreditscoresm_initial_mtcs_WAITING_MULTI_TENANT_RESULTS_initial_WaitingMultiTenantResultAction(
            CreditScoreSMFacade creditScoreFacade) {
        return new INITIAL_MTCS_$$_WAITING_MULTI_TENANT_RESULTS_initial_WaitingMultiTenantResult_ActionImpl(creditScoreFacade);
    }
    @Bean
    public WAITING_MULTI_TENANT_RESULTS_$$_FINALSTATE_waitingMultiTenantResult_finalState_Action
        multitenantscreditscoresm_waiting_multi_tenant_results_FinalState_waitingMultiTenantResult_finalStateAction(CreditSMFacade creditSMFacade) {
        return new WAITING_MULTI_TENANT_RESULTS_$$_FINALSTATE_waitingMultiTenantResult_finalState_ActionImpl(creditSMFacade);
    }
    @Bean
    public WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_waitingMultiTenantResults_waitingMultiTenantResults_Action
        multitenantscreditscoresm_waiting_multi_tenant_results_WAITING_MULTI_TENANT_RESULTS_waitingMultiTenantResults_waitingMultiTenantResultsAction() {
        return new WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_waitingMultiTenantResults_waitingMultiTenantResults_ActionImpl();
    }
}
package org.salgar.fsm.pekko.foureyes.creditscore.guards.config;

import org.salgar.fsm.pekko.foureyes.creditscore.guards.WAITING_MULTI_TENANT_RESULTS_$$_FINALSTATE_isAllCreditScoreMultitenantResponded_Guard;
import org.salgar.fsm.pekko.foureyes.creditscore.guards.WAITING_MULTI_TENANT_RESULTS_$$_FINALSTATE_isAllCreditScoreMultitenantResponded_GuardImpl;
import org.salgar.fsm.pekko.foureyes.creditscore.guards.WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_isNotAllTenantsResponded_Guard;
import org.salgar.fsm.pekko.foureyes.creditscore.guards.WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_isNotAllTenantsResponded_GuardImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultiTenantScreditScoreSMGuardConfiguration {
    @Bean
    public WAITING_MULTI_TENANT_RESULTS_$$_FINALSTATE_isAllCreditScoreMultitenantResponded_Guard
    multitenantscreditscoresm_waiting_multi_tenant_results_FinalState_waitingMultiTenantResult_finalStateGuard() {
        return new WAITING_MULTI_TENANT_RESULTS_$$_FINALSTATE_isAllCreditScoreMultitenantResponded_GuardImpl();
    }
    @Bean
    public WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_isNotAllTenantsResponded_Guard
    multitenantscreditscoresm_waiting_multi_tenant_results_WAITING_MULTI_TENANT_RESULTS_waitingMultiTenantResults_waitingMultiTenantResultsGuard() {
        return new WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_isNotAllTenantsResponded_GuardImpl();
    }
}
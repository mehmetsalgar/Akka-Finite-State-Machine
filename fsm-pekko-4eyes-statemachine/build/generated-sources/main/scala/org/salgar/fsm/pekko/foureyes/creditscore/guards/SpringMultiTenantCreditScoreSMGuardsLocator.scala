package org.salgar.fsm.pekko.foureyes.creditscore.guards

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

object SpringMultiTenantCreditScoreSMGuardsLocator {
    private var springMultiTenantCreditScoreSMGuardsLocator: SpringMultiTenantCreditScoreSMGuardsLocator = _

    def getInstance: SpringMultiTenantCreditScoreSMGuardsLocator = {
        springMultiTenantCreditScoreSMGuardsLocator
    }
}

@Component
case class SpringMultiTenantCreditScoreSMGuardsLocator(
    @Autowired multitenantcreditscoresm_WAITING_MULTI_TENANT_RESULTS_$$_FINALSTATE_onCreditScoreReceived_isAllCreditScoreMultitenantResponded: WAITING_MULTI_TENANT_RESULTS_$$_FINALSTATE_isAllCreditScoreMultitenantResponded_Guard,
    @Autowired multitenantcreditscoresm_WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_onCreditScoreReceived_isNotAllTenantsResponded: WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_isNotAllTenantsResponded_Guard
 ) {
 import SpringMultiTenantCreditScoreSMGuardsLocator._

 @PostConstruct
 def init: Unit = {
    springMultiTenantCreditScoreSMGuardsLocator = this
 }
}

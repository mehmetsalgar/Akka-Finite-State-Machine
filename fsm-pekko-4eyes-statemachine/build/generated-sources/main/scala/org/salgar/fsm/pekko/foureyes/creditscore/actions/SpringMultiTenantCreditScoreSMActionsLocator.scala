package org.salgar.fsm.pekko.foureyes.creditscore.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

object SpringMultiTenantCreditScoreSMActionsLocator {
  private var springMultiTenantCreditScoreSMActionsLocator: SpringMultiTenantCreditScoreSMActionsLocator = _

  def getInstance(): SpringMultiTenantCreditScoreSMActionsLocator = {
    springMultiTenantCreditScoreSMActionsLocator
  }
}

@Component
case class SpringMultiTenantCreditScoreSMActionsLocator(
    @Autowired multitenantcreditscoresm_INITIAL_MTCS_$$_WAITING_MULTI_TENANT_RESULTS_initial_WaitingMultiTenantResultAction: INITIAL_MTCS_$$_WAITING_MULTI_TENANT_RESULTS_initial_WaitingMultiTenantResult_Action,
    @Autowired multitenantcreditscoresm_WAITING_MULTI_TENANT_RESULTS_$$_FINALSTATE_waitingMultiTenantResult_finalStateAction: WAITING_MULTI_TENANT_RESULTS_$$_FINALSTATE_waitingMultiTenantResult_finalState_Action,
    @Autowired multitenantcreditscoresm_WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_waitingMultiTenantResults_waitingMultiTenantResultsAction: WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_waitingMultiTenantResults_waitingMultiTenantResults_Action
 ) {
  import SpringMultiTenantCreditScoreSMActionsLocator._

  @PostConstruct
  def init: Unit = {
    springMultiTenantCreditScoreSMActionsLocator = this
  }
}

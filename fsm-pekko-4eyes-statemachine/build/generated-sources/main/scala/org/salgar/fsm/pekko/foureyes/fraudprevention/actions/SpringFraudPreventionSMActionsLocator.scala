package org.salgar.fsm.pekko.foureyes.fraudprevention.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

object SpringFraudPreventionSMActionsLocator {
  private var springFraudPreventionSMActionsLocator: SpringFraudPreventionSMActionsLocator = _

  def getInstance(): SpringFraudPreventionSMActionsLocator = {
    springFraudPreventionSMActionsLocator
  }
}

@Component
case class SpringFraudPreventionSMActionsLocator(
    @Autowired fraudpreventionsm_ERROR_$$_WAITING_RESULT_error_onRetryAction: ERROR_$$_WAITING_RESULT_error_onRetry_Action,
    @Autowired fraudpreventionsm_INITIAL_$$_WAITING_RESULT_initialAction: INITIAL_$$_WAITING_RESULT_initial_Action,
    @Autowired fraudpreventionsm_WAITING_RESULT_$$_ERROR_waitingResult_onErrorAction: WAITING_RESULT_$$_ERROR_waitingResult_onError_Action,
    @Autowired fraudpreventionsm_WAITING_RESULT_$$_RESULTRECEIVED_waitingResult_onResultAction: WAITING_RESULT_$$_RESULTRECEIVED_waitingResult_onResult_Action
 ) {
  import SpringFraudPreventionSMActionsLocator._

  @PostConstruct
  def init: Unit = {
    springFraudPreventionSMActionsLocator = this
  }
}

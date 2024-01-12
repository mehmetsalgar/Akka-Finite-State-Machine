package org.salgar.fsm.pekko.foureyes.creditscore.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

object SpringCreditScoreSMActionsLocator {
  private var springCreditScoreSMActionsLocator: SpringCreditScoreSMActionsLocator = _

  def getInstance(): SpringCreditScoreSMActionsLocator = {
    springCreditScoreSMActionsLocator
  }
}

@Component
case class SpringCreditScoreSMActionsLocator(
    @Autowired creditscoresm_ERROR_$$_WAITING_RESULT_error_onRetryAction: ERROR_$$_WAITING_RESULT_error_onRetry_Action,
    @Autowired creditscoresm_INITIAL_$$_WAITING_RESULT_initialAction: INITIAL_$$_WAITING_RESULT_initial_Action,
    @Autowired creditscoresm_WAITING_RESULT_$$_ERROR_waitingResult_onErrorAction: WAITING_RESULT_$$_ERROR_waitingResult_onError_Action,
    @Autowired creditscoresm_WAITING_RESULT_$$_RESULTRECEIVED_waitingResult_onResultReceivedAction: WAITING_RESULT_$$_RESULTRECEIVED_waitingResult_onResultReceived_Action
 ) {
  import SpringCreditScoreSMActionsLocator._

  @PostConstruct
  def init: Unit = {
    springCreditScoreSMActionsLocator = this
  }
}

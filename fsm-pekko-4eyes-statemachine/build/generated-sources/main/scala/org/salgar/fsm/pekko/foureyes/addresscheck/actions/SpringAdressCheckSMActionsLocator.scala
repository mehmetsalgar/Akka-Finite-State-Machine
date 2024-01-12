package org.salgar.fsm.pekko.foureyes.addresscheck.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

object SpringAdressCheckSMActionsLocator {
  private var springAdressCheckSMActionsLocator: SpringAdressCheckSMActionsLocator = _

  def getInstance(): SpringAdressCheckSMActionsLocator = {
    springAdressCheckSMActionsLocator
  }
}

@Component
case class SpringAdressCheckSMActionsLocator(
    @Autowired adresschecksm_ERROR_$$_WAITING_RESULT_error_onRetryAction: ERROR_$$_WAITING_RESULT_error_onRetry_Action,
    @Autowired adresschecksm_INITIAL_$$_WAITING_RESULT_initialAction: INITIAL_$$_WAITING_RESULT_initial_Action,
    @Autowired adresschecksm_WAITING_RESULT_$$_ERROR_waiting_onErrorAction: WAITING_RESULT_$$_ERROR_waiting_onError_Action,
    @Autowired adresschecksm_WAITING_RESULT_$$_RESULTRECEIVED_waiting_onResultAction: WAITING_RESULT_$$_RESULTRECEIVED_waiting_onResult_Action
 ) {
  import SpringAdressCheckSMActionsLocator._

  @PostConstruct
  def init: Unit = {
    springAdressCheckSMActionsLocator = this
  }
}

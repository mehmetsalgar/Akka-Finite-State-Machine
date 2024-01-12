package org.salgar.fsm.pekko.foureyes.fraudprevention.facade

import org.salgar.fsm.pekko.foureyes.fraudprevention.FraudPreventionSM.Response
import org.salgar.pekko.fsm.api.UseCaseKey

trait FraudPreventionSMFacade {
    def currentState(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]) : Future[Response]
    def error(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Unit
    def askError(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Future[Response]
    def result(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Unit
    def askResult(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Future[Response]
    def retry(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Unit
    def askRetry(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Future[Response]
    def startFraudPreventionEvaluation(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Unit
    def askStartFraudPreventionEvaluation(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Future[Response]
}

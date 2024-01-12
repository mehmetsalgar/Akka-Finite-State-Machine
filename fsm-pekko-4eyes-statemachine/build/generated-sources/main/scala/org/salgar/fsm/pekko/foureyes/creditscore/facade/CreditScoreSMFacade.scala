package org.salgar.fsm.pekko.foureyes.creditscore.facade

import org.salgar.fsm.pekko.foureyes.creditscore.CreditScoreSM.Response
import org.salgar.pekko.fsm.api.UseCaseKey

trait CreditScoreSMFacade {
    def currentState(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]) : Future[Response]
    def error(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Unit
    def askError(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Future[Response]
    def resultReceived(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Unit
    def askResultReceived(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Future[Response]
    def retry(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Unit
    def askRetry(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Future[Response]
    def startCreditScoreResearch(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Unit
    def askStartCreditScoreResearch(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Future[Response]
}

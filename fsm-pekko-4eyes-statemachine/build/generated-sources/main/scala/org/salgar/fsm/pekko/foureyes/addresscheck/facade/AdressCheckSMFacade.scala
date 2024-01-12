package org.salgar.fsm.pekko.foureyes.addresscheck.facade

import org.salgar.fsm.pekko.foureyes.addresscheck.AdressCheckSM.Response
import org.salgar.pekko.fsm.api.UseCaseKey

trait AdressCheckSMFacade {
    def currentState(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]) : Future[Response]
    def error(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Unit
    def askError(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Future[Response]
    def result(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Unit
    def askResult(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Future[Response]
    def retry(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Unit
    def askRetry(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Future[Response]
    def startAdressCheckResearch(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Unit
    def askStartAdressCheckResearch(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Future[Response]
}

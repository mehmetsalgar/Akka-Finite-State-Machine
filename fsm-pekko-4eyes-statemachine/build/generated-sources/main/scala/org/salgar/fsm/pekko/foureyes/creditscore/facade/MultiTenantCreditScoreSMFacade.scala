package org.salgar.fsm.pekko.foureyes.creditscore.facade

import org.salgar.fsm.pekko.foureyes.creditscore.MultiTenantCreditScoreSM.Response
import org.salgar.pekko.fsm.api.UseCaseKey

trait MultiTenantCreditScoreSMFacade {
    def currentState(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]) : Future[Response]
    def creditScoreReceived(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Unit
    def askCreditScoreReceived(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Future[Response]
    def startMultiTenantCreditScoreResearch(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Unit
    def askStartMultiTenantCreditScoreResearch(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Future[Response]
}

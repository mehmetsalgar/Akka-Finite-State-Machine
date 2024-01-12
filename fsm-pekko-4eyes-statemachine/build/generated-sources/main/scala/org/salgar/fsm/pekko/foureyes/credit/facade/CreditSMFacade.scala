package org.salgar.fsm.pekko.foureyes.credit.facade

import org.salgar.fsm.pekko.foureyes.credit.CreditSM.Response

trait CreditSMFacade {
    def currentState(payload: java.util.Map[String, AnyRef]) : Future[Response]
    def acceptableScore(payload: java.util.Map[String, AnyRef]): Unit
    def askAcceptableScore(payload: java.util.Map[String, AnyRef]): Future[Response]
    def accepted(payload: java.util.Map[String, AnyRef]): Unit
    def askAccepted(payload: java.util.Map[String, AnyRef]): Future[Response]
    def customerUpdated(payload: java.util.Map[String, AnyRef]): Unit
    def askCustomerUpdated(payload: java.util.Map[String, AnyRef]): Future[Response]
    def rejected(payload: java.util.Map[String, AnyRef]): Unit
    def askRejected(payload: java.util.Map[String, AnyRef]): Future[Response]
    def relationshipManagerApproved(payload: java.util.Map[String, AnyRef]): Unit
    def askRelationshipManagerApproved(payload: java.util.Map[String, AnyRef]): Future[Response]
    def resultReceived(payload: java.util.Map[String, AnyRef]): Unit
    def askResultReceived(payload: java.util.Map[String, AnyRef]): Future[Response]
    def salesManagerApproved(payload: java.util.Map[String, AnyRef]): Unit
    def askSalesManagerApproved(payload: java.util.Map[String, AnyRef]): Future[Response]
    def submit(payload: java.util.Map[String, AnyRef]): Unit
    def askSubmit(payload: java.util.Map[String, AnyRef]): Future[Response]
}

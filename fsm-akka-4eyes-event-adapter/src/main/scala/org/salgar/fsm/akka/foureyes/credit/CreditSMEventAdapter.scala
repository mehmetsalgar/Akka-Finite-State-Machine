package org.salgar.fsm.akka.foureyes.credit

import akka.persistence.typed.{EventAdapter, EventSeq}
import org.salgar.fsm.akka.foureyes.credit.CreditSM.{SalesManagerApprovalPersistEvent, SomeAdditionalManagerApprovedPersistEvent}

object CreditSMEventAdapter
  extends EventAdapter[CreditSM.PersistEvent, CreditSM.PersistEvent] {

  override def toJournal(event: CreditSM.PersistEvent): CreditSM.PersistEvent = event match {
    case _ => event
  }

  override def manifest(event: CreditSM.PersistEvent): String = ""

  override def fromJournal(event: CreditSM.PersistEvent, manifest: String): EventSeq[CreditSM.PersistEvent] = event match {
    case SalesManagerApprovalPersistEvent(controlObject) => EventSeq.single(SomeAdditionalManagerApprovedPersistEvent(controlObject))
    case _ => EventSeq.single(event)
  }
}
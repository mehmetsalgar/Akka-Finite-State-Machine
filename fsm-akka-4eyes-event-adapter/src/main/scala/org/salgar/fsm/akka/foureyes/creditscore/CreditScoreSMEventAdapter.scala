package org.salgar.fsm.akka.foureyes.creditscore

import akka.persistence.typed.{EventAdapter, EventSeq}
import org.salgar.fsm.akka.foureyes.credit.model._
import org.salgar.fsm.akka.foureyes.creditscore.CreditScoreSM.StartCreditScoreResearchPersistEvent
import org.slf4j.{Logger, LoggerFactory}

import java.util
import java.util.UUID

object CreditScoreSMEventAdapter extends EventAdapter[CreditScoreSM.PersistEvent, CreditScoreSM.PersistEvent] {
  val log : Logger = LoggerFactory.getLogger(CreditScoreSMEventAdapter.getClass)
  override def toJournal(event: CreditScoreSM.PersistEvent): CreditScoreSM.PersistEvent = event match {
    case _ => event
  }

  override def manifest(event: CreditScoreSM.PersistEvent): String = ""

  override def fromJournal(event: CreditScoreSM.PersistEvent, manifest: String): EventSeq[CreditScoreSM.PersistEvent] = try {
      event match {
        case _start@StartCreditScoreResearchPersistEvent(controlObject) => {
          val customer = controlObject.get("customer")
          customer match {
            case customer1: Customer =>
              controlObject.put("customer", convertCustomer(customer1))
            case _ =>
          }
          
          EventSeq.single(_start)
        }
        case _ => EventSeq.single(event)

    }
  } catch {
    case e@_ => log.error("we can Adapt the following Event: {}", event.toString, e)
      EventSeq.single(event)
  }

   private def convertCustomer(customer : Customer) : CustomerV2 = {
     val identificationInformation = new IdentificationInformation(
     customer.getPersonalId,
     "PASS")
     val incomeProof = new IncomeProof(
     UUID.randomUUID().toString,
     "ABC",
     "99999.99"
     )
     val expanseRent = new FixExpanse(
     UUID.randomUUID().toString,
     "1500",
     "Rent")
     val expanseCarCredit = new FixExpanse(
     UUID.randomUUID().toString,
     "600",
     "Credit")

     new CustomerV2(
     customer.getPersonalId,
     customer.getFirstName,
     customer.getLastName,
     "customer1@test.org",
     java.util.List.of(customer.getAddress),
     java.util.List.of(identificationInformation),
     java.util.List.of(incomeProof),
     util.Arrays.asList(expanseRent, expanseCarCredit))
  }
}
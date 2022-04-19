package org.salgar.fsm.akka.foureyes.creditscore

import akka.persistence.typed.{EventAdapter, EventSeq}
import org.salgar.fsm.akka.foureyes.credit.model._
import org.salgar.fsm.akka.foureyes.creditscore.CreditScoreSM.StartCreditScoreResearchPersistEvent

import java.util
import java.util.UUID

object CreditScoreSMEventAdapter extends EventAdapter[CreditScoreSM.PersistEvent, CreditScoreSM.PersistEvent] {
  override def toJournal(event: CreditScoreSM.PersistEvent): CreditScoreSM.PersistEvent = event match {
    case _ => event
  }

  override def manifest(event: CreditScoreSM.PersistEvent): String = ""

  override def fromJournal(event: CreditScoreSM.PersistEvent, manifest: String): EventSeq[CreditScoreSM.PersistEvent] =
    event match {
      case _start@StartCreditScoreResearchPersistEvent(controlObject) => {
        val customer:  Customer = controlObject.get("customer").asInstanceOf[Customer]

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

        val customerV2 : CustomerV2 =
          new CustomerV2(
            customer.getPersonalId,
            customer.getFirstName,
            customer.getLastName,
            java.util.List.of(identificationInformation),
            java.util.List.of(incomeProof),
            util.Arrays.asList(expanseRent, expanseCarCredit),
            java.util.List.of(customer.getAddress),
            "customer1@test.org"
          )

        controlObject.put("customer", customerV2)
        EventSeq.single(_start)
      }
      case _ => EventSeq.single(event)

  }
}
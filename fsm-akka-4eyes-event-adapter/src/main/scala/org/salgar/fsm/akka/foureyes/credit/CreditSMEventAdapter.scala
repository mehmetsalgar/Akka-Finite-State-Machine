package org.salgar.fsm.akka.foureyes.credit

import akka.persistence.typed.{EventAdapter, EventSeq}
import org.salgar.fsm.akka.foureyes.credit.CreditSM.{SalesManagerApprovalPersistEvent, SomeAdditionalManagerApprovedPersistEvent}
import org.salgar.fsm.akka.foureyes.credit.model._

import java.util
import java.util.UUID

object CreditSMEventAdapter
  extends EventAdapter[CreditSM.PersistEvent, CreditSM.PersistEvent] {

  override def toJournal(event: CreditSM.PersistEvent): CreditSM.PersistEvent = event match {
    case _ => event
  }

  override def manifest(event: CreditSM.PersistEvent): String = ""

  override def fromJournal(event: CreditSM.PersistEvent, manifest: String): EventSeq[CreditSM.PersistEvent] = event match {
    case SalesManagerApprovalPersistEvent(controlObject) => {
      val creditTenants:  java.util.List[Customer]  = controlObject.get("creditTenants").asInstanceOf[java.util.List[Customer]]
      val customerV2Tenants : java.util.List[CustomerV2] = new util.ArrayList[CustomerV2]()

      creditTenants.forEach( customer => {
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

        customerV2Tenants.add(customerV2)
      })

      controlObject.put("", customerV2Tenants)
      EventSeq.single(SomeAdditionalManagerApprovedPersistEvent(controlObject))
    }
    case _ => EventSeq.single(event)
  }
}
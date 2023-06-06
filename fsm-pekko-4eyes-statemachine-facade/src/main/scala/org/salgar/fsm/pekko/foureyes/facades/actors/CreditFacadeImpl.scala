package org.salgar.fsm.pekko.foureyes.facades.actors

import org.apache.pekko.actor.typed.scaladsl.AskPattern.Askable
import org.salgar.fsm.pekko.foureyes.credit.CreditSM.Response
import org.salgar.fsm.pekko.foureyes.credit.CreditSMGuardian
import org.salgar.fsm.pekko.foureyes.credit.CreditSMGuardian._
import org.salgar.fsm.pekko.foureyes.credit.facade.CreditSMFacade
import org.salgar.fsm.pekko.foureyes.credit.kafka.config.TopicProperties
import org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditSMCommand
import org.salgar.fsm.pekko.foureyes.slaves.SlaveStatemachineConstants.{ADDRESS_CHECK_SM, CUSTOMER_SCORE_SM, FRAUD_PREVENTION_SM, SOURCE_SLAVE_SM_TAG}
import org.salgar.fsm.pekko.kafka.config.ConsumerConfig
import org.salgar.fsm.pekko.pekkosystem.ActorService
import org.salgar.fsm.pekko.statemachine.facade.StateMachineFacade
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component

import java.util
import javax.annotation.PostConstruct
import scala.concurrent.Future

@Component
@DependsOn(Array("actorService"))
class CreditFacadeImpl(actorService: ActorService,
                       creditSMConsumerConfig: ConsumerConfig[String, CreditSMCommand],
                       topicProperties: TopicProperties)
  extends StateMachineFacade[CreditSMGuardian.CreditSMGuardianEvent, Response] (
    actorService, "creditSMGuardian",
    CreditSMGuardian()
    (actorService.actorSystem(), actorService.sharding()))
    with CreditSMFacade {
  import ActorService._

  def currentState(payload: java.util.Map[String, AnyRef]) : Future[Response] = {
    actorRef.ask[Response](ref => CreditSMGuardian.onReportState(payload, ref))
  }

  def submit(payload: java.util.Map[String, AnyRef]): Unit= {
    actorRef ! onSubmit(payload)
  }

  def askSubmit(payload: java.util.Map[String, AnyRef]): Future[Response] = {
    actorRef.ask[Response](ref =>  CreditSMGuardian.onSubmit(payload, ref))
  }

  def relationshipManagerApproved(payload: java.util.Map[String, AnyRef]): Unit = {
    actorRef ! onRelationshipManagerApproved(payload)
  }

  def salesManagerApproved(payload: java.util.Map[String, AnyRef]): Unit = {
    actorRef ! onSalesManagerApproved(payload)
  }

  def resultReceived(payload: java.util.Map[String, AnyRef]) : Unit = {
    actorRef ! onResultReceived(payload)
  }

  def creditScoreReceived1(payload: java.util.Map[String, AnyRef]) : Unit = {
    payload.put(SOURCE_SLAVE_SM_TAG, CUSTOMER_SCORE_SM)
    actorRef ! onResultReceived(payload)
  }

  def fraudPreventionReceived1(payload: java.util.Map[String, AnyRef]) : Unit = {
    payload.put(SOURCE_SLAVE_SM_TAG, FRAUD_PREVENTION_SM)
    actorRef ! onResultReceived(payload)
  }

  def addressCheckReceived1(payload: java.util.Map[String, AnyRef]) : Unit = {
    payload.put(SOURCE_SLAVE_SM_TAG, ADDRESS_CHECK_SM)
    actorRef ! onResultReceived(payload)
  }

  def accepted(payload: java.util.Map[String, AnyRef]) : Unit = {
    actorRef ! onAccepted(payload)
  }

  def rejected(payload: java.util.Map[String, AnyRef]) : Unit = {
    actorRef ! onRejected(payload)
  }

  def acceptableScore(payload: java.util.Map[String, AnyRef]) : Unit = {
    actorRef ! onAcceptableScore(payload)
  }

  override def askAcceptableScore(payload: util.Map[String, AnyRef]): Future[Response] = {
    actorRef.ask[Response](ref =>  CreditSMGuardian.onAcceptableScore(payload, ref))
  }

  override def askAccepted(payload: util.Map[String, AnyRef]): Future[Response] = {
    actorRef.ask[Response](ref =>  CreditSMGuardian.onAccepted(payload, ref))
  }

  override def askRejected(payload: util.Map[String, AnyRef]): Future[Response] = {
    actorRef.ask[Response](ref =>  CreditSMGuardian.onRejected(payload, ref))
  }

  override def askRelationshipManagerApproved(payload: util.Map[String, AnyRef]): Future[Response] = {
    actorRef.ask[Response](ref =>  CreditSMGuardian.onRelationshipManagerApproved(payload, ref))
  }

  override def askResultReceived(payload: util.Map[String, AnyRef]): Future[Response] = {
    actorRef.ask[Response](ref =>  CreditSMGuardian.onResultReceived(payload, ref))
  }

  override def askSalesManagerApproved(payload: util.Map[String, AnyRef]): Future[Response] = {
    actorRef.ask[Response](ref =>  CreditSMGuardian.onSalesManagerApproved(payload, ref))
  }

  override def customerUpdated(payload: util.Map[String, AnyRef]): Unit = {
    actorRef ! onCustomerUpdated(payload)
  }

  override def askCustomerUpdated(payload: util.Map[String, AnyRef]): Future[Response] = {
    actorRef.ask[Response](ref =>  CreditSMGuardian.onCustomerUpdated(payload, ref))
  }

  @PostConstruct
  override protected def init = {
    super.init
  }
}
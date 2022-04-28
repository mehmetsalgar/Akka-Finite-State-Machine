package org.salgar.fsm.akka.foureyes.facades.actors

import akka.cluster.sharding.typed.scaladsl.ClusterSharding
import org.salgar.fsm.akka.akkasystem.ActorService
import org.salgar.fsm.akka.foureyes.credit.CreditSM.Response
import org.salgar.fsm.akka.foureyes.credit.facade.CreditSMFacade
import org.salgar.fsm.akka.foureyes.credit.{CreditSM, CreditSMGuardian}
import org.salgar.fsm.akka.kafka.sharding.FsmAkkaKafkaClusterSharding
import org.salgar.fsm.akka.statemachine.facade.StateMachineFacade
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.util.Success

object CreditFacadeImpl {

}

@Component
@DependsOn(Array("actorService"))
class CreditFacadeImpl(actorService: ActorService)
  extends StateMachineFacade[CreditSMGuardian.CreditSMGuardianEvent, Response] (
    actorService, "creditSMGuardian",
    CreditSMGuardian()(actorService.sharding()))
    with CreditSMFacade {

  def clusterSharding(): ClusterSharding = {
    val future : Future[FsmAkkaKafkaClusterSharding.KafkaShardingNoEnvelopeExtractor[CreditSM.CreditSMEvent]] =
      FsmAkkaKafkaClusterSharding(actorService.actorSystem())
        .messageExtractorNoEnvelope(
          timeout = 10.seconds,
          topic = "test",
          entityIdExtractor = (event : CreditSM.CreditSMEvent) => event.useCaseKey.getKey,
          shardIdExtractor=  entityId => entityId,
          settings = _
        )

        future.onComplete(
          case Success(extractor) =>
        )
      })
  }

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
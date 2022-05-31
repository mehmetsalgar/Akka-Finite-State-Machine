package org.salgar.fsm.akka.foureyes.facades.actors.fraudprevention

import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.AskPattern.Askable
import org.salgar.akka.fsm.api.UseCaseKey
import org.salgar.fsm.akka.akkasystem.ActorService
import org.salgar.fsm.akka.foureyes.credit.kafka.config.TopicProperties
import org.salgar.fsm.akka.foureyes.fraudprevention.FraudPreventionSM.{FraudPreventionSMEvent, Response, onStartFraudPreventionEvaluation}
import org.salgar.fsm.akka.foureyes.fraudprevention.FraudPreventionSMGuardian._
import org.salgar.fsm.akka.foureyes.fraudprevention.facade.FraudPreventionSMFacade
import org.salgar.fsm.akka.foureyes.fraudprevention.protobuf.FraudPreventionSMCommand
import org.salgar.fsm.akka.foureyes.fraudprevention.{FraudPreventionSM, FraudPreventionSMGuardian}
import org.salgar.fsm.akka.kafka.config.ConsumerConfig
import org.salgar.fsm.akka.statemachine.facade.StateMachineFacade
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Service

import java.util
import javax.annotation.PostConstruct
import scala.concurrent.Future

@Service
@DependsOn(Array("actorService"))
class FraudPreventionSMFacadeImpl(actorService: ActorService,
                                  fraudPreventionSMConsumerConfig: ConsumerConfig[String, FraudPreventionSMCommand],
                                  topicProperties: TopicProperties)
  extends StateMachineFacade[FraudPreventionSMGuardianEvent, Response] (
    actorService, "fraudPreventionSMGuardian",
    FraudPreventionSMGuardian()(actorService.actorSystem(), actorService.sharding()))
    with FraudPreventionSMFacade {
  import ActorService._

  override def currentState(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]): Future[Response] =
    actorRef.ask[Response](ref => onReportState(useCaseKey, payload, ref))

  override def error(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]): Unit =
    actorRef ! onError(useCaseKey,payload)

  override def askError(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]): Future[Response] =
    actorRef.ask(ref => onError(useCaseKey,payload, ref))

  override def result(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]): Unit =
    actorRef ! onResult(useCaseKey,payload)

  override def askResult(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]): Future[Response] =
    actorRef.ask(ref => onResult(useCaseKey,payload, ref))

  override def retry(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]): Unit =
    actorRef ! onRetry(useCaseKey,payload)

  override def askRetry(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]): Future[Response] =
    actorRef.ask(ref => onRetry(useCaseKey,payload, ref))

  def startFraudPreventionEvaluation(
                                      useCaseKey : UseCaseKey,
                                      payload: util.Map[String, AnyRef]): Unit = {
    val fraudPreventionRef: ActorRef[FraudPreventionSMEvent]  = FraudPreventionSMGuardian.shardInit()(actorService.sharding(), actorService.actorSystem())
    fraudPreventionRef ! FraudPreventionSM.onStartFraudPreventionEvaluation(useCaseKey, payload)
  }

  override def askStartFraudPreventionEvaluation(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]): Future[Response] ={
    val fraudPreventionRef: ActorRef[FraudPreventionSMEvent]  = FraudPreventionSMGuardian.shardInit()(actorService.sharding(), actorService.actorSystem())
    fraudPreventionRef.ask(ref => onStartFraudPreventionEvaluation(useCaseKey, payload, ref))
  }

  @PostConstruct
  override protected def init =
    super.init
}
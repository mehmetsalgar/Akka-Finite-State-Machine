package org.salgar.fsm.akka.foureyes.facades.actors.creditscore

import akka.NotUsed
import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.actor.typed.{ActorRef, ActorSystem}
import org.salgar.akka.fsm.api.UseCaseKey
import org.salgar.fsm.akka.akkasystem.ActorService
import org.salgar.fsm.akka.foureyes.credit.kafka.config.TopicProperties
import org.salgar.fsm.akka.foureyes.creditscore.CreditScoreSM.{CreditScoreSMEvent, Response}
import org.salgar.fsm.akka.foureyes.creditscore.CreditScoreSMGuardian._
import org.salgar.fsm.akka.foureyes.creditscore.facade.CreditScoreSMFacade
import org.salgar.fsm.akka.foureyes.creditscore.protobuf.CreditScoreSMCommand
import org.salgar.fsm.akka.foureyes.creditscore.{CreditScoreSM, CreditScoreSMGuardian}
import org.salgar.fsm.akka.foureyes.facades.actors.creditscore.CreditScoreSMFacadeImpl.messageExtractor
import org.salgar.fsm.akka.foureyes.facades.utility.ShardIdUUtility
import org.salgar.fsm.akka.kafka.config.ConsumerConfig
import org.salgar.fsm.akka.kafka.sharding.FsmAkkaKafkaClusterSharding
import org.salgar.fsm.akka.statemachine.facade.StateMachineFacade
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Service

import java.util
import javax.annotation.PostConstruct
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object CreditScoreSMFacadeImpl {
  def messageExtractor(
                        actorSystem: ActorSystem[NotUsed],
                        creditSMConsumerConfig: ConsumerConfig[String, CreditScoreSMCommand],
                        topicProperties: TopicProperties): FsmAkkaKafkaClusterSharding.KafkaShardingNoEnvelopeExtractor[CreditScoreSM.CreditScoreSMEvent] = {
    val numOfShards: Int = actorSystem.settings.config.getInt("akka.fsm.numberOfShards")
    val future: Future[FsmAkkaKafkaClusterSharding.KafkaShardingNoEnvelopeExtractor[CreditScoreSM.CreditScoreSMEvent]] =
      FsmAkkaKafkaClusterSharding(actorSystem)
        .messageExtractorNoEnvelope(
          timeout = 10.seconds,
          topic = topicProperties.getCreditScoreSM,
          entityIdExtractor = (event: CreditScoreSM.CreditScoreSMEvent) => event.useCaseKey.getKey,
          shardIdExtractor = (entityId, partitions) => ShardIdUUtility.calculateSlaveShardId(entityId, partitions, numOfShards),
          settings = creditSMConsumerConfig.consumerSettings()
        )

    Await.result(future, 5.seconds)
  }
}

@Service
@DependsOn(Array("actorService"))
class CreditScoreSMFacadeImpl(actorService: ActorService,
                              creditScoreSMConsumerConfig: ConsumerConfig[String, CreditScoreSMCommand],
                              topicProperties: TopicProperties)
  extends StateMachineFacade[CreditScoreSMGuardianEvent, Response](
      actorService, "creditScoreSMGuardian",
      CreditScoreSMGuardian(
        messageExtractor(
          actorService.actorSystem(),
          creditScoreSMConsumerConfig,
          topicProperties),
        externalAllocationStrategy = true)(actorService.actorSystem(), actorService.sharding()))
    with CreditScoreSMFacade{
  import ActorService._

  override def currentState(useCaseKey : UseCaseKey, payload: util.Map[String, AnyRef]): Future[Response] = {
    actorRef.ask[Response](ref => onReportState(useCaseKey, payload, ref))
  }

  override def startCreditScoreResearch(useCaseKey : UseCaseKey,
                               payload: util.Map[String, AnyRef]) : Unit = {
    val creditScoreRef: ActorRef[CreditScoreSMEvent] = CreditScoreSMGuardian.shardInit()(actorService.sharding(), actorService.actorSystem())
    creditScoreRef ! CreditScoreSM.onStartCreditScoreResearch(useCaseKey, payload)
  }

  override def askStartCreditScoreResearch(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]): Future[Response] = {
    val creditScoreRef: ActorRef[CreditScoreSMEvent] = CreditScoreSMGuardian.shardInit()(actorService.sharding(), actorService.actorSystem())
    creditScoreRef.ask(ref => CreditScoreSM.onStartCreditScoreResearch(useCaseKey, payload, ref))
  }

  override def resultReceived(useCaseKey : UseCaseKey,
                          payload: util.Map[String, AnyRef]) : Unit =
    actorRef ! onResultReceived(useCaseKey, payload)

  override def askResultReceived(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]): Future[Response] = {
    actorRef.ask[Response](ref => onResultReceived(useCaseKey, payload, ref))
  }

  override def error(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]): Unit = {
    actorRef ! onError(useCaseKey, payload)
  }

  override def askError(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]): Future[Response] = {
    actorRef.ask[Response](ref => onResultReceived(useCaseKey, payload, ref))
  }

  override def retry(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]): Unit = {
    actorRef ! onRetry(useCaseKey, payload)
  }

  override def askRetry(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]): Future[Response] = {
    actorRef.ask[Response](ref => onRetry(useCaseKey, payload, ref))
  }

  @PostConstruct
  override protected def init: Unit = {
    super.init
  }
}
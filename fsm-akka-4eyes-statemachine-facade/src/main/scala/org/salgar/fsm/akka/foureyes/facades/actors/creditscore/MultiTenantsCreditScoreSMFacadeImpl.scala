package org.salgar.fsm.akka.foureyes.facades.actors.creditscore

import akka.NotUsed
import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.actor.typed.{ActorRef, ActorSystem}
import org.salgar.akka.fsm.api.UseCaseKey
import org.salgar.fsm.akka.akkasystem.ActorService
import org.salgar.fsm.akka.foureyes.credit.kafka.config.TopicProperties
import org.salgar.fsm.akka.foureyes.creditscore.MultiTenantCreditScoreSM.{MultiTenantCreditScoreSMEvent, Response, onStartMultiTenantCreditScoreResearch}
import org.salgar.fsm.akka.foureyes.creditscore.MultiTenantCreditScoreSMGuardian.{MultiTenantCreditScoreSMGuardianEvent, onCreditScoreReceived}
import org.salgar.fsm.akka.foureyes.creditscore.facade.MultiTenantCreditScoreSMFacade
import org.salgar.fsm.akka.foureyes.creditscore.protobuf.MultiTenantCreditScoreSMCommand
import org.salgar.fsm.akka.foureyes.creditscore.{MultiTenantCreditScoreSM, MultiTenantCreditScoreSMGuardian}
import org.salgar.fsm.akka.foureyes.facades.actors.creditscore.MultiTenantsCreditScoreSMFacadeImpl.messageExtractor
import org.salgar.fsm.akka.foureyes.facades.utility.ShardIdUUtility
import org.salgar.fsm.akka.kafka.config.ConsumerConfig
import org.salgar.fsm.akka.kafka.sharding.FsmAkkaKafkaClusterSharding
import org.salgar.fsm.akka.statemachine.facade.StateMachineFacade
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object MultiTenantsCreditScoreSMFacadeImpl {
  def messageExtractor(
                        actorSystem: ActorSystem[NotUsed],
                        creditSMConsumerConfig: ConsumerConfig[String, MultiTenantCreditScoreSMCommand],
                        topicProperties: TopicProperties): FsmAkkaKafkaClusterSharding.KafkaShardingNoEnvelopeExtractor[MultiTenantCreditScoreSM.MultiTenantCreditScoreSMEvent] = {
    val numOfShards: Int = actorSystem.settings.config.getInt("akka.fsm.numberOfShards")
    val future: Future[FsmAkkaKafkaClusterSharding.KafkaShardingNoEnvelopeExtractor[MultiTenantCreditScoreSM.MultiTenantCreditScoreSMEvent]] =
      FsmAkkaKafkaClusterSharding(actorSystem)
        .messageExtractorNoEnvelope(
          timeout = 10.seconds,
          topic = topicProperties.getMultiTenantCreditScoreSM,
          entityIdExtractor = (event: MultiTenantCreditScoreSM.MultiTenantCreditScoreSMEvent) => event.useCaseKey.getKey,
          shardIdExtractor = (entityId, partitions) => ShardIdUUtility.calculateShardId(entityId, partitions, numOfShards),
          settings = creditSMConsumerConfig.consumerSettings()
        )

    Await.result(future, 5.seconds)
  }
}

@Service
@DependsOn(Array("actorService"))
class MultiTenantsCreditScoreSMFacadeImpl(actorService: ActorService,
                                          multiTenantCreditScoreSMConsumerConfig: ConsumerConfig[String, MultiTenantCreditScoreSMCommand],
                                          topicProperties: TopicProperties)
  extends StateMachineFacade[MultiTenantCreditScoreSMGuardianEvent, Response](
    actorService, "multiTenantCreditScoreSMGuardian",
    MultiTenantCreditScoreSMGuardian(
      messageExtractor(
        actorService.actorSystem(),
        multiTenantCreditScoreSMConsumerConfig,
        topicProperties),
      externalAllocationStrategy = true
    )(actorService.actorSystem(), actorService.sharding()))
    with MultiTenantCreditScoreSMFacade{
  import ActorService._

  def currentState(useCaseKey : UseCaseKey,payload: java.util.Map[String, AnyRef]): Future[Response] = {
    actorRef.ask[Response](ref => MultiTenantCreditScoreSMGuardian.onReportState(useCaseKey, payload, ref))
  }

  def creditScoreReceived(useCaseKey: UseCaseKey, payload: java.util.Map[String, AnyRef]): Unit = {
    actorRef ! onCreditScoreReceived(useCaseKey, payload)
  }

  def askCreditScoreReceived(useCaseKey: UseCaseKey, payload: java.util.Map[String, AnyRef]): Future[Response] = {
    actorRef.ask[Response](ref => onCreditScoreReceived(useCaseKey, payload, ref))
  }

  def startMultiTenantCreditScoreResearch(useCaseKey: UseCaseKey, payload: java.util.Map[String, AnyRef]): Unit = {
    // TODO Document the need for Slave to initialize this way because of the Akka Persistence Recovery
    val multiTenantScreditScoreRef: ActorRef[MultiTenantCreditScoreSMEvent] =
      MultiTenantCreditScoreSMGuardian.shardInit()(actorService.sharding(), actorService.actorSystem())
    multiTenantScreditScoreRef ! onStartMultiTenantCreditScoreResearch(useCaseKey, payload)
  }

  def askStartMultiTenantCreditScoreResearch(
                                              useCaseKey: UseCaseKey,
                                              payload: java.util.Map[String, AnyRef]
                                            ): Future[Response] = {

    val multiTenantScreditScoreRef: ActorRef[MultiTenantCreditScoreSMEvent] =
      MultiTenantCreditScoreSMGuardian.shardInit()(actorService.sharding(), actorService.actorSystem())
    multiTenantScreditScoreRef.ask[Response](ref => onStartMultiTenantCreditScoreResearch(useCaseKey, payload, ref))
  }

  @PostConstruct
  override protected def init: Unit = {
    super.init
  }
}
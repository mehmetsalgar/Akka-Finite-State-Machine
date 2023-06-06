package org.salgar.fsm.pekko.foureyes.facades.actors.creditscore

import org.apache.pekko.actor.typed.ActorRef
import org.apache.pekko.actor.typed.scaladsl.AskPattern.Askable
import org.salgar.fsm.pekko.foureyes.credit.kafka.config.TopicProperties
import org.salgar.fsm.pekko.foureyes.creditscore.MultiTenantCreditScoreSM.{MultiTenantCreditScoreSMEvent, Response, onStartMultiTenantCreditScoreResearch}
import org.salgar.fsm.pekko.foureyes.creditscore.MultiTenantCreditScoreSMGuardian
import org.salgar.fsm.pekko.foureyes.creditscore.MultiTenantCreditScoreSMGuardian.{MultiTenantCreditScoreSMGuardianEvent, onCreditScoreReceived}
import org.salgar.fsm.pekko.foureyes.creditscore.facade.MultiTenantCreditScoreSMFacade
import org.salgar.fsm.pekko.foureyes.creditscore.protobuf.MultiTenantCreditScoreSMCommand
import org.salgar.fsm.pekko.kafka.config.ConsumerConfig
import org.salgar.fsm.pekko.pekkosystem.ActorService
import org.salgar.fsm.pekko.statemachine.facade.StateMachineFacade
import org.salgar.pekko.fsm.api.UseCaseKey
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct
import scala.concurrent.Future

@Service
@DependsOn(Array("actorService"))
class MultiTenantsCreditScoreSMFacadeImpl(actorService: ActorService,
                                          multiTenantCreditScoreSMConsumerConfig: ConsumerConfig[String, MultiTenantCreditScoreSMCommand],
                                          topicProperties: TopicProperties)
  extends StateMachineFacade[MultiTenantCreditScoreSMGuardianEvent, Response](
    actorService, "multiTenantCreditScoreSMGuardian",
    MultiTenantCreditScoreSMGuardian()(actorService.actorSystem(), actorService.sharding()))
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
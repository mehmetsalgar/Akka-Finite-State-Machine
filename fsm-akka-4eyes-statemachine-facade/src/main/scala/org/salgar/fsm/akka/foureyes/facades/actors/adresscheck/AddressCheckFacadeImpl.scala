package org.salgar.fsm.akka.foureyes.facades.actors.adresscheck

import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.AskPattern.Askable
import org.salgar.akka.fsm.api.UseCaseKey
import org.salgar.fsm.akka.akkasystem.ActorService
import org.salgar.fsm.akka.foureyes.addresscheck.AdressCheckSM.{AdressCheckSMEvent, Response}
import org.salgar.fsm.akka.foureyes.addresscheck.AdressCheckSMGuardian._
import org.salgar.fsm.akka.foureyes.addresscheck.facade.AdressCheckSMFacade
import org.salgar.fsm.akka.foureyes.addresscheck.{AdressCheckSM, AdressCheckSMGuardian}
import org.salgar.fsm.akka.statemachine.facade.StateMachineFacade
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Service

import java.util
import javax.annotation.PostConstruct
import scala.concurrent.Future

@Service
@DependsOn(Array("actorService"))
class AddressCheckSMFacadeImpl(actorService: ActorService)
  extends StateMachineFacade[AdressCheckSMGuardianEvent, Response] (actorService, "addressCheckSMGuardian", AdressCheckSMGuardian()(actorService.sharding(), actorService.actorSystem()))
    with AdressCheckSMFacade {
  import ActorService._

  override def currentState(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]): Future[Response] =
    actorRef.ask(ref => onReportState(useCaseKey, payload, ref))

  override def error(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]): Unit =
    actorRef ! onError(useCaseKey : UseCaseKey, payload: util.Map[String, AnyRef])

  override def askError(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]): Future[Response] =
    actorRef.ask(ref => onError(useCaseKey, payload, ref))

  override def result(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]): Unit =
    actorRef ! onResult(useCaseKey : UseCaseKey, payload: util.Map[String, AnyRef])

  override def askResult(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]): Future[Response] =
    actorRef.ask(ref => onResult(useCaseKey, payload, ref))

  override def retry(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]): Unit =
    actorRef ! onRetry(useCaseKey : UseCaseKey, payload: util.Map[String, AnyRef])

  override def askRetry(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]): Future[Response] =
    actorRef.ask(ref => onRetry(useCaseKey, payload, ref))

  override def startAdressCheckResearch(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]): Unit = {
    val addressCheckRef : ActorRef[AdressCheckSMEvent] = AdressCheckSMGuardian.shardInit()(actorService.sharding(), actorService.actorSystem())
    addressCheckRef ! AdressCheckSM.onStartAdressCheckResearch(useCaseKey, payload)
  }

  override def askStartAdressCheckResearch(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]): Future[Response] = {
    val addressCheckRef : ActorRef[AdressCheckSMEvent] = AdressCheckSMGuardian.shardInit()(actorService.sharding(), actorService.actorSystem())
    addressCheckRef.ask( ref => AdressCheckSM.onStartAdressCheckResearch(useCaseKey, payload, ref))
  }

  @PostConstruct
  override protected def init =
    super.init
}
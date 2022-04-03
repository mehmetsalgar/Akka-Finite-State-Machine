package org.salgar.akka.fsm.base.actors

import akka.actor.typed.ActorRef
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.persistence.typed.scaladsl.{Effect, ReplyEffect}
import org.salgar.akka.fsm.base.actors.BaseActor.InternalBaseMessage
import org.salgar.akka.fsm.base.config.ServiceLocatorConfig
import shapeless.TypeCase

import scala.reflect.ClassTag

object BaseActor {
  trait InternalBaseMessage {
    def listing: Receptionist.Listing
  }
}

abstract class BaseActor[E: ClassTag, S <: InternalBaseMessage, EVENT, STATE](
                                                                               signal: TypeCase[S],
                                                                               useCaseKey: String
                                                                             ) {
  private val typed = signal

  def base[B <:  E: ClassTag](cmd: E, state: STATE)(f: B => ReplyEffect[EVENT, STATE]): ReplyEffect[EVENT, STATE] =
    cmd match {
      case typed(listing) =>
        //context.log.info("Processing: {}", typed.toString)
        ServiceLocatorConfig.getInstance.receptionFacade.put(
          useCaseKey,
          listing.listing.key,
          listing.listing)
        Effect.noReply

      case m: B => f(m)
    }

  protected def findActor[T](useCaseKey: String, serviceKey: ServiceKey[T], actorName: String): Option[ActorRef[T]] = {
    ServiceLocatorConfig.getInstance.receptionFacade.findActor[T](
      useCaseKey,
      serviceKey,
      actorName
    )
  }
}
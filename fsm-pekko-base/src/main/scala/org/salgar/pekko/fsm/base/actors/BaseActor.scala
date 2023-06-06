package org.salgar.pekko.fsm.base.actors

import org.apache.pekko.actor.typed.ActorRef
import org.apache.pekko.actor.typed.receptionist.{Receptionist, ServiceKey}
import org.apache.pekko.persistence.typed.scaladsl.{Effect, ReplyEffect}
import org.salgar.pekko.fsm.base.actors.BaseActor.InternalBaseMessage
import org.salgar.pekko.fsm.base.config.ServiceLocatorConfig
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
package org.salgar.fsm.pekko.statemachine.facade

import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.apache.pekko.actor.typed.{ActorRef, Behavior}
import org.salgar.fsm.pekko.pekkosystem.ActorService

abstract class StateMachineFacade[EVENT, RESPONSE](
    actorService: ActorService,
    name: String,
    behaviour: Behavior[EVENT]
) {
  protected var actorRef: ActorRef[EVENT] = _
  implicit val ec                         = actorService.ec()
  implicit val scheduler                  = actorService.scheduler()
  implicit val sharding                   = actorService.sharding()

  protected def init = {
    actorRef = actorService
      .actorSystem()
      .systemActorOf(
        Behaviors.setup[EVENT] { context =>
          {
            context.log.info("We are here initializing Guardian: {}!", name)
            behaviour
          }
        },
        name
      )
  }
}

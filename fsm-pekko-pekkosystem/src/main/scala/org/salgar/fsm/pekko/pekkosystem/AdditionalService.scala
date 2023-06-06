package org.salgar.fsm.pekko.pekkosystem

import com.typesafe.config.Config
import org.apache.pekko.NotUsed
import org.apache.pekko.actor.typed.ActorSystem

trait AdditionalService {
  def config(actorSystem : ActorSystem[NotUsed]) : Unit
  def configureFromFiles() : Config
}
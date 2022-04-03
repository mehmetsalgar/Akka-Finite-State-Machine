package org.salgar.fsm.akka.akkasystem

import akka.NotUsed
import akka.actor.typed.ActorSystem
import com.typesafe.config.Config

trait AdditionalService {
  def config(actorSystem : ActorSystem[NotUsed]) : Unit
  def configureFromFiles() : Config
}
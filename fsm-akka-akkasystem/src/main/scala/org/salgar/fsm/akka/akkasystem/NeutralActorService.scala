package org.salgar.fsm.akka.akkasystem

import akka.NotUsed
import akka.actor.typed.ActorSystem
import com.typesafe.config.{Config, ConfigFactory}
import org.salgar.fsm.akka.akkasystem.NeutralActorService.log
import org.slf4j.{Logger, LoggerFactory}

object NeutralActorService {
  val log : Logger = LoggerFactory.getLogger(NeutralActorService.getClass)
}

class NeutralActorService extends AdditionalService {
  override def config(actorSystem : ActorSystem[NotUsed]): Unit = {
    log.info("We are initializing without Kafka")
  }

  override def configureFromFiles(): Config = {
    ConfigFactory.load("application-local.conf")
  }
}
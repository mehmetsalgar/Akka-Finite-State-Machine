package org.salgar.fsm.pekko.pekkosystem

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.pekko.NotUsed
import org.apache.pekko.actor.typed.ActorSystem
import org.salgar.fsm.pekko.pekkosystem.NeutralActorService.log
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
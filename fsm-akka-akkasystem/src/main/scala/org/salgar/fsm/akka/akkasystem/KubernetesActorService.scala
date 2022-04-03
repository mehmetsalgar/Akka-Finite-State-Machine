package org.salgar.fsm.akka.akkasystem

import akka.NotUsed
import akka.actor.typed.ActorSystem
import akka.management.cluster.bootstrap.ClusterBootstrap
import akka.management.scaladsl.AkkaManagement
import com.typesafe.config.{Config, ConfigFactory}
import org.salgar.fsm.akka.akkasystem.KubernetesActorService.log
import org.slf4j.{Logger, LoggerFactory}

object KubernetesActorService {
  private val log : Logger = LoggerFactory.getLogger(KubernetesActorService.getClass)
}

class KubernetesActorService extends AdditionalService {
  override def config(actorSystem : ActorSystem[NotUsed]): Unit = {
    log.info("We are initializing with Kubernetes")
    AkkaManagement(actorSystem).start()
    ClusterBootstrap(actorSystem).start()
  }

  override def configureFromFiles(): Config = {
    ConfigFactory.load("application-kubernetes.conf")
  }
}
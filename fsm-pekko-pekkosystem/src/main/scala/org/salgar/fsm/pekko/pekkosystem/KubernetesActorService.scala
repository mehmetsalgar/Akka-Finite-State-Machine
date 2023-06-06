package org.salgar.fsm.pekko.pekkosystem

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.pekko.NotUsed
import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.management.cluster.bootstrap.ClusterBootstrap
import org.apache.pekko.management.scaladsl.PekkoManagement
import org.salgar.fsm.pekko.pekkosystem.KubernetesActorService.log
import org.slf4j.{Logger, LoggerFactory}

object KubernetesActorService {
  private val log : Logger = LoggerFactory.getLogger(KubernetesActorService.getClass)
}

class KubernetesActorService extends AdditionalService {
  override def config(actorSystem : ActorSystem[NotUsed]): Unit = {
    log.info("We are initializing with Kubernetes")
    PekkoManagement(actorSystem).start()
    ClusterBootstrap(actorSystem).start()
  }

  override def configureFromFiles(): Config = {
    ConfigFactory.load("application-kubernetes.conf")
  }
}
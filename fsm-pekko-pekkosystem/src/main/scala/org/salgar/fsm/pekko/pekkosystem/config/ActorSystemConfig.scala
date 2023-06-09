package org.salgar.fsm.pekko.pekkosystem.config

import org.salgar.fsm.pekko.pekkosystem.{AdditionalService, KubernetesActorService, NeutralActorService}
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.{Bean, Configuration, Primary, Profile}

@Configuration
class ActorSystemConfig {
  @Profile(Array("kubernetes"))
  @Bean
  @Primary
  def kubernetesActorService() : AdditionalService = {
    new KubernetesActorService();
  }

  @Bean
  def neutralActorService() : AdditionalService = {
    new NeutralActorService()
  }

  @Bean
  @ConfigurationProperties("org.salgar.fsm.pekko.pekko-system")
  def akkaApplicationProperty() : PekkoApplicationProperty = {
    new PekkoApplicationProperty()
  }
}
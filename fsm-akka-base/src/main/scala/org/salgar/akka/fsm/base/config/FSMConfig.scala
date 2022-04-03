package org.salgar.akka.fsm.base.config

import org.salgar.akka.fsm.base.actors.ReceptionFacade
import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class FSMConfig {
  @Bean
  def receptionFacade(): ReceptionFacade = {
    new ReceptionFacade()
  }
}
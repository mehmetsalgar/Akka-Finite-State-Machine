package org.salgar.pekko.fsm.base.config

import org.salgar.pekko.fsm.base.actors.ReceptionFacade
import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class FSMConfig {
  @Bean
  def receptionFacade(): ReceptionFacade = {
    new ReceptionFacade()
  }
}
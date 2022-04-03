package org.salgar.akka.fsm.base.config

import org.salgar.akka.fsm.api.UseCaseKeyStrategy
import org.salgar.akka.fsm.base.actors.ReceptionFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

object ServiceLocatorConfig {
  private var INSTANCE : ServiceLocatorConfig = _

  def getInstance : ServiceLocatorConfig = INSTANCE
}

@Component
case class ServiceLocatorConfig(
                                 @Autowired receptionFacade: ReceptionFacade,
                                 @Autowired useCaseKeyStrategy: UseCaseKeyStrategy
                               ) {
  import ServiceLocatorConfig._

  @PostConstruct
  private def init: Unit = {
    INSTANCE = this
  }
}
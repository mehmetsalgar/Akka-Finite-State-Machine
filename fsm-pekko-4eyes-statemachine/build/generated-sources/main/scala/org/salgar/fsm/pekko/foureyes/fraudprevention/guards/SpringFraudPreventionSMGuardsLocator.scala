package org.salgar.fsm.pekko.foureyes.fraudprevention.guards

import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

object SpringFraudPreventionSMGuardsLocator {
    private var springFraudPreventionSMGuardsLocator: SpringFraudPreventionSMGuardsLocator = _

    def getInstance: SpringFraudPreventionSMGuardsLocator = {
        springFraudPreventionSMGuardsLocator
    }
}

@Component
case class SpringFraudPreventionSMGuardsLocator(
 ) {
 import SpringFraudPreventionSMGuardsLocator._

 @PostConstruct
 def init: Unit = {
    springFraudPreventionSMGuardsLocator = this
 }
}

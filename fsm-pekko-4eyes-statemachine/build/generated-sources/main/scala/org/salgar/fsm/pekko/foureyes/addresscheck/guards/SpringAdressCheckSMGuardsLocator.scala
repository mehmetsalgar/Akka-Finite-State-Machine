package org.salgar.fsm.pekko.foureyes.addresscheck.guards

import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

object SpringAdressCheckSMGuardsLocator {
    private var springAdressCheckSMGuardsLocator: SpringAdressCheckSMGuardsLocator = _

    def getInstance: SpringAdressCheckSMGuardsLocator = {
        springAdressCheckSMGuardsLocator
    }
}

@Component
case class SpringAdressCheckSMGuardsLocator(
 ) {
 import SpringAdressCheckSMGuardsLocator._

 @PostConstruct
 def init: Unit = {
    springAdressCheckSMGuardsLocator = this
 }
}

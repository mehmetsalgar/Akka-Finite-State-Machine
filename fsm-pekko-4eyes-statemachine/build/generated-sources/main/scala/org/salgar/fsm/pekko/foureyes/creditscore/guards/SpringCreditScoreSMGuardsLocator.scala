package org.salgar.fsm.pekko.foureyes.creditscore.guards

import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

object SpringCreditScoreSMGuardsLocator {
    private var springCreditScoreSMGuardsLocator: SpringCreditScoreSMGuardsLocator = _

    def getInstance: SpringCreditScoreSMGuardsLocator = {
        springCreditScoreSMGuardsLocator
    }
}

@Component
case class SpringCreditScoreSMGuardsLocator(
 ) {
 import SpringCreditScoreSMGuardsLocator._

 @PostConstruct
 def init: Unit = {
    springCreditScoreSMGuardsLocator = this
 }
}

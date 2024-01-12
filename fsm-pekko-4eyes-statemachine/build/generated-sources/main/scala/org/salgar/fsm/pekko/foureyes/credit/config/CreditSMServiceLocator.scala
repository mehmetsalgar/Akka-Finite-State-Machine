package org.salgar.fsm.pekko.foureyes.credit.config
import org.salgar.pekko.fsm.api.UseCaseKeyStrategy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

object CreditSMServiceLocator {
    private var INSTANCE : CreditSMServiceLocator = _

    def getInstance : CreditSMServiceLocator = INSTANCE
}

@Component
case class CreditSMServiceLocator (
                            @Autowired useCaseKeyStrategy: UseCaseKeyStrategy
                          ) {
    import CreditSMServiceLocator._

    @PostConstruct
    private def init: Unit = {
        INSTANCE = this
    }
}

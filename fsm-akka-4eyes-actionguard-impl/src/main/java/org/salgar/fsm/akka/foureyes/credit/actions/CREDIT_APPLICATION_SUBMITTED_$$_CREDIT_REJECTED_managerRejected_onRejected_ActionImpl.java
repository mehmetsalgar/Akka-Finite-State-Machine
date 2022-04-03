package org.salgar.fsm.akka.foureyes.credit.actions;

import akka.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.akka.foureyes.credit.CreditSM;

import java.util.Map;

public class CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_managerRejected_onRejected_ActionImpl
        extends CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_managerRejected_onRejected_Action  {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        return payload;
    }
}

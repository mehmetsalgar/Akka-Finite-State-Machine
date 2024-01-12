package org.salgar.fsm.pekko.foureyes.credit.actions;

import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.apache.pekko.persistence.typed.scaladsl.Effect;
import org.apache.pekko.persistence.typed.scaladsl.EffectBuilder;
import org.apache.pekko.persistence.typed.scaladsl.ReplyEffect;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;
import org.salgar.pekko.fsm.api.action.Action;

import java.util.Map;

/**
    <a href="fsm-pekko-asciidoc/build/puml/activity/customer_updated.png">Activity Diagram - customer_updated</a><br>

    This Action will search the State Context for the updated Customer object and actualise it's information.
*/
public abstract class CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_APPLICATION_SUBMITTED_customer_updated_Action
     implements Action<CreditSM.CreditSMEvent, CreditSM.PersistEvent, CreditSM.State, CreditSM.Response> {

    @Override
    public ReplyEffect<CreditSM.PersistEvent, CreditSM.State> doAction(
            ActorContext<CreditSM.CreditSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload,
            ActorRef<CreditSM.Response> replyTo) throws InterruptedException {
        actorContext.log().debug("Executing CREDIT_APPLICATION_SUBMITTED customer_updated Action");

        Map<String, Object> persistPayload = processCustomAction(actorContext, controlObject, payload);

        return processPersist(controlObject, persistPayload, replyTo);
    }

    protected abstract Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                               Map<String, Object> controlObject,
                                                               Map<String, Object> payload);


    protected ReplyEffect<CreditSM.PersistEvent, CreditSM.State> processPersist(
                                                                    Map<String, Object> controlObject,
                                                                    Map<String, Object> persistPayload,
                                                                    ActorRef<CreditSM.Response> replyTo) {
        controlObject.putAll(persistPayload);
        EffectBuilder<CreditSM.PersistEvent, CreditSM.State> effectBuilder =
                        Effect
                        .persist(new CreditSM.CustomerUpdatedEvent(controlObject));

        ReplyEffect<CreditSM.PersistEvent, CreditSM.State> replyEffect;
        if(replyTo == null) {
            replyEffect = effectBuilder.thenNoReply();
        } else {
            replyEffect= effectBuilder.thenReply(replyTo, (state) -> new CreditSM.AcknowledgeResponse());
        }

        return replyEffect;
    }
}

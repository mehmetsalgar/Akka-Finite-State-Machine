package org.salgar.fsm.pekko.foureyes.addresscheck.actions;

import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.apache.pekko.persistence.typed.scaladsl.Effect;
import org.apache.pekko.persistence.typed.scaladsl.EffectBuilder;
import org.apache.pekko.persistence.typed.scaladsl.ReplyEffect;
import org.salgar.fsm.pekko.foureyes.addresscheck.AdressCheckSM;
import org.salgar.pekko.fsm.api.action.Action;

import java.util.Map;

public abstract class WAITING_RESULT_$$_RESULTRECEIVED_waiting_onResult_Action
     implements Action<AdressCheckSM.AdressCheckSMEvent, AdressCheckSM.PersistEvent, AdressCheckSM.State, AdressCheckSM.Response> {

    @Override
    public ReplyEffect<AdressCheckSM.PersistEvent, AdressCheckSM.State> doAction(
            ActorContext<AdressCheckSM.AdressCheckSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload,
            ActorRef<AdressCheckSM.Response> replyTo) throws InterruptedException {
        actorContext.log().debug("Executing WAITING_RESULT waiting_onResult Action");

        Map<String, Object> persistPayload = processCustomAction(actorContext, controlObject, payload);

        return processPersist(controlObject, persistPayload, replyTo);
    }

    protected abstract Map<String, Object> processCustomAction(ActorContext<AdressCheckSM.AdressCheckSMEvent> actorContext,
                                                               Map<String, Object> controlObject,
                                                               Map<String, Object> payload);


    protected ReplyEffect<AdressCheckSM.PersistEvent, AdressCheckSM.State> processPersist(
                                                                    Map<String, Object> controlObject,
                                                                    Map<String, Object> persistPayload,
                                                                    ActorRef<AdressCheckSM.Response> replyTo) {
        controlObject.putAll(persistPayload);
        EffectBuilder<AdressCheckSM.PersistEvent, AdressCheckSM.State> effectBuilder =
                        Effect
                        .persist(new AdressCheckSM.AddressCheckPersistEvent(controlObject));

        ReplyEffect<AdressCheckSM.PersistEvent, AdressCheckSM.State> replyEffect;
        if(replyTo == null) {
            replyEffect = effectBuilder.thenStop().thenNoReply();
        } else {
            replyEffect= effectBuilder.thenStop().thenReply(replyTo, (state) -> new AdressCheckSM.AcknowledgeResponse());
        }

        return replyEffect;
    }
}

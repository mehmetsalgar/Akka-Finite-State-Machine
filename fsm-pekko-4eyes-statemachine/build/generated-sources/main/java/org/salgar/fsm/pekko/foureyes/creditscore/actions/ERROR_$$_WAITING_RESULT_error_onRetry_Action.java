package org.salgar.fsm.pekko.foureyes.creditscore.actions;

import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.apache.pekko.persistence.typed.scaladsl.Effect;
import org.apache.pekko.persistence.typed.scaladsl.EffectBuilder;
import org.apache.pekko.persistence.typed.scaladsl.ReplyEffect;
import org.salgar.fsm.pekko.foureyes.creditscore.CreditScoreSM;
import org.salgar.pekko.fsm.api.action.Action;

import java.util.Map;

public abstract class ERROR_$$_WAITING_RESULT_error_onRetry_Action
     implements Action<CreditScoreSM.CreditScoreSMEvent, CreditScoreSM.PersistEvent, CreditScoreSM.State, CreditScoreSM.Response> {

    @Override
    public ReplyEffect<CreditScoreSM.PersistEvent, CreditScoreSM.State> doAction(
            ActorContext<CreditScoreSM.CreditScoreSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload,
            ActorRef<CreditScoreSM.Response> replyTo) throws InterruptedException {
        actorContext.log().debug("Executing ERROR error_onRetry Action");

        Map<String, Object> persistPayload = processCustomAction(actorContext, controlObject, payload);

        return processPersist(controlObject, persistPayload, replyTo);
    }

    protected abstract Map<String, Object> processCustomAction(ActorContext<CreditScoreSM.CreditScoreSMEvent> actorContext,
                                                               Map<String, Object> controlObject,
                                                               Map<String, Object> payload);


    protected ReplyEffect<CreditScoreSM.PersistEvent, CreditScoreSM.State> processPersist(
                                                                    Map<String, Object> controlObject,
                                                                    Map<String, Object> persistPayload,
                                                                    ActorRef<CreditScoreSM.Response> replyTo) {
        controlObject.putAll(persistPayload);
        EffectBuilder<CreditScoreSM.PersistEvent, CreditScoreSM.State> effectBuilder =
                        Effect
                        .persist(new CreditScoreSM.CreditScoreRetryPersistEvent(controlObject));

        ReplyEffect<CreditScoreSM.PersistEvent, CreditScoreSM.State> replyEffect;
        if(replyTo == null) {
            replyEffect = effectBuilder.thenNoReply();
        } else {
            replyEffect= effectBuilder.thenReply(replyTo, (state) -> new CreditScoreSM.AcknowledgeResponse());
        }

        return replyEffect;
    }
}

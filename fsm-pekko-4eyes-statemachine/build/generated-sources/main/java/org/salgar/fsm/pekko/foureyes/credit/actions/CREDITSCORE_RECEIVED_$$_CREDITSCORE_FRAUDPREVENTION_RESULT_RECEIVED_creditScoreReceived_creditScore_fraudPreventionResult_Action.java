package org.salgar.fsm.pekko.foureyes.credit.actions;

import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.apache.pekko.persistence.typed.scaladsl.Effect;
import org.apache.pekko.persistence.typed.scaladsl.EffectBuilder;
import org.apache.pekko.persistence.typed.scaladsl.ReplyEffect;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;
import org.salgar.pekko.fsm.api.action.Action;

import java.util.Map;

public abstract class CREDITSCORE_RECEIVED_$$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED_creditScoreReceived_creditScore_fraudPreventionResult_Action
     implements Action<CreditSM.CreditSMEvent, CreditSM.PersistEvent, CreditSM.State, CreditSM.Response> {

    @Override
    public ReplyEffect<CreditSM.PersistEvent, CreditSM.State> doAction(
            ActorContext<CreditSM.CreditSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload,
            ActorRef<CreditSM.Response> replyTo) throws InterruptedException {
        actorContext.log().debug("Executing CREDITSCORE_RECEIVED creditScoreReceived_creditScore_fraudPreventionResult Action");

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
                        .persist(new CreditSM.CreditScoreFraudPreventionReceivedPersistEvent(controlObject));

        ReplyEffect<CreditSM.PersistEvent, CreditSM.State> replyEffect;
        if(replyTo == null) {
            replyEffect = effectBuilder.thenNoReply();
        } else {
            replyEffect= effectBuilder.thenReply(replyTo, (state) -> new CreditSM.AcknowledgeResponse());
        }

        return replyEffect;
    }
}
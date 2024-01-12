package org.salgar.fsm.pekko.foureyes.fraudprevention.actions;

import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.apache.pekko.persistence.typed.scaladsl.Effect;
import org.apache.pekko.persistence.typed.scaladsl.EffectBuilder;
import org.apache.pekko.persistence.typed.scaladsl.ReplyEffect;
import org.salgar.fsm.pekko.foureyes.fraudprevention.FraudPreventionSM;
import org.salgar.pekko.fsm.api.action.Action;

import java.util.Map;

public abstract class WAITING_RESULT_$$_RESULTRECEIVED_waitingResult_onResult_Action
     implements Action<FraudPreventionSM.FraudPreventionSMEvent, FraudPreventionSM.PersistEvent, FraudPreventionSM.State, FraudPreventionSM.Response> {

    @Override
    public ReplyEffect<FraudPreventionSM.PersistEvent, FraudPreventionSM.State> doAction(
            ActorContext<FraudPreventionSM.FraudPreventionSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload,
            ActorRef<FraudPreventionSM.Response> replyTo) throws InterruptedException {
        actorContext.log().debug("Executing WAITING_RESULT waitingResult_onResult Action");

        Map<String, Object> persistPayload = processCustomAction(actorContext, controlObject, payload);

        return processPersist(controlObject, persistPayload, replyTo);
    }

    protected abstract Map<String, Object> processCustomAction(ActorContext<FraudPreventionSM.FraudPreventionSMEvent> actorContext,
                                                               Map<String, Object> controlObject,
                                                               Map<String, Object> payload);


    protected ReplyEffect<FraudPreventionSM.PersistEvent, FraudPreventionSM.State> processPersist(
                                                                    Map<String, Object> controlObject,
                                                                    Map<String, Object> persistPayload,
                                                                    ActorRef<FraudPreventionSM.Response> replyTo) {
        controlObject.putAll(persistPayload);
        EffectBuilder<FraudPreventionSM.PersistEvent, FraudPreventionSM.State> effectBuilder =
                        Effect
                        .persist(new FraudPreventionSM.FraudPreventionReceivedPersistEvent(controlObject));

        ReplyEffect<FraudPreventionSM.PersistEvent, FraudPreventionSM.State> replyEffect;
        if(replyTo == null) {
            replyEffect = effectBuilder.thenStop().thenNoReply();
        } else {
            replyEffect= effectBuilder.thenStop().thenReply(replyTo, (state) -> new FraudPreventionSM.AcknowledgeResponse());
        }

        return replyEffect;
    }
}

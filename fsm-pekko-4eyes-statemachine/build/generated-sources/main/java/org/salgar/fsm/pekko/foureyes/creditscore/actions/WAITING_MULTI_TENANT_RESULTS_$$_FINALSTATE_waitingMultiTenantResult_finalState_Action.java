package org.salgar.fsm.pekko.foureyes.creditscore.actions;

import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.apache.pekko.persistence.typed.scaladsl.Effect;
import org.apache.pekko.persistence.typed.scaladsl.EffectBuilder;
import org.apache.pekko.persistence.typed.scaladsl.ReplyEffect;
import org.salgar.fsm.pekko.foureyes.creditscore.MultiTenantCreditScoreSM;
import org.salgar.pekko.fsm.api.action.Action;

import java.util.Map;

public abstract class WAITING_MULTI_TENANT_RESULTS_$$_FINALSTATE_waitingMultiTenantResult_finalState_Action
     implements Action<MultiTenantCreditScoreSM.MultiTenantCreditScoreSMEvent, MultiTenantCreditScoreSM.PersistEvent, MultiTenantCreditScoreSM.State, MultiTenantCreditScoreSM.Response> {

    @Override
    public ReplyEffect<MultiTenantCreditScoreSM.PersistEvent, MultiTenantCreditScoreSM.State> doAction(
            ActorContext<MultiTenantCreditScoreSM.MultiTenantCreditScoreSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload,
            ActorRef<MultiTenantCreditScoreSM.Response> replyTo) throws InterruptedException {
        actorContext.log().debug("Executing WAITING_MULTI_TENANT_RESULTS waitingMultiTenantResult_finalState Action");

        Map<String, Object> persistPayload = processCustomAction(actorContext, controlObject, payload);

        return processPersist(controlObject, persistPayload, replyTo);
    }

    protected abstract Map<String, Object> processCustomAction(ActorContext<MultiTenantCreditScoreSM.MultiTenantCreditScoreSMEvent> actorContext,
                                                               Map<String, Object> controlObject,
                                                               Map<String, Object> payload);


    protected ReplyEffect<MultiTenantCreditScoreSM.PersistEvent, MultiTenantCreditScoreSM.State> processPersist(
                                                                    Map<String, Object> controlObject,
                                                                    Map<String, Object> persistPayload,
                                                                    ActorRef<MultiTenantCreditScoreSM.Response> replyTo) {
        controlObject.putAll(persistPayload);
        EffectBuilder<MultiTenantCreditScoreSM.PersistEvent, MultiTenantCreditScoreSM.State> effectBuilder =
                        Effect
                        .persist(new MultiTenantCreditScoreSM.MultiTenantResultsReceivedPersistentEvent(controlObject));

        ReplyEffect<MultiTenantCreditScoreSM.PersistEvent, MultiTenantCreditScoreSM.State> replyEffect;
        if(replyTo == null) {
            replyEffect = effectBuilder.thenStop().thenNoReply();
        } else {
            replyEffect= effectBuilder.thenStop().thenReply(replyTo, (state) -> new MultiTenantCreditScoreSM.AcknowledgeResponse());
        }

        return replyEffect;
    }
}

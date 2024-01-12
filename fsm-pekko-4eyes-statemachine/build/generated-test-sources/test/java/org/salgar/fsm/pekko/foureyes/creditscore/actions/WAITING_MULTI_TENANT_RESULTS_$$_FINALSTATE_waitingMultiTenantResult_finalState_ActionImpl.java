package org.salgar.fsm.pekko.foureyes.creditscore.actions;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.creditscore.MultiTenantCreditScoreSM;

import java.util.Map;

public class WAITING_MULTI_TENANT_RESULTS_$$_FINALSTATE_waitingMultiTenantResult_finalState_ActionImpl
    extends WAITING_MULTI_TENANT_RESULTS_$$_FINALSTATE_waitingMultiTenantResult_finalState_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<MultiTenantCreditScoreSM.MultiTenantCreditScoreSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        return payload;
    }
}

package org.salgar.fsm.pekko.foureyes.creditscore.actions;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.creditscore.MultiTenantCreditScoreSM;

import java.util.Map;

public class INITIAL_MTCS_$$_WAITING_MULTI_TENANT_RESULTS_initial_WaitingMultiTenantResult_ActionImpl
    extends INITIAL_MTCS_$$_WAITING_MULTI_TENANT_RESULTS_initial_WaitingMultiTenantResult_Action {

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<MultiTenantCreditScoreSM.MultiTenantCreditScoreSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        return payload;
    }
}

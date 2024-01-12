package org.salgar.fsm.pekko.foureyes.creditscore.guards;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.creditscore.MultiTenantCreditScoreSM;

import java.util.Map;

public class WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_isNotAllTenantsResponded_GuardImpl
    implements WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_isNotAllTenantsResponded_Guard {
    @Override
    public boolean evaluate(
            ActorContext<MultiTenantCreditScoreSM.MultiTenantCreditScoreSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload) {
        actorContext.log().debug("Evaluating WAITING_MULTI_TENANT_RESULTS waitingMultiTenantResults_waitingMultiTenantResults Guard");

        return true;
    }
}

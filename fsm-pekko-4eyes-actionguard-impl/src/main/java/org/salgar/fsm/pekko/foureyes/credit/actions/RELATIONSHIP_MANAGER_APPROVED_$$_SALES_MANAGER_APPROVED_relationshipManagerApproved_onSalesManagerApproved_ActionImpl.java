package org.salgar.fsm.pekko.foureyes.credit.actions;

import lombok.RequiredArgsConstructor;
import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.addresscheck.facade.AdressCheckSMFacade;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;
import org.salgar.fsm.pekko.foureyes.credit.config.CreditSMServiceLocator;
import org.salgar.fsm.pekko.foureyes.creditscore.facade.MultiTenantCreditScoreSMFacade;
import org.salgar.fsm.pekko.foureyes.fraudprevention.facade.FraudPreventionSMFacade;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class RELATIONSHIP_MANAGER_APPROVED_$$_SALES_MANAGER_APPROVED_relationshipManagerApproved_onSalesManagerApproved_ActionImpl
    extends RELATIONSHIP_MANAGER_APPROVED_$$_SALES_MANAGER_APPROVED_relationshipManagerApproved_onSalesManagerApproved_Action {
    private final MultiTenantCreditScoreSMFacade multiTenantScreditScoreSMFacade;
    private final FraudPreventionSMFacade fraudPreventionSMFacade;
    private final AdressCheckSMFacade adressCheckSMFacade;

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<CreditSM.CreditSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        if(payload != null) {
            String useCaseKey = CreditSMServiceLocator.getInstance().useCaseKeyStrategy().getKey(payload);

            Map<String, Object> creditScorePayload = new HashMap<>(payload);
            multiTenantScreditScoreSMFacade.startMultiTenantCreditScoreResearch(
                    () -> useCaseKey,
                    creditScorePayload
            );

            Map<String, Object> fraudPreventionPayload = new HashMap<>(payload);
            fraudPreventionSMFacade.startFraudPreventionEvaluation(
                    () -> useCaseKey,
                    fraudPreventionPayload
            );

            Map<String, Object> addressCheckPayload = new HashMap<>(payload);
            adressCheckSMFacade.startAdressCheckResearch(
                    () -> useCaseKey,
                    addressCheckPayload
            );
        }

        return payload;
    }
}
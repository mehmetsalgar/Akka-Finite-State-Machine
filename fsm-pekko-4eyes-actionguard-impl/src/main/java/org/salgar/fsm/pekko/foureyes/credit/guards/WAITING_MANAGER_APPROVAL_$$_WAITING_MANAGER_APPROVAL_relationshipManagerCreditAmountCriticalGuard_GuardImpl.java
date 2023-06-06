package org.salgar.fsm.pekko.foureyes.credit.guards;

import lombok.RequiredArgsConstructor;
import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.List;
import java.util.Map;

import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.CREDIT_AMOUNT;
import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.NUMBER_OF_RELATIONSHIP_MANAGERS_APPROVED;
import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.RELATIONSHIP_MANAGERS;

@RequiredArgsConstructor
public class WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_relationshipManagerCreditAmountCriticalGuard_GuardImpl
    implements WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_relationshipManagerCreditAmountCriticalGuard_Guard {
    @Override
    public boolean evaluate(
            ActorContext<CreditSM.CreditSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload) {
        actorContext.log().debug("Evaluating WAITING_MANAGER_APPROVAL waitingManagerApproval_onRelationshipManagerApproved Guard");

        Double creditAmount = (Double) controlObject.get(CREDIT_AMOUNT);
        if(creditAmount >= 10000000.0) {
            List<String> listOfRm = (List<String>) controlObject.get(RELATIONSHIP_MANAGERS);
            if(listOfRm != null) {
                Integer numberOfManagers = listOfRm.size();

                Integer actualRMApprovals  = (Integer) controlObject.get(NUMBER_OF_RELATIONSHIP_MANAGERS_APPROVED);

                if(actualRMApprovals == null && numberOfManagers == 1) {
                    return false;
                } else if (actualRMApprovals == null && numberOfManagers > 1  ) {
                    return true;
                } else if (actualRMApprovals != null && actualRMApprovals < numberOfManagers - 1 ) {
                    return true;
                } else {
                    return false;
                }
            }
            return true;
        }

        return false;
    }
}

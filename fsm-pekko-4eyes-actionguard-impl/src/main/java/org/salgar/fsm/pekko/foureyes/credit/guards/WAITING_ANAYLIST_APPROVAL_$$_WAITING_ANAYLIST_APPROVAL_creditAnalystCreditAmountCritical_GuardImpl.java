package org.salgar.fsm.pekko.foureyes.credit.guards;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;

import java.util.List;
import java.util.Map;

import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.CREDIT_AMOUNT;
import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.CREDIT_ANALYSTS;
import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.NUMBER_OF_CREDIT_ANALYST_APPROVED;

public class WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_creditAnalystCreditAmountCritical_GuardImpl
    implements WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_creditAnalystCreditAmountCritical_Guard {
    @Override
    public boolean evaluate(
            ActorContext<CreditSM.CreditSMEvent> actorContext,
            Map<String, Object> controlObject,
            Map<String, Object> payload) {
        actorContext.log().debug("Evaluating WAITING_ANAYLIST_APPROVAL waitingAnalystApproval_onCreditAccepted Guard");

        Double creditAmount = (Double) controlObject.get(CREDIT_AMOUNT);
        if(creditAmount >= 10000000.0) {
            List<String> listOfRm = (List<String>) controlObject.get(CREDIT_ANALYSTS);
            if(listOfRm != null) {
                Integer numberOfManagers = listOfRm.size();

                Integer actualRMApprovals  = (Integer) controlObject.get(NUMBER_OF_CREDIT_ANALYST_APPROVED);

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
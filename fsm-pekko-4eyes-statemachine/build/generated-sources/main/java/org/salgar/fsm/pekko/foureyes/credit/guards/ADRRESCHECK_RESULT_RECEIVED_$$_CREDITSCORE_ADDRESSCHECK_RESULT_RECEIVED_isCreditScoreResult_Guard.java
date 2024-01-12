package org.salgar.fsm.pekko.foureyes.credit.guards;

import org.salgar.fsm.pekko.foureyes.credit.CreditSM;
import org.salgar.pekko.fsm.api.guard.Guard;

/**
    We have to check the Result is arriving from Credit Score Slave State Machine.
*/
public interface ADRRESCHECK_RESULT_RECEIVED_$$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED_isCreditScoreResult_Guard
    extends Guard<CreditSM.CreditSMEvent> {
}

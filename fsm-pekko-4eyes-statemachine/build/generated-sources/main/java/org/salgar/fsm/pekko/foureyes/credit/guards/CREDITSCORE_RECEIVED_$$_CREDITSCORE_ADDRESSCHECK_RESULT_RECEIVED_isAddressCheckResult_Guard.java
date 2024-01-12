package org.salgar.fsm.pekko.foureyes.credit.guards;

import org.salgar.fsm.pekko.foureyes.credit.CreditSM;
import org.salgar.pekko.fsm.api.guard.Guard;

/**
    We have to check the Result is arriving from Address Check Slave State Machine.
*/
public interface CREDITSCORE_RECEIVED_$$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED_isAddressCheckResult_Guard
    extends Guard<CreditSM.CreditSMEvent> {
}

package org.salgar.fsm.pekko.foureyes.credit

import org.salgar.fsm.pekko.foureyes.credit.CreditSM._

import java.util

object CreditSMSnapshotAdapter extends SnapshotAdapter[State] {
    override def toJournal(state: State): Any = {
        CreditSMSnapshot(state.getClass.getSimpleName, state.controlObject)
    }

    override def fromJournal(from: Any): State = {
        from match {
            case creditsmSnapshot: CreditSMSnapshot =>
                creditsmSnapshot.state match {
                    case "INITIAL" => INITIAL(new util.HashMap[java.lang.String, AnyRef])
                    case "CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_WAITING_MANAGER_APPROVAL" => CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_WAITING_MANAGER_APPROVAL(creditsmSnapshot.controlObject)
                    case "CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_WAITING_MANAGER_APPROVAL" => CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_WAITING_MANAGER_APPROVAL(creditsmSnapshot.controlObject)
                    case "CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_RECEIVED" => CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_RECEIVED(creditsmSnapshot.controlObject)
                    case "CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_RESULT_RECEIVED" => CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_RESULT_RECEIVED(creditsmSnapshot.controlObject)
                    case "CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_ADRRESCHECK_RESULT_RECEIVED" => CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_ADRRESCHECK_RESULT_RECEIVED(creditsmSnapshot.controlObject)
                    case "CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED" => CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED(creditsmSnapshot.controlObject)
                    case "CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED" => CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED(creditsmSnapshot.controlObject)
                    case "CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED" => CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED(creditsmSnapshot.controlObject)
                    case "CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_WAITING_ANAYLIST_APPROVAL" => CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_WAITING_ANAYLIST_APPROVAL(creditsmSnapshot.controlObject)
                    case "CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_FROM_SENIOR_MANAGER" => CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_FROM_SENIOR_MANAGER(creditsmSnapshot.controlObject)
                    case "CREDIT_APPLICATION_SUBMITTED_$_CREDIT_ACCEPTED" => CREDIT_APPLICATION_SUBMITTED_$_CREDIT_ACCEPTED(creditsmSnapshot.controlObject)
                    case "CREDIT_REJECTED" => CREDIT_REJECTED(creditsmSnapshot.controlObject)
                        case _und @ _ => throw new IllegalStateException("Unidentified Event for Snapshot, may be an Event/Schema evolution occurring  Type: " + _und.toString)
                }
            case _unk @ _  => throw new IllegalStateException("Unknown Snapshot Type: " + _unk.toString)
        }
    }
}

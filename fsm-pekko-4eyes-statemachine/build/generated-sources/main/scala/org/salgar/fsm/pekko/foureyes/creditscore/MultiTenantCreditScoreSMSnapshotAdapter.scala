package org.salgar.fsm.pekko.foureyes.creditscore

import org.salgar.fsm.pekko.foureyes.creditscore.MultiTenantCreditScoreSM._

import java.util

object MultiTenantCreditScoreSMSnapshotAdapter extends SnapshotAdapter[State] {
    override def toJournal(state: State): Any = {
        MultiTenantCreditScoreSMSnapshot(state.getClass.getSimpleName, state.controlObject)
    }

    override def fromJournal(from: Any): State = {
        from match {
            case multitenantcreditscoresmSnapshot: MultiTenantCreditScoreSMSnapshot =>
                multitenantcreditscoresmSnapshot.state match {
                    case "INITIAL_MTCS" => INITIAL_MTCS(new util.HashMap[java.lang.String, AnyRef])
                    case "FINALSTATE" => FINALSTATE(multitenantcreditscoresmSnapshot.controlObject)
                    case "WAITING_MULTI_TENANT_RESULTS" => WAITING_MULTI_TENANT_RESULTS(multitenantcreditscoresmSnapshot.controlObject)
                        case _und @ _ => throw new IllegalStateException("Unidentified Event for Snapshot, may be an Event/Schema evolution occurring  Type: " + _und.toString)
                }
            case _unk @ _  => throw new IllegalStateException("Unknown Snapshot Type: " + _unk.toString)
        }
    }
}

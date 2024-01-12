package org.salgar.fsm.pekko.foureyes.fraudprevention

import org.salgar.fsm.pekko.foureyes.fraudprevention.FraudPreventionSM._

import java.util

object FraudPreventionSMSnapshotAdapter extends SnapshotAdapter[State] {
    override def toJournal(state: State): Any = {
        FraudPreventionSMSnapshot(state.getClass.getSimpleName, state.controlObject)
    }

    override def fromJournal(from: Any): State = {
        from match {
            case fraudpreventionsmSnapshot: FraudPreventionSMSnapshot =>
                fraudpreventionsmSnapshot.state match {
                    case "INITIAL" => INITIAL(new util.HashMap[java.lang.String, AnyRef])
                    case "ERROR" => ERROR(fraudpreventionsmSnapshot.controlObject)
                    case "RESULTRECEIVED" => RESULTRECEIVED(fraudpreventionsmSnapshot.controlObject)
                    case "WAITING_RESULT" => WAITING_RESULT(fraudpreventionsmSnapshot.controlObject)
                        case _und @ _ => throw new IllegalStateException("Unidentified Event for Snapshot, may be an Event/Schema evolution occurring  Type: " + _und.toString)
                }
            case _unk @ _  => throw new IllegalStateException("Unknown Snapshot Type: " + _unk.toString)
        }
    }
}

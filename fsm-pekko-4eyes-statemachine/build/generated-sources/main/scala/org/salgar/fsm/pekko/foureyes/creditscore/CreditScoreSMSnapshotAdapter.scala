package org.salgar.fsm.pekko.foureyes.creditscore

import org.salgar.fsm.pekko.foureyes.creditscore.CreditScoreSM._

import java.util

object CreditScoreSMSnapshotAdapter extends SnapshotAdapter[State] {
    override def toJournal(state: State): Any = {
        CreditScoreSMSnapshot(state.getClass.getSimpleName, state.controlObject)
    }

    override def fromJournal(from: Any): State = {
        from match {
            case creditscoresmSnapshot: CreditScoreSMSnapshot =>
                creditscoresmSnapshot.state match {
                    case "INITIAL" => INITIAL(new util.HashMap[java.lang.String, AnyRef])
                    case "ERROR" => ERROR(creditscoresmSnapshot.controlObject)
                    case "RESULTRECEIVED" => RESULTRECEIVED(creditscoresmSnapshot.controlObject)
                    case "WAITING_RESULT" => WAITING_RESULT(creditscoresmSnapshot.controlObject)
                        case _und @ _ => throw new IllegalStateException("Unidentified Event for Snapshot, may be an Event/Schema evolution occurring  Type: " + _und.toString)
                }
            case _unk @ _  => throw new IllegalStateException("Unknown Snapshot Type: " + _unk.toString)
        }
    }
}

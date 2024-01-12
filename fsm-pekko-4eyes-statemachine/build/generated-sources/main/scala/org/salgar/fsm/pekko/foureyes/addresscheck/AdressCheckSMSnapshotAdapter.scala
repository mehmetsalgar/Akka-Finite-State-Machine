package org.salgar.fsm.pekko.foureyes.addresscheck

import org.salgar.fsm.pekko.foureyes.addresscheck.AdressCheckSM._

import java.util

object AdressCheckSMSnapshotAdapter extends SnapshotAdapter[State] {
    override def toJournal(state: State): Any = {
        AdressCheckSMSnapshot(state.getClass.getSimpleName, state.controlObject)
    }

    override def fromJournal(from: Any): State = {
        from match {
            case adresschecksmSnapshot: AdressCheckSMSnapshot =>
                adresschecksmSnapshot.state match {
                    case "INITIAL" => INITIAL(new util.HashMap[java.lang.String, AnyRef])
                    case "ERROR" => ERROR(adresschecksmSnapshot.controlObject)
                    case "RESULTRECEIVED" => RESULTRECEIVED(adresschecksmSnapshot.controlObject)
                    case "WAITING_RESULT" => WAITING_RESULT(adresschecksmSnapshot.controlObject)
                        case _und @ _ => throw new IllegalStateException("Unidentified Event for Snapshot, may be an Event/Schema evolution occurring  Type: " + _und.toString)
                }
            case _unk @ _  => throw new IllegalStateException("Unknown Snapshot Type: " + _unk.toString)
        }
    }
}

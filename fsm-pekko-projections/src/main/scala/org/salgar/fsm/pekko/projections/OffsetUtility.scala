package org.salgar.fsm.pekko.projections

import org.apache.pekko.persistence.query._

object OffsetUtility {
  def calculateOffset(offset: Offset): String = {
    offset match {
      case NoOffset => "NoOffset"
      case Sequence(value) => value.toString;
      case TimeBasedUUID(value) => value.toString
      case TimestampOffset(timestamp, readTimestamp, seen) => timestamp.toString + "_" + readTimestamp + "_" + seen
      case _ => throw new IllegalStateException("Unknown Offset type: " + offset.getClass.getName)
    }
  }
}

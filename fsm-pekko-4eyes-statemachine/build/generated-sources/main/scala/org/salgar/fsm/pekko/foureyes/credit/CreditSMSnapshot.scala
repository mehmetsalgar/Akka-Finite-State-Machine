package org.salgar.fsm.pekko.foureyes.credit

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.salgar.pekko.fsm.base.CborSerializable

import java.util

case class CreditSMSnapshot(
                               state : String,
                               @JsonDeserialize(as = classOf[util.Map[java.lang.String, AnyRef]]) controlObject: util.Map[java.lang.String, AnyRef])
  extends CborSerializable

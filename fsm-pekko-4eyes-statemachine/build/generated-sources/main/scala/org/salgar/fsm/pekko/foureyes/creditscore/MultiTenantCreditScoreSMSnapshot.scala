package org.salgar.fsm.pekko.foureyes.creditscore

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.salgar.pekko.fsm.base.CborSerializable

import java.util

case class MultiTenantCreditScoreSMSnapshot(
                               state : String,
                               @JsonDeserialize(as = classOf[util.Map[java.lang.String, AnyRef]]) controlObject: util.Map[java.lang.String, AnyRef])
  extends CborSerializable

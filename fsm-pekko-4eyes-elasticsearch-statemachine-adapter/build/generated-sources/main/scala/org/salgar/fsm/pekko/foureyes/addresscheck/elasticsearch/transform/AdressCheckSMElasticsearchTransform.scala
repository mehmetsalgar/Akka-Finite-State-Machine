package org.salgar.fsm.pekko.foureyes.addresscheck.elasticsearch.transform

import org.salgar.fsm.pekko.foureyes.addresscheck.AdressCheckSM

object AdressCheckSMElasticsearchTransform {
  def transform(persistEvent: AdressCheckSM.PersistEvent): String = {
    persistEvent match {
      case AdressCheckSM.AddressCheckRetryPersistEvent(payload) =>
        "WAITING_RESULT"

      case AdressCheckSM.StartAdressCheckPersistEvent(payload) =>
        "WAITING_RESULT"

      case AdressCheckSM.AddressCheckErrorPersistEvent(payload) =>
        "ERROR"

      case AdressCheckSM.AddressCheckPersistEvent(payload) =>
        "RESULTRECEIVED"

      case _ => null
    }
  }
}

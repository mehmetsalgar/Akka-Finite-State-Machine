package org.salgar.fsm.pekko.foureyes.fraudprevention.elasticsearch.transform

import org.salgar.fsm.pekko.foureyes.fraudprevention.FraudPreventionSM

object FraudPreventionSMElasticsearchTransform {
  def transform(persistEvent: FraudPreventionSM.PersistEvent): String = {
    persistEvent match {
      case FraudPreventionSM.FraudPreventionRetryPersistEvent(payload) =>
        "WAITING_RESULT"

      case FraudPreventionSM.FraudPreventionPersistEvemt(payload) =>
        "WAITING_RESULT"

      case FraudPreventionSM.FraudPreventionErrorPersistEvent(payload) =>
        "ERROR"

      case FraudPreventionSM.FraudPreventionReceivedPersistEvent(payload) =>
        "RESULTRECEIVED"

      case _ => null
    }
  }
}

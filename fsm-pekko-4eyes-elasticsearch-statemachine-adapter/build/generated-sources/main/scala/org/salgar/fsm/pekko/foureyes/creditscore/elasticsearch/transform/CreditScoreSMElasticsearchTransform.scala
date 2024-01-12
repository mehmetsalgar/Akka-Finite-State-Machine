package org.salgar.fsm.pekko.foureyes.creditscore.elasticsearch.transform

import org.salgar.fsm.pekko.foureyes.creditscore.CreditScoreSM

object CreditScoreSMElasticsearchTransform {
  def transform(persistEvent: CreditScoreSM.PersistEvent): String = {
    persistEvent match {
      case CreditScoreSM.CreditScoreRetryPersistEvent(payload) =>
        "WAITING_RESULT"

      case CreditScoreSM.StartCreditScoreResearchPersistEvent(payload) =>
        "WAITING_RESULT"

      case CreditScoreSM.CreditScoreErrorPersistEvent(payload) =>
        "ERROR"

      case CreditScoreSM.CreditScorePersistEvent(payload) =>
        "RESULTRECEIVED"

      case _ => null
    }
  }
}

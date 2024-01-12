package org.salgar.fsm.pekko.foureyes.creditscore.elasticsearch.transform

import org.salgar.fsm.pekko.foureyes.creditscore.MultiTenantCreditScoreSM

object MultiTenantCreditScoreSMElasticsearchTransform {
  def transform(persistEvent: MultiTenantCreditScoreSM.PersistEvent): String = {
    persistEvent match {
      case MultiTenantCreditScoreSM.StartMultiTenantResearchPersistEvent(payload) =>
        "WAITING_MULTI_TENANT_RESULTS"

      case MultiTenantCreditScoreSM.MultiTenantResultsReceivedPersistentEvent(payload) =>
        "FINALSTATE"

      case MultiTenantCreditScoreSM.OneTenantCreditScoreResultReceived(payload) =>
        "WAITING_MULTI_TENANT_RESULTS"

      case _ => null
    }
  }
}

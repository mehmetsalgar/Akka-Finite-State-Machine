package org.salgar.fsm.pekko.foureyes.creditscore.projections

import org.apache.pekko.projection.elasticsearch.ElasticsearchEnvelope
import org.salgar.fsm.pekko.elasticsearch.ElasticsearchRepository
import org.salgar.fsm.pekko.foureyes.creditscore.MultiTenantCreditScoreSM
import org.salgar.fsm.pekko.foureyes.creditscore.elasticsearch.transform.MultiTenantCreditScoreSMElasticsearchTransform
import org.salgar.fsm.pekko.pekkosystem.ActorService
import org.salgar.fsm.pekko.projections.OffsetUtility.calculateOffset
import org.salgar.pekko.fsm.api.UseCaseKeyStrategy

import java.util.concurrent.CompletableFuture

class MultiTenantCreditScoreSMPersistEventProcessor(
    elasticsearchRepository: ElasticsearchRepository,
    useCaseKeyStrategy: UseCaseKeyStrategy,
    actorService: ActorService
) {
  implicit def executionContext: ExecutionContext = actorService.actorSystem().executionContext

  def matching(envelope: ElasticsearchEnvelope[EventEnvelope[MultiTenantCreditScoreSM.PersistEvent]]): Future[Done] = {
    envelope.eventEnvelope.event match {
      case creditScoreRetryPersistEvent @ MultiTenantCreditScoreSM.CreditScoreRetryPersistEvent(payload) => {
        process(envelope, payload, creditScoreRetryPersistEvent)
      }
      case creditScoreErrorPersistEvent @ MultiTenantCreditScoreSM.CreditScoreErrorPersistEvent(payload) => {
        process(envelope, payload, creditScoreErrorPersistEvent)
      }
      case startMultiTenantResearchPersistEvent @ MultiTenantCreditScoreSM.StartMultiTenantResearchPersistEvent(
            payload
          ) => {
        process(envelope, payload, startMultiTenantResearchPersistEvent)
      }
      case oneTenantCreditScoreResultReceived @ MultiTenantCreditScoreSM.OneTenantCreditScoreResultReceived(payload) => {
        process(envelope, payload, oneTenantCreditScoreResultReceived)
      }
      case multiTenantResultsReceivedPersistentEvent @ MultiTenantCreditScoreSM
            .MultiTenantResultsReceivedPersistentEvent(payload) => {
        process(envelope, payload, multiTenantResultsReceivedPersistentEvent)
      }
      case creditScorePersistEvent @ MultiTenantCreditScoreSM.CreditScorePersistEvent(payload) => {
        process(envelope, payload, creditScorePersistEvent)
      }
      case startCreditScoreResearchPersistEvent @ MultiTenantCreditScoreSM.StartCreditScoreResearchPersistEvent(
            payload
          ) => {
        process(envelope, payload, startCreditScoreResearchPersistEvent)
      }
    }
  }

  private def process(
      envelope: ElasticsearchEnvelope[EventEnvelope[MultiTenantCreditScoreSM.PersistEvent]],
      payload: java.util.Map[String, AnyRef],
      persistEvent: MultiTenantCreditScoreSM.PersistEvent
  ): Future[Done] = {
    val future: CompletableFuture[Void] = elasticsearchRepository
      .index(
        calculateOffset(envelope.eventEnvelope.offset),
        envelope.eventEnvelope.persistenceId,
        envelope.eventEnvelope.sequenceNr,
        envelope.projectionId.name,
        envelope.projectionId.key,
        useCaseKeyStrategy.getKey(payload),
        "multitenantcreditscoresm",
        MultiTenantCreditScoreSMElasticsearchTransform.transform(persistEvent),
        payload
      )
    future.asScala.map(_ => Done)
  }
}

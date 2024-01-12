package org.salgar.fsm.pekko.foureyes.creditscore.projections

import org.apache.pekko.projection.elasticsearch.ElasticsearchEnvelope
import org.salgar.fsm.pekko.elasticsearch.ElasticsearchRepository
import org.salgar.fsm.pekko.foureyes.creditscore.CreditScoreSM
import org.salgar.fsm.pekko.foureyes.creditscore.elasticsearch.transform.CreditScoreSMElasticsearchTransform
import org.salgar.fsm.pekko.pekkosystem.ActorService
import org.salgar.fsm.pekko.projections.OffsetUtility.calculateOffset
import org.salgar.pekko.fsm.api.UseCaseKeyStrategy

import java.util.concurrent.CompletableFuture

class CreditScoreSMPersistEventProcessor(
    elasticsearchRepository: ElasticsearchRepository,
    useCaseKeyStrategy: UseCaseKeyStrategy,
    actorService: ActorService
) {
  implicit def executionContext: ExecutionContext = actorService.actorSystem().executionContext

  def matching(envelope: ElasticsearchEnvelope[EventEnvelope[CreditScoreSM.PersistEvent]]): Future[Done] = {
    envelope.eventEnvelope.event match {
      case creditScoreRetryPersistEvent @ CreditScoreSM.CreditScoreRetryPersistEvent(payload) => {
        process(envelope, payload, creditScoreRetryPersistEvent)
      }
      case creditScoreErrorPersistEvent @ CreditScoreSM.CreditScoreErrorPersistEvent(payload) => {
        process(envelope, payload, creditScoreErrorPersistEvent)
      }
      case startMultiTenantResearchPersistEvent @ CreditScoreSM.StartMultiTenantResearchPersistEvent(payload) => {
        process(envelope, payload, startMultiTenantResearchPersistEvent)
      }
      case oneTenantCreditScoreResultReceived @ CreditScoreSM.OneTenantCreditScoreResultReceived(payload) => {
        process(envelope, payload, oneTenantCreditScoreResultReceived)
      }
      case creditScorePersistEvent @ CreditScoreSM.CreditScorePersistEvent(payload) => {
        process(envelope, payload, creditScorePersistEvent)
      }
      case multiTenantResultsReceivedPersistentEvent @ CreditScoreSM.MultiTenantResultsReceivedPersistentEvent(
            payload
          ) => {
        process(envelope, payload, multiTenantResultsReceivedPersistentEvent)
      }
      case startCreditScoreResearchPersistEvent @ CreditScoreSM.StartCreditScoreResearchPersistEvent(payload) => {
        process(envelope, payload, startCreditScoreResearchPersistEvent)
      }
    }
  }

  private def process(
      envelope: ElasticsearchEnvelope[EventEnvelope[CreditScoreSM.PersistEvent]],
      payload: java.util.Map[String, AnyRef],
      persistEvent: CreditScoreSM.PersistEvent
  ): Future[Done] = {
    val future: CompletableFuture[Void] = elasticsearchRepository
      .index(
        calculateOffset(envelope.eventEnvelope.offset),
        envelope.eventEnvelope.persistenceId,
        envelope.eventEnvelope.sequenceNr,
        envelope.projectionId.name,
        envelope.projectionId.key,
        useCaseKeyStrategy.getKey(payload),
        "creditscoresm",
        CreditScoreSMElasticsearchTransform.transform(persistEvent),
        payload
      )
    future.asScala.map(_ => Done)
  }
}

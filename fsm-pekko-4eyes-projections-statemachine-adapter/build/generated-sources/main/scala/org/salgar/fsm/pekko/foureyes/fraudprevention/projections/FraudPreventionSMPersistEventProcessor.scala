package org.salgar.fsm.pekko.foureyes.fraudprevention.projections

import org.apache.pekko.projection.elasticsearch.ElasticsearchEnvelope
import org.salgar.fsm.pekko.elasticsearch.ElasticsearchRepository
import org.salgar.fsm.pekko.foureyes.fraudprevention.FraudPreventionSM
import org.salgar.fsm.pekko.foureyes.fraudprevention.elasticsearch.transform.FraudPreventionSMElasticsearchTransform
import org.salgar.fsm.pekko.pekkosystem.ActorService
import org.salgar.fsm.pekko.projections.OffsetUtility.calculateOffset
import org.salgar.pekko.fsm.api.UseCaseKeyStrategy

import java.util.concurrent.CompletableFuture

class FraudPreventionSMPersistEventProcessor(
    elasticsearchRepository: ElasticsearchRepository,
    useCaseKeyStrategy: UseCaseKeyStrategy,
    actorService: ActorService
) {
  implicit def executionContext: ExecutionContext = actorService.actorSystem().executionContext

  def matching(envelope: ElasticsearchEnvelope[EventEnvelope[FraudPreventionSM.PersistEvent]]): Future[Done] = {
    envelope.eventEnvelope.event match {
      case fraudPreventionRetryPersistEvent @ FraudPreventionSM.FraudPreventionRetryPersistEvent(payload) => {
        process(envelope, payload, fraudPreventionRetryPersistEvent)
      }
      case fraudPreventionErrorPersistEvent @ FraudPreventionSM.FraudPreventionErrorPersistEvent(payload) => {
        process(envelope, payload, fraudPreventionErrorPersistEvent)
      }
      case fraudPreventionPersistEvemt @ FraudPreventionSM.FraudPreventionPersistEvemt(payload) => {
        process(envelope, payload, fraudPreventionPersistEvemt)
      }
      case fraudPreventionReceivedPersistEvent @ FraudPreventionSM.FraudPreventionReceivedPersistEvent(payload) => {
        process(envelope, payload, fraudPreventionReceivedPersistEvent)
      }
    }
  }

  private def process(
      envelope: ElasticsearchEnvelope[EventEnvelope[FraudPreventionSM.PersistEvent]],
      payload: java.util.Map[String, AnyRef],
      persistEvent: FraudPreventionSM.PersistEvent
  ): Future[Done] = {
    val future: CompletableFuture[Void] = elasticsearchRepository
      .index(
        calculateOffset(envelope.eventEnvelope.offset),
        envelope.eventEnvelope.persistenceId,
        envelope.eventEnvelope.sequenceNr,
        envelope.projectionId.name,
        envelope.projectionId.key,
        useCaseKeyStrategy.getKey(payload),
        "fraudpreventionsm",
        FraudPreventionSMElasticsearchTransform.transform(persistEvent),
        payload
      )
    future.asScala.map(_ => Done)
  }
}

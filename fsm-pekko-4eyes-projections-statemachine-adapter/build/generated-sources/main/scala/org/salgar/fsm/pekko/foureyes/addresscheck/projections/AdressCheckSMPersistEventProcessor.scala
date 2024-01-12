package org.salgar.fsm.pekko.foureyes.addresscheck.projections

import org.apache.pekko.projection.elasticsearch.ElasticsearchEnvelope
import org.salgar.fsm.pekko.elasticsearch.ElasticsearchRepository
import org.salgar.fsm.pekko.foureyes.addresscheck.AdressCheckSM
import org.salgar.fsm.pekko.foureyes.addresscheck.elasticsearch.transform.AdressCheckSMElasticsearchTransform
import org.salgar.fsm.pekko.pekkosystem.ActorService
import org.salgar.fsm.pekko.projections.OffsetUtility.calculateOffset
import org.salgar.pekko.fsm.api.UseCaseKeyStrategy

import java.util.concurrent.CompletableFuture

class AdressCheckSMPersistEventProcessor(
    elasticsearchRepository: ElasticsearchRepository,
    useCaseKeyStrategy: UseCaseKeyStrategy,
    actorService: ActorService
) {
  implicit def executionContext: ExecutionContext = actorService.actorSystem().executionContext

  def matching(envelope: ElasticsearchEnvelope[EventEnvelope[AdressCheckSM.PersistEvent]]): Future[Done] = {
    envelope.eventEnvelope.event match {
      case addressCheckPersistEvent @ AdressCheckSM.AddressCheckPersistEvent(payload) => {
        process(envelope, payload, addressCheckPersistEvent)
      }
      case addressCheckErrorPersistEvent @ AdressCheckSM.AddressCheckErrorPersistEvent(payload) => {
        process(envelope, payload, addressCheckErrorPersistEvent)
      }
      case startAdressCheckPersistEvent @ AdressCheckSM.StartAdressCheckPersistEvent(payload) => {
        process(envelope, payload, startAdressCheckPersistEvent)
      }
      case addressCheckRetryPersistEvent @ AdressCheckSM.AddressCheckRetryPersistEvent(payload) => {
        process(envelope, payload, addressCheckRetryPersistEvent)
      }
    }
  }

  private def process(
      envelope: ElasticsearchEnvelope[EventEnvelope[AdressCheckSM.PersistEvent]],
      payload: java.util.Map[String, AnyRef],
      persistEvent: AdressCheckSM.PersistEvent
  ): Future[Done] = {
    val future: CompletableFuture[Void] = elasticsearchRepository
      .index(
        calculateOffset(envelope.eventEnvelope.offset),
        envelope.eventEnvelope.persistenceId,
        envelope.eventEnvelope.sequenceNr,
        envelope.projectionId.name,
        envelope.projectionId.key,
        useCaseKeyStrategy.getKey(payload),
        "adresschecksm",
        AdressCheckSMElasticsearchTransform.transform(persistEvent),
        payload
      )
    future.asScala.map(_ => Done)
  }
}

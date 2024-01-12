package org.salgar.fsm.pekko.foureyes.credit.projections

import org.apache.pekko.projection.elasticsearch.ElasticsearchEnvelope
import org.salgar.fsm.pekko.elasticsearch.ElasticsearchRepository
import org.salgar.fsm.pekko.foureyes.credit.CreditSM
import org.salgar.fsm.pekko.foureyes.credit.elasticsearch.transform.CreditSMElasticsearchTransform
import org.salgar.fsm.pekko.pekkosystem.ActorService
import org.salgar.fsm.pekko.projections.OffsetUtility.calculateOffset
import org.salgar.pekko.fsm.api.UseCaseKeyStrategy

import java.util.concurrent.CompletableFuture

class CreditSMPersistEventProcessor(
    elasticsearchRepository: ElasticsearchRepository,
    useCaseKeyStrategy: UseCaseKeyStrategy,
    actorService: ActorService
) {
  implicit def executionContext: ExecutionContext = actorService.actorSystem().executionContext

  def matching(envelope: ElasticsearchEnvelope[EventEnvelope[CreditSM.PersistEvent]]): Future[Done] = {
    envelope.eventEnvelope.event match {
      case positiveResultPersistedEvent @ CreditSM.PositiveResultPersistedEvent(payload) => {
        process(envelope, payload, positiveResultPersistedEvent)
      }
      case creditScoreAddressResultReceivedPersistEvent @ CreditSM.CreditScoreAddressResultReceivedPersistEvent(
            payload
          ) => {
        process(envelope, payload, creditScoreAddressResultReceivedPersistEvent)
      }
      case creditScoreNotEnoughPersisteEvent @ CreditSM.CreditScoreNotEnoughPersisteEvent(payload) => {
        process(envelope, payload, creditScoreNotEnoughPersisteEvent)
      }
      case creditRejectedPersistEvent @ CreditSM.CreditRejectedPersistEvent(payload) => {
        process(envelope, payload, creditRejectedPersistEvent)
      }
      case waitingCreditAnalystApprovalCreditAcceptedPersistEvent @ CreditSM
            .WaitingCreditAnalystApprovalCreditAcceptedPersistEvent(payload) => {
        process(envelope, payload, waitingCreditAnalystApprovalCreditAcceptedPersistEvent)
      }
      case acceptableScorePersistedEvent @ CreditSM.AcceptableScorePersistedEvent(payload) => {
        process(envelope, payload, acceptableScorePersistedEvent)
      }
      case creditInitialPersistEvent @ CreditSM.CreditInitialPersistEvent(payload) => {
        process(envelope, payload, creditInitialPersistEvent)
      }
      case fraudPreventionReceivedPersistEvent @ CreditSM.FraudPreventionReceivedPersistEvent(payload) => {
        process(envelope, payload, fraudPreventionReceivedPersistEvent)
      }
      case relationshipManagerApprovedPersistEvent @ CreditSM.RelationshipManagerApprovedPersistEvent(payload) => {
        process(envelope, payload, relationshipManagerApprovedPersistEvent)
      }
      case fraudPreventionCreditScoreReceivedPersistEvent @ CreditSM.FraudPreventionCreditScoreReceivedPersistEvent(
            payload
          ) => {
        process(envelope, payload, fraudPreventionCreditScoreReceivedPersistEvent)
      }
      case addressCheckCreditScoreResultReceivedPersistEvent @ CreditSM
            .AddressCheckCreditScoreResultReceivedPersistEvent(payload) => {
        process(envelope, payload, addressCheckCreditScoreResultReceivedPersistEvent)
      }
      case customerUpdatedEvent @ CreditSM.CustomerUpdatedEvent(payload) => {
        process(envelope, payload, customerUpdatedEvent)
      }
      case fraudPreventionFraudPersistEvent @ CreditSM.FraudPreventionFraudPersistEvent(payload) => {
        process(envelope, payload, fraudPreventionFraudPersistEvent)
      }
      case positiveResultReceivedPersistEvent @ CreditSM.PositiveResultReceivedPersistEvent(payload) => {
        process(envelope, payload, positiveResultReceivedPersistEvent)
      }
      case creditScoreAddressCheckReceivedPersistEvent @ CreditSM.CreditScoreAddressCheckReceivedPersistEvent(
            payload
          ) => {
        process(envelope, payload, creditScoreAddressCheckReceivedPersistEvent)
      }
      case waitingManagerApprovalRelationshipManagerPersistEvent @ CreditSM
            .WaitingManagerApprovalRelationshipManagerPersistEvent(payload) => {
        process(envelope, payload, waitingManagerApprovalRelationshipManagerPersistEvent)
      }
      case creditScoreToLowPersistEvent @ CreditSM.CreditScoreToLowPersistEvent(payload) => {
        process(envelope, payload, creditScoreToLowPersistEvent)
      }
      case adressCheckFraudPreventionReceviedPersistEvent @ CreditSM.AdressCheckFraudPreventionReceviedPersistEvent(
            payload
          ) => {
        process(envelope, payload, adressCheckFraudPreventionReceviedPersistEvent)
      }
      case creditAcceptedPersistEvent @ CreditSM.CreditAcceptedPersistEvent(payload) => {
        process(envelope, payload, creditAcceptedPersistEvent)
      }
      case adressCheckReceivedPersistEvent @ CreditSM.AdressCheckReceivedPersistEvent(payload) => {
        process(envelope, payload, adressCheckReceivedPersistEvent)
      }
      case creditScoreReceivedPersistEvent @ CreditSM.CreditScoreReceivedPersistEvent(payload) => {
        process(envelope, payload, creditScoreReceivedPersistEvent)
      }
      case salesManagerApprovalPersistEvent @ CreditSM.SalesManagerApprovalPersistEvent(payload) => {
        process(envelope, payload, salesManagerApprovalPersistEvent)
      }
      case creditScoreFraudPreventionReceivedPersistEvent @ CreditSM.CreditScoreFraudPreventionReceivedPersistEvent(
            payload
          ) => {
        process(envelope, payload, creditScoreFraudPreventionReceivedPersistEvent)
      }
      case fraudPreventionAdressCheckReceivedPersistEvent @ CreditSM.FraudPreventionAdressCheckReceivedPersistEvent(
            payload
          ) => {
        process(envelope, payload, fraudPreventionAdressCheckReceivedPersistEvent)
      }
      case waitingManagerApprovalSalesManagerApprovedPersistEvent @ CreditSM
            .WaitingManagerApprovalSalesManagerApprovedPersistEvent(payload) => {
        process(envelope, payload, waitingManagerApprovalSalesManagerApprovedPersistEvent)
      }
      case creditApplicationSubmittedPersistEvent @ CreditSM.CreditApplicationSubmittedPersistEvent(payload) => {
        process(envelope, payload, creditApplicationSubmittedPersistEvent)
      }
      case creditAanlystApprovedPersistEvent @ CreditSM.CreditAanlystApprovedPersistEvent(payload) => {
        process(envelope, payload, creditAanlystApprovedPersistEvent)
      }
    }
  }

  private def process(
      envelope: ElasticsearchEnvelope[EventEnvelope[CreditSM.PersistEvent]],
      payload: java.util.Map[String, AnyRef],
      persistEvent: CreditSM.PersistEvent
  ): Future[Done] = {
    val future: CompletableFuture[Void] = elasticsearchRepository
      .index(
        calculateOffset(envelope.eventEnvelope.offset),
        envelope.eventEnvelope.persistenceId,
        envelope.eventEnvelope.sequenceNr,
        envelope.projectionId.name,
        envelope.projectionId.key,
        useCaseKeyStrategy.getKey(payload),
        "creditsm",
        CreditSMElasticsearchTransform.transform(persistEvent),
        payload
      )
    future.asScala.map(_ => Done)
  }
}

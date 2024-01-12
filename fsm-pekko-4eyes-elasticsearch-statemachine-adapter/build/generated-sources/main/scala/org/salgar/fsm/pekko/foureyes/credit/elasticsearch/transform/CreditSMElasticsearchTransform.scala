package org.salgar.fsm.pekko.foureyes.credit.elasticsearch.transform

import org.salgar.fsm.pekko.foureyes.credit.CreditSM

object CreditSMElasticsearchTransform {
  def transform(persistEvent: CreditSM.PersistEvent): String = {
    persistEvent match {
      case CreditSM.AdressCheckFraudPreventionReceviedPersistEvent(payload) =>
        "FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED"

      case CreditSM.AddressCheckCreditScoreResultReceivedPersistEvent(payload) =>
        "CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED"

      case CreditSM.CreditAcceptedPersistEvent(payload) =>
        "CREDIT_ACCEPTED"

      case CreditSM.CreditScoreFraudPreventionReceivedPersistEvent(payload) =>
        "CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED"

      case CreditSM.CreditScoreAddressResultReceivedPersistEvent(payload) =>
        "CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED"

      case CreditSM.CustomerUpdatedEvent(payload) =>
        "CREDIT_APPLICATION_SUBMITTED"

      case CreditSM.FraudPreventionAdressCheckReceivedPersistEvent(payload) =>
        "FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED"

      case CreditSM.FraudPreventionCreditScoreReceivedPersistEvent(payload) =>
        "CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED"

      case CreditSM.AdressCheckReceivedPersistEvent(payload) =>
        "ADRRESCHECK_RESULT_RECEIVED"

      case CreditSM.FraudPreventionReceivedPersistEvent(payload) =>
        "FRAUDPREVENTION_RESULT_RECEIVED"

      case CreditSM.CreditScoreReceivedPersistEvent(payload) =>
        "CREDITSCORE_RECEIVED"

      case CreditSM.CreditApplicationSubmittedPersistEvent(payload) =>
        "CREDIT_APPLICATION_SUBMITTED"

      case CreditSM.CreditScoreToLowPersistEvent(payload) =>
        "CREDIT_REJECTED"

      case CreditSM.CreditRejectedPersistEvent(payload) =>
        "CREDIT_REJECTED"

      case CreditSM.SalesManagerApprovalPersistEvent(payload) =>
        "SALES_MANAGER_APPROVED"

      case CreditSM.CreditScoreNotEnoughPersisteEvent(payload) =>
        "WAITING_APPROVAL_FROM_SENIOR_MANAGER"

      case CreditSM.PositiveResultPersistedEvent(payload) =>
        "WAITING_CREDIT_ANALYST_APPROVAL"

      case CreditSM.WaitingCreditAnalystApprovalCreditAcceptedPersistEvent(payload) =>
        "WAITING_ANAYLIST_APPROVAL"

      case CreditSM.AcceptableScorePersistedEvent(payload) =>
        "WAITING_CREDIT_ANALYST_APPROVAL"

      case CreditSM.RelationshipManagerApprovedPersistEvent(payload) =>
        "RELATIONSHIP_MANAGER_APPROVED"

      case CreditSM.WaitingManagerApprovalSalesManagerApprovedPersistEvent(payload) =>
        "WAITING_MANAGER_APPROVAL"

      case CreditSM.WaitingManagerApprovalRelationshipManagerPersistEvent(payload) =>
        "WAITING_MANAGER_APPROVAL"

      case _ => null
    }
  }
}

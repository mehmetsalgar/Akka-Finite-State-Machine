package org.salgar.fsm.pekko.foureyes.credit

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.salgar.fsm.pekko.foureyes.credit.CreditSM._
import org.salgar.fsm.pekko.foureyes.credit.actions.SpringCreditSMActionsLocator
import org.salgar.fsm.pekko.foureyes.credit.guards.SpringCreditSMGuardsLocator
import org.salgar.pekko.fsm.api.UseCaseKey
import org.salgar.pekko.fsm.base.CborSerializable
import org.salgar.pekko.fsm.base.actors.BaseActor
import org.salgar.pekko.fsm.base.actors.BaseActor.InternalBaseMessage

import java.util

object CreditSM {
    sealed trait State {
        private[credit] def controlObject: util.Map[java.lang.String, AnyRef]
    }
    final case class INITIAL(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class CREDIT_APPLICATION_SUBMITTED_$_INITIAL_CA(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class CREDIT_APPLICATION_SUBMITTED_$_CREDIT_ACCEPTED(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_INITIAL_FE_SM(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_WAITING_MANAGER_APPROVAL(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_INITIAL_CSC(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_ADRRESCHECK_RESULT_RECEIVED(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_RECEIVED(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_RESULT_RECEIVED(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_INITIAL_FE(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_WAITING_MANAGER_APPROVAL(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_FROM_SENIOR_MANAGER(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_INITIAL_CA_SM(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_WAITING_ANAYLIST_APPROVAL(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class CREDIT_REJECTED(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable

    sealed trait CreditSMEvent {
        def useCaseKey: UseCaseKey
    }
    final case class onAcceptableScore(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef], replyTo: ActorRef[Response]) extends CreditSMEvent
    object onAcceptableScore {
        def apply(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]) : onAcceptableScore = {
            onAcceptableScore(useCaseKey, payload, null)
        }
    }
    final case class onAccepted(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef], replyTo: ActorRef[Response]) extends CreditSMEvent
    object onAccepted {
        def apply(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]) : onAccepted = {
            onAccepted(useCaseKey, payload, null)
        }
    }
    final case class onCustomerUpdated(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef], replyTo: ActorRef[Response]) extends CreditSMEvent
    object onCustomerUpdated {
        def apply(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]) : onCustomerUpdated = {
            onCustomerUpdated(useCaseKey, payload, null)
        }
    }
    final case class onRejected(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef], replyTo: ActorRef[Response]) extends CreditSMEvent
    object onRejected {
        def apply(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]) : onRejected = {
            onRejected(useCaseKey, payload, null)
        }
    }
    final case class onRelationshipManagerApproved(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef], replyTo: ActorRef[Response]) extends CreditSMEvent
    object onRelationshipManagerApproved {
        def apply(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]) : onRelationshipManagerApproved = {
            onRelationshipManagerApproved(useCaseKey, payload, null)
        }
    }
    final case class onResultReceived(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef], replyTo: ActorRef[Response]) extends CreditSMEvent
    object onResultReceived {
        def apply(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]) : onResultReceived = {
            onResultReceived(useCaseKey, payload, null)
        }
    }
    final case class onSalesManagerApproved(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef], replyTo: ActorRef[Response]) extends CreditSMEvent
    object onSalesManagerApproved {
        def apply(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]) : onSalesManagerApproved = {
            onSalesManagerApproved(useCaseKey, payload, null)
        }
    }
    final case class onSubmit(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef], replyTo: ActorRef[Response]) extends CreditSMEvent
    object onSubmit {
        def apply(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]) : onSubmit = {
            onSubmit(useCaseKey, payload, null)
        }
    }

    //Reporting
    sealed trait ReportEvent extends CreditSMEvent
    final case class onReport(useCaseKey: UseCaseKey, replyTo: ActorRef[Response])
        extends ReportEvent

    sealed trait Response
    final case class ReportResponse(state: State, payload: util.Map[String, AnyRef]) extends Response
    final case class AcknowledgeResponse() extends Response

    //internal protocol
    sealed trait InternalMessage extends CreditSMEvent
    final case class onAddCreditSMRelatedReference(useCaseKey: UseCaseKey, listing: Receptionist.Listing) extends InternalMessage with InternalBaseMessage

    sealed trait PersistEvent
    final case class PositiveResultPersistedEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class CreditScoreAddressResultReceivedPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class CreditScoreNotEnoughPersisteEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class CreditRejectedPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class WaitingCreditAnalystApprovalCreditAcceptedPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class AcceptableScorePersistedEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class CreditInitialPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class FraudPreventionReceivedPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class RelationshipManagerApprovedPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class FraudPreventionCreditScoreReceivedPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class AddressCheckCreditScoreResultReceivedPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class CustomerUpdatedEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class FraudPreventionFraudPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class PositiveResultReceivedPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class CreditScoreAddressCheckReceivedPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class WaitingManagerApprovalRelationshipManagerPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class CreditScoreToLowPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class AdressCheckFraudPreventionReceviedPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class CreditAcceptedPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class AdressCheckReceivedPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class CreditScoreReceivedPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class SalesManagerApprovalPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class CreditScoreFraudPreventionReceivedPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class FraudPreventionAdressCheckReceivedPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class WaitingManagerApprovalSalesManagerApprovedPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class CreditApplicationSubmittedPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class CreditAanlystApprovedPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
}

class CreditSM (
                    ctx: ActorContext[CreditSMEvent],
                    useCaseKey: String
                  ) extends BaseActor[CreditSMEvent, onAddCreditSMRelatedReference, PersistEvent, State] (
                                TypeCase[onAddCreditSMRelatedReference],
                                useCaseKey
                            ) {
    import CreditSM._

    private val listingAdapter: ActorRef[Receptionist.Listing] = ctx.messageAdapter {
        listing => onAddCreditSMRelatedReference(() => useCaseKey, listing)
    }

    ctx.system.receptionist ! Receptionist.Subscribe(
        ServiceKey[CreditSMEvent]("creditsmService_" + useCaseKey),
        listingAdapter)

    def commandHandler(context: ActorContext[CreditSMEvent], cmd: CreditSMEvent, state: State): ReplyEffect[PersistEvent, State] =
        cmd match {
            case onReport(useCaseKey, replyTo) =>
                Effect.reply(replyTo)( ReportResponse(state, java.util.Collections.unmodifiableMap(state.controlObject)))

            case _ =>
                commandHandlerInternal(context, cmd, state)
    }

    def commandHandlerInternal(context: ActorContext[CreditSMEvent], cmd: CreditSMEvent, state: State): ReplyEffect[PersistEvent, State] =
        state match {
            //State Command Handler
            case INITIAL(controlObject) =>
                base[CreditSMEvent](cmd, state) {
                    case onSubmit(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing INITIAL onSubmit payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_INITIAL_$$_CREDIT_APPLICATION_SUBMITTED_intial_onSubmitAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
            //State Command Handler
            case CREDIT_APPLICATION_SUBMITTED_$_INITIAL_CA(controlObject) =>
                base[CreditSMEvent](cmd, state) {
                    case onCustomerUpdated(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_INITIAL_CA onCustomerUpdated payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_APPLICATION_SUBMITTED_customer_updatedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onRejected(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_INITIAL_CA onRejected payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_managerRejected_onRejectedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onResultReceived(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_INITIAL_CA onResultReceived payload: {}", payload.toString)
                        if(SpringCreditSMGuardsLocator.getInstance.creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_onResultReceived_isScoreTooLowGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_lowScore_onResultReceivedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                        }
                    
                        Effect.unhandled.thenNoReply()
                    }
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
            //State Command Handler
            case CREDIT_APPLICATION_SUBMITTED_$_CREDIT_ACCEPTED(controlObject) =>
                base[CreditSMEvent](cmd, state) {
                    case onCustomerUpdated(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_CREDIT_ACCEPTED onCustomerUpdated payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_APPLICATION_SUBMITTED_customer_updatedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onRejected(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_CREDIT_ACCEPTED onRejected payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_managerRejected_onRejectedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onResultReceived(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_CREDIT_ACCEPTED onResultReceived payload: {}", payload.toString)
                        if(SpringCreditSMGuardsLocator.getInstance.creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_onResultReceived_isScoreTooLowGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_lowScore_onResultReceivedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                        }
                    
                        Effect.unhandled.thenNoReply()
                    }
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
            //State Command Handler
            case CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_INITIAL_FE_SM(controlObject) =>
                base[CreditSMEvent](cmd, state) {
                    case onCustomerUpdated(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_INITIAL_FE_SM onCustomerUpdated payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_APPLICATION_SUBMITTED_customer_updatedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onRejected(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_INITIAL_FE_SM onRejected payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_managerRejected_onRejectedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onResultReceived(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_INITIAL_FE_SM onResultReceived payload: {}", payload.toString)
                        if(SpringCreditSMGuardsLocator.getInstance.creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_onResultReceived_isScoreTooLowGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_lowScore_onResultReceivedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                        }
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onSalesManagerApproved(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_INITIAL_FE_SM onSalesManagerApproved payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_RELATIONSHIP_MANAGER_APPROVED_$$_SALES_MANAGER_APPROVED_relationshipManagerApproved_onSalesManagerApprovedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
            //State Command Handler
            case CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_WAITING_MANAGER_APPROVAL(controlObject) =>
                base[CreditSMEvent](cmd, state) {
                    case onCustomerUpdated(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_WAITING_MANAGER_APPROVAL onCustomerUpdated payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_APPLICATION_SUBMITTED_customer_updatedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onRejected(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_WAITING_MANAGER_APPROVAL onRejected payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_managerRejected_onRejectedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onResultReceived(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_WAITING_MANAGER_APPROVAL onResultReceived payload: {}", payload.toString)
                        if(SpringCreditSMGuardsLocator.getInstance.creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_onResultReceived_isScoreTooLowGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_lowScore_onResultReceivedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                        }
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onSalesManagerApproved(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_WAITING_MANAGER_APPROVAL onSalesManagerApproved payload: {}", payload.toString)
                        if(SpringCreditSMGuardsLocator.getInstance.creditsm_WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_onSalesManagerApproved_salesManagerCreditAmountCriticalGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_waitingForApproval_onSalesManagerApprovedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                        }
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_RELATIONSHIP_MANAGER_APPROVED_$$_SALES_MANAGER_APPROVED_relationshipManagerApproved_onSalesManagerApprovedAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
            //State Command Handler
            case CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_INITIAL_CSC(controlObject) =>
                base[CreditSMEvent](cmd, state) {
                    case onCustomerUpdated(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_INITIAL_CSC onCustomerUpdated payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_APPLICATION_SUBMITTED_customer_updatedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onRejected(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_INITIAL_CSC onRejected payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_managerRejected_onRejectedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onResultReceived(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_INITIAL_CSC onResultReceived payload: {}", payload.toString)
                        if(SpringCreditSMGuardsLocator.getInstance.creditsm_INITIAL_CSC_$$_ADRRESCHECK_RESULT_RECEIVED_onResultReceived_isAdressCheckResultGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_INITIAL_CSC_$$_ADRRESCHECK_RESULT_RECEIVED_initial_AddressCheckResultReceivedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                        }
                        else if(SpringCreditSMGuardsLocator.getInstance.creditsm_INITIAL_CSC_$$_FRAUDPREVENTION_RESULT_RECEIVED_onResultReceived_isFraudPreventionResultGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_INITIAL_CSC_$$_FRAUDPREVENTION_RESULT_RECEIVED_initial_FraudPreventionResultReceivedAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                        }
                        else if(SpringCreditSMGuardsLocator.getInstance.creditsm_INITIAL_CSC_$$_CREDITSCORE_RECEIVED_onResultReceived_isCreditScoreResultGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_INITIAL_CSC_$$_CREDITSCORE_RECEIVED_initial_creditScoreReceivedAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                        }
                        else if(SpringCreditSMGuardsLocator.getInstance.creditsm_SALES_MANAGER_APPROVED_$$_WAITING_APPROVAL_FROM_SENIOR_MANAGER_onResultReceived_isCreditScoreNotSufficientGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_SALES_MANAGER_APPROVED_$$_WAITING_APPROVAL_FROM_SENIOR_MANAGER_salesManagerApproved_onCreditScoreReceivedAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                        }
                        else if(SpringCreditSMGuardsLocator.getInstance.creditsm_SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_onResultReceived_isResultSufficientGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_salesManagerApproved_onResultReceivedAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                        }
                        else if(SpringCreditSMGuardsLocator.getInstance.creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_onResultReceived_isScoreTooLowGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_lowScore_onResultReceivedAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                        }
                    
                        Effect.unhandled.thenNoReply()
                    }
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
            //State Command Handler
            case CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_ADRRESCHECK_RESULT_RECEIVED(controlObject) =>
                base[CreditSMEvent](cmd, state) {
                    case onCustomerUpdated(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_ADRRESCHECK_RESULT_RECEIVED onCustomerUpdated payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_APPLICATION_SUBMITTED_customer_updatedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onRejected(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_ADRRESCHECK_RESULT_RECEIVED onRejected payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_managerRejected_onRejectedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onResultReceived(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_ADRRESCHECK_RESULT_RECEIVED onResultReceived payload: {}", payload.toString)
                        if(SpringCreditSMGuardsLocator.getInstance.creditsm_ADRRESCHECK_RESULT_RECEIVED_$$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED_onResultReceived_isCreditScoreResult.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_ADRRESCHECK_RESULT_RECEIVED_$$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED_addressCheck_CredfitScore_onResultReceivedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                        }
                        else if(SpringCreditSMGuardsLocator.getInstance.creditsm_ADRRESCHECK_RESULT_RECEIVED_$$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED_onResultReceived_isFraudPreventionResultGaurd.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_ADRRESCHECK_RESULT_RECEIVED_$$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED_addressCheckResultReceived_fraudPreventionResultReceivedAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                        }
                        else if(SpringCreditSMGuardsLocator.getInstance.creditsm_SALES_MANAGER_APPROVED_$$_WAITING_APPROVAL_FROM_SENIOR_MANAGER_onResultReceived_isCreditScoreNotSufficientGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_SALES_MANAGER_APPROVED_$$_WAITING_APPROVAL_FROM_SENIOR_MANAGER_salesManagerApproved_onCreditScoreReceivedAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                        }
                        else if(SpringCreditSMGuardsLocator.getInstance.creditsm_SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_onResultReceived_isResultSufficientGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_salesManagerApproved_onResultReceivedAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                        }
                        else if(SpringCreditSMGuardsLocator.getInstance.creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_onResultReceived_isScoreTooLowGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_lowScore_onResultReceivedAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                        }
                    
                        Effect.unhandled.thenNoReply()
                    }
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
            //State Command Handler
            case CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED(controlObject) =>
                base[CreditSMEvent](cmd, state) {
                    case onCustomerUpdated(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED onCustomerUpdated payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_APPLICATION_SUBMITTED_customer_updatedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onRejected(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED onRejected payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_managerRejected_onRejectedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onResultReceived(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED onResultReceived payload: {}", payload.toString)
                        if(SpringCreditSMGuardsLocator.getInstance.creditsm_SALES_MANAGER_APPROVED_$$_WAITING_APPROVAL_FROM_SENIOR_MANAGER_onResultReceived_isCreditScoreNotSufficientGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_SALES_MANAGER_APPROVED_$$_WAITING_APPROVAL_FROM_SENIOR_MANAGER_salesManagerApproved_onCreditScoreReceivedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                        }
                        else if(SpringCreditSMGuardsLocator.getInstance.creditsm_SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_onResultReceived_isResultSufficientGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_salesManagerApproved_onResultReceivedAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                        }
                        else if(SpringCreditSMGuardsLocator.getInstance.creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_onResultReceived_isScoreTooLowGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_lowScore_onResultReceivedAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                        }
                    
                        Effect.unhandled.thenNoReply()
                    }
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
            //State Command Handler
            case CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED(controlObject) =>
                base[CreditSMEvent](cmd, state) {
                    case onCustomerUpdated(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED onCustomerUpdated payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_APPLICATION_SUBMITTED_customer_updatedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onRejected(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED onRejected payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_managerRejected_onRejectedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onResultReceived(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED onResultReceived payload: {}", payload.toString)
                        if(SpringCreditSMGuardsLocator.getInstance.creditsm_SALES_MANAGER_APPROVED_$$_WAITING_APPROVAL_FROM_SENIOR_MANAGER_onResultReceived_isCreditScoreNotSufficientGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_SALES_MANAGER_APPROVED_$$_WAITING_APPROVAL_FROM_SENIOR_MANAGER_salesManagerApproved_onCreditScoreReceivedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                        }
                        else if(SpringCreditSMGuardsLocator.getInstance.creditsm_SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_onResultReceived_isResultSufficientGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_salesManagerApproved_onResultReceivedAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                        }
                        else if(SpringCreditSMGuardsLocator.getInstance.creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_onResultReceived_isScoreTooLowGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_lowScore_onResultReceivedAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                        }
                    
                        Effect.unhandled.thenNoReply()
                    }
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
            //State Command Handler
            case CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_RECEIVED(controlObject) =>
                base[CreditSMEvent](cmd, state) {
                    case onCustomerUpdated(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_RECEIVED onCustomerUpdated payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_APPLICATION_SUBMITTED_customer_updatedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onRejected(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_RECEIVED onRejected payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_managerRejected_onRejectedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onResultReceived(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_RECEIVED onResultReceived payload: {}", payload.toString)
                        if(SpringCreditSMGuardsLocator.getInstance.creditsm_CREDITSCORE_RECEIVED_$$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED_onResultReceived_isAddressCheckResult.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDITSCORE_RECEIVED_$$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED_creditScore_addressCheck_onResultReceivedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                        }
                        else if(SpringCreditSMGuardsLocator.getInstance.creditsm_CREDITSCORE_RECEIVED_$$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED_onResultReceived_isFraudPreventionResultGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDITSCORE_RECEIVED_$$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED_creditScoreReceived_creditScore_fraudPreventionResultAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                        }
                        else if(SpringCreditSMGuardsLocator.getInstance.creditsm_SALES_MANAGER_APPROVED_$$_WAITING_APPROVAL_FROM_SENIOR_MANAGER_onResultReceived_isCreditScoreNotSufficientGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_SALES_MANAGER_APPROVED_$$_WAITING_APPROVAL_FROM_SENIOR_MANAGER_salesManagerApproved_onCreditScoreReceivedAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                        }
                        else if(SpringCreditSMGuardsLocator.getInstance.creditsm_SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_onResultReceived_isResultSufficientGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_salesManagerApproved_onResultReceivedAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                        }
                        else if(SpringCreditSMGuardsLocator.getInstance.creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_onResultReceived_isScoreTooLowGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_lowScore_onResultReceivedAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                        }
                    
                        Effect.unhandled.thenNoReply()
                    }
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
            //State Command Handler
            case CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED(controlObject) =>
                base[CreditSMEvent](cmd, state) {
                    case onCustomerUpdated(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED onCustomerUpdated payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_APPLICATION_SUBMITTED_customer_updatedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onRejected(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED onRejected payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_managerRejected_onRejectedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onResultReceived(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED onResultReceived payload: {}", payload.toString)
                        if(SpringCreditSMGuardsLocator.getInstance.creditsm_SALES_MANAGER_APPROVED_$$_WAITING_APPROVAL_FROM_SENIOR_MANAGER_onResultReceived_isCreditScoreNotSufficientGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_SALES_MANAGER_APPROVED_$$_WAITING_APPROVAL_FROM_SENIOR_MANAGER_salesManagerApproved_onCreditScoreReceivedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                        }
                        else if(SpringCreditSMGuardsLocator.getInstance.creditsm_SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_onResultReceived_isResultSufficientGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_salesManagerApproved_onResultReceivedAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                        }
                        else if(SpringCreditSMGuardsLocator.getInstance.creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_onResultReceived_isScoreTooLowGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_lowScore_onResultReceivedAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                        }
                    
                        Effect.unhandled.thenNoReply()
                    }
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
            //State Command Handler
            case CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_RESULT_RECEIVED(controlObject) =>
                base[CreditSMEvent](cmd, state) {
                    case onCustomerUpdated(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_RESULT_RECEIVED onCustomerUpdated payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_APPLICATION_SUBMITTED_customer_updatedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onRejected(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_RESULT_RECEIVED onRejected payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_managerRejected_onRejectedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onResultReceived(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_RESULT_RECEIVED onResultReceived payload: {}", payload.toString)
                        if(SpringCreditSMGuardsLocator.getInstance.creditsm_FRAUDPREVENTION_RESULT_RECEIVED_$$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED_onResultReceived_isAddressCheckResultGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_FRAUDPREVENTION_RESULT_RECEIVED_$$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED_fraudPReventionResultReceived_fraudPReventionResultReceivedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                        }
                        else if(SpringCreditSMGuardsLocator.getInstance.creditsm_FRAUDPREVENTION_RESULT_RECEIVED_$$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED_onResultReceived_isCreditScoreResultGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_FRAUDPREVENTION_RESULT_RECEIVED_$$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED_fraudPreventionResultReceived_creditScoreFraudPreventionResultReceivedAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                        }
                        else if(SpringCreditSMGuardsLocator.getInstance.creditsm_SALES_MANAGER_APPROVED_$$_WAITING_APPROVAL_FROM_SENIOR_MANAGER_onResultReceived_isCreditScoreNotSufficientGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_SALES_MANAGER_APPROVED_$$_WAITING_APPROVAL_FROM_SENIOR_MANAGER_salesManagerApproved_onCreditScoreReceivedAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                        }
                        else if(SpringCreditSMGuardsLocator.getInstance.creditsm_SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_onResultReceived_isResultSufficientGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_salesManagerApproved_onResultReceivedAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                        }
                        else if(SpringCreditSMGuardsLocator.getInstance.creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_onResultReceived_isScoreTooLowGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_lowScore_onResultReceivedAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                        }
                    
                        Effect.unhandled.thenNoReply()
                    }
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
            //State Command Handler
            case CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_INITIAL_FE(controlObject) =>
                base[CreditSMEvent](cmd, state) {
                    case onCustomerUpdated(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_INITIAL_FE onCustomerUpdated payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_APPLICATION_SUBMITTED_customer_updatedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onRejected(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_INITIAL_FE onRejected payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_managerRejected_onRejectedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onRelationshipManagerApproved(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_INITIAL_FE onRelationshipManagerApproved payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_WAITING_APPROVAL_$$_RELATIONSHIP_MANAGER_APPROVED_waitingApproval_onRelationshipManagerApprovedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onResultReceived(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_INITIAL_FE onResultReceived payload: {}", payload.toString)
                        if(SpringCreditSMGuardsLocator.getInstance.creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_onResultReceived_isScoreTooLowGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_lowScore_onResultReceivedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                        }
                    
                        Effect.unhandled.thenNoReply()
                    }
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
            //State Command Handler
            case CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_WAITING_MANAGER_APPROVAL(controlObject) =>
                base[CreditSMEvent](cmd, state) {
                    case onCustomerUpdated(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_WAITING_MANAGER_APPROVAL onCustomerUpdated payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_APPLICATION_SUBMITTED_customer_updatedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onRejected(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_WAITING_MANAGER_APPROVAL onRejected payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_managerRejected_onRejectedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onRelationshipManagerApproved(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_WAITING_MANAGER_APPROVAL onRelationshipManagerApproved payload: {}", payload.toString)
                        if(SpringCreditSMGuardsLocator.getInstance.creditsm_WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_onRelationshipManagerApproved_relationshipManagerCreditAmountCriticalGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_waitingManagerApproval_onRelationshipManagerApprovedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                        }
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_WAITING_APPROVAL_$$_RELATIONSHIP_MANAGER_APPROVED_waitingApproval_onRelationshipManagerApprovedAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onResultReceived(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_WAITING_MANAGER_APPROVAL onResultReceived payload: {}", payload.toString)
                        if(SpringCreditSMGuardsLocator.getInstance.creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_onResultReceived_isScoreTooLowGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_lowScore_onResultReceivedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                        }
                    
                        Effect.unhandled.thenNoReply()
                    }
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
            //State Command Handler
            case CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_FROM_SENIOR_MANAGER(controlObject) =>
                base[CreditSMEvent](cmd, state) {
                    case onAcceptableScore(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_FROM_SENIOR_MANAGER onAcceptableScore payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_WAITING_APPROVAL_FROM_SENIOR_MANAGER_$$_WAITING_CREDIT_ANALYST_APPROVAL_waitingApprovalFromSeniorOfficier_onAcceptableScoreAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onCustomerUpdated(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_FROM_SENIOR_MANAGER onCustomerUpdated payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_APPLICATION_SUBMITTED_customer_updatedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onRejected(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_FROM_SENIOR_MANAGER onRejected payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_managerRejected_onRejectedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onResultReceived(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_FROM_SENIOR_MANAGER onResultReceived payload: {}", payload.toString)
                        if(SpringCreditSMGuardsLocator.getInstance.creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_onResultReceived_isScoreTooLowGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_lowScore_onResultReceivedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                        }
                    
                        Effect.unhandled.thenNoReply()
                    }
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
            //State Command Handler
            case CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_INITIAL_CA_SM(controlObject) =>
                base[CreditSMEvent](cmd, state) {
                    case onAccepted(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_INITIAL_CA_SM onAccepted payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_WAITING_CREDIT_ANALYST_APPROVAL_$$_CREDIT_ACCEPTED_creditAnalyst_onAcceptedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onCustomerUpdated(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_INITIAL_CA_SM onCustomerUpdated payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_APPLICATION_SUBMITTED_customer_updatedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onRejected(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_INITIAL_CA_SM onRejected payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_managerRejected_onRejectedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onResultReceived(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_INITIAL_CA_SM onResultReceived payload: {}", payload.toString)
                        if(SpringCreditSMGuardsLocator.getInstance.creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_onResultReceived_isScoreTooLowGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_lowScore_onResultReceivedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                        }
                    
                        Effect.unhandled.thenNoReply()
                    }
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
            //State Command Handler
            case CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_WAITING_ANAYLIST_APPROVAL(controlObject) =>
                base[CreditSMEvent](cmd, state) {
                    case onAccepted(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_WAITING_ANAYLIST_APPROVAL onAccepted payload: {}", payload.toString)
                        if(SpringCreditSMGuardsLocator.getInstance.creditsm_WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_onAccepted_creditAnalystCreditAmountCritical.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_waitingAnalystApproval_onCreditAcceptedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                        }
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_WAITING_CREDIT_ANALYST_APPROVAL_$$_CREDIT_ACCEPTED_creditAnalyst_onAcceptedAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onCustomerUpdated(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_WAITING_ANAYLIST_APPROVAL onCustomerUpdated payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_APPLICATION_SUBMITTED_customer_updatedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onRejected(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_WAITING_ANAYLIST_APPROVAL onRejected payload: {}", payload.toString)
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_managerRejected_onRejectedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onResultReceived(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_WAITING_ANAYLIST_APPROVAL onResultReceived payload: {}", payload.toString)
                        if(SpringCreditSMGuardsLocator.getInstance.creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_onResultReceived_isScoreTooLowGuard.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringCreditSMActionsLocator.getInstance().creditsm_CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_lowScore_onResultReceivedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                        }
                    
                        Effect.unhandled.thenNoReply()
                    }
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
            //State Command Handler
            case CREDIT_REJECTED(controlObject) =>
                base[CreditSMEvent](cmd, state) {
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
                case _ =>
                    Effect.unhandled.thenNoReply()
        }

    def eventHandler(context: ActorContext[CreditSMEvent], state: State, event: PersistEvent): State = {
        state match {
            //1 Pseud0
            case _initial @ INITIAL(controlObject) =>
                event match {
                case CreditApplicationSubmittedPersistEvent(payload) =>
                    context.log.debug("Processing INITIAL CreditApplicationSubmittedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit5 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_WAITING_MANAGER_APPROVAL(_initial.controlObject)
            
                case unh @ _ => {
                    context.log.warn("This PersistEvent is not handled during INITIAL State {}", unh)
                    _initial
                  }
            }

            //1 Recursive
            //pseudo
            case _credit_application_submitted_initial_ca @ CREDIT_APPLICATION_SUBMITTED_$_INITIAL_CA(controlObject) =>
                event match {
                case CustomerUpdatedEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_INITIAL_CA CustomerUpdatedEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit6
                    _credit_application_submitted_initial_ca
                case CreditScoreToLowPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_INITIAL_CA CreditScoreToLowPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_initial_ca.controlObject)
                case CreditRejectedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_INITIAL_CA CreditRejectedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_initial_ca.controlObject)
            
                case unh @ _ => {
                    context.log.warn("This PersistEvent is not handled during CREDIT_APPLICATION_SUBMITTED_$_INITIAL_CA State {}", unh)
                    _credit_application_submitted_initial_ca
                  }
            }
            //recursive
            case _credit_application_submitted_credit_accepted @ CREDIT_APPLICATION_SUBMITTED_$_CREDIT_ACCEPTED(controlObject) =>
                event match {
                case CustomerUpdatedEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_CREDIT_ACCEPTED CustomerUpdatedEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit6
                    _credit_application_submitted_credit_accepted
                case CreditScoreToLowPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_CREDIT_ACCEPTED CreditScoreToLowPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_credit_accepted.controlObject)
                case CreditRejectedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_CREDIT_ACCEPTED CreditRejectedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_credit_accepted.controlObject)
            
                case unh @ _ => {
                    context.log.warn("This PersistEvent is not handled during CREDIT_APPLICATION_SUBMITTED_$_CREDIT_ACCEPTED State {}", unh)
                    _credit_application_submitted_credit_accepted
                  }
            }
            //recursive
            //pseudo
            case _credit_application_submitted_relationship_manager_approved_initial_fe_sm @ CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_INITIAL_FE_SM(controlObject) =>
                event match {
                case CustomerUpdatedEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_INITIAL_FE_SM CustomerUpdatedEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit6
                    _credit_application_submitted_relationship_manager_approved_initial_fe_sm
                case CreditScoreToLowPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_INITIAL_FE_SM CreditScoreToLowPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_relationship_manager_approved_initial_fe_sm.controlObject)
                case CreditRejectedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_INITIAL_FE_SM CreditRejectedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_relationship_manager_approved_initial_fe_sm.controlObject)
                case SalesManagerApprovalPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_INITIAL_FE_SM SalesManagerApprovalPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_INITIAL_CSC(_credit_application_submitted_relationship_manager_approved_initial_fe_sm.controlObject)
            
                case unh @ _ => {
                    context.log.warn("This PersistEvent is not handled during CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_INITIAL_FE_SM State {}", unh)
                    _credit_application_submitted_relationship_manager_approved_initial_fe_sm
                  }
            }
            //recursive
            case _credit_application_submitted_relationship_manager_approved_waiting_manager_approval @ CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_WAITING_MANAGER_APPROVAL(controlObject) =>
                event match {
                case CustomerUpdatedEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_WAITING_MANAGER_APPROVAL CustomerUpdatedEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit6
                    _credit_application_submitted_relationship_manager_approved_waiting_manager_approval
                case CreditScoreToLowPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_WAITING_MANAGER_APPROVAL CreditScoreToLowPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_relationship_manager_approved_waiting_manager_approval.controlObject)
                case CreditRejectedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_WAITING_MANAGER_APPROVAL CreditRejectedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_relationship_manager_approved_waiting_manager_approval.controlObject)
                case SalesManagerApprovalPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_WAITING_MANAGER_APPROVAL SalesManagerApprovalPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_INITIAL_CSC(_credit_application_submitted_relationship_manager_approved_waiting_manager_approval.controlObject)
                case WaitingManagerApprovalSalesManagerApprovedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_WAITING_MANAGER_APPROVAL WaitingManagerApprovalSalesManagerApprovedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit6
                    _credit_application_submitted_relationship_manager_approved_waiting_manager_approval
            
                case unh @ _ => {
                    context.log.warn("This PersistEvent is not handled during CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_WAITING_MANAGER_APPROVAL State {}", unh)
                    _credit_application_submitted_relationship_manager_approved_waiting_manager_approval
                  }
            }
            //recursive
            //pseudo
            case _credit_application_submitted_sales_manager_approved_initial_csc @ CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_INITIAL_CSC(controlObject) =>
                event match {
                case CustomerUpdatedEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_INITIAL_CSC CustomerUpdatedEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit6
                    _credit_application_submitted_sales_manager_approved_initial_csc
                case AdressCheckReceivedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_INITIAL_CSC AdressCheckReceivedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit4 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_ADRRESCHECK_RESULT_RECEIVED(_credit_application_submitted_sales_manager_approved_initial_csc.controlObject)
                case FraudPreventionReceivedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_INITIAL_CSC FraudPreventionReceivedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit4 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_RESULT_RECEIVED(_credit_application_submitted_sales_manager_approved_initial_csc.controlObject)
                case CreditScoreReceivedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_INITIAL_CSC CreditScoreReceivedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit4 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_RECEIVED(_credit_application_submitted_sales_manager_approved_initial_csc.controlObject)
                case CreditScoreToLowPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_INITIAL_CSC CreditScoreToLowPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_sales_manager_approved_initial_csc.controlObject)
                case CreditRejectedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_INITIAL_CSC CreditRejectedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_sales_manager_approved_initial_csc.controlObject)
                case CreditScoreNotEnoughPersisteEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_INITIAL_CSC CreditScoreNotEnoughPersisteEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_FROM_SENIOR_MANAGER(_credit_application_submitted_sales_manager_approved_initial_csc.controlObject)
                case PositiveResultPersistedEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_INITIAL_CSC PositiveResultPersistedEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_WAITING_ANAYLIST_APPROVAL(_credit_application_submitted_sales_manager_approved_initial_csc.controlObject)
            
                case unh @ _ => {
                    context.log.warn("This PersistEvent is not handled during CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_INITIAL_CSC State {}", unh)
                    _credit_application_submitted_sales_manager_approved_initial_csc
                  }
            }
            //recursive
            case _credit_application_submitted_sales_manager_approved_adrrescheck_result_received @ CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_ADRRESCHECK_RESULT_RECEIVED(controlObject) =>
                event match {
                case AdressCheckFraudPreventionReceviedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_ADRRESCHECK_RESULT_RECEIVED AdressCheckFraudPreventionReceviedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit4 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED(_credit_application_submitted_sales_manager_approved_adrrescheck_result_received.controlObject)
                case AddressCheckCreditScoreResultReceivedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_ADRRESCHECK_RESULT_RECEIVED AddressCheckCreditScoreResultReceivedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit4 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED(_credit_application_submitted_sales_manager_approved_adrrescheck_result_received.controlObject)
                case CustomerUpdatedEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_ADRRESCHECK_RESULT_RECEIVED CustomerUpdatedEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit6
                    _credit_application_submitted_sales_manager_approved_adrrescheck_result_received
                case CreditScoreToLowPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_ADRRESCHECK_RESULT_RECEIVED CreditScoreToLowPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_sales_manager_approved_adrrescheck_result_received.controlObject)
                case CreditRejectedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_ADRRESCHECK_RESULT_RECEIVED CreditRejectedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_sales_manager_approved_adrrescheck_result_received.controlObject)
                case CreditScoreNotEnoughPersisteEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_ADRRESCHECK_RESULT_RECEIVED CreditScoreNotEnoughPersisteEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_FROM_SENIOR_MANAGER(_credit_application_submitted_sales_manager_approved_adrrescheck_result_received.controlObject)
                case PositiveResultPersistedEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_ADRRESCHECK_RESULT_RECEIVED PositiveResultPersistedEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_WAITING_ANAYLIST_APPROVAL(_credit_application_submitted_sales_manager_approved_adrrescheck_result_received.controlObject)
            
                case unh @ _ => {
                    context.log.warn("This PersistEvent is not handled during CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_ADRRESCHECK_RESULT_RECEIVED State {}", unh)
                    _credit_application_submitted_sales_manager_approved_adrrescheck_result_received
                  }
            }
            //recursive
            case _credit_application_submitted_sales_manager_approved_creditscore_addresscheck_result_received @ CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED(controlObject) =>
                event match {
                case CustomerUpdatedEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED CustomerUpdatedEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit6
                    _credit_application_submitted_sales_manager_approved_creditscore_addresscheck_result_received
                case CreditScoreToLowPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED CreditScoreToLowPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_sales_manager_approved_creditscore_addresscheck_result_received.controlObject)
                case CreditRejectedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED CreditRejectedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_sales_manager_approved_creditscore_addresscheck_result_received.controlObject)
                case CreditScoreNotEnoughPersisteEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED CreditScoreNotEnoughPersisteEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_FROM_SENIOR_MANAGER(_credit_application_submitted_sales_manager_approved_creditscore_addresscheck_result_received.controlObject)
                case PositiveResultPersistedEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED PositiveResultPersistedEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_WAITING_ANAYLIST_APPROVAL(_credit_application_submitted_sales_manager_approved_creditscore_addresscheck_result_received.controlObject)
            
                case unh @ _ => {
                    context.log.warn("This PersistEvent is not handled during CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED State {}", unh)
                    _credit_application_submitted_sales_manager_approved_creditscore_addresscheck_result_received
                  }
            }
            //recursive
            case _credit_application_submitted_sales_manager_approved_creditscore_fraudprevention_result_received @ CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED(controlObject) =>
                event match {
                case CustomerUpdatedEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED CustomerUpdatedEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit6
                    _credit_application_submitted_sales_manager_approved_creditscore_fraudprevention_result_received
                case CreditScoreToLowPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED CreditScoreToLowPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_sales_manager_approved_creditscore_fraudprevention_result_received.controlObject)
                case CreditRejectedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED CreditRejectedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_sales_manager_approved_creditscore_fraudprevention_result_received.controlObject)
                case CreditScoreNotEnoughPersisteEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED CreditScoreNotEnoughPersisteEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_FROM_SENIOR_MANAGER(_credit_application_submitted_sales_manager_approved_creditscore_fraudprevention_result_received.controlObject)
                case PositiveResultPersistedEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED PositiveResultPersistedEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_WAITING_ANAYLIST_APPROVAL(_credit_application_submitted_sales_manager_approved_creditscore_fraudprevention_result_received.controlObject)
            
                case unh @ _ => {
                    context.log.warn("This PersistEvent is not handled during CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED State {}", unh)
                    _credit_application_submitted_sales_manager_approved_creditscore_fraudprevention_result_received
                  }
            }
            //recursive
            case _credit_application_submitted_sales_manager_approved_creditscore_received @ CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_RECEIVED(controlObject) =>
                event match {
                case CreditScoreFraudPreventionReceivedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_RECEIVED CreditScoreFraudPreventionReceivedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit4 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED(_credit_application_submitted_sales_manager_approved_creditscore_received.controlObject)
                case CreditScoreAddressResultReceivedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_RECEIVED CreditScoreAddressResultReceivedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit4 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED(_credit_application_submitted_sales_manager_approved_creditscore_received.controlObject)
                case CustomerUpdatedEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_RECEIVED CustomerUpdatedEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit6
                    _credit_application_submitted_sales_manager_approved_creditscore_received
                case CreditScoreToLowPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_RECEIVED CreditScoreToLowPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_sales_manager_approved_creditscore_received.controlObject)
                case CreditRejectedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_RECEIVED CreditRejectedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_sales_manager_approved_creditscore_received.controlObject)
                case CreditScoreNotEnoughPersisteEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_RECEIVED CreditScoreNotEnoughPersisteEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_FROM_SENIOR_MANAGER(_credit_application_submitted_sales_manager_approved_creditscore_received.controlObject)
                case PositiveResultPersistedEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_RECEIVED PositiveResultPersistedEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_WAITING_ANAYLIST_APPROVAL(_credit_application_submitted_sales_manager_approved_creditscore_received.controlObject)
            
                case unh @ _ => {
                    context.log.warn("This PersistEvent is not handled during CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_RECEIVED State {}", unh)
                    _credit_application_submitted_sales_manager_approved_creditscore_received
                  }
            }
            //recursive
            case _credit_application_submitted_sales_manager_approved_fraudprevention_adresscheck_result_received @ CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED(controlObject) =>
                event match {
                case CustomerUpdatedEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED CustomerUpdatedEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit6
                    _credit_application_submitted_sales_manager_approved_fraudprevention_adresscheck_result_received
                case CreditScoreToLowPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED CreditScoreToLowPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_sales_manager_approved_fraudprevention_adresscheck_result_received.controlObject)
                case CreditRejectedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED CreditRejectedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_sales_manager_approved_fraudprevention_adresscheck_result_received.controlObject)
                case CreditScoreNotEnoughPersisteEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED CreditScoreNotEnoughPersisteEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_FROM_SENIOR_MANAGER(_credit_application_submitted_sales_manager_approved_fraudprevention_adresscheck_result_received.controlObject)
                case PositiveResultPersistedEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED PositiveResultPersistedEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_WAITING_ANAYLIST_APPROVAL(_credit_application_submitted_sales_manager_approved_fraudprevention_adresscheck_result_received.controlObject)
            
                case unh @ _ => {
                    context.log.warn("This PersistEvent is not handled during CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED State {}", unh)
                    _credit_application_submitted_sales_manager_approved_fraudprevention_adresscheck_result_received
                  }
            }
            //recursive
            case _credit_application_submitted_sales_manager_approved_fraudprevention_result_received @ CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_RESULT_RECEIVED(controlObject) =>
                event match {
                case CustomerUpdatedEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_RESULT_RECEIVED CustomerUpdatedEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit6
                    _credit_application_submitted_sales_manager_approved_fraudprevention_result_received
                case FraudPreventionAdressCheckReceivedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_RESULT_RECEIVED FraudPreventionAdressCheckReceivedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit4 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED(_credit_application_submitted_sales_manager_approved_fraudprevention_result_received.controlObject)
                case FraudPreventionCreditScoreReceivedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_RESULT_RECEIVED FraudPreventionCreditScoreReceivedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit4 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED(_credit_application_submitted_sales_manager_approved_fraudprevention_result_received.controlObject)
                case CreditScoreToLowPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_RESULT_RECEIVED CreditScoreToLowPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_sales_manager_approved_fraudprevention_result_received.controlObject)
                case CreditRejectedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_RESULT_RECEIVED CreditRejectedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_sales_manager_approved_fraudprevention_result_received.controlObject)
                case CreditScoreNotEnoughPersisteEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_RESULT_RECEIVED CreditScoreNotEnoughPersisteEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_FROM_SENIOR_MANAGER(_credit_application_submitted_sales_manager_approved_fraudprevention_result_received.controlObject)
                case PositiveResultPersistedEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_RESULT_RECEIVED PositiveResultPersistedEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_WAITING_ANAYLIST_APPROVAL(_credit_application_submitted_sales_manager_approved_fraudprevention_result_received.controlObject)
            
                case unh @ _ => {
                    context.log.warn("This PersistEvent is not handled during CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_RESULT_RECEIVED State {}", unh)
                    _credit_application_submitted_sales_manager_approved_fraudprevention_result_received
                  }
            }
            //recursive
            //pseudo
            case _credit_application_submitted_waiting_approval_initial_fe @ CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_INITIAL_FE(controlObject) =>
                event match {
                case CustomerUpdatedEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_INITIAL_FE CustomerUpdatedEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit6
                    _credit_application_submitted_waiting_approval_initial_fe
                case CreditScoreToLowPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_INITIAL_FE CreditScoreToLowPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_waiting_approval_initial_fe.controlObject)
                case CreditRejectedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_INITIAL_FE CreditRejectedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_waiting_approval_initial_fe.controlObject)
                case RelationshipManagerApprovedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_INITIAL_FE RelationshipManagerApprovedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_WAITING_MANAGER_APPROVAL(_credit_application_submitted_waiting_approval_initial_fe.controlObject)
            
                case unh @ _ => {
                    context.log.warn("This PersistEvent is not handled during CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_INITIAL_FE State {}", unh)
                    _credit_application_submitted_waiting_approval_initial_fe
                  }
            }
            //recursive
            case _credit_application_submitted_waiting_approval_waiting_manager_approval @ CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_WAITING_MANAGER_APPROVAL(controlObject) =>
                event match {
                case CustomerUpdatedEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_WAITING_MANAGER_APPROVAL CustomerUpdatedEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit6
                    _credit_application_submitted_waiting_approval_waiting_manager_approval
                case CreditScoreToLowPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_WAITING_MANAGER_APPROVAL CreditScoreToLowPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_waiting_approval_waiting_manager_approval.controlObject)
                case CreditRejectedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_WAITING_MANAGER_APPROVAL CreditRejectedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_waiting_approval_waiting_manager_approval.controlObject)
                case RelationshipManagerApprovedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_WAITING_MANAGER_APPROVAL RelationshipManagerApprovedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_WAITING_MANAGER_APPROVAL(_credit_application_submitted_waiting_approval_waiting_manager_approval.controlObject)
                case WaitingManagerApprovalRelationshipManagerPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_WAITING_MANAGER_APPROVAL WaitingManagerApprovalRelationshipManagerPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit6
                    _credit_application_submitted_waiting_approval_waiting_manager_approval
            
                case unh @ _ => {
                    context.log.warn("This PersistEvent is not handled during CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_WAITING_MANAGER_APPROVAL State {}", unh)
                    _credit_application_submitted_waiting_approval_waiting_manager_approval
                  }
            }
            //recursive
            case _credit_application_submitted_waiting_approval_from_senior_manager @ CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_FROM_SENIOR_MANAGER(controlObject) =>
                event match {
                case CustomerUpdatedEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_FROM_SENIOR_MANAGER CustomerUpdatedEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit6
                    _credit_application_submitted_waiting_approval_from_senior_manager
                case CreditScoreToLowPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_FROM_SENIOR_MANAGER CreditScoreToLowPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_waiting_approval_from_senior_manager.controlObject)
                case CreditRejectedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_FROM_SENIOR_MANAGER CreditRejectedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_waiting_approval_from_senior_manager.controlObject)
                case AcceptableScorePersistedEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_FROM_SENIOR_MANAGER AcceptableScorePersistedEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit4 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_WAITING_ANAYLIST_APPROVAL(_credit_application_submitted_waiting_approval_from_senior_manager.controlObject)
            
                case unh @ _ => {
                    context.log.warn("This PersistEvent is not handled during CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_FROM_SENIOR_MANAGER State {}", unh)
                    _credit_application_submitted_waiting_approval_from_senior_manager
                  }
            }
            //recursive
            //pseudo
            case _credit_application_submitted_waiting_credit_analyst_approval_initial_ca_sm @ CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_INITIAL_CA_SM(controlObject) =>
                event match {
                case CreditAcceptedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_INITIAL_CA_SM CreditAcceptedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_CREDIT_ACCEPTED(_credit_application_submitted_waiting_credit_analyst_approval_initial_ca_sm.controlObject)
                case CustomerUpdatedEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_INITIAL_CA_SM CustomerUpdatedEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit6
                    _credit_application_submitted_waiting_credit_analyst_approval_initial_ca_sm
                case CreditScoreToLowPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_INITIAL_CA_SM CreditScoreToLowPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_waiting_credit_analyst_approval_initial_ca_sm.controlObject)
                case CreditRejectedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_INITIAL_CA_SM CreditRejectedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_waiting_credit_analyst_approval_initial_ca_sm.controlObject)
            
                case unh @ _ => {
                    context.log.warn("This PersistEvent is not handled during CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_INITIAL_CA_SM State {}", unh)
                    _credit_application_submitted_waiting_credit_analyst_approval_initial_ca_sm
                  }
            }
            //recursive
            case _credit_application_submitted_waiting_credit_analyst_approval_waiting_anaylist_approval @ CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_WAITING_ANAYLIST_APPROVAL(controlObject) =>
                event match {
                case CreditAcceptedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_WAITING_ANAYLIST_APPROVAL CreditAcceptedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_APPLICATION_SUBMITTED_$_CREDIT_ACCEPTED(_credit_application_submitted_waiting_credit_analyst_approval_waiting_anaylist_approval.controlObject)
                case CustomerUpdatedEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_WAITING_ANAYLIST_APPROVAL CustomerUpdatedEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit6
                    _credit_application_submitted_waiting_credit_analyst_approval_waiting_anaylist_approval
                case CreditScoreToLowPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_WAITING_ANAYLIST_APPROVAL CreditScoreToLowPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_waiting_credit_analyst_approval_waiting_anaylist_approval.controlObject)
                case CreditRejectedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_WAITING_ANAYLIST_APPROVAL CreditRejectedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit1 from Master State
                    CREDIT_REJECTED(_credit_application_submitted_waiting_credit_analyst_approval_waiting_anaylist_approval.controlObject)
                case WaitingCreditAnalystApprovalCreditAcceptedPersistEvent(payload) =>
                    context.log.debug("Processing CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_WAITING_ANAYLIST_APPROVAL WaitingCreditAnalystApprovalCreditAcceptedPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit6
                    _credit_application_submitted_waiting_credit_analyst_approval_waiting_anaylist_approval
            
                case unh @ _ => {
                    context.log.warn("This PersistEvent is not handled during CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_WAITING_ANAYLIST_APPROVAL State {}", unh)
                    _credit_application_submitted_waiting_credit_analyst_approval_waiting_anaylist_approval
                  }
            }
            case _credit_rejected @ CREDIT_REJECTED(controlObject) =>
                event match {
            
                case unh @ _ => {
                    context.log.warn("This PersistEvent is not handled during CREDIT_REJECTED State {}", unh)
                    _credit_rejected
                  }
            }
        }
    }
}

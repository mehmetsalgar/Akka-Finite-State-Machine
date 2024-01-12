package org.salgar.fsm.pekko.foureyes.creditscore

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.salgar.fsm.pekko.foureyes.creditscore.MultiTenantCreditScoreSM._
import org.salgar.fsm.pekko.foureyes.creditscore.actions.SpringMultiTenantCreditScoreSMActionsLocator
import org.salgar.fsm.pekko.foureyes.creditscore.guards.SpringMultiTenantCreditScoreSMGuardsLocator
import org.salgar.pekko.fsm.api.UseCaseKey
import org.salgar.pekko.fsm.base.CborSerializable
import org.salgar.pekko.fsm.base.actors.BaseActor
import org.salgar.pekko.fsm.base.actors.BaseActor.InternalBaseMessage

import java.util

object MultiTenantCreditScoreSM {
    sealed trait State {
        private[creditscore] def controlObject: util.Map[java.lang.String, AnyRef]
    }
    final case class INITIAL_MTCS(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class FINALSTATE(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class WAITING_MULTI_TENANT_RESULTS(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable

    sealed trait MultiTenantCreditScoreSMEvent {
        def useCaseKey: UseCaseKey
    }
    final case class onCreditScoreReceived(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef], replyTo: ActorRef[Response]) extends MultiTenantCreditScoreSMEvent
    object onCreditScoreReceived {
        def apply(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]) : onCreditScoreReceived = {
            onCreditScoreReceived(useCaseKey, payload, null)
        }
    }
    final case class onStartMultiTenantCreditScoreResearch(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef], replyTo: ActorRef[Response]) extends MultiTenantCreditScoreSMEvent
    object onStartMultiTenantCreditScoreResearch {
        def apply(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]) : onStartMultiTenantCreditScoreResearch = {
            onStartMultiTenantCreditScoreResearch(useCaseKey, payload, null)
        }
    }

    //Reporting
    sealed trait ReportEvent extends MultiTenantCreditScoreSMEvent
    final case class onReport(useCaseKey: UseCaseKey, replyTo: ActorRef[Response])
        extends ReportEvent

    sealed trait Response
    final case class ReportResponse(state: State, payload: util.Map[String, AnyRef]) extends Response
    final case class AcknowledgeResponse() extends Response

    //internal protocol
    sealed trait InternalMessage extends MultiTenantCreditScoreSMEvent
    final case class onAddMultiTenantCreditScoreSMRelatedReference(useCaseKey: UseCaseKey, listing: Receptionist.Listing) extends InternalMessage with InternalBaseMessage

    sealed trait PersistEvent
    final case class CreditScoreRetryPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class CreditScoreErrorPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class StartMultiTenantResearchPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class OneTenantCreditScoreResultReceived(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class MultiTenantResultsReceivedPersistentEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class CreditScorePersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class StartCreditScoreResearchPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
}

class MultiTenantCreditScoreSM (
                    ctx: ActorContext[MultiTenantCreditScoreSMEvent],
                    useCaseKey: String
                  ) extends BaseActor[MultiTenantCreditScoreSMEvent, onAddMultiTenantCreditScoreSMRelatedReference, PersistEvent, State] (
                                TypeCase[onAddMultiTenantCreditScoreSMRelatedReference],
                                useCaseKey
                            ) {
    import MultiTenantCreditScoreSM._

    private val listingAdapter: ActorRef[Receptionist.Listing] = ctx.messageAdapter {
        listing => onAddMultiTenantCreditScoreSMRelatedReference(() => useCaseKey, listing)
    }

    ctx.system.receptionist ! Receptionist.Subscribe(
        ServiceKey[MultiTenantCreditScoreSMEvent]("multitenantcreditscoresmService_" + useCaseKey),
        listingAdapter)

    def commandHandler(context: ActorContext[MultiTenantCreditScoreSMEvent], cmd: MultiTenantCreditScoreSMEvent, state: State): ReplyEffect[PersistEvent, State] =
        cmd match {
            case onReport(useCaseKey, replyTo) =>
                Effect.reply(replyTo)( ReportResponse(state, java.util.Collections.unmodifiableMap(state.controlObject)))

            case _ =>
                commandHandlerInternal(context, cmd, state)
    }

    def commandHandlerInternal(context: ActorContext[MultiTenantCreditScoreSMEvent], cmd: MultiTenantCreditScoreSMEvent, state: State): ReplyEffect[PersistEvent, State] =
        state match {
            //State Command Handler
            case INITIAL_MTCS(controlObject) =>
                base[MultiTenantCreditScoreSMEvent](cmd, state) {
                    case onStartMultiTenantCreditScoreResearch(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing INITIAL_MTCS onStartMultiTenantCreditScoreResearch payload: {}", payload.toString)
                    
                        return SpringMultiTenantCreditScoreSMActionsLocator.getInstance().multitenantcreditscoresm_INITIAL_MTCS_$$_WAITING_MULTI_TENANT_RESULTS_initial_WaitingMultiTenantResultAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
            //State Command Handler
            case FINALSTATE(controlObject) =>
                base[MultiTenantCreditScoreSMEvent](cmd, state) {
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
            //State Command Handler
            case WAITING_MULTI_TENANT_RESULTS(controlObject) =>
                base[MultiTenantCreditScoreSMEvent](cmd, state) {
                    case onCreditScoreReceived(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing WAITING_MULTI_TENANT_RESULTS onCreditScoreReceived payload: {}", payload.toString)
                        if(SpringMultiTenantCreditScoreSMGuardsLocator.getInstance.multitenantcreditscoresm_WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_onCreditScoreReceived_isNotAllTenantsResponded.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringMultiTenantCreditScoreSMActionsLocator.getInstance().multitenantcreditscoresm_WAITING_MULTI_TENANT_RESULTS_$$_WAITING_MULTI_TENANT_RESULTS_waitingMultiTenantResults_waitingMultiTenantResultsAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                        }
                        else if(SpringMultiTenantCreditScoreSMGuardsLocator.getInstance.multitenantcreditscoresm_WAITING_MULTI_TENANT_RESULTS_$$_FINALSTATE_onCreditScoreReceived_isAllCreditScoreMultitenantResponded.evaluate(
                            context,
                            controlObject,
                            payload)) {
                    
                        return SpringMultiTenantCreditScoreSMActionsLocator.getInstance().multitenantcreditscoresm_WAITING_MULTI_TENANT_RESULTS_$$_FINALSTATE_waitingMultiTenantResult_finalStateAction.doAction(
                            context,
                            controlObject,
                            payload, replyTo)
                        }
                    
                        Effect.unhandled.thenNoReply()
                    }
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
                case _ =>
                    Effect.unhandled.thenNoReply()
        }

    def eventHandler(context: ActorContext[MultiTenantCreditScoreSMEvent], state: State, event: PersistEvent): State = {
        state match {
            //1 Pseud0
            case _initial_mtcs @ INITIAL_MTCS(controlObject) =>
                event match {
                case StartMultiTenantResearchPersistEvent(payload) =>
                    context.log.debug("Processing INITIAL_MTCS StartMultiTenantResearchPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit4 from Master State
                    WAITING_MULTI_TENANT_RESULTS(_initial_mtcs.controlObject)
            
                case unh @ _ => {
                    context.log.warn("This PersistEvent is not handled during INITIAL_MTCS State {}", unh)
                    _initial_mtcs
                  }
            }

            //1 Recursive
            case _finalstate @ FINALSTATE(controlObject) =>
                event match {
            
                case unh @ _ => {
                    context.log.warn("This PersistEvent is not handled during FINALSTATE State {}", unh)
                    _finalstate
                  }
            }
            case _waiting_multi_tenant_results @ WAITING_MULTI_TENANT_RESULTS(controlObject) =>
                event match {
                case MultiTenantResultsReceivedPersistentEvent(payload) =>
                    context.log.debug("Processing WAITING_MULTI_TENANT_RESULTS MultiTenantResultsReceivedPersistentEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit4 from Master State
                    FINALSTATE(_waiting_multi_tenant_results.controlObject)
                case OneTenantCreditScoreResultReceived(payload) =>
                    context.log.debug("Processing WAITING_MULTI_TENANT_RESULTS OneTenantCreditScoreResultReceived payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit6
                    _waiting_multi_tenant_results
            
                case unh @ _ => {
                    context.log.warn("This PersistEvent is not handled during WAITING_MULTI_TENANT_RESULTS State {}", unh)
                    _waiting_multi_tenant_results
                  }
            }
        }
    }
}

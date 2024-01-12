package org.salgar.fsm.pekko.foureyes.creditscore

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.salgar.fsm.pekko.foureyes.creditscore.CreditScoreSM._
import org.salgar.fsm.pekko.foureyes.creditscore.actions.SpringCreditScoreSMActionsLocator
import org.salgar.pekko.fsm.api.UseCaseKey
import org.salgar.pekko.fsm.base.CborSerializable
import org.salgar.pekko.fsm.base.actors.BaseActor
import org.salgar.pekko.fsm.base.actors.BaseActor.InternalBaseMessage

import java.util

object CreditScoreSM {
    sealed trait State {
        private[creditscore] def controlObject: util.Map[java.lang.String, AnyRef]
    }
    final case class INITIAL(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class ERROR(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class RESULTRECEIVED(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class WAITING_RESULT(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable

    sealed trait CreditScoreSMEvent {
        def useCaseKey: UseCaseKey
    }
    final case class onError(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef], replyTo: ActorRef[Response]) extends CreditScoreSMEvent
    object onError {
        def apply(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]) : onError = {
            onError(useCaseKey, payload, null)
        }
    }
    final case class onResultReceived(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef], replyTo: ActorRef[Response]) extends CreditScoreSMEvent
    object onResultReceived {
        def apply(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]) : onResultReceived = {
            onResultReceived(useCaseKey, payload, null)
        }
    }
    final case class onRetry(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef], replyTo: ActorRef[Response]) extends CreditScoreSMEvent
    object onRetry {
        def apply(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]) : onRetry = {
            onRetry(useCaseKey, payload, null)
        }
    }
    final case class onStartCreditScoreResearch(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef], replyTo: ActorRef[Response]) extends CreditScoreSMEvent
    object onStartCreditScoreResearch {
        def apply(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]) : onStartCreditScoreResearch = {
            onStartCreditScoreResearch(useCaseKey, payload, null)
        }
    }

    //Reporting
    sealed trait ReportEvent extends CreditScoreSMEvent
    final case class onReport(useCaseKey: UseCaseKey, replyTo: ActorRef[Response])
        extends ReportEvent

    sealed trait Response
    final case class ReportResponse(state: State, payload: util.Map[String, AnyRef]) extends Response
    final case class AcknowledgeResponse() extends Response

    //internal protocol
    sealed trait InternalMessage extends CreditScoreSMEvent
    final case class onAddCreditScoreSMRelatedReference(useCaseKey: UseCaseKey, listing: Receptionist.Listing) extends InternalMessage with InternalBaseMessage

    sealed trait PersistEvent
    final case class CreditScoreRetryPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class CreditScoreErrorPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class StartMultiTenantResearchPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class OneTenantCreditScoreResultReceived(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class CreditScorePersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class MultiTenantResultsReceivedPersistentEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class StartCreditScoreResearchPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
}

class CreditScoreSM (
                    ctx: ActorContext[CreditScoreSMEvent],
                    useCaseKey: String
                  ) extends BaseActor[CreditScoreSMEvent, onAddCreditScoreSMRelatedReference, PersistEvent, State] (
                                TypeCase[onAddCreditScoreSMRelatedReference],
                                useCaseKey
                            ) {
    import CreditScoreSM._

    private val listingAdapter: ActorRef[Receptionist.Listing] = ctx.messageAdapter {
        listing => onAddCreditScoreSMRelatedReference(() => useCaseKey, listing)
    }

    ctx.system.receptionist ! Receptionist.Subscribe(
        ServiceKey[CreditScoreSMEvent]("creditscoresmService_" + useCaseKey),
        listingAdapter)

    def commandHandler(context: ActorContext[CreditScoreSMEvent], cmd: CreditScoreSMEvent, state: State): ReplyEffect[PersistEvent, State] =
        cmd match {
            case onReport(useCaseKey, replyTo) =>
                Effect.reply(replyTo)( ReportResponse(state, java.util.Collections.unmodifiableMap(state.controlObject)))

            case _ =>
                commandHandlerInternal(context, cmd, state)
    }

    def commandHandlerInternal(context: ActorContext[CreditScoreSMEvent], cmd: CreditScoreSMEvent, state: State): ReplyEffect[PersistEvent, State] =
        state match {
            //State Command Handler
            case INITIAL(controlObject) =>
                base[CreditScoreSMEvent](cmd, state) {
                    case onStartCreditScoreResearch(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing INITIAL onStartCreditScoreResearch payload: {}", payload.toString)
                    
                        return SpringCreditScoreSMActionsLocator.getInstance().creditscoresm_INITIAL_$$_WAITING_RESULT_initialAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
            //State Command Handler
            case ERROR(controlObject) =>
                base[CreditScoreSMEvent](cmd, state) {
                    case onRetry(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing ERROR onRetry payload: {}", payload.toString)
                    
                        return SpringCreditScoreSMActionsLocator.getInstance().creditscoresm_ERROR_$$_WAITING_RESULT_error_onRetryAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
            //State Command Handler
            case RESULTRECEIVED(controlObject) =>
                base[CreditScoreSMEvent](cmd, state) {
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
            //State Command Handler
            case WAITING_RESULT(controlObject) =>
                base[CreditScoreSMEvent](cmd, state) {
                    case onError(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing WAITING_RESULT onError payload: {}", payload.toString)
                    
                        return SpringCreditScoreSMActionsLocator.getInstance().creditscoresm_WAITING_RESULT_$$_ERROR_waitingResult_onErrorAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onResultReceived(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing WAITING_RESULT onResultReceived payload: {}", payload.toString)
                    
                        return SpringCreditScoreSMActionsLocator.getInstance().creditscoresm_WAITING_RESULT_$$_RESULTRECEIVED_waitingResult_onResultReceivedAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
                case _ =>
                    Effect.unhandled.thenNoReply()
        }

    def eventHandler(context: ActorContext[CreditScoreSMEvent], state: State, event: PersistEvent): State = {
        state match {
            //1 Pseud0
            case _initial @ INITIAL(controlObject) =>
                event match {
                case StartCreditScoreResearchPersistEvent(payload) =>
                    context.log.debug("Processing INITIAL StartCreditScoreResearchPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit4 from Master State
                    WAITING_RESULT(_initial.controlObject)
            
                case unh @ _ => {
                    context.log.warn("This PersistEvent is not handled during INITIAL State {}", unh)
                    _initial
                  }
            }

            //1 Recursive
            case _error @ ERROR(controlObject) =>
                event match {
                case CreditScoreRetryPersistEvent(payload) =>
                    context.log.debug("Processing ERROR CreditScoreRetryPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit4 from Master State
                    WAITING_RESULT(_error.controlObject)
            
                case unh @ _ => {
                    context.log.warn("This PersistEvent is not handled during ERROR State {}", unh)
                    _error
                  }
            }
            case _resultreceived @ RESULTRECEIVED(controlObject) =>
                event match {
            
                case unh @ _ => {
                    context.log.warn("This PersistEvent is not handled during RESULTRECEIVED State {}", unh)
                    _resultreceived
                  }
            }
            case _waiting_result @ WAITING_RESULT(controlObject) =>
                event match {
                case CreditScoreErrorPersistEvent(payload) =>
                    context.log.debug("Processing WAITING_RESULT CreditScoreErrorPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit4 from Master State
                    ERROR(_waiting_result.controlObject)
                case CreditScorePersistEvent(payload) =>
                    context.log.debug("Processing WAITING_RESULT CreditScorePersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit4 from Master State
                    RESULTRECEIVED(_waiting_result.controlObject)
            
                case unh @ _ => {
                    context.log.warn("This PersistEvent is not handled during WAITING_RESULT State {}", unh)
                    _waiting_result
                  }
            }
        }
    }
}

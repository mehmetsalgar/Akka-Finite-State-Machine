package org.salgar.fsm.pekko.foureyes.fraudprevention

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.salgar.fsm.pekko.foureyes.fraudprevention.FraudPreventionSM._
import org.salgar.fsm.pekko.foureyes.fraudprevention.actions.SpringFraudPreventionSMActionsLocator
import org.salgar.pekko.fsm.api.UseCaseKey
import org.salgar.pekko.fsm.base.CborSerializable
import org.salgar.pekko.fsm.base.actors.BaseActor
import org.salgar.pekko.fsm.base.actors.BaseActor.InternalBaseMessage

import java.util

object FraudPreventionSM {
    sealed trait State {
        private[fraudprevention] def controlObject: util.Map[java.lang.String, AnyRef]
    }
    final case class INITIAL(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class ERROR(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class RESULTRECEIVED(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class WAITING_RESULT(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable

    sealed trait FraudPreventionSMEvent {
        def useCaseKey: UseCaseKey
    }
    final case class onError(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef], replyTo: ActorRef[Response]) extends FraudPreventionSMEvent
    object onError {
        def apply(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]) : onError = {
            onError(useCaseKey, payload, null)
        }
    }
    final case class onResult(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef], replyTo: ActorRef[Response]) extends FraudPreventionSMEvent
    object onResult {
        def apply(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]) : onResult = {
            onResult(useCaseKey, payload, null)
        }
    }
    final case class onRetry(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef], replyTo: ActorRef[Response]) extends FraudPreventionSMEvent
    object onRetry {
        def apply(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]) : onRetry = {
            onRetry(useCaseKey, payload, null)
        }
    }
    final case class onStartFraudPreventionEvaluation(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef], replyTo: ActorRef[Response]) extends FraudPreventionSMEvent
    object onStartFraudPreventionEvaluation {
        def apply(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]) : onStartFraudPreventionEvaluation = {
            onStartFraudPreventionEvaluation(useCaseKey, payload, null)
        }
    }

    //Reporting
    sealed trait ReportEvent extends FraudPreventionSMEvent
    final case class onReport(useCaseKey: UseCaseKey, replyTo: ActorRef[Response])
        extends ReportEvent

    sealed trait Response
    final case class ReportResponse(state: State, payload: util.Map[String, AnyRef]) extends Response
    final case class AcknowledgeResponse() extends Response

    //internal protocol
    sealed trait InternalMessage extends FraudPreventionSMEvent
    final case class onAddFraudPreventionSMRelatedReference(useCaseKey: UseCaseKey, listing: Receptionist.Listing) extends InternalMessage with InternalBaseMessage

    sealed trait PersistEvent
    final case class FraudPreventionRetryPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class FraudPreventionErrorPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class FraudPreventionPersistEvemt(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class FraudPreventionReceivedPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
}

class FraudPreventionSM (
                    ctx: ActorContext[FraudPreventionSMEvent],
                    useCaseKey: String
                  ) extends BaseActor[FraudPreventionSMEvent, onAddFraudPreventionSMRelatedReference, PersistEvent, State] (
                                TypeCase[onAddFraudPreventionSMRelatedReference],
                                useCaseKey
                            ) {
    import FraudPreventionSM._

    private val listingAdapter: ActorRef[Receptionist.Listing] = ctx.messageAdapter {
        listing => onAddFraudPreventionSMRelatedReference(() => useCaseKey, listing)
    }

    ctx.system.receptionist ! Receptionist.Subscribe(
        ServiceKey[FraudPreventionSMEvent]("fraudpreventionsmService_" + useCaseKey),
        listingAdapter)

    def commandHandler(context: ActorContext[FraudPreventionSMEvent], cmd: FraudPreventionSMEvent, state: State): ReplyEffect[PersistEvent, State] =
        cmd match {
            case onReport(useCaseKey, replyTo) =>
                Effect.reply(replyTo)( ReportResponse(state, java.util.Collections.unmodifiableMap(state.controlObject)))

            case _ =>
                commandHandlerInternal(context, cmd, state)
    }

    def commandHandlerInternal(context: ActorContext[FraudPreventionSMEvent], cmd: FraudPreventionSMEvent, state: State): ReplyEffect[PersistEvent, State] =
        state match {
            //State Command Handler
            case INITIAL(controlObject) =>
                base[FraudPreventionSMEvent](cmd, state) {
                    case onStartFraudPreventionEvaluation(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing INITIAL onStartFraudPreventionEvaluation payload: {}", payload.toString)
                    
                        return SpringFraudPreventionSMActionsLocator.getInstance().fraudpreventionsm_INITIAL_$$_WAITING_RESULT_initialAction.doAction(
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
                base[FraudPreventionSMEvent](cmd, state) {
                    case onRetry(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing ERROR onRetry payload: {}", payload.toString)
                    
                        return SpringFraudPreventionSMActionsLocator.getInstance().fraudpreventionsm_ERROR_$$_WAITING_RESULT_error_onRetryAction.doAction(
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
                base[FraudPreventionSMEvent](cmd, state) {
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
            //State Command Handler
            case WAITING_RESULT(controlObject) =>
                base[FraudPreventionSMEvent](cmd, state) {
                    case onError(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing WAITING_RESULT onError payload: {}", payload.toString)
                    
                        return SpringFraudPreventionSMActionsLocator.getInstance().fraudpreventionsm_WAITING_RESULT_$$_ERROR_waitingResult_onErrorAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onResult(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing WAITING_RESULT onResult payload: {}", payload.toString)
                    
                        return SpringFraudPreventionSMActionsLocator.getInstance().fraudpreventionsm_WAITING_RESULT_$$_RESULTRECEIVED_waitingResult_onResultAction.doAction(
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

    def eventHandler(context: ActorContext[FraudPreventionSMEvent], state: State, event: PersistEvent): State = {
        state match {
            //1 Pseud0
            case _initial @ INITIAL(controlObject) =>
                event match {
                case FraudPreventionPersistEvemt(payload) =>
                    context.log.debug("Processing INITIAL FraudPreventionPersistEvemt payload: {}", payload)
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
                case FraudPreventionRetryPersistEvent(payload) =>
                    context.log.debug("Processing ERROR FraudPreventionRetryPersistEvent payload: {}", payload)
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
                case FraudPreventionErrorPersistEvent(payload) =>
                    context.log.debug("Processing WAITING_RESULT FraudPreventionErrorPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit4 from Master State
                    ERROR(_waiting_result.controlObject)
                case FraudPreventionReceivedPersistEvent(payload) =>
                    context.log.debug("Processing WAITING_RESULT FraudPreventionReceivedPersistEvent payload: {}", payload)
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

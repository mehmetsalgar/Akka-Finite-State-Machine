package org.salgar.fsm.pekko.foureyes.addresscheck

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.salgar.fsm.pekko.foureyes.addresscheck.AdressCheckSM._
import org.salgar.fsm.pekko.foureyes.addresscheck.actions.SpringAdressCheckSMActionsLocator
import org.salgar.pekko.fsm.api.UseCaseKey
import org.salgar.pekko.fsm.base.CborSerializable
import org.salgar.pekko.fsm.base.actors.BaseActor
import org.salgar.pekko.fsm.base.actors.BaseActor.InternalBaseMessage

import java.util

object AdressCheckSM {
    sealed trait State {
        private[addresscheck] def controlObject: util.Map[java.lang.String, AnyRef]
    }
    final case class INITIAL(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class ERROR(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class RESULTRECEIVED(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    final case class WAITING_RESULT(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable

    sealed trait AdressCheckSMEvent {
        def useCaseKey: UseCaseKey
    }
    final case class onError(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef], replyTo: ActorRef[Response]) extends AdressCheckSMEvent
    object onError {
        def apply(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]) : onError = {
            onError(useCaseKey, payload, null)
        }
    }
    final case class onResult(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef], replyTo: ActorRef[Response]) extends AdressCheckSMEvent
    object onResult {
        def apply(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]) : onResult = {
            onResult(useCaseKey, payload, null)
        }
    }
    final case class onRetry(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef], replyTo: ActorRef[Response]) extends AdressCheckSMEvent
    object onRetry {
        def apply(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]) : onRetry = {
            onRetry(useCaseKey, payload, null)
        }
    }
    final case class onStartAdressCheckResearch(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef], replyTo: ActorRef[Response]) extends AdressCheckSMEvent
    object onStartAdressCheckResearch {
        def apply(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]) : onStartAdressCheckResearch = {
            onStartAdressCheckResearch(useCaseKey, payload, null)
        }
    }

    //Reporting
    sealed trait ReportEvent extends AdressCheckSMEvent
    final case class onReport(useCaseKey: UseCaseKey, replyTo: ActorRef[Response])
        extends ReportEvent

    sealed trait Response
    final case class ReportResponse(state: State, payload: util.Map[String, AnyRef]) extends Response
    final case class AcknowledgeResponse() extends Response

    //internal protocol
    sealed trait InternalMessage extends AdressCheckSMEvent
    final case class onAddAdressCheckSMRelatedReference(useCaseKey: UseCaseKey, listing: Receptionist.Listing) extends InternalMessage with InternalBaseMessage

    sealed trait PersistEvent
    final case class AddressCheckPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class AddressCheckErrorPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class StartAdressCheckPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
    final case class AddressCheckRetryPersistEvent(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
}

class AdressCheckSM (
                    ctx: ActorContext[AdressCheckSMEvent],
                    useCaseKey: String
                  ) extends BaseActor[AdressCheckSMEvent, onAddAdressCheckSMRelatedReference, PersistEvent, State] (
                                TypeCase[onAddAdressCheckSMRelatedReference],
                                useCaseKey
                            ) {
    import AdressCheckSM._

    private val listingAdapter: ActorRef[Receptionist.Listing] = ctx.messageAdapter {
        listing => onAddAdressCheckSMRelatedReference(() => useCaseKey, listing)
    }

    ctx.system.receptionist ! Receptionist.Subscribe(
        ServiceKey[AdressCheckSMEvent]("adresschecksmService_" + useCaseKey),
        listingAdapter)

    def commandHandler(context: ActorContext[AdressCheckSMEvent], cmd: AdressCheckSMEvent, state: State): ReplyEffect[PersistEvent, State] =
        cmd match {
            case onReport(useCaseKey, replyTo) =>
                Effect.reply(replyTo)( ReportResponse(state, java.util.Collections.unmodifiableMap(state.controlObject)))

            case _ =>
                commandHandlerInternal(context, cmd, state)
    }

    def commandHandlerInternal(context: ActorContext[AdressCheckSMEvent], cmd: AdressCheckSMEvent, state: State): ReplyEffect[PersistEvent, State] =
        state match {
            //State Command Handler
            case INITIAL(controlObject) =>
                base[AdressCheckSMEvent](cmd, state) {
                    case onStartAdressCheckResearch(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing INITIAL onStartAdressCheckResearch payload: {}", payload.toString)
                    
                        return SpringAdressCheckSMActionsLocator.getInstance().adresschecksm_INITIAL_$$_WAITING_RESULT_initialAction.doAction(
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
                base[AdressCheckSMEvent](cmd, state) {
                    case onRetry(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing ERROR onRetry payload: {}", payload.toString)
                    
                        return SpringAdressCheckSMActionsLocator.getInstance().adresschecksm_ERROR_$$_WAITING_RESULT_error_onRetryAction.doAction(
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
                base[AdressCheckSMEvent](cmd, state) {
            
                    case _ =>
                        Effect.unhandled.thenNoReply()
                }
            //State Command Handler
            case WAITING_RESULT(controlObject) =>
                base[AdressCheckSMEvent](cmd, state) {
                    case onError(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing WAITING_RESULT onError payload: {}", payload.toString)
                    
                        return SpringAdressCheckSMActionsLocator.getInstance().adresschecksm_WAITING_RESULT_$$_ERROR_waiting_onErrorAction.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    
                        Effect.unhandled.thenNoReply()
                    }
                    case onResult(useCaseKey, payload, replyTo) => {
                        context.log.debug("Processing WAITING_RESULT onResult payload: {}", payload.toString)
                    
                        return SpringAdressCheckSMActionsLocator.getInstance().adresschecksm_WAITING_RESULT_$$_RESULTRECEIVED_waiting_onResultAction.doAction(
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

    def eventHandler(context: ActorContext[AdressCheckSMEvent], state: State, event: PersistEvent): State = {
        state match {
            //1 Pseud0
            case _initial @ INITIAL(controlObject) =>
                event match {
                case StartAdressCheckPersistEvent(payload) =>
                    context.log.debug("Processing INITIAL StartAdressCheckPersistEvent payload: {}", payload)
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
                case AddressCheckRetryPersistEvent(payload) =>
                    context.log.debug("Processing ERROR AddressCheckRetryPersistEvent payload: {}", payload)
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
                case AddressCheckErrorPersistEvent(payload) =>
                    context.log.debug("Processing WAITING_RESULT AddressCheckErrorPersistEvent payload: {}", payload)
                    controlObject.putAll(payload)
                    //Exit4 from Master State
                    ERROR(_waiting_result.controlObject)
                case AddressCheckPersistEvent(payload) =>
                    context.log.debug("Processing WAITING_RESULT AddressCheckPersistEvent payload: {}", payload)
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

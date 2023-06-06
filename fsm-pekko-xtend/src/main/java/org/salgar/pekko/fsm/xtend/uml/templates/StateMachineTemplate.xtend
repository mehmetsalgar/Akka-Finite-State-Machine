package org.salgar.pekko.fsm.xtend.uml.templates

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IGeneratorContext

import org.eclipse.uml2.uml.Pseudostate
import org.eclipse.uml2.uml.State
import org.eclipse.uml2.uml.Transition
import org.eclipse.uml2.uml.Trigger
import org.eclipse.uml2.uml.Vertex

import java.util.ArrayList
import java.util.List

import org.salgar.pekko.fsm.xtend.uml.templates.facade.StateMachineFacadeTemplate
import org.salgar.pekko.fsm.xtend.uml.templates.facade.SlaveStateMachineFacadeTemplate

class StateMachineTemplate extends AbstractGenerator {
	@Inject extension Naming
	@Inject extension GlobalVariableHelper
	@Inject extension StateMachineHelper
	@Inject extension SubStateMachineHelper
	@Inject ScalaActionsTemplate scalaActionsTemplate
	@Inject ScalaGuardsTemplate scalaGuardsTemplate
	@Inject GuardianTemplate guardianTemplate
	@Inject ServiceLocatorTemplate serviceLocatorTemplate
	@Inject SlaveGuardianTemplate slaveGuardianTemplate
	@Inject SnapshotTemplate snapshotTemplate
	@Inject SnapshotAdapterTemplate snapshotAdapterTemplate
	@Inject StateMachineFacadeTemplate stateMachineFacadeTemplate
	@Inject SlaveStateMachineFacadeTemplate slaveStateMachineFacadeTemplate

	override doGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) {
		input.allContents.filter(org.eclipse.uml2.uml.StateMachine).filter(s | s.isActive).forEach[
			val content = generate(context)
			fsa.generateFile(packagePath+"/"+name+".scala", content)
			scalaActionsTemplate.doGenerate(it, fsa, context)
			scalaGuardsTemplate.doGenerate(it, fsa, context)

			if(!isAbstract) {
			    guardianTemplate.doGenerate(it, fsa)
			    serviceLocatorTemplate.doGenerate(it, fsa)
			    stateMachineFacadeTemplate.doGenerate(it, fsa)
			} else {
			    slaveGuardianTemplate.doGenerate(it, fsa)
			    slaveStateMachineFacadeTemplate.doGenerate(it, fsa)
			}
			snapshotTemplate.doGenerate(it, fsa)
			snapshotAdapterTemplate.doGenerate(it, fsa, context)

			//stateMachineFacadeImplTemplate.doGenerate(it, fsa)
		]
	}

    def generate (org.eclipse.uml2.uml.StateMachine it, IGeneratorContext context) '''
        package «packageName»

        import java.util

        import com.fasterxml.jackson.databind.annotation.JsonDeserialize
        import org.apache.pekko.actor.typed.ActorRef
        import org.apache.pekko.actor.typed.receptionist.{Receptionist, ServiceKey}
        import org.apache.pekko.actor.typed.scaladsl.ActorContext
        import org.apache.pekko.persistence.typed.scaladsl.{Effect, ReplyEffect}
        import org.salgar.pekko.fsm.api.UseCaseKey
        import org.salgar.pekko.fsm.base.CborSerializable
        import org.salgar.pekko.fsm.base.actors.BaseActor
        import org.salgar.pekko.fsm.base.actors.BaseActor.InternalBaseMessage
        import «packageName».«name»._
        import «packageName».actions.Spring«name»ActionsLocator
        import «packageName».guards.Spring«name»GuardsLocator
        import shapeless.TypeCase

        «IF (getOwnedComments !== null) && (!getOwnedComments.isEmpty)»
            /**
                «FOR comment: getOwnedComments»
                    «comment.body»
                «ENDFOR»
            */
        «ENDIF»
        object «name» {
            sealed trait State {
                private[«nearestPackageName»] def controlObject: util.Map[java.lang.String, AnyRef]
            }
            «FOR state : allOwnedElements().filter(Pseudostate).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])»
                final case class «state.name.toUpperCase()»(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
            «ENDFOR»
            «FOR state : allOwnedElements().filter(State).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])»
                «subStateRecursive(state, new ArrayList<Vertex>, context)»
            «ENDFOR»

            sealed trait «name»Event {
                def useCaseKey: UseCaseKey
            }
            «FOR trigger : giveTransitionWithTrigger(allOwnedElements().filter(Transition)).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])»
                final case class «trigger.name»(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef], replyTo: ActorRef[Response]) extends «name»Event
                object «trigger.name» {
                    def apply(useCaseKey: UseCaseKey, payload: util.Map[String, AnyRef]) : «trigger.name» = {
                        «trigger.name»(useCaseKey, payload, null)
                    }
                }
            «ENDFOR»

            //Reporting
            sealed trait ReportEvent extends «name»Event
            final case class onReport(useCaseKey: UseCaseKey, replyTo: ActorRef[Response])
                extends ReportEvent

            sealed trait Response
            final case class ReportResponse(state: State, payload: util.Map[String, AnyRef]) extends Response
            final case class AcknowledgeResponse() extends Response

            //internal protocol
            sealed trait InternalMessage extends «name»Event
            final case class onAdd«name»RelatedReference(useCaseKey: UseCaseKey, listing: Receptionist.Listing) extends InternalMessage with InternalBaseMessage

            sealed trait PersistEvent
            «FOR event : findSubMachinePersistEventsRecursive»
                final case class «event.name»(@JsonDeserialize(as = classOf[util.Map[String, AnyRef]]) payload: util.Map[String, AnyRef]) extends PersistEvent with CborSerializable
            «ENDFOR»
        }

        class «name» (
                            ctx: ActorContext[«name»Event],
                            useCaseKey: String
                          ) extends BaseActor[«name»Event, onAdd«name»RelatedReference, PersistEvent, State] (
                                        TypeCase[onAdd«name»RelatedReference],
                                        useCaseKey
                                    ) {
            import «name»._

            private val listingAdapter: ActorRef[Receptionist.Listing] = ctx.messageAdapter {
                listing => onAdd«name»RelatedReference(() => useCaseKey, listing)
            }

            ctx.system.receptionist ! Receptionist.Subscribe(
                ServiceKey[«name»Event]("«name.toLowerCase()»Service_" + useCaseKey),
                listingAdapter)

            def commandHandler(context: ActorContext[«name»Event], cmd: «name»Event, state: State): ReplyEffect[PersistEvent, State] =
                cmd match {
                    case onReport(useCaseKey, replyTo) =>
                        Effect.reply(replyTo)( ReportResponse(state, java.util.Collections.unmodifiableMap(state.controlObject)))

                    case _ =>
                        commandHandlerInternal(context, cmd, state)
            }

            def commandHandlerInternal(context: ActorContext[«name»Event], cmd: «name»Event, state: State): ReplyEffect[PersistEvent, State] =
                state match {
                    «FOR pseudostate : allOwnedElements().filter(Pseudostate)»
                        «stateCommandHandler(
                            pseudostate,
                            pseudostate.getOutgoings(),
                            new ArrayList<State>,
                            context)»
                    «ENDFOR»
                    «FOR state : allOwnedElements().filter(State).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])»
                        «stateCommandHandlerRecursive(
                            state,
                            new ArrayList<Transition>,
                            new ArrayList<State>,
                            context)»
                    «ENDFOR»
                        case _ =>
                            Effect.unhandled.thenNoReply()
                }

            def eventHandler(context: ActorContext[«name»Event], state: State, event: PersistEvent): State = {
                state match {
                    //1 Pseud0
                    «FOR state : allOwnedElements().filter(Pseudostate)»
                        «state.stateEventHandlerRecursive(new ArrayList<Transition>, new ArrayList<Vertex>, context)»
                    «ENDFOR»

                    //1 Recursive
                    «FOR state : allOwnedElements().filter(State).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])»
                        «state.stateEventHandlerRecursive(new ArrayList<Transition>, new ArrayList<Vertex>, context)»
                    «ENDFOR»
                }
            }
        }
    '''

    def String subStateRecursive (Vertex state, List<Vertex> parentStates, IGeneratorContext context) '''
        «IF (state instanceof State)»
            «IF (state.getSubmachine() !== null)»
                «val parentStatesList = addStateToStateList(parentStates, state)»
                «FOR subPseudoState : state.getSubmachine().allOwnedElements().filter(Pseudostate)»
                    «subStateRecursive(subPseudoState, createRecursiveStateList(parentStatesList), context)»
                «ENDFOR»
                «FOR subState : state.getSubmachine().allOwnedElements().filter(State).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])»
                    «subStateRecursive(subState, createRecursiveStateList(parentStatesList), context)»
                «ENDFOR»
            «ELSE»
                «subState(state, parentStates, context)»
            «ENDIF»
        «ELSE»
            «subState(state, parentStates, context)»
        «ENDIF»
    '''

    def String subState (Vertex state, List<Vertex> parentStates, IGeneratorContext context) '''
        final case class «FOR parentState : parentStates»«parentState.name.toUpperCase()»«context.getGlobalVariable('submachineSeperator')»«ENDFOR»«state.name.toUpperCase()»(controlObject: util.Map[java.lang.String, AnyRef]) extends State with CborSerializable
    '''

    def String stateCommandHandlerRecursive (
        org.eclipse.uml2.uml.StateMachine it,
        Vertex state,
        List<Transition> outgoings,
        List<State> parentStates,
        IGeneratorContext context) '''
        «IF (state instanceof State)»
            «IF (state.getSubmachine() !== null)»
                «val parentStatesList = addStateToStateList(parentStates, state)»
                «FOR subPseudostate : state.getSubmachine().allOwnedElements().filter(Pseudostate).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])»
                    «stateCommandHandlerRecursive(
                        subPseudostate,
                        createRecursiveStateList(outgoings, state.getOutgoings()),
                        createRecursiveStateList(parentStatesList),
                        context)»
                «ENDFOR»
                «FOR subState : state.getSubmachine().allOwnedElements().filter(State).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])»
                    «stateCommandHandlerRecursive(
                        subState,
                        createRecursiveStateList(outgoings, state.getOutgoings()),
                        createRecursiveStateList(parentStatesList),
                        context)»
                «ENDFOR»
            «ELSE»
                «stateCommandHandler(state, createRecursiveStateList(outgoings, state.getOutgoings()), parentStates, context)»
            «ENDIF»
        «ELSE»
            «stateCommandHandler(state, createRecursiveStateList(outgoings, state.getOutgoings()), parentStates, context)»
        «ENDIF»
    '''

    def String stateCommandHandler (
        org.eclipse.uml2.uml.StateMachine it,
        Vertex state,
        List<Transition> outgoings,
        List<State> parentStates,
        IGeneratorContext context) '''
        //State Command Handler
        case «FOR parentState : parentStates»«parentState.name.toUpperCase()»«context.getGlobalVariable('submachineSeperator')»«ENDFOR»«state.name.toUpperCase()»(controlObject) =>
            base[«name»Event](cmd, state) {
                «triggerCommandHandler(
                    state,
                    outgoings,
                    giveTransitionWithTriggerForCase(outgoings.reverseView).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())]),
                    parentStates,
                    context)»

                case _ =>
                    Effect.unhandled.thenNoReply()
            }
    '''

    def String triggerCommandHandler (
        org.eclipse.uml2.uml.StateMachine it,
        Vertex state,
        List<Transition> outgoings,
        List<Trigger> triggers,
        List<State> parentStates,
        IGeneratorContext context) '''
        «FOR trigger : triggers»
            case «trigger.name»(useCaseKey, payload, replyTo) => {
                context.log.debug("Processing «FOR parentState : parentStates»«parentState.name.toUpperCase()»«context.getGlobalVariable('submachineSeperator')»«ENDFOR»«state.name.toUpperCase()» «trigger.name» payload: {}", payload.toString)
                «val transitionsForTriggerList = giveTransitionsForTrigger(outgoings.reverseView, trigger)»
                «FOR transition : transitionsForTriggerList»
                    «IF (transition.guard !== null) && transitionsForTriggerList.get(0).guard !== null»
                        «IF (transitionsForTriggerList.get(0) != transition)»else «ENDIF»if(Spring«name»GuardsLocator.getInstance.«name.toLowerCase()»_«transition.source.name.toUpperCase()»«context.getGlobalVariable('targetSourceStateSeperator')»«transition.target.name.toUpperCase()»_«transition.triggers.head.name»_«transition.guard.name».evaluate(
                            context,
                            controlObject,
                            payload)) {
                    «ENDIF»

                    «IF (transitionsForTriggerList.get(0) == transition)»
                        return Spring«name»ActionsLocator.getInstance().«name.toLowerCase()»_«transition.source.name.toUpperCase()»«context.getGlobalVariable('targetSourceStateSeperator')»«transition.target.name.toUpperCase()»_«transition.name»Action.doAction(
                                        context,
                                        controlObject,
                                        payload, replyTo)
                    «ELSE»
                        «IF (transitionsForTriggerList.get(0).guard !== null)»
                            return Spring«name»ActionsLocator.getInstance().«name.toLowerCase()»_«transition.source.name.toUpperCase()»«context.getGlobalVariable('targetSourceStateSeperator')»«transition.target.name.toUpperCase()»_«transition.name»Action.doAction(
                                context,
                                controlObject,
                                payload, replyTo)
                        «ELSE»
                            //There are multiple transitions for this Trigger: «trigger.name» - «transition.source.name.toUpperCase()» - «transition.target.name.toUpperCase()» - «transition.name» and no guard conditions. Are you sure there a no design mistakes in your Submachines
                        «ENDIF»
                    «ENDIF»
                    «IF (transition.guard !== null) && transitionsForTriggerList.get(0).guard !== null»
                        }
                    «ENDIF»
                «ENDFOR»

                Effect.unhandled.thenNoReply()
            }
        «ENDFOR»
    '''

    def dispatch String stateEventHandlerRecursive (
        Pseudostate state,
        List<Transition> outgoings,
        List<Vertex> parentStates,
        IGeneratorContext context) '''
        «stateEventHandler(state, createRecursiveStateList(outgoings, state.getOutgoings()), parentStates, context)»
    '''

    def dispatch String stateEventHandlerRecursive (
            State state,
            List<Transition> outgoings,
            List<Vertex> parentStates,
            IGeneratorContext context) '''
            «IF state.getSubmachine() !== null»
                «val parentStatesList = addStateToStateList(parentStates, state)»
                «FOR pseudostate : state.getSubmachine().allOwnedElements().filter(Pseudostate).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])»
                    //pseudo
                    «stateEventHandler(pseudostate, createRecursiveStateList(createRecursiveStateList(outgoings, pseudostate.getOutgoings()),state.getOutgoings()), parentStates, context)»
                «ENDFOR»
                «FOR subState : state.getSubmachine().allOwnedElements().filter(State).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])»
                    //recursive
                    «stateEventHandlerRecursive(subState, createRecursiveStateList(outgoings, state.getOutgoings()), createRecursiveStateList(parentStatesList), context)»
                «ENDFOR»
            «ELSE»
                «stateEventHandler(state, createRecursiveStateList(outgoings, state.getOutgoings()), parentStates, context)»
            «ENDIF»
        '''

    def String stateEventHandler (
        Vertex state,
        List<Transition> outgoings,
        List<Vertex> parentStates,
        IGeneratorContext context) '''
        case «FOR parentState : parentStates»_«parentState.name.toLowerCase()»«ENDFOR»_«state.name.toLowerCase()» @ «FOR parentState : parentStates»«parentState.name.toUpperCase()»«context.getGlobalVariable('submachineSeperator')»«ENDFOR»«state.name.toUpperCase()»(controlObject) =>
            event match {
            «eventEventHandler(state, outgoings, parentStates, context)»

            case unh @ _ => {
                context.log.warn("This PersistEvent is not handled during «FOR parentState : parentStates»«parentState.name.toUpperCase()»«context.getGlobalVariable('submachineSeperator')»«ENDFOR»«state.name.toUpperCase()» State {}", unh)
                «FOR parentState : parentStates»_«parentState.name.toLowerCase()»«ENDFOR»_«state.name.toLowerCase()»
              }
        }
    '''

    def String eventEventHandler (
        Vertex state,
        List<Transition> outgoings,
        List<Vertex> parentStates,
        IGeneratorContext context) '''
        «FOR transition : giveTransitionsWithUniqueEvent(outgoings).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])»
            «IF (transition.triggers.get(0).event !== null)»
                case «transition.triggers.get(0).event.name»(payload) =>
                    context.log.debug("Processing «FOR parentState : parentStates»«parentState.name.toUpperCase()»«context.getGlobalVariable('submachineSeperator')»«ENDFOR»«state.name.toUpperCase()» «transition.triggers.get(0).event.name» payload: {}", payload)
                    controlObject.putAll(payload)
                    «IF (transition.target != transition.source)»
                        «IF parentStates.contains(transition.source) && state!=transition.source»
                            //Exit1 from Master State
                            «FOR parentState : transition.source.giveParentStatesForMasterState(parentStates)»«parentState.name.toUpperCase()»«context.getGlobalVariable('submachineSeperator')»«ENDFOR»«transition.target.name.toUpperCase()»«transition.target.getFirstPseudoStateOrAnonymousTransition(context)»(«FOR parentState : parentStates»_«parentState.name.toLowerCase()»«ENDFOR»_«state.name.toLowerCase()».controlObject)
                        «ELSEIF transition.source.isSubmachineState && !transition.target.isSubmachineState»
                            //Exit2 from Master State
                            «FOR parentState : removeLastSubmachineState(parentStates)»«parentState.name.toUpperCase()»«context.getGlobalVariable('submachineSeperator')»«ENDFOR»«transition.target.name.toUpperCase()»(«FOR parentState : parentStates»_«parentState.name.toLowerCase()»«ENDFOR»_«state.name.toLowerCase()».controlObject)
                        «ELSEIF transition.source.isSubmachineState && transition.target.isSubmachineState»
                            //Exit3 from Master State
                            «FOR parentState : removeLastSubmachineState(parentStates)»«parentState.name.toUpperCase()»«context.getGlobalVariable('submachineSeperator')»«ENDFOR»«transition.target.name.toUpperCase()»«transition.target.getFirstPseudoState(context)»(«FOR parentState : parentStates»_«parentState.name.toLowerCase()»«ENDFOR»_«state.name.toLowerCase()».controlObject)
                        «ELSEIF transition.source instanceof Pseudostate && transition.target.isFirstPseudoStateOrAnonymousTransitionState(context)»
                            //Exit5 from Master State
                            «FOR parentState : parentStates»«parentState.name.toUpperCase()»«context.getGlobalVariable('submachineSeperator')»«ENDFOR»«transition.target.name.toUpperCase()»«transition.target.getFirstPseudoStateOrAnonymousTransition(context)»(«FOR parentState : parentStates»_«parentState.name.toLowerCase()»«ENDFOR»_«state.name.toLowerCase()».controlObject)
                        «ELSE»
                            //Exit4 from Master State
                            «FOR parentState : parentStates»«parentState.name.toUpperCase()»«context.getGlobalVariable('submachineSeperator')»«ENDFOR»«transition.target.name.toUpperCase()»«transition.target.getFirstPseudoStateOrAnonymousTransition(context)»(«FOR parentState : parentStates»_«parentState.name.toLowerCase()»«ENDFOR»_«state.name.toLowerCase()».controlObject)
                        «ENDIF»
                    «ELSE»
                        //Exit6
                        «FOR parentState : parentStates»_«parentState.name.toLowerCase()»«ENDFOR»_«state.name.toLowerCase()»
                    «ENDIF»
            «ENDIF»
        «ENDFOR»
    '''
}
package org.salgar.pekko.fsm.xtend.uml.templates

import org.eclipse.xtext.generator.IGeneratorContext

import org.eclipse.uml2.uml.Pseudostate
import org.eclipse.uml2.uml.State
import org.eclipse.uml2.uml.Transition
import org.eclipse.uml2.uml.Trigger
import org.eclipse.uml2.uml.Vertex

import java.util.ArrayList
import java.util.Collection
import java.util.HashMap
import java.util.List

import javax.inject.Inject

class SubStateMachineHelper {
    @Inject extension GlobalVariableHelper

    def Collection<Transition> giveTransitionsRecursive(Iterable<Pseudostate> pseudostates, Iterable<State> states) {
        val result = new HashMap<Transition, Transition>

        for (Pseudostate state : pseudostates) {
            state.getOutgoings().forEach[transition, index | result.put(transition, transition)]
        }

        for (State state : states) {
            state.getOutgoings().forEach[transition, index | result.put(transition, transition)]
            if(state.getSubmachine() !== null) {
                giveTransitionsRecursive(
                    state.getSubmachine().allOwnedElements().filter(Pseudostate),
                    state.getSubmachine().allOwnedElements().filter(State).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())]))
                    .forEach[transition, index | result.put(transition, transition)]
            }
        }

        return result.values
    }

    def dispatch boolean isSubmachineState(State state) {
        return state.isSubmachineState
    }

    def dispatch boolean isSubmachineState(Pseudostate pseudostate) {
        return false;
    }

    def dispatch getFirstPseudoStateOrAnonymousTransition(State state, IGeneratorContext context) {
        if(state.getSubmachine()!==null) {
            val Pseudostate pseudoState = state.getSubmachine().allOwnedElements().filter(Pseudostate).head
            for(Transition tr: pseudoState.outgoings) {
                if(tr.getTriggers.empty) {
                    if(isSubmachineState(tr.target)) {
                        val result = getFirstPseudoStateOrAnonymousTransition(tr.target, context)
                        return context.getGlobalVariable('submachineSeperator') + tr.target.name.toUpperCase + result
                    } else {
                        return context.getGlobalVariable('submachineSeperator') + tr.target.name.toUpperCase
                    }
                }
            }
            return context.getGlobalVariable('submachineSeperator') + state.getSubmachine().allOwnedElements().filter(Pseudostate).head.name.toUpperCase
        }
    }

    def dispatch getFirstPseudoStateOrAnonymousTransition(Pseudostate pseudostate, IGeneratorContext context) {
    }

    def dispatch Vertex getFirstPseudoStateOrAnonymousTransitionState(State state, IGeneratorContext context) {
        if(state.getSubmachine()!==null) {
            val Pseudostate pseudoState = state.getSubmachine().allOwnedElements().filter(Pseudostate).head
            for(Transition tr: pseudoState.outgoings) {
                if(tr.getTriggers.empty) {
                    if(isSubmachineState(tr.target)) {
                        return getFirstPseudoStateOrAnonymousTransitionState(tr.target, context)
                    } else {
                        return tr.target
                    }
                }
            }
            return state.getSubmachine().allOwnedElements().filter(Pseudostate).head
        }
    }

    def dispatch Vertex getFirstPseudoStateOrAnonymousTransitionState(Pseudostate pseudoState, IGeneratorContext context) {
        for(Transition tr: pseudoState.outgoings) {
            if(tr.getTriggers.empty) {
                return tr.target
            }
        }
    }

    def dispatch boolean isFirstPseudoStateOrAnonymousTransitionState(State state, IGeneratorContext context) {
        if(state.getSubmachine()!==null) {
            val Pseudostate pseudoState = state.getSubmachine().allOwnedElements().filter(Pseudostate).head
            for(Transition tr: pseudoState.outgoings) {
                if(tr.getTriggers.empty) {
                    if(isSubmachineState(tr.target)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    def dispatch boolean isFirstPseudoStateOrAnonymousTransitionState(Pseudostate pseudoState, IGeneratorContext context) {
        return false
    }

    def dispatch getFirstPseudoState(State state, IGeneratorContext context) {
        if(state.getSubmachine()!==null) {
            return context.getGlobalVariable('submachineSeperator') + state.getSubmachine().allOwnedElements().filter(Pseudostate).head.name.toUpperCase
        }
    }

    def dispatch getFirstPseudoState(Pseudostate pseudostate, IGeneratorContext context) {
    }

    def dispatch giveParentStatesForMasterState(State state, List<Vertex> parentStates) {
        val List<Vertex> masterStates = new ArrayList
        for(Vertex parentState : parentStates) {
            if(parentState!==state) {
                masterStates.add(parentState)
            } else {
                return masterStates
            }
        }
    }

    def dispatch giveParentStatesForMasterState(Pseudostate Pseudostate, List<Vertex> parentStates) {
        return parentStates
    }

    def Collection<Transition> giveTransitionsWithUniqueEvent(List<Transition> transitions) {
        val result = new HashMap<String, Transition>()

        for (Transition transition : transitions) {
            if (transition.getTriggers() !== null && !transition.getTriggers().isEmpty()) {
                for (Trigger trigger : transition.getTriggers()) {
                    if(trigger.getEvent() !== null) {
                        result.put(trigger.getEvent().getName() + "_" + transition.getTarget().getName(), transition)
                    }
                }
            }
        }

        return result.values()
    }

    def <T> createRecursiveStateList(List<T> newElements) {
        val recursiveList = new ArrayList<T>()
        recursiveList.addAll(newElements)

        return recursiveList
    }

    def <T> createRecursiveStateList(List<T> oldElements, List<T> newElements) {
        val recursiveList = new ArrayList<T>()
        recursiveList.addAll(oldElements)
        recursiveList.addAll(newElements)

        return recursiveList
    }
}
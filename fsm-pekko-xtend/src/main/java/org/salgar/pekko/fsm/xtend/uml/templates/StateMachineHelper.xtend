package org.salgar.pekko.fsm.xtend.uml.templates

import org.eclipse.uml2.uml.Event
import org.eclipse.uml2.uml.Pseudostate
import org.eclipse.uml2.uml.SignalEvent
import org.eclipse.uml2.uml.State
import org.eclipse.uml2.uml.Trigger
import org.eclipse.uml2.uml.Transition
import org.eclipse.uml2.uml.Vertex

import java.util.ArrayList
import java.util.HashMap
import java.util.List
import java.util.Map
import java.util.Collection

class StateMachineHelper {
    def Collection<Trigger> giveTransitionWithTrigger(Iterable<Transition> transitions) {
        val result = giveTransitionWithTriggerRecursive(transitions)

        return result.values()
    }

    private def Map<String, Trigger> giveTransitionWithTriggerRecursive(Iterable<Transition> transitions) {
        val result = new HashMap<String, Trigger>
        for (Transition transition : transitions) {
            if (transition.getTriggers() !== null && !transition.getTriggers().isEmpty()) {
                for (Trigger trigger : transition.getTriggers()) {
                    result.put(trigger.getName(), trigger)
                }
             }
            val state = transition.getTarget() as State
            if(state.getSubmachine() !== null) {
                result.putAll(
                    giveTransitionWithTriggerRecursive(
                        state
                            .getSubmachine()
                            .allOwnedElements()
                            .filter(Transition)
                            .sortWith([o1, o2 | (o1.getName() !== null && o2.getName() !== null)? o1.getName().compareTo(o2.getName()):throw new IllegalArgumentException("Transition must have a name: first Transition: " + o1.toString() + " second Transition: " + o2.toString() + " ")])
                    )
                )
            }
        }
        result
    }

    def Collection<Trigger> giveTransitionWithTriggerForCase(Iterable<Transition> transitions) {
        val result = new HashMap<String, Trigger>
        for (Transition transition : transitions) {
            if (transition.getTriggers() !== null && !transition.getTriggers().isEmpty()) {
                for (Trigger trigger : transition.getTriggers()) {
                    result.put(trigger.getName(), trigger)
                }
            }
        }

        return result.values()
    }

    def Collection<Trigger> giveSubTransitionWithTriggerForCase(Iterable<Transition> transitions) {
        val result = giveSubTransitionWithTriggerForCaseRecursive(transitions)

        return result.values()
    }

    private def Map<String, Trigger> giveSubTransitionWithTriggerForCaseRecursive(Iterable<Transition> transitions) {
        val result = new HashMap<String, Trigger>
        for (Transition transition : transitions) {
            if (transition.getTriggers() !== null && !transition.getTriggers().isEmpty()) {
                for (Trigger trigger : transition.getTriggers()) {
                    result.put(trigger.getName(), trigger)
                }
             }
            val state = transition.getTarget() as State
            if(state.getSubmachine() !== null) {
                result.putAll(
                    giveSubTransitionWithTriggerForCaseRecursive(
                        state.getSubmachine().allOwnedElements().filter(Transition).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])
                    )
                )
            }
        }

        return result
    }

    def Collection<Event> giveTransitionEvents(Iterable<Transition> transitions) {
        val result = new HashMap<String, Event>()

        for (Transition transition : transitions) {
            if (transition.getTriggers() !== null && !transition.getTriggers().isEmpty()) {
                for (Trigger trigger : transition.getTriggers()) {
                    if(trigger.getEvent() !== null) {
                        result.put(trigger.getEvent().getName(), trigger.getEvent())
                    }
                }
            }
        }

        return result.values()
    }

    def Collection<Transition> giveTransitionsForTrigger(List<Transition> transitions, Trigger trigger) {
        val result = new ArrayList<Transition>();

        for (Transition transition : transitions) {
            if (transition.getTriggers() !== null && !transition.getTriggers().isEmpty()) {
                for (Trigger internTrigger : transition.getTriggers()) {
                    if(internTrigger.getName().equals(trigger.getName())) {
                        result.add(transition)
                    }
                }
            }
        }

        return result
    }

    def Collection<SignalEvent> giveSnapshotEventsCollection(org.eclipse.uml2.uml.StateMachine it) {
        val results = giveSnapshotEvents

        return results.values()
    }

    private def Map<String, SignalEvent> giveSnapshotEvents(org.eclipse.uml2.uml.StateMachine it) {
        val results = new HashMap<String, SignalEvent>()
        for(Pseudostate pseudostate : allOwnedElements.filter(Pseudostate).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])) {
            for(Transition transition : pseudostate.getOutgoings()) {
                if(!transition.triggers.empty) {
                    processSignalEvent(results, transition.triggers.get(0))
                }
            }
        }
        for(State state : allOwnedElements.filter(State).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])) {
            for(Transition transition : state.getOutgoings()) {
                if(!transition.triggers.empty) {
                    processSignalEvent(results, transition.triggers.get(0))
                }
            }
            if(state.getSubmachine()!==null) {
                results.putAll(state.getSubmachine().giveSnapshotEvents)
            }
        }

        return results
     }

     private def processSignalEvent(Map<String, SignalEvent> results, Trigger trigger) {
        if(trigger.event !== null) {
            if(trigger.event instanceof SignalEvent) {
                val signalEvent = trigger.event as SignalEvent
                if(signalEvent.signal !== null) {
                    results.put(signalEvent.getName(), signalEvent)
                }
            }
        }
     }

    def <T> addStateToStateList(List<T> existing, T newElement) {
        existing.add(newElement)

        return existing
    }

    def <T> addStateToStateList(List<T> existing, List<T> newElement) {
        existing.addAll(newElement)

        return existing
    }

    def Collection<Vertex> removeLastSubmachineState(List<Vertex> states) {
        if(states !== null && !states.isEmpty) {
            val List<Vertex> results = new ArrayList<Vertex>

            for(var i = 0; i < states.length-1; i++) {
                results.add(states.get(i))
            }

            return results
        }
        return states
    }

    def void findSubMachinePersistEventsRecursiveV1(
        org.eclipse.uml2.uml.StateMachine it,
        Map<String, Event> eventsMap) {
        for(signalEvent : nearestPackage.allOwnedElements().filter(SignalEvent).sortWith([o1, o2 | (o1.getName() !== null && o2.getName() !== null) ? o1.getName().compareTo(o2.getName()):throw new IllegalArgumentException("Transition must have a name: first Transition: " + o1.toString() + " second Transition: " + o2.toString() + " ")])) {
            eventsMap.put(signalEvent.name, signalEvent)
        }
    }

    def Collection<Event> findSubMachinePersistEventsRecursive(org.eclipse.uml2.uml.StateMachine it) {
            val map = new HashMap<String, Event>
            for(state : allOwnedElements().filter(Pseudostate).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])) {
                for(event : giveTransitionEvents(state.getOutgoings()).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])) {
                    map.put(event.name, event)
                }
            }
            for(state : allOwnedElements().filter(State).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])) {
                for(event : giveTransitionEvents(state.getOutgoings()).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])) {
                    map.put(event.name, event)
                }
                if(state.getSubmachine() !== null) {
                    findSubMachinePersistEventsRecursive(
                        state.getSubmachine()).forEach[element, i | map.put(element.name, element)]
                }
            }
            findSubMachinePersistEventsRecursiveV1(it, map)

            return map.values()
        }

    def String renderInitialState(org.eclipse.uml2.uml.StateMachine it) '''
        «val Collection<Vertex> result = findInitialState(it)»

        «IF result !== null»
            «FOR Vertex intialState : result SEPARATOR '_'»«intialState.name.toUpperCase()»«ENDFOR»
        «ENDIF»
    '''

    def Collection<Vertex> findInitialState(org.eclipse.uml2.uml.StateMachine it) {
        val Collection<Vertex> result = new ArrayList
        for(Pseudostate pseudostate : allOwnedElements().filter(Pseudostate)) {
            for(Transition transition : pseudostate.getOutgoings()) {
                if(transition.triggers !== null && transition.triggers.empty) {
                    result.add(transition.target)
                    if(transition.target instanceof State) {
                        val State state = transition.target as State
                        if(state.submachine !== null) {
                            val Collection<Vertex> resultRec = findInitialState(state.submachine)
                            if(resultRec !== null) {
                                result.addAll(resultRec)
                            }
                        }
                    }
                } else if(transition.triggers === null) {
                    result.add(transition.target)
                    if(transition.target instanceof State) {
                        val State state = transition.target as State
                        if(state.submachine !== null) {
                            val Collection<Vertex> resultRec = findInitialState(state.submachine)
                            if(resultRec !== null) {
                                result.addAll(resultRec)
                            }
                        }
                    }
                } else {
                    result.add(pseudostate)
                }
            }
        }
        return result
    }
}
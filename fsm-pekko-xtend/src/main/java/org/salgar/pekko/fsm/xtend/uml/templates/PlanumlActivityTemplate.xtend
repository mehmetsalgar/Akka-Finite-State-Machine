package org.salgar.pekko.fsm.xtend.uml.templates

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess

import org.eclipse.uml2.uml.Transition
import org.eclipse.uml2.uml.Vertex

import org.eclipse.emf.common.util.EList
import java.util.Map
import java.util.HashMap

class PlanumlActivityTemplate {
	@Inject AsciidocTemplate asciidocTemplate

	def doGenerate(Resource input, IFileSystemAccess fsa) {
	    asciidocTemplate.doGenerate(input,fsa)

	    for(it : input.allContents.toIterable.filter(org.eclipse.uml2.uml.StateMachine)) {
	        for(Transition transition : allOwnedElements.filter(org.eclipse.uml2.uml.Transition)) {
	            val content = generate(transition)
	            if(transition.guard === null) {
                    fsa.generateFile("/activity/" + name.toLowerCase() + "/" + transition.name + ".puml", content)
                } else {
                    fsa.generateFile("/activity/" + name.toLowerCase() + "/" + transition.name + "_" + transition.guard.name + ".puml", content)
                }
            }
        }
	}

    def generate(org.eclipse.uml2.uml.StateMachine it, Transition actualTransition) '''
        @startuml
        start
            «val initial = allOwnedElements.filter(org.eclipse.uml2.uml.Pseudostate).iterator.next»
               «generateTransitions(initial.outgoings, actualTransition, new HashMap)»
            else (false)
                :unhandled;
            endif;
        @enduml
    '''

    def String generateTransitions(EList<Transition> transitions, Transition actualTransition, Map<String, Vertex> processedStates) '''
        «FOR transition : transitions SEPARATOR '\nelse (false)'»
            if(Command '«transition.name»'«IF transition.guard!==null» AND '«transition.guard.name»' is true«ENDIF») then (true)
                «IF transition === actualTransition»#Red«ENDIF»:Process '«transition.name»';
                «IF transition.source !== transition.target»:Switch to State «transition.target.name»;«ENDIF»
                «IF transition.target instanceof org.eclipse.uml2.uml.FinalState»stop«ENDIF»
                «IF transition.target.outgoings!==null && !transition.target.outgoings.isEmpty»
                    «IF processedStates.get(transition.target.name)===null»
                        «handleState(transition.target, processedStates)»
                        «generateTransitions(transition.target.outgoings, actualTransition, processedStates)»
                    «ENDIF»
                «ENDIF»
        «ENDFOR»
    '''

    def String handleState(Vertex state, Map<String, Vertex> processedStates) {
        processedStates.put(state.name, state)
        return ''
    }
}
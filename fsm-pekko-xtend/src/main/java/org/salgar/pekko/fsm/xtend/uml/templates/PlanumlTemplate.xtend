package org.salgar.pekko.fsm.xtend.uml.templates

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess

import org.eclipse.uml2.uml.State

class PlanumlTemplate {
    def doGenerate(Resource input, IFileSystemAccess fsa) {
	    for(sm : input.allContents.toIterable.filter(org.eclipse.uml2.uml.StateMachine)) {
            val content = generate(sm)
            fsa.generateFile("/"+sm.name.toLowerCase()+"/"+sm.name+".puml", content)
        }
	}

    def generate (org.eclipse.uml2.uml.StateMachine it) '''
        @startuml
            «FOR state : allOwnedElements().filter(State)»
                («name») --> («state.name»)
                «FOR transition : state.getOutgoings()»
                    («state.name») ---> («transition.name»)
                    «IF (transition.guard !== null)»
                        «IF (transition.guard.getOwnedComments !== null && !transition.guard.getOwnedComments.isEmpty)»
                            note bottom of («transition.name»)
                                «FOR comment: transition.guard.getOwnedComments»
                                    «comment.body»
                                «ENDFOR»
                            end note
                        «ENDIF»
                    «ENDIF»
               «ENDFOR»
            «ENDFOR»
        @enduml
    '''
}
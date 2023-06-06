package org.salgar.pekko.fsm.xtend.uml.templates

import org.eclipse.xtext.generator.IFileSystemAccess

import org.eclipse.uml2.uml.State

class AsciidocsUsecasesTemplate {
    def generate(org.eclipse.uml2.uml.StateMachine it, IFileSystemAccess fsa) {
        for(state: allOwnedElements.filter(State)) {
            val content = generate(state)
            fsa.generateFile("/"+name.toLowerCase()+"/usecases/"+state.name+".adoc", content)
        }
    }

    def generate (org.eclipse.uml2.uml.StateMachine it, State state) '''
        = Use Case : «state.name»

        «FOR comment: state.getOwnedComments»
            «comment.body»
        «ENDFOR»

        «FOR transition : state.getOutgoings()»

            //«transition.name»

            include::«state.name»_«transition.name».adoc[leveloffset=+1]

         «ENDFOR»
    '''
}
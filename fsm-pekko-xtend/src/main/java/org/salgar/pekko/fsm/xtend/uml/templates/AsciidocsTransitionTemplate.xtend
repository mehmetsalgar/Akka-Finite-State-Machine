package org.salgar.pekko.fsm.xtend.uml.templates

import org.eclipse.xtext.generator.IFileSystemAccess

import org.eclipse.uml2.uml.State
import org.eclipse.uml2.uml.Transition

import org.salgar.pekko.fsm.xtend.uml.engine.TemplateAutoInjector

class AsciidocsTransitionTemplate {
    def generate(org.eclipse.uml2.uml.StateMachine it, IFileSystemAccess fsa) {
        TemplateAutoInjector.injectTemplates(this)
        for(state: allOwnedElements.filter(State)) {
            for(tr: state.getOutgoings()) {
                val content = generate(it, tr)
                fsa.generateFile("/"+name.toLowerCase()+"/usecases/"+state.name+"_"+tr.name+".adoc", content)
            }
        }
    }

    def generate (org.eclipse.uml2.uml.StateMachine it, Transition tr) '''
        = Use Case : «tr.name»

        //guard
        «IF (tr.guard !== null)»
            «FOR comment: tr.guard.getOwnedComments»
                 - «comment.body»
            «ENDFOR»
        «ENDIF»

        //action
        «FOR comment: tr.getOwnedComments»
            - «comment.body»
        «ENDFOR»
    '''
}
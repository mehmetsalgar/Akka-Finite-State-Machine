package org.salgar.pekko.fsm.xtend.uml.templates

import javax.inject.Inject
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext

import org.eclipse.uml2.uml.Pseudostate
import org.eclipse.uml2.uml.State
import org.eclipse.uml2.uml.Transition

class JavaGuardTemplate {
    @Inject extension Naming
    @Inject extension SubStateMachineHelper
    @Inject extension GlobalVariableHelper

    def generate(org.eclipse.uml2.uml.StateMachine it, IFileSystemAccess2 fsa, IGeneratorContext context) {
        generate(name, it, fsa, context)
    }

    def void generate(String name, org.eclipse.uml2.uml.StateMachine it, IFileSystemAccess2 fsa, IGeneratorContext context) {
        for(tr : giveTransitionsRecursive(
                                    allOwnedElements().filter(Pseudostate),
                                    allOwnedElements().filter(State)).filter(t|t.guard !== null)) {
            val content = generate(name, it, tr, context)
            fsa.generateFile(packagePath(it)+"/guards/" + tr.source.name.toUpperCase() + context.getGlobalVariable('targetSourceStateSeperator') + tr.target.name.toUpperCase() + "_" + tr.guard.name  + "_Guard.java", content)
        }
    }

    def generate (String name, org.eclipse.uml2.uml.StateMachine it, Transition tr, IGeneratorContext context) '''
        package «packageName(it)».guards;

        import «packageName(it)».«name»;
        import org.salgar.pekko.fsm.api.guard.Guard;

        «IF (tr.guard.getOwnedComments !== null) && (!tr.guard.getOwnedComments.isEmpty)»
            /**
                «FOR comment : tr.guard.getOwnedComments»
                    «comment.body»
                «ENDFOR»
            */
        «ENDIF»
        public interface «tr.source.name.toUpperCase()»«context.getGlobalVariable('targetSourceStateSeperator')»«tr.target.name.toUpperCase()»_«tr.guard.name»_Guard
            extends Guard<«name».«name»Event> {
        }
    '''
}
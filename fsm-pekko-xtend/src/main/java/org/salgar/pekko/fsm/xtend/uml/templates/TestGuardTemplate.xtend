package org.salgar.pekko.fsm.xtend.uml.templates

import javax.inject.Inject
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext

import org.eclipse.uml2.uml.Pseudostate
import org.eclipse.uml2.uml.State
import org.eclipse.uml2.uml.Transition

class TestGuardTemplate {
    @Inject extension Naming
    @Inject extension SubStateMachineHelper
    @Inject extension GlobalVariableHelper

    def generate(org.eclipse.uml2.uml.StateMachine it, IFileSystemAccess2 fsa, IGeneratorContext context) {
        for(tr : giveTransitionsRecursive(
                   allOwnedElements().filter(Pseudostate),
                   allOwnedElements().filter(State)).filter(t|t.guard !== null)) {
            val content = generate(tr, context)
            fsa.generateFile(packagePath+"/guards/" + tr.source.name.toUpperCase() + context.getGlobalVariable('targetSourceStateSeperator') + tr.target.name.toUpperCase() + "_" + tr.guard.name  + "_GuardImpl.java", content)
        }
    }

    def generate (org.eclipse.uml2.uml.StateMachine it, Transition tr, IGeneratorContext context) '''
        package «packageName».guards;

        import org.apache.pekko.actor.typed.scaladsl.ActorContext;
        import «packageName».«name»;
        import org.salgar.pekko.fsm.api.guard.Guard;

        import java.util.Map;

        public class «tr.source.name.toUpperCase()»«context.getGlobalVariable('targetSourceStateSeperator')»«tr.target.name.toUpperCase()»_«tr.guard.name»_GuardImpl
            implements «tr.source.name.toUpperCase()»«context.getGlobalVariable('targetSourceStateSeperator')»«tr.target.name.toUpperCase()»_«tr.guard.name»_Guard {
            @Override
            public boolean evaluate(
                    ActorContext<«name».«name»Event> actorContext,
                    Map<String, Object> controlObject,
                    Map<String, Object> payload) {
                actorContext.log().debug("Evaluating «tr.source.name.toUpperCase()» «tr.name» Guard");

                return true;
            }
        }
    '''
}
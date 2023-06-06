package org.salgar.pekko.fsm.xtend.uml.templates

import javax.inject.Inject
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext

import org.eclipse.uml2.uml.Pseudostate
import org.eclipse.uml2.uml.State
import org.eclipse.uml2.uml.Transition

class TestActionTemplate {
    @Inject extension Naming
    @Inject extension GlobalVariableHelper

    def generate(org.eclipse.uml2.uml.StateMachine it, IFileSystemAccess2 fsa, IGeneratorContext context) {
        generate(name, it, fsa, context)
    }

    private def String generate(String name, org.eclipse.uml2.uml.StateMachine sm, IFileSystemAccess2 fsa, IGeneratorContext context) {
        for(state : sm.allOwnedElements.filter(Pseudostate).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])) {
            for(tr : state.getOutgoings().sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])) {
                fsa.generateFile(packagePath(sm)+"/actions/"+tr.source.name.toUpperCase()+context.getGlobalVariable('targetSourceStateSeperator')+tr.target.name.toUpperCase()+"_"+tr.name+"_ActionImpl.java",
                generate(name, sm,  tr, context))
            }
        }
        for(state : sm.allOwnedElements.filter(State).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])) {
            for(tr : state.getOutgoings().sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])) {
                fsa.generateFile(packagePath(sm)+"/actions/"+tr.source.name.toUpperCase()+context.getGlobalVariable('targetSourceStateSeperator')+tr.target.name.toUpperCase()+"_"+tr.name+"_ActionImpl.java",
                generate(name, sm,  tr, context))
                if(state.getSubmachine() !== null) {
                    generate(name, state.getSubmachine(), fsa, context)
                }
            }
        }
    }

    private def generate (String name, org.eclipse.uml2.uml.StateMachine sm, Transition tr, IGeneratorContext context) '''
        package «packageName(sm)».actions;

        import org.apache.pekko.actor.typed.scaladsl.ActorContext;
        import «packageName(sm)».«name»;

        import java.util.Map;

        public class «tr.source.name.toUpperCase()»«context.getGlobalVariable('targetSourceStateSeperator')»«tr.target.name.toUpperCase()»_«tr.name»_ActionImpl
            extends «tr.source.name.toUpperCase()»«context.getGlobalVariable('targetSourceStateSeperator')»«tr.target.name.toUpperCase()»_«tr.name»_Action {

            @Override
            protected Map<String, Object> processCustomAction(ActorContext<«name».«name»Event> actorContext,
                                                                Map<String, Object> controlObject,
                                                                Map<String, Object> payload) {
                return payload;
            }
        }
    '''
}
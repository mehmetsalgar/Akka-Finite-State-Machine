package org.salgar.pekko.fsm.xtend.uml.templates

import javax.inject.Inject
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext

import org.eclipse.uml2.uml.Pseudostate
import org.eclipse.uml2.uml.State
import org.eclipse.uml2.uml.Transition

class TestActionConfigTemplate {
    @Inject extension Naming
    @Inject extension SubStateMachineHelper
    @Inject extension GlobalVariableHelper

    def generate(org.eclipse.uml2.uml.StateMachine it, IFileSystemAccess2 fsa, IGeneratorContext context) {
        val content = generate(context)
        fsa.generateFile(packagePath+"/actions/config/"+name+"ActionConfiguration.java", content)
    }

    private def generate (org.eclipse.uml2.uml.StateMachine it, IGeneratorContext context)'''
        package «packageName».actions.config;

        import «packageName».actions.*;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.context.annotation.Profile;

        @Configuration
        @Profile("test")
        public class «name»ActionConfiguration {
            «FOR transition : giveTransitionsRecursive(allOwnedElements().filter(Pseudostate), allOwnedElements().filter(State)).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])»
                «transition.generateActionConfig(name, context)»
            «ENDFOR»
        }
    '''

    private def String generateActionConfig(Transition transition, String name, IGeneratorContext context)'''
        @Bean
        public «transition.source.name.toUpperCase()»«context.getGlobalVariable('targetSourceStateSeperator')»«transition.target.name.toUpperCase()»_«transition.name»_Action «name.toLowerCase()»_«transition.source.name.toLowerCase()»_«transition.target.name»_«transition.name»Action() {
            return new «transition.source.name.toUpperCase()»«context.getGlobalVariable('targetSourceStateSeperator')»«transition.target.name.toUpperCase()»_«transition.name»_ActionImpl();
        }
    '''
}

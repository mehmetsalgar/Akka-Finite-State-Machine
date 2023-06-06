package org.salgar.pekko.fsm.xtend.uml.templates

import javax.inject.Inject
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext

import org.eclipse.uml2.uml.Pseudostate
import org.eclipse.uml2.uml.State

class TestGuardConfigTemplate {
    @Inject extension Naming
	@Inject extension GlobalVariableHelper
    @Inject extension SubStateMachineHelper

    def generate(org.eclipse.uml2.uml.StateMachine it, IFileSystemAccess2 fsa, IGeneratorContext context) {
        val content = generate(context)
        fsa.generateFile(packagePath+"/guards/config/"+name+"GuardConfiguration.java", content)
    }

    def generate (org.eclipse.uml2.uml.StateMachine it, IGeneratorContext context)'''
        package «packageName».guards.config;

        import «packageName».guards.*;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.context.annotation.Profile;

        @Configuration
        @Profile("test")
        public class «name»GuardConfiguration {
            «FOR transition : giveTransitionsRecursive(
                      allOwnedElements().filter(Pseudostate),
                      allOwnedElements().filter(State)).filter(t|t.guard !== null).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])»
                @Bean
                public «transition.source.name.toUpperCase()»«context.getGlobalVariable('targetSourceStateSeperator')»«transition.target.name.toUpperCase()»_«transition.guard.name»_Guard «name.toLowerCase()»_«transition.source.name.toLowerCase()»_«transition.target.name»_«transition.name»Guard() {
                    return new «transition.source.name.toUpperCase()»«context.getGlobalVariable('targetSourceStateSeperator')»«transition.target.name.toUpperCase()»_«transition.guard.name»_GuardImpl();
                }
            «ENDFOR»
        }
    '''
}
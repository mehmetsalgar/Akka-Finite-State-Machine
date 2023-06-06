package org.salgar.pekko.fsm.xtend.uml.templates

import javax.inject.Inject
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext

import org.eclipse.uml2.uml.Pseudostate
import org.eclipse.uml2.uml.State

class ScalaGuardsTemplate {
	@Inject extension Naming
	@Inject extension SubStateMachineHelper
	@Inject extension GlobalVariableHelper

	def doGenerate (org.eclipse.uml2.uml.StateMachine it, IFileSystemAccess2 fsa, IGeneratorContext context) {
        val content = generate(context)
        fsa.generateFile(packagePath+"/guards/Spring"+name+"GuardsLocator.scala", content)
    }

    def generate (org.eclipse.uml2.uml.StateMachine it, IGeneratorContext context) '''
        package «packageName».guards

        import javax.annotation.PostConstruct
        import «packageName».«name».«name»Event
        import org.salgar.pekko.fsm.api.guard.Guard
        import org.springframework.beans.factory.annotation.Autowired
        import org.springframework.stereotype.Component

        object Spring«name»GuardsLocator {
            private var spring«name»GuardsLocator: Spring«name»GuardsLocator = _

            def getInstance: Spring«name»GuardsLocator = {
                spring«name»GuardsLocator
            }
        }

        @Component
        case class Spring«name»GuardsLocator(
            «FOR transition : giveTransitionsRecursive(
                allOwnedElements().filter(Pseudostate),
                allOwnedElements().filter(State)).filter(t|t.guard !== null).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())]) SEPARATOR ','»
                «IF (transition.guard !== null)»
                    @Autowired «name.toLowerCase()»_«transition.source.name.toUpperCase()»«context.getGlobalVariable('targetSourceStateSeperator')»«transition.target.name.toUpperCase()»_«transition.triggers.head.name»_«transition.guard.name»: «transition.source.name.toUpperCase()»«context.getGlobalVariable('targetSourceStateSeperator')»«transition.target.name.toUpperCase()»_«transition.guard.name»_Guard
                «ENDIF»
            «ENDFOR»
         ) {
         import Spring«name»GuardsLocator._

         @PostConstruct
         def init: Unit = {
            spring«name»GuardsLocator = this
         }
        }
    '''
}
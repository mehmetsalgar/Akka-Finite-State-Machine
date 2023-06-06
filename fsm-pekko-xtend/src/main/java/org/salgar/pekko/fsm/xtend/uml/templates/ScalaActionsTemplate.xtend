package org.salgar.pekko.fsm.xtend.uml.templates

import javax.inject.Inject
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext

import org.eclipse.uml2.uml.Pseudostate
import org.eclipse.uml2.uml.State

class ScalaActionsTemplate {
	@Inject extension Naming
	@Inject extension SubStateMachineHelper
	@Inject extension GlobalVariableHelper

	def doGenerate (org.eclipse.uml2.uml.StateMachine it, IFileSystemAccess2 fsa, IGeneratorContext context) {
        val content = generate(context)
        fsa.generateFile(packagePath+"/actions/Spring"+name+"ActionsLocator.scala", content)
    }

    def generate (org.eclipse.uml2.uml.StateMachine it, IGeneratorContext context) '''
        package «packageName».actions

        import javax.annotation.PostConstruct
        import org.salgar.pekko.fsm.api.action.Action
        import «packageName».«name».{«name»Event, PersistEvent, State}
        import org.springframework.beans.factory.annotation.Autowired
        import org.springframework.stereotype.Component

        object Spring«name»ActionsLocator {
          private var spring«name»ActionsLocator: Spring«name»ActionsLocator = _

          def getInstance(): Spring«name»ActionsLocator = {
            spring«name»ActionsLocator
          }
        }

        @Component
        case class Spring«name»ActionsLocator(
            «FOR transition : giveTransitionsRecursive(allOwnedElements().filter(Pseudostate), allOwnedElements().filter(State)).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())]) SEPARATOR ','»
                @Autowired «name.toLowerCase()»_«transition.source.name.toUpperCase()»«context.getGlobalVariable('targetSourceStateSeperator')»«transition.target.name.toUpperCase()»_«transition.name»Action: «transition.source.name.toUpperCase()»«context.getGlobalVariable('targetSourceStateSeperator')»«transition.target.name.toUpperCase()»_«transition.name»_Action
            «ENDFOR»
         ) {
          import Spring«name»ActionsLocator._

          @PostConstruct
          def init: Unit = {
            spring«name»ActionsLocator = this
          }
        }
    '''
}
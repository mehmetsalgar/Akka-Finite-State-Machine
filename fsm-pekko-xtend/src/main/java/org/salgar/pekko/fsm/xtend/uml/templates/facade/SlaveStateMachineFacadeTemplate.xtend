package org.salgar.pekko.fsm.xtend.uml.templates.facade

import javax.inject.Inject
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.uml2.uml.Transition

import org.salgar.pekko.fsm.xtend.uml.templates.Naming
import org.salgar.pekko.fsm.xtend.uml.templates.StateMachineHelper

class SlaveStateMachineFacadeTemplate {
    @Inject extension Naming
    @Inject extension StateMachineHelper

    def doGenerate (org.eclipse.uml2.uml.StateMachine it, IFileSystemAccess2 fsa) {
        val content = generate
        fsa.generateFile(packagePath+"/facade/"+name+"Facade.scala", content)
    }

    def generate(org.eclipse.uml2.uml.StateMachine it) '''
        package «packageName».facade

        import «packageName».«name».Response

        import org.salgar.pekko.fsm.api.UseCaseKey

        import scala.concurrent.Future

        trait «name»Facade {
            def currentState(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]) : Future[Response]
            «FOR trigger : giveTransitionWithTrigger(allOwnedElements().filter(Transition)).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])»
                def «trigger.name.substring(2, trigger.name.length).toFirstLower»(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Unit
                def ask«trigger.name.substring(2, trigger.name.length)»(useCaseKey : UseCaseKey, payload: java.util.Map[String, AnyRef]): Future[Response]
            «ENDFOR»
        }
    '''
}
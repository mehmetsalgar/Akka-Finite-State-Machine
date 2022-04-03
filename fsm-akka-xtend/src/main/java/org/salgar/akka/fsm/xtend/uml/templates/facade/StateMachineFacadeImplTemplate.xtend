package org.salgar.akka.fsm.xtend.uml.templates.facade

import javax.inject.Inject
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.uml2.uml.Transition

import org.salgar.akka.fsm.xtend.uml.templates.Naming
import org.salgar.akka.fsm.xtend.uml.templates.StateMachineHelper

class StateMachineFacadeImplTemplate {
    @Inject extension Naming
    @Inject extension StateMachineHelper

    def doGenerate (org.eclipse.uml2.uml.StateMachine it, IFileSystemAccess2 fsa) {
        val content = generate
        fsa.generateFile(packagePath+"/facade/"+name+"FacadeImpl.scala", content)
    }

    def generate(org.eclipse.uml2.uml.StateMachine it) '''
        package «packageName».facade

        import «packageName».«name».Response
        import «packageName».«name»Guardian.«name»GuardianEvent
        import org.salgar.fsm.akka.statemachine.facade.StateMachineFacade

        import scala.concurrent.Future

        class «name»FacadeImpl {
            extends StateMachineFacade[«name»Guardian.«name»GuardianEvent, Response] (actorService, "«name.toFirstLower»Guardian", «name»Guardian()(actorService.sharding()))
                with CreditFacade {
            import ActorService._
            «FOR trigger : giveTransitionWithTrigger(allOwnedElements().filter(Transition)).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])»
                def «trigger.name.substring(2, trigger.name.length).toFirstLower»(payload: java.util.Map[String, AnyRef]): «name»GuardianEvent{
                     «trigger.name»(payload)
                  }
            «ENDFOR»
        }
    '''
}
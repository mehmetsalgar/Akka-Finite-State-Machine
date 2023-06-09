package org.salgar.pekko.fsm.xtend.uml.templates.kafka

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.salgar.pekko.fsm.xtend.uml.templates.Naming

class AskFacadeTemplate {
	@Inject extension Naming

	def doGenerate(Resource input, IFileSystemAccess fsa) {
	    val masterSm = input
	        .allContents
	        .toIterable
	        .filter(org.eclipse.uml2.uml.StateMachine).filter[i|i.active]
	        .filter[s|!s.abstract]
	        .head

        val content = generate(masterSm, input)
        fsa.generateFile(packagePath(masterSm)+"/kafka/facade/AskFacade.scala", content)
    }

    def generate(org.eclipse.uml2.uml.StateMachine masterSm, Resource input)'''
        package «packageName(masterSm)».kafka.facade

        import org.salgar.fsm.pekko.command.CommandHandler
        «FOR org.eclipse.uml2.uml.StateMachine sm : input.allContents.toIterable.filter(org.eclipse.uml2.uml.StateMachine).filter[i|i.active]»
            import «packageName(sm)».«sm.name»
        «ENDFOR»
        «FOR org.eclipse.uml2.uml.StateMachine sm : input.allContents.toIterable.filter(org.eclipse.uml2.uml.StateMachine).filter[i|i.active]»
            import «packageName(sm)».protobuf.«sm.name»Command
        «ENDFOR»
        import org.springframework.stereotype.Component
        import javax.annotation.PostConstruct
        import scala.concurrent.Future

        @Component
        class AskFacade(
            «FOR org.eclipse.uml2.uml.StateMachine sm : input.allContents.toIterable.filter(org.eclipse.uml2.uml.StateMachine).filter[i|i.active] SEPARATOR ','»
                «sm.name.toFirstLower»CommandHandlers: java.util.List[CommandHandler[«sm.name»Command, «sm.name».Response]]
            «ENDFOR»
        ) {
            «FOR org.eclipse.uml2.uml.StateMachine sm : input.allContents.toIterable.filter(org.eclipse.uml2.uml.StateMachine).filter[i|i.active]»
                private var _«sm.name.toFirstLower»CommandHandlers: Map[String, CommandHandler[«sm.name»Command, «sm.name».Response]] = Map()
            «ENDFOR»

            «FOR org.eclipse.uml2.uml.StateMachine sm : input.allContents.toIterable.filter(org.eclipse.uml2.uml.StateMachine).filter[i|i.active]»
                def ask«sm.name»Command(«sm.name.toFirstLower»Command: «sm.name»Command): Future[«sm.name».Response] = {
                    val commandHandler: CommandHandler[«sm.name»Command, «sm.name».Response] = _«sm.name.toFirstLower»CommandHandlers(
                      «sm.name.toFirstLower»Command.getCommand
                    )
                    commandHandler.handleCommand(«sm.name.toFirstLower»Command)
                }
            «ENDFOR»

             @PostConstruct
             private def init() = {
                «FOR org.eclipse.uml2.uml.StateMachine sm : input.allContents.toIterable.filter(org.eclipse.uml2.uml.StateMachine).filter[i|i.active]»
                    «sm.name.toFirstLower»CommandHandlers
                          .stream()
                          .forEach(commandHandler => {
                            _«sm.name.toFirstLower»CommandHandlers += (commandHandler.`type`() -> commandHandler)
                      })
                «ENDFOR»
             }
        }
    '''
}
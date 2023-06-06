package org.salgar.pekko.fsm.xtend.uml.templates

import javax.inject.Inject
import org.eclipse.xtext.generator.IFileSystemAccess

class ServiceLocatorTemplate {
	@Inject extension Naming

	def doGenerate (org.eclipse.uml2.uml.StateMachine it, IFileSystemAccess fsa) {
        val content = generate
        fsa.generateFile(packagePath+"/config/"+name+"ServiceLocator.scala", content)
    }

    def generate (org.eclipse.uml2.uml.StateMachine it) '''
        package «packageName».config
        import javax.annotation.PostConstruct
        import org.salgar.pekko.fsm.api.UseCaseKeyStrategy
        import org.springframework.beans.factory.annotation.Autowired
        import org.springframework.stereotype.Component

        object «name»ServiceLocator {
            private var INSTANCE : «name»ServiceLocator = _

            def getInstance : «name»ServiceLocator = INSTANCE
        }

        @Component
        case class «name»ServiceLocator (
                                    @Autowired useCaseKeyStrategy: UseCaseKeyStrategy
                                  ) {
            import «name»ServiceLocator._

            @PostConstruct
            private def init: Unit = {
                INSTANCE = this
            }
        }
    '''
}
package org.salgar.pekko.fsm.xtend.uml.templates.kafka

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.salgar.pekko.fsm.xtend.uml.templates.Naming

class TopicConfigTemplate {
	@Inject extension Naming

	def doGenerate(Resource input, IFileSystemAccess fsa) {
        val masterSm = input.allContents.toIterable.filter(org.eclipse.uml2.uml.StateMachine).filter[i|i.active].filter[s|!s.abstract].head
	    val content = generate(masterSm, input)
        fsa.generateFile(packagePath(masterSm)+"/kafka/config/"+"TopicConfig.scala", content)
    }

    def generate(org.eclipse.uml2.uml.StateMachine masterSm, Resource input)'''
        package «packageName(masterSm)».kafka.config

        import org.apache.kafka.common.serialization.{Serde, Serdes}
        «FOR org.eclipse.uml2.uml.StateMachine sm : input.allContents.toIterable.filter(org.eclipse.uml2.uml.StateMachine).filter[i|i.active]»
            import «packageName(sm)».protobuf.«sm.name»Command
        «ENDFOR»

        import «packageName(masterSm)».kafka.config.TopicConfig.Topic
        import org.salgar.fsm.pekko.kafka.stream.SerdeFactory
        import org.springframework.beans.factory.annotation.Qualifier
        import org.springframework.boot.autoconfigure.kafka.KafkaProperties
        import org.springframework.stereotype.Component

        import javax.annotation.PostConstruct

        object TopicConfig {
          final case class Topic[K, V](name: String, key: Serde[K], value: Serde[V]) {
            def configure(configs : java.util.Map[String, _]) = {
              key.configure(configs, true)
              value.configure(configs, false)
            }
          }
        }

        @Component
        class TopicConfig(
                           serdeFactory : SerdeFactory,
                           «FOR org.eclipse.uml2.uml.StateMachine sm : input.allContents.toIterable.filter(org.eclipse.uml2.uml.StateMachine).filter[i|i.active] SEPARATOR ','»
                               @Qualifier("«sm.name.toFirstLower»Properties") «sm.name.toFirstLower»Properties: KafkaProperties.Consumer
                           «ENDFOR»
                         ) {
          «FOR org.eclipse.uml2.uml.StateMachine sm : input.allContents.toIterable.filter(org.eclipse.uml2.uml.StateMachine).filter[i|i.active]»
            val topic«sm.name» : Topic[String, «sm.name»Command] = Topic("«sm.name.toFirstLower»", Serdes.String(), serdeFactory.create())
          «ENDFOR»

          @PostConstruct
          def configure() = {
            «FOR org.eclipse.uml2.uml.StateMachine sm : input.allContents.toIterable.filter(org.eclipse.uml2.uml.StateMachine).filter[i|i.active]»
                topic«sm.name».key.configure(«sm.name.toFirstLower»Properties.getProperties, true)
                topic«sm.name».value.configure(«sm.name.toFirstLower»Properties.getProperties, false)
            «ENDFOR»
          }
        }
    '''
}
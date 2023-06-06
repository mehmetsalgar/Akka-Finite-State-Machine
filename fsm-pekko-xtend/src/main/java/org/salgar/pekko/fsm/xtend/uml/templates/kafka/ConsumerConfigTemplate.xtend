package org.salgar.pekko.fsm.xtend.uml.templates.kafka

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import com.google.common.base.CaseFormat
import org.salgar.pekko.fsm.xtend.uml.templates.Naming

class ConsumerConfigTemplate {
	@Inject extension Naming

	def doGenerate(Resource input, IFileSystemAccess fsa) {
	    val masterSm = input
	        .allContents
	        .toIterable
	        .filter(org.eclipse.uml2.uml.StateMachine).filter[i|i.active]
	        .filter[s|!s.abstract]
	        .head

        input.allContents.filter(org.eclipse.uml2.uml.StateMachine).filter(s | s.isActive).forEach[
            val content = generate(it, masterSm)
            fsa.generateFile(packagePath(it)+"/kafka/config/"+name.toFirstUpper+"ConsumerConfig.scala", content)
        ]
    }

    def generate(org.eclipse.uml2.uml.StateMachine sm, org.eclipse.uml2.uml.StateMachine masterSm)'''
        package «packageName(sm)».kafka.config

        import org.apache.pekko.kafka.ConsumerSettings
        import org.salgar.fsm.pekko.pekkosystem.ActorService
        import org.salgar.fsm.pekko.kafka.config.ConsumerConfig
        import «packageName(masterSm)».kafka.config.TopicConfig
        import «packageName(sm)».protobuf.«sm.name.toFirstUpper»Command
        import org.springframework.beans.factory.annotation.Qualifier
        import org.springframework.boot.autoconfigure.kafka.KafkaProperties
        import org.springframework.stereotype.Component

        import java.util.stream.Collectors
        import scala.concurrent.duration.DurationInt

        @Component
        class «sm.name.toFirstUpper»ConsumerConfig(actorService: ActorService,
                                     topicConfig: TopicConfig,
                                     kafkaProperties: KafkaProperties,
                                     @Qualifier("«sm.name.toFirstLower»Properties") consumerProperties: KafkaProperties.Consumer)
                                     extends ConsumerConfig[String, «sm.name.toFirstUpper»Command] {
        override val consumerSettings : ConsumerSettings[String, «sm.name.toFirstUpper»Command] =
          ConsumerSettings(
            actorService.actorSystem(),
            topicConfig.topic«sm.name.toFirstUpper».key.deserializer(),
            topicConfig.topic«sm.name.toFirstUpper».value.deserializer()
          ).withBootstrapServers(
              kafkaProperties.getBootstrapServers
                .stream()
                .map(server => server)
                .collect(Collectors.joining(","))
            )
            .withClientId("pekko-consumer-«CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, sm.name).toLowerCase»")
            .withGroupId(consumerProperties.getGroupId)
            .withProperties(consumerProperties.buildProperties().asInstanceOf[java.util.Map[String, String]])
            .withStopTimeout(0.seconds)
        }
    '''
}
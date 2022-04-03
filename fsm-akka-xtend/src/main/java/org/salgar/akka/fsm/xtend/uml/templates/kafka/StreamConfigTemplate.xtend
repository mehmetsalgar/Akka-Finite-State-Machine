package org.salgar.akka.fsm.xtend.uml.templates.kafka

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import com.google.common.base.CaseFormat
import org.salgar.akka.fsm.xtend.uml.templates.Naming

class StreamConfigTemplate {
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
            fsa.generateFile(packagePath(it)+"/kafka/stream/"+name.toFirstUpper+"StreamConfig.scala", content)
        ]
    }

    def generate(org.eclipse.uml2.uml.StateMachine sm, org.eclipse.uml2.uml.StateMachine masterSm)'''
        package «packageName(sm)».kafka.stream

        import akka.Done
        import akka.kafka.scaladsl.{Committer, Consumer}
        import akka.kafka.{CommitterSettings, ConsumerSettings, Subscriptions}
        import akka.stream.Materializer
        import akka.stream.scaladsl.Sink
        import org.salgar.fsm.akka.akkasystem.ActorService
        import «packageName(masterSm)».kafka.config.{TopicConfig, TopicProperties}
        import «packageName(sm)».protobuf.«sm.name.toFirstUpper»Command
        import «packageName(masterSm)».kafka.facade.AskFacade
        import org.springframework.boot.autoconfigure.kafka.KafkaProperties

        import java.util.stream.Collectors
        import scala.concurrent.duration.DurationInt

        object «sm.name.toFirstUpper»StreamConfig {
          def apply(
              actorService: ActorService,
              topicConfig: TopicConfig,
              kafkaProperties: KafkaProperties,
              consumerProperties: KafkaProperties.Consumer,
              topicProperties: TopicProperties,
              askFacade: AskFacade
          ) = {
            implicit val materializer: Materializer = Materializer.createMaterializer(actorService.actorSystem())

            val consumerSettings: ConsumerSettings[String, «sm.name.toFirstUpper»Command] =
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
                .withClientId("akka-consumer-«CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, sm.name).toLowerCase»")
                .withGroupId(consumerProperties.getGroupId)
                .withProperties(consumerProperties.buildProperties().asInstanceOf[java.util.Map[String, String]])
                .withStopTimeout(0.seconds)

            val control: akka.kafka.scaladsl.Consumer.DrainingControl[Done] =
              Consumer
                .sourceWithOffsetContext(consumerSettings, Subscriptions.topics(topicProperties.get«sm.name.toFirstUpper»))
                .mapAsync(Runtime.getRuntime.availableProcessors() * 2) { consumerRecord =>
                  {
                    actorService.actorSystem().log.info("-------------------------------------------")
                    actorService.actorSystem().log.info("----- We are processing «sm.name.toFirstUpper» Event ----")
                    actorService.actorSystem().log.info("-------------------------------------------")
                    val «sm.name.toFirstLower»Command = consumerRecord.value()

                    actorService.actorSystem().log.debug("Received Command: {}", «sm.name.toFirstLower»Command)

                    askFacade.ask«sm.name.toFirstUpper»Command(«sm.name.toFirstLower»Command)
                  }
                }
                .via(Committer.flowWithOffsetContext(CommitterSettings(actorService.actorSystem())))
                .toMat(Sink.ignore)(Consumer.DrainingControl.apply)
                .run()
          }
        }
    '''
}
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
        import akka.actor.typed.ActorRef
        import akka.kafka.cluster.sharding.KafkaClusterSharding
        import akka.kafka.scaladsl.{Committer, Consumer}
        import akka.kafka.{CommitterSettings, ConsumerRebalanceEvent, Subscriptions}
        import akka.stream.Materializer
        import akka.stream.scaladsl.Sink
        import org.salgar.fsm.akka.akkasystem.ActorService
        import «packageName(masterSm)».kafka.config.TopicProperties
        import «packageName(masterSm)».kafka.facade.AskFacade
        import «packageName(sm)».«sm.name»Guardian
        import «packageName(sm)».protobuf.«sm.name.toFirstUpper»Command
        import org.salgar.fsm.akka.kafka.config.ConsumerConfig

        object «sm.name.toFirstUpper»StreamConfig {
          def apply(
              «sm.name.toFirstLower»ConsumerConfig: ConsumerConfig[String, «sm.name.toFirstUpper»Command],
              actorService: ActorService,
              topicProperties: TopicProperties,
              askFacade: AskFacade
          ) = {
            implicit val materializer: Materializer = Materializer.createMaterializer(actorService.actorSystem())

            val rebalanceListener: ActorRef[ConsumerRebalanceEvent] =
              KafkaClusterSharding(actorService.actorSystem()).rebalanceListener(«sm.name»Guardian.«sm.name.toLowerCase»TypeKey)
            import akka.actor.typed.scaladsl.adapter._
            val rebalanceListenerClassic: akka.actor.ActorRef = rebalanceListener.toClassic

            val control: akka.kafka.scaladsl.Consumer.DrainingControl[Done] =
              Consumer
                .sourceWithOffsetContext(
                    «sm.name.toFirstLower»ConsumerConfig.consumerSettings,
                    Subscriptions
                        .topics(topicProperties.get«sm.name.toFirstUpper»)
                        .withRebalanceListener(rebalanceListenerClassic))
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
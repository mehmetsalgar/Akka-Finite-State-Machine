package org.salgar.fsm.pekko.foureyes.fraudprevention.kafka.stream

import org.salgar.fsm.pekko.foureyes.credit.kafka.config.TopicProperties
import org.salgar.fsm.pekko.foureyes.credit.kafka.facade.AskFacade
import org.salgar.fsm.pekko.foureyes.fraudprevention.protobuf.FraudPreventionSMCommand
import org.salgar.fsm.pekko.kafka.config.ConsumerConfig
import org.salgar.fsm.pekko.pekkosystem.ActorService

object FraudPreventionSMStreamConfig {
  def apply(
      fraudPreventionSMConsumerConfig: ConsumerConfig[String, FraudPreventionSMCommand],
      actorService: ActorService,
      topicProperties: TopicProperties,
      askFacade: AskFacade
  ) = {
    implicit val materializer: Materializer = Materializer.createMaterializer(actorService.actorSystem())

    val control: org.apache.pekko.kafka.scaladsl.Consumer.DrainingControl[Done] =
      Consumer
        .sourceWithOffsetContext(
          fraudPreventionSMConsumerConfig.consumerSettings,
          Subscriptions.topics(topicProperties.getFraudPreventionSM)
        )
        .mapAsync(Runtime.getRuntime.availableProcessors() * 2) { consumerRecord =>
          {
            actorService.actorSystem().log.info("-------------------------------------------")
            actorService.actorSystem().log.info("----- We are processing FraudPreventionSM Event ----")
            actorService.actorSystem().log.info("-------------------------------------------")
            val fraudPreventionSMCommand = consumerRecord.value()

            actorService.actorSystem().log.debug("Received Command: {}", fraudPreventionSMCommand)

            askFacade.askFraudPreventionSMCommand(fraudPreventionSMCommand)
          }
        }
        .via(Committer.flowWithOffsetContext(CommitterSettings(actorService.actorSystem())))
        .toMat(Sink.ignore)(Consumer.DrainingControl.apply)
        .run()
  }
}

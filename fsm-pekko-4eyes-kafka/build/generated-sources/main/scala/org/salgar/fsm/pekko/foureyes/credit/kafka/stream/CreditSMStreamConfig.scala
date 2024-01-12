package org.salgar.fsm.pekko.foureyes.credit.kafka.stream

import org.salgar.fsm.pekko.foureyes.credit.kafka.config.TopicProperties
import org.salgar.fsm.pekko.foureyes.credit.kafka.facade.AskFacade
import org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditSMCommand
import org.salgar.fsm.pekko.kafka.config.ConsumerConfig
import org.salgar.fsm.pekko.pekkosystem.ActorService

object CreditSMStreamConfig {
  def apply(
      creditSMConsumerConfig: ConsumerConfig[String, CreditSMCommand],
      actorService: ActorService,
      topicProperties: TopicProperties,
      askFacade: AskFacade
  ) = {
    implicit val materializer: Materializer = Materializer.createMaterializer(actorService.actorSystem())

    val control: org.apache.pekko.kafka.scaladsl.Consumer.DrainingControl[Done] =
      Consumer
        .sourceWithOffsetContext(
          creditSMConsumerConfig.consumerSettings,
          Subscriptions.topics(topicProperties.getCreditSM)
        )
        .mapAsync(Runtime.getRuntime.availableProcessors() * 2) { consumerRecord =>
          {
            actorService.actorSystem().log.info("-------------------------------------------")
            actorService.actorSystem().log.info("----- We are processing CreditSM Event ----")
            actorService.actorSystem().log.info("-------------------------------------------")
            val creditSMCommand = consumerRecord.value()

            actorService.actorSystem().log.debug("Received Command: {}", creditSMCommand)

            askFacade.askCreditSMCommand(creditSMCommand)
          }
        }
        .via(Committer.flowWithOffsetContext(CommitterSettings(actorService.actorSystem())))
        .toMat(Sink.ignore)(Consumer.DrainingControl.apply)
        .run()
  }
}

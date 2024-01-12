package org.salgar.fsm.pekko.foureyes.addresscheck.kafka.stream

import org.salgar.fsm.pekko.foureyes.addresscheck.protobuf.AdressCheckSMCommand
import org.salgar.fsm.pekko.foureyes.credit.kafka.config.TopicProperties
import org.salgar.fsm.pekko.foureyes.credit.kafka.facade.AskFacade
import org.salgar.fsm.pekko.kafka.config.ConsumerConfig
import org.salgar.fsm.pekko.pekkosystem.ActorService

object AdressCheckSMStreamConfig {
  def apply(
      adressCheckSMConsumerConfig: ConsumerConfig[String, AdressCheckSMCommand],
      actorService: ActorService,
      topicProperties: TopicProperties,
      askFacade: AskFacade
  ) = {
    implicit val materializer: Materializer = Materializer.createMaterializer(actorService.actorSystem())

    val control: org.apache.pekko.kafka.scaladsl.Consumer.DrainingControl[Done] =
      Consumer
        .sourceWithOffsetContext(
          adressCheckSMConsumerConfig.consumerSettings,
          Subscriptions.topics(topicProperties.getAdressCheckSM)
        )
        .mapAsync(Runtime.getRuntime.availableProcessors() * 2) { consumerRecord =>
          {
            actorService.actorSystem().log.info("-------------------------------------------")
            actorService.actorSystem().log.info("----- We are processing AdressCheckSM Event ----")
            actorService.actorSystem().log.info("-------------------------------------------")
            val adressCheckSMCommand = consumerRecord.value()

            actorService.actorSystem().log.debug("Received Command: {}", adressCheckSMCommand)

            askFacade.askAdressCheckSMCommand(adressCheckSMCommand)
          }
        }
        .via(Committer.flowWithOffsetContext(CommitterSettings(actorService.actorSystem())))
        .toMat(Sink.ignore)(Consumer.DrainingControl.apply)
        .run()
  }
}

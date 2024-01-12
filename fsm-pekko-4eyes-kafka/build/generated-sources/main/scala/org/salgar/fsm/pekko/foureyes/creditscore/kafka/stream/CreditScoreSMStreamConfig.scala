package org.salgar.fsm.pekko.foureyes.creditscore.kafka.stream

import org.salgar.fsm.pekko.foureyes.credit.kafka.config.TopicProperties
import org.salgar.fsm.pekko.foureyes.credit.kafka.facade.AskFacade
import org.salgar.fsm.pekko.foureyes.creditscore.protobuf.CreditScoreSMCommand
import org.salgar.fsm.pekko.kafka.config.ConsumerConfig
import org.salgar.fsm.pekko.pekkosystem.ActorService

object CreditScoreSMStreamConfig {
  def apply(
      creditScoreSMConsumerConfig: ConsumerConfig[String, CreditScoreSMCommand],
      actorService: ActorService,
      topicProperties: TopicProperties,
      askFacade: AskFacade
  ) = {
    implicit val materializer: Materializer = Materializer.createMaterializer(actorService.actorSystem())

    val control: org.apache.pekko.kafka.scaladsl.Consumer.DrainingControl[Done] =
      Consumer
        .sourceWithOffsetContext(
          creditScoreSMConsumerConfig.consumerSettings,
          Subscriptions.topics(topicProperties.getCreditScoreSM)
        )
        .mapAsync(Runtime.getRuntime.availableProcessors() * 2) { consumerRecord =>
          {
            actorService.actorSystem().log.info("-------------------------------------------")
            actorService.actorSystem().log.info("----- We are processing CreditScoreSM Event ----")
            actorService.actorSystem().log.info("-------------------------------------------")
            val creditScoreSMCommand = consumerRecord.value()

            actorService.actorSystem().log.debug("Received Command: {}", creditScoreSMCommand)

            askFacade.askCreditScoreSMCommand(creditScoreSMCommand)
          }
        }
        .via(Committer.flowWithOffsetContext(CommitterSettings(actorService.actorSystem())))
        .toMat(Sink.ignore)(Consumer.DrainingControl.apply)
        .run()
  }
}

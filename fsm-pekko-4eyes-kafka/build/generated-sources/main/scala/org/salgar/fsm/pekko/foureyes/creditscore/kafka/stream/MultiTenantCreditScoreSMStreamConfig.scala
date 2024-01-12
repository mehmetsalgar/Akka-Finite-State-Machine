package org.salgar.fsm.pekko.foureyes.creditscore.kafka.stream

import org.salgar.fsm.pekko.foureyes.credit.kafka.config.TopicProperties
import org.salgar.fsm.pekko.foureyes.credit.kafka.facade.AskFacade
import org.salgar.fsm.pekko.foureyes.creditscore.protobuf.MultiTenantCreditScoreSMCommand
import org.salgar.fsm.pekko.kafka.config.ConsumerConfig
import org.salgar.fsm.pekko.pekkosystem.ActorService

object MultiTenantCreditScoreSMStreamConfig {
  def apply(
      multiTenantCreditScoreSMConsumerConfig: ConsumerConfig[String, MultiTenantCreditScoreSMCommand],
      actorService: ActorService,
      topicProperties: TopicProperties,
      askFacade: AskFacade
  ) = {
    implicit val materializer: Materializer = Materializer.createMaterializer(actorService.actorSystem())

    val control: org.apache.pekko.kafka.scaladsl.Consumer.DrainingControl[Done] =
      Consumer
        .sourceWithOffsetContext(
          multiTenantCreditScoreSMConsumerConfig.consumerSettings,
          Subscriptions.topics(topicProperties.getMultiTenantCreditScoreSM)
        )
        .mapAsync(Runtime.getRuntime.availableProcessors() * 2) { consumerRecord =>
          {
            actorService.actorSystem().log.info("-------------------------------------------")
            actorService.actorSystem().log.info("----- We are processing MultiTenantCreditScoreSM Event ----")
            actorService.actorSystem().log.info("-------------------------------------------")
            val multiTenantCreditScoreSMCommand = consumerRecord.value()

            actorService.actorSystem().log.debug("Received Command: {}", multiTenantCreditScoreSMCommand)

            askFacade.askMultiTenantCreditScoreSMCommand(multiTenantCreditScoreSMCommand)
          }
        }
        .via(Committer.flowWithOffsetContext(CommitterSettings(actorService.actorSystem())))
        .toMat(Sink.ignore)(Consumer.DrainingControl.apply)
        .run()
  }
}

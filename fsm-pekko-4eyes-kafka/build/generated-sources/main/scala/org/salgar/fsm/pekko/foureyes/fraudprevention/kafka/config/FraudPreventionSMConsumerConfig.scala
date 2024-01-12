package org.salgar.fsm.pekko.foureyes.fraudprevention.kafka.config

import org.salgar.fsm.pekko.foureyes.credit.kafka.config.TopicConfig
import org.salgar.fsm.pekko.foureyes.fraudprevention.protobuf.FraudPreventionSMCommand
import org.salgar.fsm.pekko.kafka.config.ConsumerConfig
import org.salgar.fsm.pekko.pekkosystem.ActorService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.stereotype.Component

import java.util.stream.Collectors

@Component
class FraudPreventionSMConsumerConfig(
    actorService: ActorService,
    topicConfig: TopicConfig,
    kafkaProperties: KafkaProperties,
    @Qualifier("fraudPreventionSMProperties") consumerProperties: KafkaProperties.Consumer
) extends ConsumerConfig[String, FraudPreventionSMCommand] {
  override val consumerSettings: ConsumerSettings[String, FraudPreventionSMCommand] =
    ConsumerSettings(
      actorService.actorSystem(),
      topicConfig.topicFraudPreventionSM.key.deserializer(),
      topicConfig.topicFraudPreventionSM.value.deserializer()
    ).withBootstrapServers(
        kafkaProperties.getBootstrapServers
          .stream()
          .map(server => server)
          .collect(Collectors.joining(","))
      )
      .withClientId("pekko-consumer-fraud-prevention-s-m")
      .withGroupId(consumerProperties.getGroupId)
      .withProperties(consumerProperties.buildProperties().asInstanceOf[java.util.Map[String, String]])
      .withStopTimeout(0.seconds)
}

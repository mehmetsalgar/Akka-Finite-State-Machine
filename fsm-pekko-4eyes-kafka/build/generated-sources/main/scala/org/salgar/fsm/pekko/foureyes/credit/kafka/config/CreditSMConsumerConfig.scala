package org.salgar.fsm.pekko.foureyes.credit.kafka.config

import org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditSMCommand
import org.salgar.fsm.pekko.kafka.config.ConsumerConfig
import org.salgar.fsm.pekko.pekkosystem.ActorService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.stereotype.Component

import java.util.stream.Collectors

@Component
class CreditSMConsumerConfig(
    actorService: ActorService,
    topicConfig: TopicConfig,
    kafkaProperties: KafkaProperties,
    @Qualifier("creditSMProperties") consumerProperties: KafkaProperties.Consumer
) extends ConsumerConfig[String, CreditSMCommand] {
  override val consumerSettings: ConsumerSettings[String, CreditSMCommand] =
    ConsumerSettings(
      actorService.actorSystem(),
      topicConfig.topicCreditSM.key.deserializer(),
      topicConfig.topicCreditSM.value.deserializer()
    ).withBootstrapServers(
        kafkaProperties.getBootstrapServers
          .stream()
          .map(server => server)
          .collect(Collectors.joining(","))
      )
      .withClientId("pekko-consumer-credit-s-m")
      .withGroupId(consumerProperties.getGroupId)
      .withProperties(consumerProperties.buildProperties().asInstanceOf[java.util.Map[String, String]])
      .withStopTimeout(0.seconds)
}

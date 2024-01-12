package org.salgar.fsm.pekko.foureyes.credit.kafka.config

import org.salgar.fsm.pekko.foureyes.addresscheck.protobuf.AdressCheckSMCommand
import org.salgar.fsm.pekko.foureyes.credit.kafka.config.TopicConfig.Topic
import org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditSMCommand
import org.salgar.fsm.pekko.foureyes.creditscore.protobuf.{CreditScoreSMCommand, MultiTenantCreditScoreSMCommand}
import org.salgar.fsm.pekko.foureyes.fraudprevention.protobuf.FraudPreventionSMCommand
import org.salgar.fsm.pekko.kafka.stream.SerdeFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

object TopicConfig {
  final case class Topic[K, V](name: String, key: Serde[K], value: Serde[V]) {
    def configure(configs: java.util.Map[String, _]) = {
      key.configure(configs, true)
      value.configure(configs, false)
    }
  }
}

@Component
class TopicConfig(
    serdeFactory: SerdeFactory,
    @Qualifier("creditSMProperties") creditSMProperties: KafkaProperties.Consumer,
    @Qualifier("creditScoreSMProperties") creditScoreSMProperties: KafkaProperties.Consumer,
    @Qualifier("multiTenantCreditScoreSMProperties") multiTenantCreditScoreSMProperties: KafkaProperties.Consumer,
    @Qualifier("fraudPreventionSMProperties") fraudPreventionSMProperties: KafkaProperties.Consumer,
    @Qualifier("adressCheckSMProperties") adressCheckSMProperties: KafkaProperties.Consumer
) {
  val topicCreditSM: Topic[String, CreditSMCommand] = Topic("creditSM", Serdes.String(), serdeFactory.create())
  val topicCreditScoreSM: Topic[String, CreditScoreSMCommand] =
    Topic("creditScoreSM", Serdes.String(), serdeFactory.create())
  val topicMultiTenantCreditScoreSM: Topic[String, MultiTenantCreditScoreSMCommand] =
    Topic("multiTenantCreditScoreSM", Serdes.String(), serdeFactory.create())
  val topicFraudPreventionSM: Topic[String, FraudPreventionSMCommand] =
    Topic("fraudPreventionSM", Serdes.String(), serdeFactory.create())
  val topicAdressCheckSM: Topic[String, AdressCheckSMCommand] =
    Topic("adressCheckSM", Serdes.String(), serdeFactory.create())

  @PostConstruct
  def configure() = {
    topicCreditSM.key.configure(creditSMProperties.getProperties, true)
    topicCreditSM.value.configure(creditSMProperties.getProperties, false)
    topicCreditScoreSM.key.configure(creditScoreSMProperties.getProperties, true)
    topicCreditScoreSM.value.configure(creditScoreSMProperties.getProperties, false)
    topicMultiTenantCreditScoreSM.key.configure(multiTenantCreditScoreSMProperties.getProperties, true)
    topicMultiTenantCreditScoreSM.value.configure(multiTenantCreditScoreSMProperties.getProperties, false)
    topicFraudPreventionSM.key.configure(fraudPreventionSMProperties.getProperties, true)
    topicFraudPreventionSM.value.configure(fraudPreventionSMProperties.getProperties, false)
    topicAdressCheckSM.key.configure(adressCheckSMProperties.getProperties, true)
    topicAdressCheckSM.value.configure(adressCheckSMProperties.getProperties, false)
  }
}

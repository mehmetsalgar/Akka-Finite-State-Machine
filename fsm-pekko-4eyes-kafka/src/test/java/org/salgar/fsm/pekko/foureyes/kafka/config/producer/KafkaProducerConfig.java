package org.salgar.fsm.pekko.foureyes.kafka.config.producer;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.CommonClientConfigs;
import org.salgar.fsm.pekko.foureyes.addresscheck.protobuf.AdressCheckSMCommand;
import org.salgar.fsm.pekko.foureyes.credit.kafka.config.TopicConfig;
import org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditSMCommand;
import org.salgar.fsm.pekko.foureyes.creditscore.protobuf.CreditScoreSMCommand;
import org.salgar.fsm.pekko.foureyes.fraudprevention.protobuf.FraudPreventionSMCommand;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {
    @Bean
    public ProducerFactory<String, CreditSMCommand> producerFactorCreditSM(
            TopicConfig topicConfig,
            KafkaProperties kafkaProperties) {
        return new DefaultKafkaProducerFactory<>(
                config(kafkaProperties),
                topicConfig.topicCreditSM().key().serializer(),
                topicConfig.topicCreditSM().value().serializer()
        );
    }

    @Bean
    public KafkaTemplate<String, CreditSMCommand> kafkaTemplateCreditSM(
            TopicConfig topicConfig,
            KafkaProperties kafkaProperties) {
        return new KafkaTemplate<>(producerFactorCreditSM(topicConfig, kafkaProperties));
    }

    @Bean
    public ProducerFactory<String, CreditScoreSMCommand> producerFactoryCreditScoreSM(
            TopicConfig topicConfig,
            KafkaProperties kafkaProperties) {
        return new DefaultKafkaProducerFactory<>(
                config(kafkaProperties),
                topicConfig.topicCreditScoreSM().key().serializer(),
                topicConfig.topicCreditScoreSM().value().serializer()
        );
    }

    @Bean
    public KafkaTemplate<String, CreditScoreSMCommand> kafkaTemplateCreditScoreSM(
            TopicConfig topicConfig,
            KafkaProperties kafkaProperties) {
        return new KafkaTemplate<>(producerFactoryCreditScoreSM(topicConfig, kafkaProperties));
    }

    @Bean
    public ProducerFactory<String, AdressCheckSMCommand> producerFactoryAddressCheckSM(
            TopicConfig topicConfig,
            KafkaProperties kafkaProperties) {
        return new DefaultKafkaProducerFactory<>(
                config(kafkaProperties),
                topicConfig.topicAdressCheckSM().key().serializer(),
                topicConfig.topicAdressCheckSM().value().serializer()
        );
    }

    @Bean
    public KafkaTemplate<String, AdressCheckSMCommand> kafkaTemplateAddressCheckSMSM(
            TopicConfig topicConfig,
            KafkaProperties kafkaProperties) {
        return new KafkaTemplate<>(producerFactoryAddressCheckSM(topicConfig, kafkaProperties));
    }

    @Bean
    public ProducerFactory<String, FraudPreventionSMCommand> producerFactorFraudPreventionSM(
            TopicConfig topicConfig,
            KafkaProperties kafkaProperties) {
        return new DefaultKafkaProducerFactory<>(
                config(kafkaProperties),
                topicConfig.topicFraudPreventionSM().key().serializer(),
                topicConfig.topicFraudPreventionSM().value().serializer()
        );
    }

    @Bean
    public KafkaTemplate<String, FraudPreventionSMCommand> kafkaTemplateFraudPreventionSM(
            TopicConfig topicConfig,
            KafkaProperties kafkaProperties) {
        return new KafkaTemplate<>(producerFactorFraudPreventionSM(topicConfig, kafkaProperties));
    }

    private Map<String, Object> config(KafkaProperties kafkaProperties) {
        final Map<String, Object> map = new HashMap<>(kafkaProperties.getProducer().buildProperties());
        map.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        map.put("schema.registry.url", kafkaProperties.getConsumer().getProperties().get("schema.registry.url"));
        return map;
    }
}
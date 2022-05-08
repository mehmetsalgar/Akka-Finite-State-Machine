package org.salgar.fsm.akka.foureyes.kafka.config.producer;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.CommonClientConfigs;
import org.salgar.fsm.akka.foureyes.addresscheck.protobuf.AdressCheckSMCommand;
import org.salgar.fsm.akka.foureyes.credit.kafka.config.TopicConfig;
import org.salgar.fsm.akka.foureyes.credit.protobuf.CreditSMCommand;
import org.salgar.fsm.akka.foureyes.creditscore.protobuf.CreditScoreSMCommand;
import org.salgar.fsm.akka.foureyes.fraudprevention.protobuf.FraudPreventionSMCommand;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {
    @Bean
    @Primary
    public KafkaProperties kafkaProperties() {
        return new KafkaProperties();
    }
    @Bean
    public ProducerFactory<String, CreditSMCommand> producerFactorCreditSM(
            TopicConfig topicConfig,
            @Qualifier("spring.kafka-org.springframework.boot.autoconfigure.kafka.KafkaProperties") KafkaProperties kafkaProperties) {
        return new DefaultKafkaProducerFactory<>(
                config(kafkaProperties),
                topicConfig.topicCreditSM().key().serializer(),
                topicConfig.topicCreditSM().value().serializer()
        );
    }

    @Bean
    public KafkaTemplate<String, CreditSMCommand> kafkaTemplateCreditSM(
            TopicConfig topicConfig,
            @Qualifier("spring.kafka-org.springframework.boot.autoconfigure.kafka.KafkaProperties") KafkaProperties kafkaProperties) {
        return new KafkaTemplate<>(producerFactorCreditSM(topicConfig, kafkaProperties));
    }

    @Bean
    @ConfigurationProperties("spring.kafka.credit-score-sm")
    public KafkaProperties creditScoreSMProducerProperties() {
        return new KafkaProperties();
    }

    @Bean
    public ProducerFactory<String, CreditScoreSMCommand> producerFactoryCreditScoreSM(
            TopicConfig topicConfig,
            @Qualifier("creditScoreSMProducerProperties") KafkaProperties kafkaProperties) {
        return new DefaultKafkaProducerFactory<>(
                config(kafkaProperties),
                topicConfig.topicCreditScoreSM().key().serializer(),
                topicConfig.topicCreditScoreSM().value().serializer()
        );
    }

    @Bean
    public KafkaTemplate<String, CreditScoreSMCommand> kafkaTemplateCreditScoreSM(
            TopicConfig topicConfig,
            @Qualifier("creditScoreSMProducerProperties") KafkaProperties kafkaProperties) {
        return new KafkaTemplate<>(producerFactoryCreditScoreSM(topicConfig, kafkaProperties));
    }

    @Bean
    public ProducerFactory<String, AdressCheckSMCommand> producerFactoryAddressCheckSM(
            TopicConfig topicConfig,
            @Qualifier("spring.kafka-org.springframework.boot.autoconfigure.kafka.KafkaProperties") KafkaProperties kafkaProperties) {
        return new DefaultKafkaProducerFactory<>(
                config(kafkaProperties),
                topicConfig.topicAdressCheckSM().key().serializer(),
                topicConfig.topicAdressCheckSM().value().serializer()
        );
    }

    @Bean
    public KafkaTemplate<String, AdressCheckSMCommand> kafkaTemplateAddressCheckSMSM(
            TopicConfig topicConfig,
            @Qualifier("spring.kafka-org.springframework.boot.autoconfigure.kafka.KafkaProperties") KafkaProperties kafkaProperties) {
        return new KafkaTemplate<>(producerFactoryAddressCheckSM(topicConfig, kafkaProperties));
    }

    @Bean
    public ProducerFactory<String, FraudPreventionSMCommand> producerFactorFraudPreventionSM(
            TopicConfig topicConfig,
            @Qualifier("spring.kafka-org.springframework.boot.autoconfigure.kafka.KafkaProperties") KafkaProperties kafkaProperties) {
        return new DefaultKafkaProducerFactory<>(
                config(kafkaProperties),
                topicConfig.topicFraudPreventionSM().key().serializer(),
                topicConfig.topicFraudPreventionSM().value().serializer()
        );
    }

    @Bean
    public KafkaTemplate<String, FraudPreventionSMCommand> kafkaTemplateFraudPreventionSM(
            TopicConfig topicConfig,
            @Qualifier("spring.kafka-org.springframework.boot.autoconfigure.kafka.KafkaProperties") KafkaProperties kafkaProperties) {
        return new KafkaTemplate<>(producerFactorFraudPreventionSM(topicConfig, kafkaProperties));
    }

    private Map<String, Object> config(KafkaProperties kafkaProperties) {
        final Map<String, Object> map = new HashMap<>(kafkaProperties.getProducer().buildProperties());
        map.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        String schemaRegistryUrl =
                kafkaProperties.getProperties().get("schema.registry.url") != null
                        ? kafkaProperties.getProperties().get("schema.registry.url")
                        : kafkaProperties.getConsumer().getProperties().get("schema.registry.url");
        map.put("schema.registry.url", schemaRegistryUrl);
        return map;
    }
}
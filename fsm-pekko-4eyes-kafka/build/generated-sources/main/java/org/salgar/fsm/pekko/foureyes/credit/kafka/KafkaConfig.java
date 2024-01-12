package org.salgar.fsm.pekko.foureyes.credit.kafka.config;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    @Bean
    @ConfigurationProperties("spring.kafka.consumer.credit-sm")
    public KafkaProperties.Consumer creditSMProperties() {
        return new KafkaProperties.Consumer();
    }

    @Bean
    @ConfigurationProperties("spring.kafka.consumer.credit-score-sm")
    public KafkaProperties.Consumer creditScoreSMProperties() {
        return new KafkaProperties.Consumer();
    }
    @Bean
    @ConfigurationProperties("spring.kafka.consumer.multi-tenant-credit-score-sm")
    public KafkaProperties.Consumer multiTenantCreditScoreSMProperties() {
        return new KafkaProperties.Consumer();
    }
    @Bean
    @ConfigurationProperties("spring.kafka.consumer.fraud-prevention-sm")
    public KafkaProperties.Consumer fraudPreventionSMProperties() {
        return new KafkaProperties.Consumer();
    }
    @Bean
    @ConfigurationProperties("spring.kafka.consumer.adress-check-sm")
    public KafkaProperties.Consumer adressCheckSMProperties() {
        return new KafkaProperties.Consumer();
    }
}

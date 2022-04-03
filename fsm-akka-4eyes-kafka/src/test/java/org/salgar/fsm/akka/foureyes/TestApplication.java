package org.salgar.fsm.akka.foureyes;

import lombok.RequiredArgsConstructor;
import org.salgar.fsm.akka.akkasystem.ActorService;
import org.salgar.fsm.akka.foureyes.addresscheck.actions.config.AdressCheckSMActionConfiguration;
import org.salgar.fsm.akka.foureyes.addresscheck.guards.config.AdressCheckSMGuardConfiguration;
import org.salgar.fsm.akka.foureyes.addresscheck.kafka.stream.AdressCheckSMStreamConfig;
import org.salgar.fsm.akka.foureyes.credit.actions.config.CreditSMActionConfiguration;
import org.salgar.fsm.akka.foureyes.credit.guards.config.CreditSMGuardConfiguration;
import org.salgar.fsm.akka.foureyes.credit.kafka.config.TopicConfig;
import org.salgar.fsm.akka.foureyes.credit.kafka.config.TopicProperties;
import org.salgar.fsm.akka.foureyes.credit.kafka.facade.AskFacade;
import org.salgar.fsm.akka.foureyes.credit.kafka.stream.CreditSMStreamConfig;
import org.salgar.fsm.akka.foureyes.creditscore.actions.config.CreditScoreSMActionConfiguration;
import org.salgar.fsm.akka.foureyes.creditscore.guards.config.CreditScoreSMGuardConfiguration;
import org.salgar.fsm.akka.foureyes.creditscore.kafka.stream.CreditScoreSMStreamConfig;
import org.salgar.fsm.akka.foureyes.creditscore.kafka.stream.MultiTenantCreditScoreSMStreamConfig;
import org.salgar.fsm.akka.foureyes.fraudprevention.actions.config.FraudPreventionSMActionConfiguration;
import org.salgar.fsm.akka.foureyes.fraudprevention.guards.config.FraudPreventionSMGuardConfiguration;
import org.salgar.fsm.akka.foureyes.fraudprevention.kafka.stream.FraudPreventionSMStreamConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;

@SpringBootApplication(scanBasePackages = {"org.salgar.akka",
        "org.salgar.fsm"})
@Import({
        AdressCheckSMActionConfiguration.class,
        AdressCheckSMGuardConfiguration.class,
        CreditSMActionConfiguration.class,
        CreditSMGuardConfiguration.class,
        CreditScoreSMActionConfiguration.class,
        CreditScoreSMGuardConfiguration.class,
        FraudPreventionSMActionConfiguration.class,
        FraudPreventionSMGuardConfiguration.class
})
@RequiredArgsConstructor
public class TestApplication {
    private final ActorService actorService;
    private final TopicConfig topicConfig;
    private final KafkaProperties kafkaProperties;
    @Qualifier("creditSMProperties")
    private final KafkaProperties.Consumer creditSMProperties;
    @Qualifier("creditScoreSMProperties")
    private final KafkaProperties.Consumer creditScoreSMProperties;
    @Qualifier("multiTenantCreditScoreSMProperties")
    private final KafkaProperties.Consumer multiTenantCreditScoreSMProperties;
    @Qualifier("adressCheckSMProperties")
    private final KafkaProperties.Consumer adressCheckSMProperties;
    @Qualifier("fraudPreventionSMProperties")
    private final KafkaProperties.Consumer fraudPreventionSMProperties;
    private final TopicProperties topicProperties;
    private final AskFacade askFacade;

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init(ApplicationReadyEvent applicationReadyEvent) {
        CreditSMStreamConfig.apply(
                actorService,
                topicConfig,
                kafkaProperties,
                creditSMProperties,
                topicProperties,
                askFacade);

        CreditScoreSMStreamConfig.apply(
                actorService,
                topicConfig,
                kafkaProperties,
                creditScoreSMProperties,
                topicProperties,
                askFacade);

        MultiTenantCreditScoreSMStreamConfig.apply(
                actorService,
                topicConfig,
                kafkaProperties,
                multiTenantCreditScoreSMProperties,
                topicProperties,
                askFacade);

        AdressCheckSMStreamConfig.apply(
                actorService,
                topicConfig,
                kafkaProperties,
                adressCheckSMProperties,
                topicProperties,
                askFacade);

        FraudPreventionSMStreamConfig.apply(
                actorService,
                topicConfig,
                kafkaProperties,
                fraudPreventionSMProperties,
                topicProperties,
                askFacade);
    }
}
package org.salgar.fsm.pekko.foureyes;

import lombok.RequiredArgsConstructor;
import org.salgar.fsm.pekko.foureyes.addresscheck.actions.config.AdressCheckSMActionConfiguration;
import org.salgar.fsm.pekko.foureyes.addresscheck.guards.config.AdressCheckSMGuardConfiguration;
import org.salgar.fsm.pekko.foureyes.addresscheck.kafka.stream.AdressCheckSMStreamConfig;
import org.salgar.fsm.pekko.foureyes.addresscheck.protobuf.AdressCheckSMCommand;
import org.salgar.fsm.pekko.foureyes.credit.actions.config.CreditSMActionConfiguration;
import org.salgar.fsm.pekko.foureyes.credit.guards.config.CreditSMGuardConfiguration;
import org.salgar.fsm.pekko.foureyes.credit.kafka.config.TopicConfig;
import org.salgar.fsm.pekko.foureyes.credit.kafka.config.TopicProperties;
import org.salgar.fsm.pekko.foureyes.credit.kafka.facade.AskFacade;
import org.salgar.fsm.pekko.foureyes.credit.kafka.stream.CreditSMStreamConfig;
import org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditSMCommand;
import org.salgar.fsm.pekko.foureyes.creditscore.actions.config.CreditScoreSMActionConfiguration;
import org.salgar.fsm.pekko.foureyes.creditscore.guards.config.CreditScoreSMGuardConfiguration;
import org.salgar.fsm.pekko.foureyes.creditscore.kafka.stream.CreditScoreSMStreamConfig;
import org.salgar.fsm.pekko.foureyes.creditscore.kafka.stream.MultiTenantCreditScoreSMStreamConfig;
import org.salgar.fsm.pekko.foureyes.creditscore.protobuf.CreditScoreSMCommand;
import org.salgar.fsm.pekko.foureyes.creditscore.protobuf.MultiTenantCreditScoreSMCommand;
import org.salgar.fsm.pekko.foureyes.fraudprevention.actions.config.FraudPreventionSMActionConfiguration;
import org.salgar.fsm.pekko.foureyes.fraudprevention.actions.guards.config.FraudPreventionSMGuardConfiguration;
import org.salgar.fsm.pekko.foureyes.fraudprevention.kafka.stream.FraudPreventionSMStreamConfig;
import org.salgar.fsm.pekko.foureyes.fraudprevention.protobuf.FraudPreventionSMCommand;
import org.salgar.fsm.pekko.kafka.config.ConsumerConfig;
import org.salgar.fsm.pekko.pekkosystem.ActorService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;

@SpringBootApplication(scanBasePackages = {"org.salgar.pekko",
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
    private final ConsumerConfig<String, CreditSMCommand> creditSMConsumerConfig;
    private final ConsumerConfig<String, CreditScoreSMCommand> creditScoreSMConsumerConfig;
    private final ConsumerConfig<String, MultiTenantCreditScoreSMCommand> multiTenantCreditScoreSMConsumerConfig;
    private final ConsumerConfig<String, AdressCheckSMCommand> adressCheckSMConsumerConfig;
    private final ConsumerConfig<String, FraudPreventionSMCommand> fraudPreventionSMConsumerConfig;

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init(ApplicationReadyEvent applicationReadyEvent) {
        CreditSMStreamConfig.apply(
                creditSMConsumerConfig,
                actorService,
                topicProperties,
                askFacade);

        CreditScoreSMStreamConfig.apply(
                creditScoreSMConsumerConfig,
                actorService,
                topicProperties,
                askFacade);

        MultiTenantCreditScoreSMStreamConfig.apply(
                multiTenantCreditScoreSMConsumerConfig,
                actorService,
                topicProperties,
                askFacade);

        AdressCheckSMStreamConfig.apply(
                adressCheckSMConsumerConfig,
                actorService,
                topicProperties,
                askFacade);

        FraudPreventionSMStreamConfig.apply(
                fraudPreventionSMConsumerConfig,
                actorService,
                topicProperties,
                askFacade);
    }
}
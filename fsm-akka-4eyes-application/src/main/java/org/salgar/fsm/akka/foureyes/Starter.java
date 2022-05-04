package org.salgar.fsm.akka.foureyes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.salgar.fsm.akka.akkasystem.ActorService;
import org.salgar.fsm.akka.elasticsearch.OffsetFacade;
import org.salgar.fsm.akka.foureyes.addresscheck.kafka.stream.AdressCheckSMStreamConfig;
import org.salgar.fsm.akka.foureyes.addresscheck.protobuf.AdressCheckSMCommand;
import org.salgar.fsm.akka.foureyes.credit.kafka.config.TopicConfig;
import org.salgar.fsm.akka.foureyes.credit.kafka.config.TopicProperties;
import org.salgar.fsm.akka.foureyes.credit.kafka.facade.AskFacade;
import org.salgar.fsm.akka.foureyes.credit.kafka.stream.CreditSMStreamConfig;
import org.salgar.fsm.akka.foureyes.credit.protobuf.CreditSMCommand;
import org.salgar.fsm.akka.foureyes.creditscore.kafka.stream.CreditScoreSMStreamConfig;
import org.salgar.fsm.akka.foureyes.creditscore.kafka.stream.MultiTenantCreditScoreSMStreamConfig;
import org.salgar.fsm.akka.foureyes.creditscore.protobuf.CreditScoreSMCommand;
import org.salgar.fsm.akka.foureyes.creditscore.protobuf.MultiTenantCreditScoreSMCommand;
import org.salgar.fsm.akka.foureyes.fraudprevention.kafka.stream.FraudPreventionSMStreamConfig;
import org.salgar.fsm.akka.foureyes.fraudprevention.protobuf.FraudPreventionSMCommand;
import org.salgar.fsm.akka.foureyes.projections.CreditSMProjection;
import org.salgar.fsm.akka.foureyes.projections.CreditSMProjectionHandler;
import org.salgar.fsm.akka.kafka.config.ConsumerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Starter {
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
    private final CreditSMProjectionHandler creditSMProjectionHandler;
    private final OffsetFacade offsetFacade;
    private final ConsumerConfig<String, CreditSMCommand> creditSMConsumerConfig;
    private final ConsumerConfig<String, CreditScoreSMCommand> creditScoreSMConsumerConfig;
    private final ConsumerConfig<String, MultiTenantCreditScoreSMCommand> multiTenantCreditScoreSMConsumerConfig;
    private final ConsumerConfig<String, AdressCheckSMCommand> adressCheckSMConsumerConfig;
    private final ConsumerConfig<String, FraudPreventionSMCommand> fraudPreventionSMConsumerConfig;

    @EventListener(ApplicationReadyEvent.class)
    private void initialized(ApplicationReadyEvent applicationReadyEvent) {
        log.info("FSM Akka 4eyes Initialized!");

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

        CreditSMProjection.init(
                actorService.actorSystem(),
                creditSMProjectionHandler,
                offsetFacade);
    }
}

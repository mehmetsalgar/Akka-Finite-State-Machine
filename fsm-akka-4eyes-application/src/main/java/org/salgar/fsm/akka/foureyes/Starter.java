package org.salgar.fsm.akka.foureyes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.salgar.fsm.akka.akkasystem.ActorService;
import org.salgar.fsm.akka.elasticsearch.OffsetFacade;
import org.salgar.fsm.akka.foureyes.addresscheck.kafka.stream.AdressCheckSMStreamConfig;
import org.salgar.fsm.akka.foureyes.credit.kafka.config.TopicConfig;
import org.salgar.fsm.akka.foureyes.credit.kafka.config.TopicProperties;
import org.salgar.fsm.akka.foureyes.credit.kafka.facade.AskFacade;
import org.salgar.fsm.akka.foureyes.credit.kafka.stream.CreditSMStreamConfig;
import org.salgar.fsm.akka.foureyes.creditscore.kafka.stream.CreditScoreSMStreamConfig;
import org.salgar.fsm.akka.foureyes.creditscore.kafka.stream.MultiTenantCreditScoreSMStreamConfig;
import org.salgar.fsm.akka.foureyes.fraudprevention.kafka.stream.FraudPreventionSMStreamConfig;
import org.salgar.fsm.akka.foureyes.projections.CreditSMProjection;
import org.salgar.fsm.akka.foureyes.projections.CreditSMProjectionHandler;
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

    @EventListener(ApplicationReadyEvent.class)
    private void initialized(ApplicationReadyEvent applicationReadyEvent) {
        log.info("FSM Akka 4eyes Initialized!");

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

        CreditSMProjection.init(
                actorService.actorSystem(),
                creditSMProjectionHandler,
                offsetFacade);
    }
}

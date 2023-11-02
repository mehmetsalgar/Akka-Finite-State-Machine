package org.salgar.fsm.pekko.foureyes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.salgar.fsm.pekko.elasticsearch.OffsetFacade;
import org.salgar.fsm.pekko.foureyes.addresscheck.kafka.stream.AdressCheckSMStreamConfig;
import org.salgar.fsm.pekko.foureyes.addresscheck.protobuf.AdressCheckSMCommand;
import org.salgar.fsm.pekko.foureyes.credit.kafka.config.TopicProperties;
import org.salgar.fsm.pekko.foureyes.credit.kafka.facade.AskFacade;
import org.salgar.fsm.pekko.foureyes.credit.kafka.stream.CreditSMStreamConfig;
import org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditSMCommand;
import org.salgar.fsm.pekko.foureyes.creditscore.kafka.stream.CreditScoreSMStreamConfig;
import org.salgar.fsm.pekko.foureyes.creditscore.kafka.stream.MultiTenantCreditScoreSMStreamConfig;
import org.salgar.fsm.pekko.foureyes.creditscore.protobuf.CreditScoreSMCommand;
import org.salgar.fsm.pekko.foureyes.creditscore.protobuf.MultiTenantCreditScoreSMCommand;
import org.salgar.fsm.pekko.foureyes.fraudprevention.kafka.stream.FraudPreventionSMStreamConfig;
import org.salgar.fsm.pekko.foureyes.fraudprevention.protobuf.FraudPreventionSMCommand;
import org.salgar.fsm.pekko.foureyes.projections.CreditSMProjection;
import org.salgar.fsm.pekko.foureyes.projections.CreditSMProjectionHandler;
import org.salgar.fsm.pekko.kafka.config.ConsumerConfig;
import org.salgar.fsm.pekko.pekkosystem.ActorService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Starter {
    private final ActorService actorService;
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
    public void initialised(ApplicationReadyEvent applicationReadyEvent) {
        log.info("FSM Pekko 4eyes Initialised!");

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

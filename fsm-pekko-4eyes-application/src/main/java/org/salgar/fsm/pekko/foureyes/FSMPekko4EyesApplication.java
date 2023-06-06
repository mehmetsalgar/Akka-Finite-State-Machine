package org.salgar.fsm.pekko.foureyes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.salgar.fsm.pekko.foureyes.addresscheck.actions.config.AdressCheckSMActionConfiguration;
import org.salgar.fsm.pekko.foureyes.addresscheck.guards.config.AdressCheckSMGuardConfiguration;
import org.salgar.fsm.pekko.foureyes.credit.actions.config.CreditSMActionConfiguration;
import org.salgar.fsm.pekko.foureyes.credit.guards.config.CreditSMGuardConfiguration;
import org.salgar.fsm.pekko.foureyes.creditscore.actions.config.CreditScoreSMActionConfiguration;
import org.salgar.fsm.pekko.foureyes.creditscore.guards.config.CreditScoreSMGuardConfiguration;
import org.salgar.fsm.pekko.foureyes.fraudprevention.actions.config.FraudPreventionSMActionConfiguration;
import org.salgar.fsm.pekko.foureyes.fraudprevention.actions.guards.config.FraudPreventionSMGuardConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

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
@EnableElasticsearchRepositories("org.salgar.fsm.pekko.elasticsearch")
@RequiredArgsConstructor
@Slf4j
@SpringBootApplication(scanBasePackages = {"org.salgar.pekko",
        "org.salgar.fsm"})
public class FSMPekko4EyesApplication {
    public static void main(String[] args) {
        log.info("Starting!");
        new SpringApplicationBuilder(FSMPekko4EyesApplication.class)
                .registerShutdownHook(true)
                .run( args);
    }
}
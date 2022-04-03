package org.salgar.akka.fsm.foureyes.creditscore.kafka;

import lombok.extern.slf4j.Slf4j;
import org.salgar.akka.fsm.foureyes.creditscore.CreditScoreService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreditScoreServiceKafkaImpl implements CreditScoreService {
    @Override
    public void calculateCreditScore(String firstName, String lastName, String peronalId) {
        log.info("Kafka configuration is not finished");
    }
}
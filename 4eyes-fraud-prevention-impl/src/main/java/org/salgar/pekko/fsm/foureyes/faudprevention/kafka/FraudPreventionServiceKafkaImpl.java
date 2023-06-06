package org.salgar.pekko.fsm.foureyes.faudprevention.kafka;

import lombok.extern.slf4j.Slf4j;
import org.salgar.pekko.fsm.foureyes.faudprevention.FraudPreventionService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FraudPreventionServiceKafkaImpl implements FraudPreventionService {
    @Override
    public void reportFraudPrevention(String firstName, String lastName, String personalId) {
        log.info("Kafka configuration is not finished");
    }
}
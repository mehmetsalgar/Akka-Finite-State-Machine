package org.salgar.pekko.fsm.foureyes.notifier.kafka;

import lombok.extern.slf4j.Slf4j;
import org.salgar.pekko.fsm.foureyes.notifier.NotifierService;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class NotifierServiceKafkaImpl implements NotifierService {
    @Override
    public void notify(List<String> notificationTargets, String message) {
        log.info("Kafka configuration is not finished");
    }

    @Override
    public List<String> calculateRecipientList(String targetGroup) {
        log.info("Kafka configuration is not finished");
        return Collections.emptyList();
    }
}
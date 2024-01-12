package org.salgar.fsm.pekko.foureyes.credit.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("org.salgar.fsm.pekko.foureyes.credit.kafka.topics")
@Data
public class TopicProperties {
    private String creditSM;
    private String creditScoreSM;
    private String multiTenantCreditScoreSM;
    private String fraudPreventionSM;
    private String adressCheckSM;
}

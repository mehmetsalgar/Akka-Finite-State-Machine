package org.salgar.fsm.pekko.elasticsearch.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("org.salgar.fsm.pekko.foureyes.elasticsearch")
@Data
public class ElasticsearchProperties {
    private String url;
    private Integer batchSize;
    private Long bulkSize;
    private Long lingerMs;
    private Integer maxInFlightRequests;
    private Integer maxRetries;
    private Long retryBackoffMs;
    private BehaviorOnMalformedDoc behaviorOnMalformedDoc;
    private Long flushTimeoutMs;
}
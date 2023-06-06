package org.salgar.fsm.pekko.elasticsearch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {
    private final ElasticsearchProperties elasticsearchProperties;
    private final List<Converter> customConverters;

    @Override
    public RestHighLevelClient elasticsearchClient() {
        log.info("Connecting to: {}", elasticsearchProperties.getUrl());
        ClientConfiguration clientConfiguration =
                ClientConfiguration
                        .builder()
                        .connectedTo(elasticsearchProperties.getUrl())
                        .build();
        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    @Override
    public ElasticsearchCustomConversions elasticsearchCustomConversions() {
        return new ElasticsearchCustomConversions(customConverters);
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(elasticsearchProperties.getMaxInFlightRequests());
    }
}
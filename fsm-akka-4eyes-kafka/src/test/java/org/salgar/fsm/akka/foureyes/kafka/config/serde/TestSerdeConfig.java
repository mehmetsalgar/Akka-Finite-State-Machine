package org.salgar.fsm.akka.foureyes.kafka.config.serde;

import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import org.salgar.fsm.akka.kafka.stream.SerdeFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TestSerdeConfig {
    @Bean
    @Primary
    public SerdeFactory testSerdeFactory(SchemaRegistryClient schemaRegistryClient) {
        return new TestSerdeFactoryImpl(schemaRegistryClient);
    }
}
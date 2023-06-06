package org.salgar.fsm.pekko.foureyes.kafka.schemaregsitry;

import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MockSchemaRegistryConfig {
    @Bean
    public SchemaRegistryClient schemaRegistryClient() {
        return new MockSchemaRegistryClient();
    }
}
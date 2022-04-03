package org.salgar.fsm.akka.kafka.config;

import org.salgar.fsm.akka.kafka.stream.SerdeFactory;
import org.salgar.fsm.akka.kafka.stream.SerdeFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SerdeConfig {
    @Bean
    public SerdeFactory serdeFactory() {
        return new SerdeFactoryImpl();
    }
}
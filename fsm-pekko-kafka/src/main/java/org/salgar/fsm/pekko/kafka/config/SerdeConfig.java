package org.salgar.fsm.pekko.kafka.config;

import org.salgar.fsm.pekko.kafka.stream.SerdeFactory;
import org.salgar.fsm.pekko.kafka.stream.SerdeFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SerdeConfig {
    @Bean
    public SerdeFactory serdeFactory() {
        return new SerdeFactoryImpl();
    }
}
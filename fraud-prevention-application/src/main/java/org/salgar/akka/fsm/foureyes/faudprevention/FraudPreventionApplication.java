package org.salgar.akka.fsm.foureyes.faudprevention;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class FraudPreventionApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(FraudPreventionApplication.class)
                .registerShutdownHook(true)
                .run(args);
    }
}

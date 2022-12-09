package org.salgar.akka.fsm.foureyes.creditscore;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class CreditScoreApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(CreditScoreApplication.class)
                .registerShutdownHook(true)
                .run(args);
    }
}
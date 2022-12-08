package org.salgar.akka.fsm.foureyes.addresscheck;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class AddressCheckApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(AddressCheckApplication.class)
                .registerShutdownHook(true)
                .run(args);
    }
}
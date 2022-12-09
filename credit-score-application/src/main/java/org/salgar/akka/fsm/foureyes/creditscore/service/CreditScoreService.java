package org.salgar.akka.fsm.foureyes.creditscore.service;

import org.springframework.stereotype.Component;

@Component
public class CreditScoreService {
    public void scoreCheck() {
        System.out.println("Credit Score Check");
    }
}
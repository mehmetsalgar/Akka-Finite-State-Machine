package org.salgar.akka.fsm.foureyes.creditscore;

public interface CreditScoreService {
    void calculateCreditScore(
            String firstName,
            String lastName,
            String peronalId);
}
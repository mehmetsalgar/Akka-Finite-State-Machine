package org.salgar.pekko.fsm.foureyes.creditscore;

public interface CreditScoreService {
    void calculateCreditScore(
            String firstName,
            String lastName,
            String peronalId);
}
package org.salgar.pekko.fsm.foureyes.faudprevention;

public interface FraudPreventionService {
    void reportFraudPrevention(String firstName, String lastName, String personalId);
}
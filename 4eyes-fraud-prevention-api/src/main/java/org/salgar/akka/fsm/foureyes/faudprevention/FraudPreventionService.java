package org.salgar.akka.fsm.foureyes.faudprevention;

public interface FraudPreventionService {
    void reportFraudPrevention(String firstName, String lastName, String personalId);
}
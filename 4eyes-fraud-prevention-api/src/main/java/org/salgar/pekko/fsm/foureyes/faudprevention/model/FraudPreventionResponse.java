package org.salgar.pekko.fsm.foureyes.faudprevention.model;

import lombok.Data;

@Data
public class FraudPreventionResponse {
    private double transactionVolume;
    private double finalApprovalRate;
    private double bankAcceptanceRate;
    private double chargeBackRate;
}
package org.salgar.fsm.pekko.foureyes.credit.guards;


import org.salgar.fsm.pekko.foureyes.credit.model.CreditTenantScoreResult;

import java.util.Map;

public class CreditScoreGaurdUtilities {
    public static boolean checkCreditScore(Map<String, CreditTenantScoreResult> creditScores, Double lowLimit, Double highLimit) {
        for(CreditTenantScoreResult creditTenantScoreResult : creditScores.values()) {
            if(creditTenantScoreResult.getCreditScore() >= lowLimit && creditTenantScoreResult.getCreditScore() <= highLimit) {
                return true;
            }
        }

        return false;
    }
}
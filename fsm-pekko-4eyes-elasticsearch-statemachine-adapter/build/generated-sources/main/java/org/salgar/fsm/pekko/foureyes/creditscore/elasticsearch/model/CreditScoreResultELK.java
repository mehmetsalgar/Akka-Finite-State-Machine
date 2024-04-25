package org.salgar.fsm.pekko.foureyes.creditscore.elasticsearch.model;

import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
public class CreditScoreResultELK {
    private org.salgar.fsm.pekko.foureyes.creditscore.model.CreditScoreResult _creditScoreResult;
    private CreditScoreResultELK() {
    }
    public CreditScoreResultELK(org.salgar.fsm.pekko.foureyes.creditscore.model.CreditScoreResult _creditScoreResult) {
        this._creditScoreResult = _creditScoreResult;
    }
    @Field(type = FieldType.Double)
    private final Double creditScore = _creditScoreResult.getCreditScore() ;
}

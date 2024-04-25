package org.salgar.fsm.pekko.foureyes.credit.elasticsearch.model;

import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
public class CreditTenantScoreResultELK {
    private org.salgar.fsm.pekko.foureyes.credit.model.CreditTenantScoreResult _creditTenantScoreResult;
    private CreditTenantScoreResultELK() {
    }
    public CreditTenantScoreResultELK(org.salgar.fsm.pekko.foureyes.credit.model.CreditTenantScoreResult _creditTenantScoreResult) {
        this._creditTenantScoreResult = _creditTenantScoreResult;
    }
    @Field(type = FieldType.Text)
    private final String personalId = _creditTenantScoreResult.getPersonalId() ;
    @Field(type = FieldType.Double)
    private final Double creditScore = _creditTenantScoreResult.getCreditScore() ;
}

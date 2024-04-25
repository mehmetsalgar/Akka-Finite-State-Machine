package org.salgar.fsm.pekko.foureyes.credit.elasticsearch.model;

import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
public class CreditApplicationELK {
    private org.salgar.fsm.pekko.foureyes.credit.model.CreditApplication _creditApplication;
    private CreditApplicationELK() {
    }
    public CreditApplicationELK(org.salgar.fsm.pekko.foureyes.credit.model.CreditApplication _creditApplication) {
        this._creditApplication = _creditApplication;
    }
    @Field(type = FieldType.Double)
    private final Double creditAmount = _creditApplication.getCreditAmount() ;
    @Field(type = FieldType.Nested)
    private final org.salgar.fsm.pekko.foureyes.credit.model.CreditTenants creditTenants = _creditApplication.getCreditTenants();
}

package org.salgar.fsm.pekko.foureyes.credit.elasticsearch.model;

import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
public class CreditTenantsELK {
    private org.salgar.fsm.pekko.foureyes.credit.model.CreditTenants _creditTenants;
    private CreditTenantsELK() {
    }
    public CreditTenantsELK(org.salgar.fsm.pekko.foureyes.credit.model.CreditTenants _creditTenants) {
        this._creditTenants = _creditTenants;
    }
    @Field(type = FieldType.Nested)
    private final java.util.List< org.salgar.fsm.pekko.foureyes.credit.model.Customer> creditTenants = _creditTenants.getCreditTenants();
}

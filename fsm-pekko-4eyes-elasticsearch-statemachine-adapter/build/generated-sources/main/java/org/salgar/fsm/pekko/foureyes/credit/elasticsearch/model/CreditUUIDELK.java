package org.salgar.fsm.pekko.foureyes.credit.elasticsearch.model;

import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
public class CreditUUIDELK {
    private org.salgar.fsm.pekko.foureyes.credit.model.CreditUUID _creditUUID;
    private CreditUUIDELK() {
    }
    public CreditUUIDELK(org.salgar.fsm.pekko.foureyes.credit.model.CreditUUID _creditUUID) {
        this._creditUUID = _creditUUID;
    }
    @Field(type = FieldType.Text)
    private final String creditUUID = _creditUUID.getCreditUUID() ;
}

package org.salgar.fsm.pekko.foureyes.addresscheck.elasticsearch.model;

import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
public class AddressCheckResultELK {
    private org.salgar.fsm.pekko.foureyes.addresscheck.model.AddressCheckResult _addressCheckResult;
    private AddressCheckResultELK() {
    }
    public AddressCheckResultELK(org.salgar.fsm.pekko.foureyes.addresscheck.model.AddressCheckResult _addressCheckResult) {
        this._addressCheckResult = _addressCheckResult;
    }
    @Field(type = FieldType.Text)
    private final Boolean addressCheckResult = _addressCheckResult.getAddressCheckResult() ;
}

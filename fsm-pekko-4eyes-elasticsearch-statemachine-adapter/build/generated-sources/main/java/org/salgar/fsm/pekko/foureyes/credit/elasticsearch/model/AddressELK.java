package org.salgar.fsm.pekko.foureyes.credit.elasticsearch.model;

import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
public class AddressELK {
    private org.salgar.fsm.pekko.foureyes.credit.model.Address _address;
    private AddressELK() {
    }
    public AddressELK(org.salgar.fsm.pekko.foureyes.credit.model.Address _address) {
        this._address = _address;
    }
    @Field(type = FieldType.Text)
    private final String street = _address.getStreet() ;
    @Field(type = FieldType.Text)
    private final String houseNo = _address.getHouseNo() ;
    @Field(type = FieldType.Text)
    private final String city = _address.getCity() ;
    @Field(type = FieldType.Text)
    private final String country = _address.getCountry() ;
}

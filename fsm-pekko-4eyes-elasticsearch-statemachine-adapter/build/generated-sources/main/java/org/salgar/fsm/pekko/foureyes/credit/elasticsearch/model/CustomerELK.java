package org.salgar.fsm.pekko.foureyes.credit.elasticsearch.model;

import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
public class CustomerELK {
    private org.salgar.fsm.pekko.foureyes.credit.model.Customer _customer;
    private CustomerELK() {
    }
    public CustomerELK(org.salgar.fsm.pekko.foureyes.credit.model.Customer _customer) {
        this._customer = _customer;
    }
    @Field(type = FieldType.Text)
    private final String firstName = _customer.getFirstName() ;
    @Field(type = FieldType.Text)
    private final String lastName = _customer.getLastName() ;
    @Field(type = FieldType.Text)
    private final String personalId = _customer.getPersonalId() ;
    @Field(type = FieldType.Nested)
    private final org.salgar.fsm.pekko.foureyes.credit.model.Address address = _customer.getAddress();
    @Field(type = FieldType.Text)
    private final String email = _customer.getEmail() ;
}

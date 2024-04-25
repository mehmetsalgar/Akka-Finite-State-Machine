package org.salgar.fsm.pekko.foureyes.credit.elasticsearch.converter;

import org.salgar.fsm.pekko.foureyes.credit.elasticsearch.model.CustomerELK;
import org.salgar.fsm.pekko.foureyes.credit.model.Customer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class CustomerElkConverter implements Converter<Customer, CustomerELK> {
    @Override
    public CustomerELK convert(Customer customer) {
        return new CustomerELK(customer);
    }
}

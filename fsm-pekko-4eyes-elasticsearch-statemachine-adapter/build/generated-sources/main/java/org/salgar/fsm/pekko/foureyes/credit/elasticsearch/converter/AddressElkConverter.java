package org.salgar.fsm.pekko.foureyes.credit.elasticsearch.converter;

import org.salgar.fsm.pekko.foureyes.credit.elasticsearch.model.AddressELK;
import org.salgar.fsm.pekko.foureyes.credit.model.Address;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class AddressElkConverter implements Converter<Address, AddressELK> {
    @Override
    public AddressELK convert(Address address) {
        return new AddressELK(address);
    }
}

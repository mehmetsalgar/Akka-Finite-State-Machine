package org.salgar.fsm.pekko.foureyes.addresscheck.elasticsearch.converter;

import org.salgar.fsm.pekko.foureyes.addresscheck.elasticsearch.model.AddressCheckResultELK;
import org.salgar.fsm.pekko.foureyes.addresscheck.model.AddressCheckResult;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class AddressCheckResultElkConverter implements Converter<AddressCheckResult, AddressCheckResultELK> {
    @Override
    public AddressCheckResultELK convert(AddressCheckResult addressCheckResult) {
        return new AddressCheckResultELK(addressCheckResult);
    }
}

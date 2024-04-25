package org.salgar.fsm.pekko.foureyes.credit.elasticsearch.converter;

import org.salgar.fsm.pekko.foureyes.credit.elasticsearch.model.CreditUUIDELK;
import org.salgar.fsm.pekko.foureyes.credit.model.CreditUUID;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class CreditUUIDElkConverter implements Converter<CreditUUID, CreditUUIDELK> {
    @Override
    public CreditUUIDELK convert(CreditUUID creditUUID) {
        return new CreditUUIDELK(creditUUID);
    }
}

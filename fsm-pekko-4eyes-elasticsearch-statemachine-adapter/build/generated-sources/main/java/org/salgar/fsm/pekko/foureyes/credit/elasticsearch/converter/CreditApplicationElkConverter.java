package org.salgar.fsm.pekko.foureyes.credit.elasticsearch.converter;

import org.salgar.fsm.pekko.foureyes.credit.elasticsearch.model.CreditApplicationELK;
import org.salgar.fsm.pekko.foureyes.credit.model.CreditApplication;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class CreditApplicationElkConverter implements Converter<CreditApplication, CreditApplicationELK> {
    @Override
    public CreditApplicationELK convert(CreditApplication creditApplication) {
        return new CreditApplicationELK(creditApplication);
    }
}

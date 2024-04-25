package org.salgar.fsm.pekko.foureyes.credit.elasticsearch.converter;

import org.salgar.fsm.pekko.foureyes.credit.elasticsearch.model.CreditTenantsELK;
import org.salgar.fsm.pekko.foureyes.credit.model.CreditTenants;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class CreditTenantsElkConverter implements Converter<CreditTenants, CreditTenantsELK> {
    @Override
    public CreditTenantsELK convert(CreditTenants creditTenants) {
        return new CreditTenantsELK(creditTenants);
    }
}

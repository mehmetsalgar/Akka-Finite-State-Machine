package org.salgar.fsm.pekko.foureyes.credit.elasticsearch.converter;

import org.salgar.fsm.pekko.foureyes.credit.elasticsearch.model.CreditTenantScoreResultELK;
import org.salgar.fsm.pekko.foureyes.credit.model.CreditTenantScoreResult;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class CreditTenantScoreResultElkConverter implements Converter<CreditTenantScoreResult, CreditTenantScoreResultELK> {
    @Override
    public CreditTenantScoreResultELK convert(CreditTenantScoreResult creditTenantScoreResult) {
        return new CreditTenantScoreResultELK(creditTenantScoreResult);
    }
}

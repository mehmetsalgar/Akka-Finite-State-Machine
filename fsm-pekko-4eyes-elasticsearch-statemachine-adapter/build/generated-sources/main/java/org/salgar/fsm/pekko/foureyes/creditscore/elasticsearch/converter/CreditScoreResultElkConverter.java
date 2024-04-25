package org.salgar.fsm.pekko.foureyes.creditscore.elasticsearch.converter;

import org.salgar.fsm.pekko.foureyes.creditscore.elasticsearch.model.CreditScoreResultELK;
import org.salgar.fsm.pekko.foureyes.creditscore.model.CreditScoreResult;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class CreditScoreResultElkConverter implements Converter<CreditScoreResult, CreditScoreResultELK> {
    @Override
    public CreditScoreResultELK convert(CreditScoreResult creditScoreResult) {
        return new CreditScoreResultELK(creditScoreResult);
    }
}

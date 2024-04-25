package org.salgar.fsm.pekko.foureyes.fraudprevention.elasticsearch.converter;

import org.salgar.fsm.pekko.foureyes.fraudprevention.elasticsearch.model.FraudPreventionResultELK;
import org.salgar.fsm.pekko.foureyes.fraudprevention.model.FraudPreventionResult;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class FraudPreventionResultElkConverter implements Converter<FraudPreventionResult, FraudPreventionResultELK> {
    @Override
    public FraudPreventionResultELK convert(FraudPreventionResult fraudPreventionResult) {
        return new FraudPreventionResultELK(fraudPreventionResult);
    }
}

package org.salgar.fsm.pekko.foureyes.creditscore.converter;

import lombok.RequiredArgsConstructor;
import org.salgar.fsm.pekko.converter.Protobuf2PojoConverter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreditScoreResultProtobuf2PojoConverter
        implements Protobuf2PojoConverter<org.salgar.fsm.pekko.foureyes.creditscore.protobuf.CreditScoreResult, Double> {

    @Override
    public Double convert(org.salgar.fsm.pekko.foureyes.creditscore.protobuf.CreditScoreResult creditScoreResult) {
        return
                creditScoreResult.getCreditScore()
            ;
    }

    @Override
    public String canConvert() {
        return PROTOBUF_PREFIX + org.salgar.fsm.pekko.foureyes.creditscore.protobuf.CreditScoreResult.getDescriptor().getFullName();
    }

    @Override
    public Class<org.salgar.fsm.pekko.foureyes.creditscore.protobuf.CreditScoreResult> destinationType() {
        return org.salgar.fsm.pekko.foureyes.creditscore.protobuf.CreditScoreResult.class;
    }
}

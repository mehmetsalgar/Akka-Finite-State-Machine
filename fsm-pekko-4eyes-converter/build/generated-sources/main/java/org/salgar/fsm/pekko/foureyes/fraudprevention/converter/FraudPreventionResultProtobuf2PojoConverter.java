package org.salgar.fsm.pekko.foureyes.fraudprevention.converter;

import lombok.RequiredArgsConstructor;
import org.salgar.fsm.pekko.converter.Protobuf2PojoConverter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FraudPreventionResultProtobuf2PojoConverter
        implements Protobuf2PojoConverter<org.salgar.fsm.pekko.foureyes.fraudprevention.protobuf.FraudPreventionResult, Boolean> {

    @Override
    public Boolean convert(org.salgar.fsm.pekko.foureyes.fraudprevention.protobuf.FraudPreventionResult fraudPreventionResult) {
        return
                fraudPreventionResult.getFraudPreventionResult()
            ;
    }

    @Override
    public String canConvert() {
        return PROTOBUF_PREFIX + org.salgar.fsm.pekko.foureyes.fraudprevention.protobuf.FraudPreventionResult.getDescriptor().getFullName();
    }

    @Override
    public Class<org.salgar.fsm.pekko.foureyes.fraudprevention.protobuf.FraudPreventionResult> destinationType() {
        return org.salgar.fsm.pekko.foureyes.fraudprevention.protobuf.FraudPreventionResult.class;
    }
}

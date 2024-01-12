package org.salgar.fsm.pekko.foureyes.addresscheck.converter;

import lombok.RequiredArgsConstructor;
import org.salgar.fsm.pekko.converter.Protobuf2PojoConverter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressCheckResultProtobuf2PojoConverter
        implements Protobuf2PojoConverter<org.salgar.fsm.pekko.foureyes.addresscheck.protobuf.AddressCheckResult, Boolean> {

    @Override
    public Boolean convert(org.salgar.fsm.pekko.foureyes.addresscheck.protobuf.AddressCheckResult addressCheckResult) {
        return
                addressCheckResult.getAddressCheckResult()
            ;
    }

    @Override
    public String canConvert() {
        return PROTOBUF_PREFIX + org.salgar.fsm.pekko.foureyes.addresscheck.protobuf.AddressCheckResult.getDescriptor().getFullName();
    }

    @Override
    public Class<org.salgar.fsm.pekko.foureyes.addresscheck.protobuf.AddressCheckResult> destinationType() {
        return org.salgar.fsm.pekko.foureyes.addresscheck.protobuf.AddressCheckResult.class;
    }
}

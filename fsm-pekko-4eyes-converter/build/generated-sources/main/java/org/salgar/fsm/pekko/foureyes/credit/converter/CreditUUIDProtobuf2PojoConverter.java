package org.salgar.fsm.pekko.foureyes.credit.converter;

import lombok.RequiredArgsConstructor;
import org.salgar.fsm.pekko.converter.Protobuf2PojoConverter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreditUUIDProtobuf2PojoConverter
        implements Protobuf2PojoConverter<org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID, String> {

    @Override
    public String convert(org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID creditUUID) {
        return
                creditUUID.getCreditUUID()
            ;
    }

    @Override
    public String canConvert() {
        return PROTOBUF_PREFIX + org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID.getDescriptor().getFullName();
    }

    @Override
    public Class<org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID> destinationType() {
        return org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID.class;
    }
}

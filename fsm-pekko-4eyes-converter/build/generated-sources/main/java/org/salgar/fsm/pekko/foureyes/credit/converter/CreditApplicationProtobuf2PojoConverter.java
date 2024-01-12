package org.salgar.fsm.pekko.foureyes.credit.converter;

import lombok.RequiredArgsConstructor;
import org.salgar.fsm.pekko.converter.Protobuf2PojoConverter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreditApplicationProtobuf2PojoConverter
        implements Protobuf2PojoConverter<org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditApplication, org.salgar.fsm.pekko.foureyes.credit.model.CreditApplication> {
    private final org.salgar.fsm.pekko.foureyes.credit.converter.CreditTenantsProtobuf2PojoConverter credittenantsProtobuf2PojoConverter;

    @Override
    public org.salgar.fsm.pekko.foureyes.credit.model.CreditApplication convert(org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditApplication creditApplication) {
        return new org.salgar.fsm.pekko.foureyes.credit.model.CreditApplication(
                creditApplication.getCreditAmount(),
                new org.salgar.fsm.pekko.foureyes.credit.model.CreditTenants(credittenantsProtobuf2PojoConverter.convert(creditApplication.getCreditTenants()))
                );
    }

    @Override
    public String canConvert() {
        return PROTOBUF_PREFIX + org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditApplication.getDescriptor().getFullName();
    }

    @Override
    public Class<org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditApplication> destinationType() {
        return org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditApplication.class;
    }
}

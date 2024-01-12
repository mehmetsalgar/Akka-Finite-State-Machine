package org.salgar.fsm.pekko.foureyes.credit.converter;

import lombok.RequiredArgsConstructor;
import org.salgar.fsm.pekko.converter.Protobuf2PojoConverter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreditTenantsProtobuf2PojoConverter
        implements Protobuf2PojoConverter<org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditTenants, java.util.List<org.salgar.fsm.pekko.foureyes.credit.model.Customer>> {
    private final org.salgar.fsm.pekko.foureyes.credit.converter.CustomerProtobuf2PojoConverter customerProtobuf2PojoConverter;

    @Override
    public java.util.List<org.salgar.fsm.pekko.foureyes.credit.model.Customer> convert(org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditTenants creditTenants) {
        return
                creditTenants
                    .getCreditTenantsList()
                    .stream()
                    .map(customerProtobuf2PojoConverter::convert)
                    .collect(java.util.stream.Collectors.toList())
            ;
    }

    @Override
    public String canConvert() {
        return PROTOBUF_PREFIX + org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditTenants.getDescriptor().getFullName();
    }

    @Override
    public Class<org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditTenants> destinationType() {
        return org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditTenants.class;
    }
}

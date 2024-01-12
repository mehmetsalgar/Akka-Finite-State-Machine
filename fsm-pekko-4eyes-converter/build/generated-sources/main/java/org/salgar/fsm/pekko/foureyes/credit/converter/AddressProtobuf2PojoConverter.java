package org.salgar.fsm.pekko.foureyes.credit.converter;

import lombok.RequiredArgsConstructor;
import org.salgar.fsm.pekko.converter.Protobuf2PojoConverter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressProtobuf2PojoConverter
        implements Protobuf2PojoConverter<org.salgar.fsm.pekko.foureyes.credit.protobuf.Address, org.salgar.fsm.pekko.foureyes.credit.model.Address> {

    @Override
    public org.salgar.fsm.pekko.foureyes.credit.model.Address convert(org.salgar.fsm.pekko.foureyes.credit.protobuf.Address address) {
        return new org.salgar.fsm.pekko.foureyes.credit.model.Address(
                address.getStreet(),
                address.getHouseNo(),
                address.getCity(),
                address.getCountry()
                );
    }

    @Override
    public String canConvert() {
        return PROTOBUF_PREFIX + org.salgar.fsm.pekko.foureyes.credit.protobuf.Address.getDescriptor().getFullName();
    }

    @Override
    public Class<org.salgar.fsm.pekko.foureyes.credit.protobuf.Address> destinationType() {
        return org.salgar.fsm.pekko.foureyes.credit.protobuf.Address.class;
    }
}

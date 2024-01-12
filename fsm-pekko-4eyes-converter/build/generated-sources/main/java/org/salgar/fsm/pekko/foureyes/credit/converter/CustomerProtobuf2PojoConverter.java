package org.salgar.fsm.pekko.foureyes.credit.converter;

import lombok.RequiredArgsConstructor;
import org.salgar.fsm.pekko.converter.Protobuf2PojoConverter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerProtobuf2PojoConverter
        implements Protobuf2PojoConverter<org.salgar.fsm.pekko.foureyes.credit.protobuf.Customer, org.salgar.fsm.pekko.foureyes.credit.model.Customer> {
    private final org.salgar.fsm.pekko.foureyes.credit.converter.AddressProtobuf2PojoConverter addressProtobuf2PojoConverter;

    @Override
    public org.salgar.fsm.pekko.foureyes.credit.model.Customer convert(org.salgar.fsm.pekko.foureyes.credit.protobuf.Customer customer) {
        return new org.salgar.fsm.pekko.foureyes.credit.model.Customer(
                customer.getFirstName(),
                customer.getLastName(),
                customer.getPersonalId(),
                addressProtobuf2PojoConverter.convert(customer.getAddress()),
                customer.getEmail()
                );
    }

    @Override
    public String canConvert() {
        return PROTOBUF_PREFIX + org.salgar.fsm.pekko.foureyes.credit.protobuf.Customer.getDescriptor().getFullName();
    }

    @Override
    public Class<org.salgar.fsm.pekko.foureyes.credit.protobuf.Customer> destinationType() {
        return org.salgar.fsm.pekko.foureyes.credit.protobuf.Customer.class;
    }
}

package org.salgar.fsm.pekko.foureyes.credit.converter;

import lombok.RequiredArgsConstructor;
import org.salgar.fsm.pekko.converter.Protobuf2PojoConverter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreditTenantScoreResultProtobuf2PojoConverter
        implements Protobuf2PojoConverter<org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditTenantScoreResult, org.salgar.fsm.pekko.foureyes.credit.model.CreditTenantScoreResult> {

    @Override
    public org.salgar.fsm.pekko.foureyes.credit.model.CreditTenantScoreResult convert(org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditTenantScoreResult creditTenantScoreResult) {
        return new org.salgar.fsm.pekko.foureyes.credit.model.CreditTenantScoreResult(
                creditTenantScoreResult.getPersonalId(),
                creditTenantScoreResult.getCreditScore()
                );
    }

    @Override
    public String canConvert() {
        return PROTOBUF_PREFIX + org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditTenantScoreResult.getDescriptor().getFullName();
    }

    @Override
    public Class<org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditTenantScoreResult> destinationType() {
        return org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditTenantScoreResult.class;
    }
}

package org.salgar.fsm.pekko.foureyes.fraudprevention.elasticsearch.model;

import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
public class FraudPreventionResultELK {
    private org.salgar.fsm.pekko.foureyes.fraudprevention.model.FraudPreventionResult _fraudPreventionResult;
    private FraudPreventionResultELK() {
    }
    public FraudPreventionResultELK(org.salgar.fsm.pekko.foureyes.fraudprevention.model.FraudPreventionResult _fraudPreventionResult) {
        this._fraudPreventionResult = _fraudPreventionResult;
    }
    @Field(type = FieldType.Text)
    private final Boolean fraudPreventionResult = _fraudPreventionResult.getFraudPreventionResult() ;
}

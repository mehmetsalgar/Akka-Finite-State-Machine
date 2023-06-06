package org.salgar.fsm.pekko.foureyes.usecasekey;

import org.salgar.pekko.fsm.api.UseCaseKeyStrategy;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CreditUseCaseKeyStrategy implements UseCaseKeyStrategy {
    public static final String CREDIT_UUID = "creditUuid";
    @Override
    public String getKey(Map<String, Object> payload) {
        return (String) payload.get(CREDIT_UUID);
    }
}
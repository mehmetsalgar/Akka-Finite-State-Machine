package org.salgar.akka.fsm.foureyes.faudprevention.rest;

import lombok.RequiredArgsConstructor;
import org.salgar.akka.fsm.foureyes.faudprevention.service.FraudPreventionService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@RequiredArgsConstructor
public class FraudPreventionController {
    private final FraudPreventionService fraudPreventionService;

    @GetMapping
    public void checkFraudPrevention() {
        fraudPreventionService.checkFraudPrevention();
    }
}
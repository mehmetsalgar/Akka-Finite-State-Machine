package org.salgar.akka.fsm.foureyes.creditscore.rest;

import lombok.RequiredArgsConstructor;
import org.salgar.akka.fsm.foureyes.creditscore.service.CreditScoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CreditScoreController {
    private final CreditScoreService creditScoreService;

    @GetMapping
    public void checkScore() {
        creditScoreService.scoreCheck();
    }
}
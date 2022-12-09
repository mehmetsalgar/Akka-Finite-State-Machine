package org.salgar.akka.fsm.foureyes.addresscheck.rest;

import lombok.RequiredArgsConstructor;
import org.salgar.akka.fsm.foureyes.addresscheck.service.InitialService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AddressCheckController {
    private final InitialService initialService;

    @GetMapping
    public void addressCheck() {
        initialService.doSoemthing();
    }
}
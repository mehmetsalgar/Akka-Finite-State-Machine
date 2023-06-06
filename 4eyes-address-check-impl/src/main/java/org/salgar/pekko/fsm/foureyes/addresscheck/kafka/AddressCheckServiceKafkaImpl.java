package org.salgar.pekko.fsm.foureyes.addresscheck.kafka;

import lombok.extern.slf4j.Slf4j;
import org.salgar.pekko.fsm.foureyes.creditscore.AddressCheckService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AddressCheckServiceKafkaImpl implements AddressCheckService {
    @Override
    public void addressExist(String street, String houseNo, String city, String country) {
        log.info("Kafka configuration is not finished");
    }
}
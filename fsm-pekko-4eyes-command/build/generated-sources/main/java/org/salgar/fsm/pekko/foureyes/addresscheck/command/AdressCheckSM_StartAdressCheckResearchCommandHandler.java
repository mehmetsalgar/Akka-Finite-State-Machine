package org.salgar.fsm.pekko.foureyes.addresscheck.command;

import com.google.protobuf.Any;
import com.google.protobuf.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.salgar.fsm.pekko.command.CommandHandler;
import org.salgar.fsm.pekko.converter.Protobuf2PojoConverter;
import org.salgar.fsm.pekko.converter.service.ConverterService;
import org.salgar.fsm.pekko.foureyes.addresscheck.AdressCheckSM;
import org.salgar.fsm.pekko.foureyes.addresscheck.facade.AdressCheckSMFacade;
import org.salgar.fsm.pekko.foureyes.addresscheck.protobuf.AdressCheckSMCommand;
import org.springframework.stereotype.Component;
import scala.concurrent.Future;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdressCheckSM_StartAdressCheckResearchCommandHandler implements CommandHandler<AdressCheckSMCommand, AdressCheckSM.Response> {
    private final AdressCheckSMFacade adressCheckSMFacade;
    private final ConverterService converterService;

    @Override
    public Future<AdressCheckSM.Response> handleCommand(AdressCheckSMCommand adressCheckSMCommand) {
        return adressCheckSMFacade.askStartAdressCheckResearch(() -> adressCheckSMCommand.getUseCaseKey(), convertProtobuf2Pojo(adressCheckSMCommand.getPayloadMap()));
    }

    @Override
    public String type() {
        return AdressCheckSMCommandConstants.ON_STARTADRESSCHECKRESEARCH ;
    }

    private Map<String, Object> convertProtobuf2Pojo(Map<String, Any> payload) {
        final Map<String, Object> result = new HashMap<>();
        for(String key : payload.keySet()) {
            Any value = payload.get(key);
            Protobuf2PojoConverter<Message, ?> converter =  converterService.converter(value.getTypeUrl());
            try {
                result.put(
                        key,
                        converterService.converter(value.getTypeUrl()).convert(value.unpack(converter.destinationType())));
            } catch(Throwable t) {
                log.error(t.getMessage(), t);
            }
        }

        return result;
    }
}

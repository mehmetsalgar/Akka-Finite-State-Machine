package org.salgar.fsm.pekko.foureyes.fraudprevention.command;

import com.google.protobuf.Any;
import com.google.protobuf.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.salgar.fsm.pekko.command.CommandHandler;
import org.salgar.fsm.pekko.converter.Protobuf2PojoConverter;
import org.salgar.fsm.pekko.converter.service.ConverterService;
import org.salgar.fsm.pekko.foureyes.fraudprevention.FraudPreventionSM;
import org.salgar.fsm.pekko.foureyes.fraudprevention.facade.FraudPreventionSMFacade;
import org.salgar.fsm.pekko.foureyes.fraudprevention.protobuf.FraudPreventionSMCommand;
import org.springframework.stereotype.Component;
import scala.concurrent.Future;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class FraudPreventionSM_ResultCommandHandler implements CommandHandler<FraudPreventionSMCommand, FraudPreventionSM.Response> {
    private final FraudPreventionSMFacade fraudPreventionSMFacade;
    private final ConverterService converterService;

    @Override
    public Future<FraudPreventionSM.Response> handleCommand(FraudPreventionSMCommand fraudPreventionSMCommand) {
        return fraudPreventionSMFacade.askResult(() -> fraudPreventionSMCommand.getUseCaseKey(), convertProtobuf2Pojo(fraudPreventionSMCommand.getPayloadMap()));
    }

    @Override
    public String type() {
        return FraudPreventionSMCommandConstants.ON_RESULT ;
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

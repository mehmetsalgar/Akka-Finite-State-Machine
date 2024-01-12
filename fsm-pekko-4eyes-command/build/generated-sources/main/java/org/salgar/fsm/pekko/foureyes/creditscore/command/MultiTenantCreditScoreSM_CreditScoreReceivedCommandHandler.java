package org.salgar.fsm.pekko.foureyes.creditscore.command;

import com.google.protobuf.Any;
import com.google.protobuf.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.salgar.fsm.pekko.command.CommandHandler;
import org.salgar.fsm.pekko.converter.Protobuf2PojoConverter;
import org.salgar.fsm.pekko.converter.service.ConverterService;
import org.salgar.fsm.pekko.foureyes.creditscore.MultiTenantCreditScoreSM;
import org.salgar.fsm.pekko.foureyes.creditscore.facade.MultiTenantCreditScoreSMFacade;
import org.salgar.fsm.pekko.foureyes.creditscore.protobuf.MultiTenantCreditScoreSMCommand;
import org.springframework.stereotype.Component;
import scala.concurrent.Future;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class MultiTenantCreditScoreSM_CreditScoreReceivedCommandHandler implements CommandHandler<MultiTenantCreditScoreSMCommand, MultiTenantCreditScoreSM.Response> {
    private final MultiTenantCreditScoreSMFacade multiTenantCreditScoreSMFacade;
    private final ConverterService converterService;

    @Override
    public Future<MultiTenantCreditScoreSM.Response> handleCommand(MultiTenantCreditScoreSMCommand multiTenantCreditScoreSMCommand) {
        return multiTenantCreditScoreSMFacade.askCreditScoreReceived(() -> multiTenantCreditScoreSMCommand.getUseCaseKey(), convertProtobuf2Pojo(multiTenantCreditScoreSMCommand.getPayloadMap()));
    }

    @Override
    public String type() {
        return MultiTenantCreditScoreSMCommandConstants.ON_CREDITSCORERECEIVED ;
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

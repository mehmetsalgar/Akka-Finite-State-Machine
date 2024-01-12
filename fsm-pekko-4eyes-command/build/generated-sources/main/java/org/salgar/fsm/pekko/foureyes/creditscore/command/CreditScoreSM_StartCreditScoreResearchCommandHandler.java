package org.salgar.fsm.pekko.foureyes.creditscore.command;

import com.google.protobuf.Any;
import com.google.protobuf.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.salgar.fsm.pekko.command.CommandHandler;
import org.salgar.fsm.pekko.converter.Protobuf2PojoConverter;
import org.salgar.fsm.pekko.converter.service.ConverterService;
import org.salgar.fsm.pekko.foureyes.creditscore.CreditScoreSM;
import org.salgar.fsm.pekko.foureyes.creditscore.facade.CreditScoreSMFacade;
import org.salgar.fsm.pekko.foureyes.creditscore.protobuf.CreditScoreSMCommand;
import org.springframework.stereotype.Component;
import scala.concurrent.Future;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreditScoreSM_StartCreditScoreResearchCommandHandler implements CommandHandler<CreditScoreSMCommand, CreditScoreSM.Response> {
    private final CreditScoreSMFacade creditScoreSMFacade;
    private final ConverterService converterService;

    @Override
    public Future<CreditScoreSM.Response> handleCommand(CreditScoreSMCommand creditScoreSMCommand) {
        return creditScoreSMFacade.askStartCreditScoreResearch(() -> creditScoreSMCommand.getUseCaseKey(), convertProtobuf2Pojo(creditScoreSMCommand.getPayloadMap()));
    }

    @Override
    public String type() {
        return CreditScoreSMCommandConstants.ON_STARTCREDITSCORERESEARCH ;
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

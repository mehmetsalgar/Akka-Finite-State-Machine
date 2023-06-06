package org.salgar.pekko.fsm.xtend.uml.templates.command

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.uml2.uml.Trigger
import org.eclipse.uml2.uml.Transition
import org.salgar.pekko.fsm.xtend.uml.templates.Naming
import org.salgar.pekko.fsm.xtend.uml.templates.StateMachineHelper

class CommandTemplate {
	@Inject extension Naming
	@Inject extension StateMachineHelper

	def doGenerate(Resource input, IFileSystemAccess fsa) {
        input
            .allContents
            .filter(org.eclipse.uml2.uml.StateMachine).filter[i|i.active]
            .forEach[
                generate(fsa)
            ]
    }

    def generate(org.eclipse.uml2.uml.StateMachine it, IFileSystemAccess fsa) {
        for(Trigger trigger : giveTransitionWithTrigger(allOwnedElements().filter(Transition)).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])) {
            val content = generate(trigger)
            fsa.generateFile(packagePath+"/command/"+name+"_" + trigger.name.substring(2, trigger.name.length) + "CommandHandler.java", content)
        }
    }

    def String generate(org.eclipse.uml2.uml.StateMachine it, org.eclipse.uml2.uml.Trigger trigger) '''
        package «packageName».command;

        import com.google.protobuf.Any;
        import com.google.protobuf.Message;
        import lombok.RequiredArgsConstructor;
        import lombok.extern.slf4j.Slf4j;
        import org.salgar.fsm.pekko.command.CommandHandler;
        import «packageName».«name»;
        import «packageName».facade.«name»Facade;
        import «packageName».protobuf.«name»Command;
        import org.salgar.fsm.pekko.converter.Protobuf2PojoConverter;
        import org.salgar.fsm.pekko.converter.service.ConverterService;
        import «packageName».command.«name»CommandConstants;
        import org.springframework.stereotype.Component;
        import scala.concurrent.Future;

        import java.util.HashMap;
        import java.util.Map;

        @Component
        @RequiredArgsConstructor
        @Slf4j
        public class «name»_«trigger.name.substring(2, trigger.name.length)»CommandHandler implements CommandHandler<«name»Command, «name».Response> {
            private final «name»Facade «name.toFirstLower»Facade;
            private final ConverterService converterService;

            @Override
            public Future<«name».Response> handleCommand(«name»Command «name.toFirstLower»Command) {
                «IF !abstract»
                    return «name.toFirstLower»Facade.ask«trigger.name.substring(2, trigger.name.length)»(convertProtobuf2Pojo(«name.toFirstLower»Command.getPayloadMap()));
                «ELSE»
                    return «name.toFirstLower»Facade.ask«trigger.name.substring(2, trigger.name.length)»(() -> «name.toFirstLower»Command.getUseCaseKey(), convertProtobuf2Pojo(«name.toFirstLower»Command.getPayloadMap()));
                «ENDIF»
            }

            @Override
            public String type() {
                return «name»CommandConstants.«trigger.name.substring(0, 2).toUpperCase»_«trigger.name.substring(2, trigger.name.length).toUpperCase» ;
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
    '''
}
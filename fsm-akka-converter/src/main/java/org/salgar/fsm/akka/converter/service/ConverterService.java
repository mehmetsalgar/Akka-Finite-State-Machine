package org.salgar.fsm.akka.converter.service;

import com.google.protobuf.Message;
import org.salgar.fsm.akka.converter.Protobuf2PojoConverter;

public interface ConverterService {
    <T extends Message> Protobuf2PojoConverter<T, ?> converter(String protobufType);
}
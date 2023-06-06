package org.salgar.fsm.pekko.converter.service;

import com.google.protobuf.Message;
import org.salgar.fsm.pekko.converter.Protobuf2PojoConverter;

public interface ConverterService {
    <T extends Message> Protobuf2PojoConverter<T, ?> converter(String protobufType);
}
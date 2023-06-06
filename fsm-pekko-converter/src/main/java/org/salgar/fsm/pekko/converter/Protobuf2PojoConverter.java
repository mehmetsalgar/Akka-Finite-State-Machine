package org.salgar.fsm.pekko.converter;

import com.google.protobuf.Message;

public interface Protobuf2PojoConverter<PB extends Message, PJ> {
    String PROTOBUF_PREFIX = "type.googleapis.com/";
    PJ convert(PB protobuf);
    String canConvert();
    Class<PB> destinationType();
}
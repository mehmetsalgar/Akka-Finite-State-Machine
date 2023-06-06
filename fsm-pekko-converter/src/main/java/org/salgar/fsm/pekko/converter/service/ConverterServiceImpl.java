package org.salgar.fsm.pekko.converter.service;

import com.google.protobuf.Message;
import lombok.RequiredArgsConstructor;
import org.salgar.fsm.pekko.converter.Protobuf2PojoConverter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ConverterServiceImpl implements ConverterService {
    private final List<Protobuf2PojoConverter<?, ?>> converters;
    private final Map<String, Protobuf2PojoConverter<? extends Message, ?>> convertersMap = new HashMap<>();


    @PostConstruct
    private void init() {
        converters.forEach(converter -> convertersMap.put(converter.canConvert(), converter));
    }

    @Override
    public <T extends Message> Protobuf2PojoConverter<T, ?> converter(String protobufType) {
        return (Protobuf2PojoConverter<T, ?>) convertersMap.get(protobufType);
    }
}
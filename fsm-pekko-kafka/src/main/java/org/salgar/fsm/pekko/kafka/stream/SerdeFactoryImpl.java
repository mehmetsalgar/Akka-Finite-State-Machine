package org.salgar.fsm.pekko.kafka.stream;

import com.google.protobuf.Message;
import io.confluent.kafka.streams.serdes.protobuf.KafkaProtobufSerde;
import org.apache.kafka.common.serialization.Serde;

public class SerdeFactoryImpl implements SerdeFactory {
    @Override
    public <M extends Message> Serde<M> create() {
        return new KafkaProtobufSerde<>();
    }
}

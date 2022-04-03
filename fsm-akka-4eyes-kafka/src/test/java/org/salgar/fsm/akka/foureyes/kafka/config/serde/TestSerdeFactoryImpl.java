package org.salgar.fsm.akka.foureyes.kafka.config.serde;

import com.google.protobuf.Message;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.streams.serdes.protobuf.KafkaProtobufSerde;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.salgar.fsm.akka.kafka.stream.SerdeFactory;

@RequiredArgsConstructor
public class TestSerdeFactoryImpl implements SerdeFactory {
    private final SchemaRegistryClient schemaRegistryClient;

    @Override
    public <M extends Message> Serde<M> create() {
        return new KafkaProtobufSerde<>(schemaRegistryClient);
    }

}
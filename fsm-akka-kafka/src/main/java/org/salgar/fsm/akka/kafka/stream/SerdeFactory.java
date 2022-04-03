package org.salgar.fsm.akka.kafka.stream;

import com.google.protobuf.Message;
import org.apache.kafka.common.serialization.Serde;

public interface SerdeFactory {
    <M extends Message> Serde<M> create();
}
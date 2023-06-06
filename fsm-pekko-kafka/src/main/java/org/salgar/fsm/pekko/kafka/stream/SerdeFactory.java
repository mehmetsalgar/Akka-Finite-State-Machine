package org.salgar.fsm.pekko.kafka.stream;

import com.google.protobuf.Message;
import org.apache.kafka.common.serialization.Serde;

public interface SerdeFactory {
    <M extends Message> Serde<M> create();
}
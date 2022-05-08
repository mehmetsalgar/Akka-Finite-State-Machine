package org.salgar.fsm.akka.foureyes.kafka.partitioner;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.utils.Utils;

import java.util.Map;

public class FoureyesPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        int numberOfPartitions = cluster.partitionCountForTopic(topic);
        String uuid = new String(keyBytes);

        String processedUuid = uuid.substring(0, uuid.indexOf("_"));

        return  org.apache.kafka.common.utils.Utils
                .toPositive(Utils.murmur2(processedUuid.getBytes())) % numberOfPartitions;
    }

    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> configs) {
    }
}
package org.salgar.fsm.akka.foureyes.facades.utility

import org.apache.kafka.common.utils.Utils

object ShardIdUUtility {
  def calculateShardId(entityId: String, partitions: Int, numOfShards: Int): String = {
    val numOfShardsPerPartition: Int = numOfShards / partitions
    val actualPartition: Int = org.apache.kafka.common.utils.Utils
      .toPositive(Utils.murmur2(entityId.getBytes())) % partitions
    val destinationShard = org.apache.kafka.common.utils.Utils
      .toPositive(Utils.murmur2(entityId.getBytes())) % numOfShardsPerPartition
    val shardId = actualPartition * numOfShardsPerPartition + destinationShard
    shardId.toString
  }
}
package org.salgar.fsm.pekko.kafka.sharding

import org.apache.pekko.actor.{ExtendedActorSystem, Extension, ExtensionId}
import org.apache.pekko.cluster.sharding.typed.ShardingMessageExtractor
import org.apache.pekko.kafka.scaladsl.MetadataClient
import org.apache.pekko.kafka.{ConsumerSettings, KafkaConsumerActor}
import org.apache.pekko.util.Timeout
import org.salgar.fsm.pekko.kafka.sharding.FsmAkkaKafkaClusterSharding.KafkaShardingNoEnvelopeExtractor

import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.{ExecutionContextExecutor, Future}

final class FsmAkkaKafkaClusterSharding(system: ExtendedActorSystem) extends Extension {
  implicit val ec : ExecutionContextExecutor = system.dispatcher
  private val metaDataConsumerActorNum = new AtomicInteger()

  def messageExtractorNoEnvelope[M] (
                                      topic: String,
                                      timeout: Timeout,
                                      entityIdExtractor: M => String,
                                      shardIdExtractor: (String, Int) => String,
                                      settings: ConsumerSettings[_, _]
                                    ): Future[KafkaShardingNoEnvelopeExtractor[M]] =
    getPartitionCount(topic, timeout, settings)
      .map(partitions => new KafkaShardingNoEnvelopeExtractor[M](
        partitions,
        (s, partitions) => shardIdExtractor.apply(s, partitions),
        e => entityIdExtractor.apply(e))
      )


  private def getPartitionCount[M](
                                    topic: String,
                                    timeout: Timeout,
                                    settings: ConsumerSettings[_, _]
                                  ): Future[Int] = {

    val num = metaDataConsumerActorNum.getAndIncrement()
    val consumerActor = system.systemActorOf(KafkaConsumerActor.props(settings), s"metadata-consumer-actor-$num")
    val metaDataClient = MetadataClient.create(consumerActor, timeout)
    val numberOfPartition = metaDataClient.getPartitionsFor(topic)
      .map(_.length)
    numberOfPartition.onComplete( _ => {
      system.stop(consumerActor)
      metaDataConsumerActorNum.getAndDecrement()
    })
    numberOfPartition.map(
      count => {
        system.log.info("Retrieved {} partitions for Topic {}", count, topic)
        count
      })
  }
}

object FsmAkkaKafkaClusterSharding extends ExtensionId[FsmAkkaKafkaClusterSharding]{
  override def createExtension(system: ExtendedActorSystem): FsmAkkaKafkaClusterSharding =
    new FsmAkkaKafkaClusterSharding(system)

  sealed trait KafkaClusterShardingContract {
    def kafkaPartitions: Int
    def shardIdExtractor: (String, Int) => String
    def shardId(entityId: String): String = shardIdExtractor.apply(entityId, kafkaPartitions)
  }

  final class KafkaShardingNoEnvelopeExtractor[M] private[kafka](
                                                                  val kafkaPartitions: Int,
                                                                  val shardIdExtractor: (String, Int) => String,
                                                                  val entityIdExtractor: M => String
                                                                )
    extends ShardingMessageExtractor[M, M]
      with KafkaClusterShardingContract {
    override def entityId(message: M): String = entityIdExtractor(message)
    override def unwrapMessage(message: M): M = message
  }
}
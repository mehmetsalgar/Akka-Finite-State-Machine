package org.salgar.fsm.akka.kafka.sharding

import akka.actor.{ExtendedActorSystem, Extension, ExtensionId}
import akka.cluster.sharding.typed.ShardingMessageExtractor
import akka.kafka.scaladsl.MetadataClient
import akka.kafka.{ConsumerSettings, KafkaConsumerActor}
import akka.util.Timeout
import org.salgar.fsm.akka.kafka.sharding.FsmAkkaKafkaClusterSharding.KafkaShardingNoEnvelopeExtractor

import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.{ExecutionContextExecutor, Future}

final class FsmAkkaKafkaClusterSharding(system: ExtendedActorSystem) extends Extension {
  implicit val ec : ExecutionContextExecutor = system.dispatcher
  private val metaDataConsumerActorNum = new AtomicInteger()

  def messageExtractorNoEnvelope[M] (
                                      topic: String,
                                      timeout: Timeout,
                                      entityIdExtractor: M => String,
                                      shardIdExtractor: String => String,
                                      settings: ConsumerSettings[_, _]
                                    ): Future[KafkaShardingNoEnvelopeExtractor[M]] =
    getPartitionCount(topic, timeout, settings)
      .map(partitions => new KafkaShardingNoEnvelopeExtractor[M](
        partitions,
        s => shardIdExtractor.apply(s),
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
    def shardIdExtractor: String => String
    def shardId(entityId: String): String = shardIdExtractor.apply(entityId)
  }

  final class KafkaShardingNoEnvelopeExtractor[M] private[kafka](
                                                                  val kafkaPartitions: Int,
                                                                  val shardIdExtractor: String => String,
                                                                  entityIdExtractor: M => String
                                                                )
    extends ShardingMessageExtractor[M, M]
      with KafkaClusterShardingContract {
    override def entityId(message: M): String = entityIdExtractor(message)
    override def unwrapMessage(message: M): M = message
  }
}
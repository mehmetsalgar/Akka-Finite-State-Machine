package org.apache.pekko.projection.elasticsearch.internal

import org.apache.pekko.Done
import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.projection.ProjectionId
import org.apache.pekko.projection.cassandra.internal.CassandraOffsetStore
import org.apache.pekko.projection.internal.ManagementState
import org.salgar.fsm.pekko.elasticsearch.OffsetFacade
import org.salgar.fsm.pekko.elasticsearch.model.ElasticsearchOffset
import org.salgar.fsm.pekko.pekkosystem.ActorService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

import java.lang
import java.util.concurrent.CompletableFuture
import scala.concurrent.{ExecutionContext, Future}

@Component
@ConditionalOnProperty(name = Array("org.salgar.fsm.akka.projections.offsetStore"), havingValue = "cassandra")
class ElasticsearchCassandraOffsetFacadeImpl(actorService: ActorService) extends OffsetFacade {
  implicit val system: ActorSystem[_] = actorService.actorSystem()
  implicit val ec: ExecutionContext = actorService.ec()
  private val offsetStore = new CassandraOffsetStore(system)

  override def readOffset(projectionName: String, projectionKey: String): CompletableFuture[ElasticsearchOffset] = {
    val cf: CompletableFuture[ElasticsearchOffset] = new CompletableFuture[ElasticsearchOffset]()
    val future: Future[Option[ElasticsearchOffset]] = offsetStore.readOffset(ProjectionId.of(projectionName, projectionKey))
    future
      .map(offsetOption => offsetOption.get)
      .map(offset => {
        cf.complete(offset)
      })
    cf
  }

  override def saveOffset(projectionName: String, projectionKey: String, offset: ElasticsearchOffset): CompletableFuture[Void] = {
    val cf: CompletableFuture[Void] = new CompletableFuture[Void]()
    val future: Future[Done] = offsetStore.saveOffset(ProjectionId.of(projectionName, projectionKey), offset)
    future
      .map(_ => {
        cf.complete(null)
      })
    cf
  }

  override def clearOffset(projectionName: String, projectionKey: String): CompletableFuture[Void] = {
    val cf: CompletableFuture[Void] = new CompletableFuture[Void]()
    val future: Future[Done] = offsetStore.clearOffset(ProjectionId.of(projectionName, projectionKey))
    future
      .map(_ => {
        cf.complete(null)
      })
    cf
  }

  override def readManagementState(projectionName: String, projectionKey: String): CompletableFuture[java.lang.Boolean] = {
    val cf: CompletableFuture[java.lang.Boolean] = new CompletableFuture[java.lang.Boolean]()
    val future: Future[Option[ManagementState]] = offsetStore.readManagementState(ProjectionId.of(projectionName, projectionKey))
    future
      .map(managementStateOption => managementStateOption.get)
      .map(managementState => {
        cf.complete(managementState.paused)
      })
    cf
  }

  override def savePaused(projectionName: String, projectionKey: String, paused: lang.Boolean): CompletableFuture[Void] = {
    val cf: CompletableFuture[Void] = new CompletableFuture[Void]()
    val future: Future[Done] = offsetStore.savePaused(ProjectionId.of(projectionName, projectionKey), paused)
    future
      .map(_ => {
        cf.complete(null)
      })
    cf
  }
}
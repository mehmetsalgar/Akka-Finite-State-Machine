package org.apache.pekko.projection.elasticsearch.internal

import org.apache.pekko.Done
import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.persistence.query
import org.apache.pekko.projection.ProjectionId
import org.apache.pekko.projection.internal.ManagementState
import org.salgar.fsm.pekko.elasticsearch.OffsetFacade
import org.salgar.fsm.pekko.elasticsearch.model.ElasticsearchOffset

import java.util.UUID
import java.util.concurrent.CompletableFuture
import scala.concurrent.{ExecutionContext, Future}

class ElasticsearchOffsetStore(
                                offsetFacade: OffsetFacade,
                                system: ActorSystem[_]) {
  private implicit val executionContext: ExecutionContext = system.executionContext

  import scala.jdk.FutureConverters._

  def readManagementState(projectionId: ProjectionId): Future[Option[ManagementState]] = {
    system.log.debug("Reading Management State")
    val future: CompletableFuture[java.lang.Boolean] = offsetFacade
      .readManagementState(
        projectionId.name,
        projectionId.key)
    future
      .asScala.
      map(
        result => Option(ManagementState(result)))
  }

  def readOffset[Offset](projectionId: ProjectionId): Future[Option[Offset]] = {
    system.log.debug("Reading Offset")

    val future: CompletableFuture[ElasticsearchOffset] = offsetFacade.readOffset(projectionId.name, projectionId.key)
    future.asScala.map(elasticsearchOffset => {
      if(elasticsearchOffset != null) {
        Option(query.Offset.timeBasedUUID(UUID.fromString(elasticsearchOffset.getOffset)).asInstanceOf[Offset])
      } else {
        Option(org.apache.pekko.persistence.query.Offset.noOffset.asInstanceOf[Offset])
      }})
    //Option(akka.persistence.query.Offset.noOffset.asInstanceOf[Offset])
  }

  def saveOffset[Offset](projectionId: ProjectionId, offset: Offset): Future[Done] = {
    system.log.debug("Saving Offset is realised from Bulk Processor")

    /*val future: CompletableFuture[Void] = offsetFacade.saveOffset(
      projectionId.name,
      projectionId.key,
      new ElasticsearchOffset(
        projectionId.name + "_" + projectionId.key,
        projectionId.name,
        projectionId.key,
        calculateOffset(offset.asInstanceOf[akka.persistence.query.Offset])
      ))
    future.asScala.map(_ => Done)*/
    Future {
      Done
    }
  }

  def clearOffset(projectionId: ProjectionId): Future[Done] = {
    system.log.debug("Clearing Offset")
    val future: CompletableFuture[Void] = offsetFacade.clearOffset(projectionId.name, projectionId.key)
    future.asScala.map(_ => Done)
  }

  def savePaused(projectionId: ProjectionId, paused: Boolean): Future[Done] = {
    system.log.debug("Saving Pause Offset")
    val future: CompletableFuture[Void] = offsetFacade.savePaused(projectionId.name, projectionId.key, paused)
    future.asScala.map(_ => Done)
  }
}
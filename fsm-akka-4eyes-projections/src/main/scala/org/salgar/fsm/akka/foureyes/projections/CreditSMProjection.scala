package org.salgar.fsm.akka.foureyes.projections

import akka.actor.typed.ActorSystem
import akka.cluster.sharding.typed.ShardedDaemonProcessSettings
import akka.cluster.sharding.typed.scaladsl.ShardedDaemonProcess
import akka.persistence.cassandra.query.scaladsl.CassandraReadJournal
import akka.persistence.query.Offset
import akka.projection.elasticsearch.internal.AdaptedElasticsearchHandler
import akka.projection.elasticsearch.scaladsl.ElasticsearchProjection
import akka.projection.eventsourced.EventEnvelope
import akka.projection.eventsourced.scaladsl.EventSourcedProvider
import akka.projection.scaladsl.{AtMostOnceProjection, SourceProvider}
import akka.projection.{ProjectionBehavior, ProjectionId}
import org.salgar.fsm.akka.elasticsearch.OffsetFacade
import org.salgar.fsm.akka.foureyes.credit.CreditSM

object CreditSMProjection {
  def init(
            system: ActorSystem[_],
            _handler: CreditSMProjectionHandler,
            _offsetFacade: OffsetFacade
          ) : Unit = {
    ShardedDaemonProcess(system).init(
      "creditSMProjection",
      system.settings.config.getInt("akka.fsm.numberOfShards"),
      index =>
        ProjectionBehavior(createProjectionFor(system, _handler, _offsetFacade, index)),
      ShardedDaemonProcessSettings(system),
      Some(ProjectionBehavior.Stop)
    )
  }

  private def createProjectionFor(
                                   system: ActorSystem[_],
                                   _handler: CreditSMProjectionHandler,
                                   _offsetFacade: OffsetFacade,
                                   index: Int) : AtMostOnceProjection[Offset, EventEnvelope[CreditSM.PersistEvent]] = {
    val tag = "creditsm-" + index

    val sourceProvider : SourceProvider[Offset, EventEnvelope[CreditSM.PersistEvent]] =
      EventSourcedProvider.eventsByTag[CreditSM.PersistEvent](
        system = system,
        readJournalPluginId = CassandraReadJournal.Identifier,
        tag = tag
      )

    ElasticsearchProjection
      .atMostOnce(
        projectionId = ProjectionId("creditSMProjection", tag),
        sourceProvider,
        handler = () => new AdaptedElasticsearchHandler[EventEnvelope[CreditSM.PersistEvent]](
          _handler,
          ProjectionId("creditSMProjection",
            tag)),
        offsetFacade = _offsetFacade
      )
  }
}
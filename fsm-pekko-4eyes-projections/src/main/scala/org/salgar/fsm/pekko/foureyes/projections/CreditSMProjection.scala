package org.salgar.fsm.pekko.foureyes.projections

import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.cluster.sharding.typed.ShardedDaemonProcessSettings
import org.apache.pekko.cluster.sharding.typed.scaladsl.ShardedDaemonProcess
import org.apache.pekko.persistence.cassandra.query.scaladsl.CassandraReadJournal
import org.apache.pekko.persistence.query.Offset
import org.apache.pekko.projection.elasticsearch.internal.AdaptedElasticsearchHandler
import org.apache.pekko.projection.elasticsearch.scaladsl.ElasticsearchProjection
import org.apache.pekko.projection.eventsourced.EventEnvelope
import org.apache.pekko.projection.eventsourced.scaladsl.EventSourcedProvider
import org.apache.pekko.projection.scaladsl.{AtMostOnceProjection, SourceProvider}
import org.apache.pekko.projection.{ProjectionBehavior, ProjectionId}
import org.salgar.fsm.pekko.elasticsearch.OffsetFacade
import org.salgar.fsm.pekko.foureyes.credit.CreditSM

object CreditSMProjection {
  def init(
            system: ActorSystem[_],
            _handler: CreditSMProjectionHandler,
            _offsetFacade: OffsetFacade
          ) : Unit = {
    ShardedDaemonProcess(system).init(
      "creditSMProjection",
      system.settings.config.getInt("pekko.fsm.numberOfShards"),
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
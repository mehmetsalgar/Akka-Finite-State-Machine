package org.apache.pekko.projection.elasticsearch.scaladsl

import org.apache.pekko.projection.ProjectionId
import org.apache.pekko.projection.elasticsearch.internal.ElasticsearchProjectionImpl
import org.apache.pekko.projection.internal.{AtMostOnce, NoopStatusObserver, SingleHandlerStrategy}
import org.apache.pekko.projection.scaladsl.{AtMostOnceProjection, Handler, SourceProvider}
import org.salgar.fsm.pekko.elasticsearch.OffsetFacade

object ElasticsearchProjection {
  def atMostOnce[Offset, Envelope](
                                    projectionId: ProjectionId,
                                    sourceProvider: SourceProvider[Offset, Envelope],
                                    handler: () => Handler[Envelope],
                                    offsetFacade: OffsetFacade): AtMostOnceProjection[Offset, Envelope] =
    new ElasticsearchProjectionImpl(
      projectionId,
      sourceProvider,
      settingsOpt = None,
      restartBackoffOpt = None,
      offsetStrategy = AtMostOnce(),
      handlerStrategy = SingleHandlerStrategy(handler),
      statusObserver = NoopStatusObserver,
      offsetFacade)
}
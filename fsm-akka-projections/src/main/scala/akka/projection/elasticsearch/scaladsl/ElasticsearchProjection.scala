package akka.projection.elasticsearch.scaladsl

import akka.projection.ProjectionId
import akka.projection.elasticsearch.internal.ElasticsearchProjectionImpl
import akka.projection.internal.{AtMostOnce, NoopStatusObserver, SingleHandlerStrategy}
import akka.projection.scaladsl.{AtMostOnceProjection, Handler, SourceProvider}
import org.salgar.fsm.akka.elasticsearch.OffsetFacade

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
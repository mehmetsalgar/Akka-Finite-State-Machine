package org.apache.pekko.projection.elasticsearch.internal

import org.apache.pekko.Done
import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.event.{Logging, LoggingAdapter}
import org.apache.pekko.projection.RunningProjection.AbortProjectionException
import org.apache.pekko.projection._
import org.apache.pekko.projection.elasticsearch.ElasticsearchEnvelope
import org.apache.pekko.projection.elasticsearch.scaladsl.ElasticsearchHandler
import org.apache.pekko.projection.internal._
import org.apache.pekko.projection.scaladsl.{AtMostOnceProjection, Handler, SourceProvider}
import org.apache.pekko.stream.RestartSettings
import org.apache.pekko.stream.scaladsl.Source
import org.salgar.fsm.pekko.elasticsearch.OffsetFacade

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}


class ElasticsearchProjectionImpl[Offset, Envelope](
                               override val projectionId: ProjectionId,
                               sourceProvider: SourceProvider[Offset, Envelope],
                               settingsOpt: Option[ProjectionSettings],
                               restartBackoffOpt: Option[RestartSettings],
                               val offsetStrategy: OffsetStrategy,
                               handlerStrategy: HandlerStrategy,
                               override val statusObserver: StatusObserver[Envelope],
                               offsetFacade: OffsetFacade
                             )
          extends AtMostOnceProjection[Offset, Envelope]
            with SettingsImpl[ElasticsearchProjectionImpl[Offset, Envelope]]
            with InternalProjection {

  private def copy(
                    settingsOpt: Option[ProjectionSettings] = this.settingsOpt,
                    restartBackoffOpt: Option[RestartSettings] = this.restartBackoffOpt,
                    offsetStrategy: OffsetStrategy = this.offsetStrategy,
                    handlerStrategy: HandlerStrategy = this.handlerStrategy,
                    statusObserver: StatusObserver[Envelope] = this.statusObserver,
                    offsetFacade: OffsetFacade = this.offsetFacade): ElasticsearchProjectionImpl[Offset, Envelope] =
    new ElasticsearchProjectionImpl(
      projectionId,
      sourceProvider,
      settingsOpt,
      restartBackoffOpt,
      offsetStrategy,
      handlerStrategy,
      statusObserver,
      offsetFacade
    )

  private def settingsOrDefaults(implicit system: ActorSystem[_]): ProjectionSettings = {
    val settings = settingsOpt.getOrElse(ProjectionSettings(system))
    restartBackoffOpt match {
      case None    => settings
      case Some(r) => settings.copy(restartBackoff = r)
    }
  }

  override def withRestartBackoffSettings(restartBackoff: RestartSettings): ElasticsearchProjectionImpl[Offset, Envelope] =
    copy(restartBackoffOpt = Some(restartBackoff))

  override def withStatusObserver(observer: StatusObserver[Envelope]): AtMostOnceProjection[Offset, Envelope] =
    copy(statusObserver = observer)

  override def withRecoveryStrategy(recoveryStrategy: StrictRecoveryStrategy): AtMostOnceProjection[Offset, Envelope] = {
    // safe cast: withRecoveryStrategy(StrictRecoveryStrategy) is only available to AtMostOnceProjection
    val atMostOnce = offsetStrategy.asInstanceOf[AtMostOnce]

    copy(offsetStrategy = atMostOnce.copy(Some(recoveryStrategy)))
  }

  override def withGroup(
                          groupAfterEnvelopes: Int,
                          groupAfterDuration: FiniteDuration): ElasticsearchProjectionImpl[Offset, Envelope] = {

    // safe cast: withGroup is only available to GroupedProjections
    val groupedHandler = handlerStrategy.asInstanceOf[GroupedHandlerStrategy[Envelope]]

    copy(handlerStrategy =
      groupedHandler.copy(afterEnvelopes = Some(groupAfterEnvelopes), orAfterDuration = Some(groupAfterDuration)))
  }

  override def withSaveOffset(
                               afterEnvelopes: Int,
                               afterDuration: FiniteDuration): ElasticsearchProjectionImpl[Offset, Envelope] = {

    // safe cast: withSaveOffset is only available to AtLeastOnceProjection
    val atLeastOnce = offsetStrategy.asInstanceOf[AtLeastOnce]

    copy(offsetStrategy =
      atLeastOnce.copy(afterEnvelopes = Some(afterEnvelopes), orAfterDuration = Some(afterDuration)))
  }

  override private[projection] def mappedSource()(implicit system: ActorSystem[_]) =
    new ElasticsearchInternalProjectionState(settingsOrDefaults).mappedSource()

  override private[projection] def actorHandlerInit[T] = handlerStrategy.actorHandlerInit

  override private[projection] def run()(implicit system: ActorSystem[_]) = {
    new ElasticsearchInternalProjectionState(settingsOrDefaults).newRunningInstance()
  }

  private class ElasticsearchInternalProjectionState(val settings: ProjectionSettings)(implicit val system: ActorSystem[_])
    extends InternalProjectionState[Offset, Envelope](
      projectionId,
      sourceProvider,
      offsetStrategy,
      handlerStrategy,
      statusObserver,
      settings
    ) {

    override implicit def executionContext: ExecutionContext = system.executionContext
    override val logger: LoggingAdapter = Logging(system.classicSystem, this.getClass)

    private val offsetStore = new ElasticsearchOffsetStore(offsetFacade, system)

    override def readPaused(): Future[Boolean] = {
      offsetStore.readManagementState(projectionId).map(_.exists(_.paused))
    }

    override def readOffsets(): Future[Option[Offset]] =
      offsetStore.readOffset(projectionId)

    override def saveOffset(projectionId: ProjectionId, offset: Offset): Future[Done] =
      offsetStore.saveOffset(projectionId, offset)

    private[projection] def newRunningInstance(): RunningProjection = {
      new ElasticsearchRunningProjection(RunningProjection.withBackoff(() => mappedSource(), settings), offsetStore, this)
    }
  }

  private class ElasticsearchRunningProjection(
                                            source: Source[Done, _],
                                            offsetStore: ElasticsearchOffsetStore,
                                            projectionState: ElasticsearchInternalProjectionState)(implicit system: ActorSystem[_])
    extends RunningProjection
      with RunningProjectionManagement[Offset] {

    private val streamDone = source.run()

    override def stop(): Future[Done] = {
      projectionState.killSwitch.shutdown()
      // if the handler is retrying it will be aborted by this,
      // otherwise the stream would not be completed by the killSwitch until after all retries
      projectionState.abort.failure(AbortProjectionException)
      streamDone
    }

    // RunningProjectionManagement
    override def getOffset(): Future[Option[Offset]] = {
      offsetStore.readOffset(projectionId)
    }

    // RunningProjectionManagement
    override def setOffset(offset: Option[Offset]): Future[Done] = {
      offset match {
        case Some(o) =>
          offsetStore.saveOffset(projectionId, o)
        case None =>
          offsetStore.clearOffset(projectionId)
      }
    }

    // RunningProjectionManagement
    override def getManagementState(): Future[Option[ManagementState]] =
      offsetStore.readManagementState(projectionId)

    // RunningProjectionManagement
    override def setPaused(paused: Boolean): Future[Done] =
      offsetStore.savePaused(projectionId, paused)
  }
}

class AdaptedElasticsearchHandler[ENVELOPE](delegate : ElasticsearchHandler[ElasticsearchEnvelope[ENVELOPE]], projectionId: ProjectionId) extends Handler[ENVELOPE] {
  override def process(envelope: ENVELOPE): Future[Done] = {
    val elasticsearchEnvelope: ElasticsearchEnvelope[ENVELOPE] = new ElasticsearchEnvelope[ENVELOPE](projectionId, envelope)
    delegate.process(elasticsearchEnvelope)
  }
}
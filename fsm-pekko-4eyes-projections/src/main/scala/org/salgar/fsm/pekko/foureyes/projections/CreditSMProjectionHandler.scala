package org.salgar.fsm.pekko.foureyes.projections

import org.apache.pekko.Done
import org.apache.pekko.projection.elasticsearch.ElasticsearchEnvelope
import org.apache.pekko.projection.elasticsearch.scaladsl.ElasticsearchHandler
import org.apache.pekko.projection.eventsourced.EventEnvelope
import org.salgar.fsm.pekko.elasticsearch.ElasticsearchRepository
import org.salgar.fsm.pekko.foureyes.credit.CreditSM
import org.salgar.fsm.pekko.foureyes.credit.projections.CreditSMPersistEventProcessor
import org.salgar.fsm.pekko.pekkosystem.ActorService
import org.salgar.pekko.fsm.api.UseCaseKeyStrategy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


@Component
class CreditSMProjectionHandler(
                                 @Autowired fourEyesRepository: ElasticsearchRepository,
                                 @Autowired useCaseKeyStrategy: UseCaseKeyStrategy,
                                 @Autowired actorService: ActorService
                               )
  extends ElasticsearchHandler[ElasticsearchEnvelope[EventEnvelope[CreditSM.PersistEvent]]] {
  implicit def executionContext: ExecutionContext = actorService.actorSystem().executionContext
  val creditSMPersistEventProcessor = new CreditSMPersistEventProcessor(
    fourEyesRepository,
    useCaseKeyStrategy,
    actorService
  )

  override def process(
                       envelope: ElasticsearchEnvelope[EventEnvelope[CreditSM.PersistEvent]]): Future[Done] = {
    actorService.actorSystem().log.debug("Handler Processing!")
    val processed = creditSMPersistEventProcessor.matching(envelope)

    processed.onComplete {
      case Success(_)             => actorService.actorSystem().log.debug("Success")
      case Failure(exception)     => actorService.actorSystem().log.error(exception.getMessage, exception)
    }
    processed
  }
}
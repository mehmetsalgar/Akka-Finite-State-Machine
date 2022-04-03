package org.salgar.fsm.akka.foureyes.projections

import akka.Done
import akka.projection.elasticsearch.ElasticsearchEnvelope
import akka.projection.elasticsearch.scaladsl.ElasticsearchHandler
import akka.projection.eventsourced.EventEnvelope
import org.salgar.akka.fsm.api.UseCaseKeyStrategy
import org.salgar.fsm.akka.akkasystem.ActorService
import org.salgar.fsm.akka.elasticsearch.ElasticsearchRepository
import org.salgar.fsm.akka.foureyes.credit.CreditSM
import org.salgar.fsm.akka.foureyes.credit.projections.CreditSMPersistEventProcessor
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
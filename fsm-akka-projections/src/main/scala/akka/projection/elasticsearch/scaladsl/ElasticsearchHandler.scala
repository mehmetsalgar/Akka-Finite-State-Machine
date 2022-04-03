package akka.projection.elasticsearch.scaladsl

import akka.Done

import scala.concurrent.Future

trait ElasticsearchHandler[Envelope] {
  def process(envelope: Envelope): Future[Done]
}
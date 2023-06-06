package org.apache.pekko.projection.elasticsearch.scaladsl

import org.apache.pekko.Done

import scala.concurrent.Future

trait ElasticsearchHandler[Envelope] {
  def process(envelope: Envelope): Future[Done]
}
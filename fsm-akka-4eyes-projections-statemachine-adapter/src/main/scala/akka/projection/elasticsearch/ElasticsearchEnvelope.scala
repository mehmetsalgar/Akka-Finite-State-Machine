package akka.projection.elasticsearch

import akka.projection.ProjectionId

final case class ElasticsearchEnvelope[ENVELOPE](projectionId: ProjectionId, eventEnvelope: ENVELOPE)
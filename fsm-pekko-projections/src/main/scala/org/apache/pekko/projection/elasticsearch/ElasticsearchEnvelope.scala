package org.apache.pekko.projection.elasticsearch

import org.apache.pekko.projection.ProjectionId

final case class ElasticsearchEnvelope[ENVELOPE](projectionId: ProjectionId, eventEnvelope: ENVELOPE)
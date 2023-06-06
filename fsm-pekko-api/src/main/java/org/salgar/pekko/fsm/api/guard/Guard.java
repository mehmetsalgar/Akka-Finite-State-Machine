package org.salgar.pekko.fsm.api.guard;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;

import java.util.Map;

public interface Guard<T> {
  boolean evaluate(
          ActorContext<T> ctx,
          Map<String, Object> controlObject,
          Map<String, Object> payload);
}
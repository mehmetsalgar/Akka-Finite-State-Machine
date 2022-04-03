package org.salgar.akka.fsm.api.guard;

import akka.actor.typed.scaladsl.ActorContext;

import java.util.Map;

public interface Guard<T> {
  boolean evaluate(
          ActorContext<T> ctx,
          Map<String, Object> controlObject,
          Map<String, Object> payload);
}
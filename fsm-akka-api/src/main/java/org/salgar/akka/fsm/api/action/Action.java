package org.salgar.akka.fsm.api.action;

import akka.actor.typed.ActorRef;
import akka.actor.typed.scaladsl.ActorContext;
import akka.persistence.typed.scaladsl.ReplyEffect;

import java.util.Map;

public interface Action<T, EVENT, STATE, RESPONSE> {
    ReplyEffect<EVENT,  STATE> doAction(
            ActorContext<T> ctx,
            Map<String, Object> controlObject,
            Map<String, Object> payload,
            ActorRef<RESPONSE> replyTo) throws InterruptedException;
}
package org.salgar.pekko.fsm.api.action;


import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.apache.pekko.persistence.typed.scaladsl.ReplyEffect;

import java.util.Map;

public interface Action<T, EVENT, STATE, RESPONSE> {
    ReplyEffect<EVENT,  STATE> doAction(
            ActorContext<T> ctx,
            Map<String, Object> controlObject,
            Map<String, Object> payload,
            ActorRef<RESPONSE> replyTo) throws InterruptedException;
}
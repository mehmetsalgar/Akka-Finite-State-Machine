package org.salgar.akka.fsm.aspect;

import akka.actor.typed.scaladsl.ActorContext;
import akka.persistence.typed.scaladsl.Effect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public aspect FSMAspect {
    private static Logger LOG = LoggerFactory.getLogger(FSMAspect.class);

    pointcut MyCommandHandler(Object o, ActorContext<?> ctx, Object event, Object state) :
            within(org.salgar.fsm.akka.foureyes..*)
            && execution(* *.commandHandler(..))
            && args(ctx, event, state)
            && this(o);

    pointcut MyUnhandled() :
            within(org.salgar.fsm.akka.foureyes..*)
            && call(* *.unhandled(..));

    pointcut UnhandledInMyCommandHandler(Object o, ActorContext<?> ctx, Object event, Object state) :
            cflow(MyCommandHandler(o, ctx, event, state))
            && MyUnhandled();

    Object around(Object o, ActorContext<?> ctx, Object event, Object state) : UnhandledInMyCommandHandler(o, ctx, event, state) {
        try {
            RuntimeException re = new RuntimeException("Unhandled transition!");
            LOG.warn("Unhandled transition! {} event: {} state: {}",
                    thisJoinPoint.toLongString(),
                    event.toString(),
                    state.toString(),
                    re);

            return Effect.unhandled();
        } catch (Throwable t) {
            LOG.error(t.getMessage(), t);
        }
        return null;
    }
}
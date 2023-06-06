package org.salgar.pekko.fsm.aspect;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.apache.pekko.persistence.typed.scaladsl.Effect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public aspect FSMAspect {
    private static Logger LOG = LoggerFactory.getLogger(FSMAspect.class);

    pointcut MyCommandHandler(Object o, ActorContext<?> ctx, Object event, Object state) :
            within(org.salgar.fsm.pekko.foureyes..*)
            && execution(* *.commandHandler(..))
            && args(ctx, event, state)
            && this(o);

    pointcut MyUnhandled() :
            within(org.salgar.fsm.pekko.foureyes..*)
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
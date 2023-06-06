package org.salgar.pekko.fsm.aspect;

import org.apache.pekko.persistence.typed.scaladsl.Effect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Aspect
public class FSMDebugAspect {
    private static Logger LOG = LoggerFactory.getLogger(FSMDebugAspect.class);

    //@Around("within(org.salgar.pekko.markercards..*) && call(* org.apache.pekko.actor.typed..Behaviors.receive(..))")
    //@Around("execution(* org.salgar.pekko.markercards..doAction(..))")
    //@Around("call(* org.apache.pekko.actor.typed.scaladsl.Behaviors.receive(..))")
    //org.apache.pekko.actor.typed.scaladsl.Behaviors$Receive org.apache.pekko.actor.typed.scaladsl.Behaviors$.receive(scala.Function2)
    //@Around("within(org.salgar.pekko.markercards..*) && call(* org.apache.pekko.actor.typed.scaladsl.Behaviors$.receive(..))")
    //@Around("within(org.salgar.pekko.fsm.markercards..*) && call(* *.unhandled(..))")
    public Object invoke(ProceedingJoinPoint proceedingJoinPoint) {
        try {
            RuntimeException re = new RuntimeException("Unhandled transition!");
            LOG.warn("Unhandled transition! {}",
                    proceedingJoinPoint.toString(),
                    re);

            return Effect.unhandled();
        } catch (Throwable t) {
            LOG.error(t.getMessage(), t);
        }
        return null;
    }

    //@Before("within(*..4eyes)")
    public void logAllInListener(JoinPoint thisJoinPoint) {
        System.out.println("  " + thisJoinPoint);
    }
}
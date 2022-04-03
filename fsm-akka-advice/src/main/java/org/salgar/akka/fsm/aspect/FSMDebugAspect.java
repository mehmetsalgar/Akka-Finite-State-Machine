package org.salgar.akka.fsm.aspect;

import akka.persistence.typed.scaladsl.Effect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Aspect
public class FSMDebugAspect {
    private static Logger LOG = LoggerFactory.getLogger(FSMDebugAspect.class);

    //@Around("within(org.salgar.akka.markercards..*) && call(* akka.actor.typed..Behaviors.receive(..))")
    //@Around("execution(* org.salgar.akka.markercards..doAction(..))")
    //@Around("call(* akka.actor.typed.scaladsl.Behaviors.receive(..))")
    //akka.actor.typed.scaladsl.Behaviors$Receive akka.actor.typed.scaladsl.Behaviors$.receive(scala.Function2)
    //@Around("within(org.salgar.akka.markercards..*) && call(* akka.actor.typed.scaladsl.Behaviors$.receive(..))")
    //@Around("within(org.salgar.akka.fsm.markercards..*) && call(* *.unhandled(..))")
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
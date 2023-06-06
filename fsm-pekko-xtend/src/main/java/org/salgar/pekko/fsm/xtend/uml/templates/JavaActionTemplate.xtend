package org.salgar.pekko.fsm.xtend.uml.templates

import javax.inject.Inject
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext

import org.eclipse.uml2.uml.FinalState
import org.eclipse.uml2.uml.Pseudostate
import org.eclipse.uml2.uml.State
import org.eclipse.uml2.uml.Transition

class JavaActionTemplate {
    @Inject extension Naming
    @Inject extension GlobalVariableHelper

    def generate(org.eclipse.uml2.uml.StateMachine it, IFileSystemAccess2 fsa, IGeneratorContext context) {
        generate(name, it, fsa, context)
    }

    private def void generate(String name, org.eclipse.uml2.uml.StateMachine sm, IFileSystemAccess2 fsa, IGeneratorContext context) {
        for(state : sm.allOwnedElements.filter(Pseudostate).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])) {
            for(tr : state.getOutgoings().sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])) {
                fsa.generateFile(packagePath(sm)+"/actions/"+tr.source.name.toUpperCase()+context.getGlobalVariable('targetSourceStateSeperator')+tr.target.name.toUpperCase()+"_"+tr.name+"_Action.java",
                generate(name, sm,  tr, context))
            }
        }
        for(state : sm.allOwnedElements.filter(State).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])) {
            for(tr : state.getOutgoings().sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])) {
                fsa.generateFile(packagePath(sm)+"/actions/"+tr.source.name.toUpperCase()+context.getGlobalVariable('targetSourceStateSeperator')+tr.target.name.toUpperCase()+"_"+tr.name+"_Action.java",
                generate(name, sm,  tr, context))
                if(state.getSubmachine() !== null) {
                    generate(name, state.getSubmachine(), fsa, context)
                }
            }
        }
    }

    private def generate (String name, org.eclipse.uml2.uml.StateMachine sm, Transition tr, IGeneratorContext context) '''
        package «packageName(sm)».actions;

        import org.apache.pekko.actor.typed.ActorRef;
        import org.apache.pekko.actor.typed.scaladsl.ActorContext;
        import org.apache.pekko.persistence.typed.scaladsl.Effect;
        import org.apache.pekko.persistence.typed.scaladsl.EffectBuilder;
        import org.apache.pekko.persistence.typed.scaladsl.ReplyEffect;
        import org.salgar.pekko.fsm.api.action.Action;
        import org.salgar.pekko.fsm.api.action.Action;
        import «packageName(sm)».«name»;

        import java.util.Map;

        «IF (tr.getOwnedComments !== null) && (!tr.getOwnedComments.isEmpty)»
            /**
                <a href="fsm-pekko-asciidoc/build/puml/activity/«tr.name».png">Activity Diagram - «tr.name»</a><br>

                «FOR comment : tr.getOwnedComments»
                    «comment.body»
                «ENDFOR»
            */
        «ENDIF»
        public abstract class «tr.source.name.toUpperCase()»«context.getGlobalVariable('targetSourceStateSeperator')»«tr.target.name.toUpperCase()»_«tr.name»_Action
             implements Action<«name».«name»Event, «name».PersistEvent, «name».State, «name».Response> {

            @Override
            public ReplyEffect<«name».PersistEvent, «name».State> doAction(
                    ActorContext<«name».«name»Event> actorContext,
                    Map<String, Object> controlObject,
                    Map<String, Object> payload,
                    ActorRef<«name».Response> replyTo) throws InterruptedException {
                actorContext.log().debug("Executing «tr.source.name.toUpperCase()» «tr.name» Action");

                Map<String, Object> persistPayload = processCustomAction(actorContext, controlObject, payload);

                return processPersist(controlObject, persistPayload, replyTo);
            }

            protected abstract Map<String, Object> processCustomAction(ActorContext<«name».«name»Event> actorContext,
                                                                       Map<String, Object> controlObject,
                                                                       Map<String, Object> payload);


            protected ReplyEffect<«name».PersistEvent, «name».State> processPersist(
                                                                            Map<String, Object> controlObject,
                                                                            Map<String, Object> persistPayload,
                                                                            ActorRef<«name».Response> replyTo) {
                controlObject.putAll(persistPayload);
                EffectBuilder<«name».PersistEvent, «name».State> effectBuilder =
                                Effect
                                «IF !tr.getTriggers().isEmpty() && tr.getTriggers().get(0).getEvent() !== null»
                                    .persist(new «name».«tr.getTriggers().get(0).getEvent().getName()»(controlObject));
                                «ELSE»
                                    .none();
                                «ENDIF»

                ReplyEffect<«name».PersistEvent, «name».State> replyEffect;
                if(replyTo == null) {
                    replyEffect = effectBuilder«IF (tr.getTarget() instanceof FinalState)».thenStop()«ENDIF».thenNoReply();
                } else {
                    replyEffect= effectBuilder«IF (tr.getTarget() instanceof FinalState)».thenStop()«ENDIF».thenReply(replyTo, (state) -> new «name».AcknowledgeResponse());
                }

                return replyEffect;
            }
        }
    '''
}
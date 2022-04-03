package org.salgar.akka.fsm.xtend.uml.templates.elk

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess

import org.salgar.akka.fsm.xtend.uml.templates.Naming

import org.eclipse.uml2.uml.Pseudostate
import org.eclipse.uml2.uml.State
import org.eclipse.uml2.uml.Transition

import org.salgar.akka.fsm.xtend.uml.templates.SubStateMachineHelper

class ElkPersistEventProcessorTemplate {
	@Inject extension Naming
	@Inject extension SubStateMachineHelper

	def doGenerate(Resource input, IFileSystemAccess fsa) {
        input
            .allContents
            .filter(org.eclipse.uml2.uml.StateMachine)
            .filter(s | s.isActive)
            .forEach[
                val content = generate
                fsa.generateFile(packagePath+"/projections//"+name+"PersistEventProcessor.scala", content)
            ]
    }

    def generate (org.eclipse.uml2.uml.StateMachine it) '''
        package «packageName».projections

        import akka.Done
        import akka.projection.elasticsearch.ElasticsearchEnvelope
        import akka.projection.eventsourced.EventEnvelope
        import org.salgar.akka.fsm.api.UseCaseKeyStrategy
        import org.salgar.fsm.akka.akkasystem.ActorService
        import org.salgar.fsm.akka.projections.OffsetUtility.calculateOffset
        import org.salgar.fsm.akka.elasticsearch.ElasticsearchRepository
        import «packageName».«name»
        import «packageName».elasticsearch.transform.«name»ElasticsearchTransform

        import java.util.concurrent.CompletableFuture
        import scala.concurrent.{ExecutionContext, Future}

        class «name»PersistEventProcessor(elasticsearchRepository: ElasticsearchRepository,
                                            useCaseKeyStrategy: UseCaseKeyStrategy,
                                            actorService: ActorService) {
          implicit def executionContext: ExecutionContext = actorService.actorSystem().executionContext

          import scala.jdk.FutureConverters._

          def matching(envelope: ElasticsearchEnvelope[EventEnvelope[«name».PersistEvent]]): Future[Done] = {
            envelope.eventEnvelope.event match {
              «FOR Transition transition : giveTransitionsRecursive(allOwnedElements().filter(Pseudostate), allOwnedElements().filter(State)).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])»
                «IF transition.triggers !== null && !transition.triggers.empty»
                  case «transition.triggers.get(0).event.name.toFirstLower»@«name».«transition.triggers.get(0).event.name»(payload)=> {
                    process(envelope, payload, «transition.triggers.get(0).event.name.toFirstLower»)
                  }
                «ENDIF»
              «ENDFOR»
            }
          }

          private def process(
                       envelope: ElasticsearchEnvelope[EventEnvelope[«name».PersistEvent]],
                       payload: java.util.Map[String, AnyRef],
                       persistEvent: «name».PersistEvent
                     ) : Future[Done] = {
            val future: CompletableFuture[Void] = elasticsearchRepository
              .index(
                calculateOffset(envelope.eventEnvelope.offset),
                envelope.eventEnvelope.persistenceId,
                envelope.eventEnvelope.sequenceNr,
                envelope.projectionId.name,
                envelope.projectionId.key,
                useCaseKeyStrategy.getKey(payload),
                "«name.toLowerCase»",
                «name»ElasticsearchTransform.transform(persistEvent),
                payload)
            future.asScala.map(_ => Done)
          }
        }
    '''
}
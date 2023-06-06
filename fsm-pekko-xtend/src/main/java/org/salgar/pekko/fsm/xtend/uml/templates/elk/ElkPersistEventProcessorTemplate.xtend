package org.salgar.pekko.fsm.xtend.uml.templates.elk

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess

import org.salgar.pekko.fsm.xtend.uml.templates.Naming

import org.eclipse.uml2.uml.Event

import org.salgar.pekko.fsm.xtend.uml.templates.StateMachineHelper

class ElkPersistEventProcessorTemplate {
	@Inject extension Naming
	@Inject extension StateMachineHelper

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

        import org.apache.pekko.Done
        import org.apache.pekko.projection.elasticsearch.ElasticsearchEnvelope
        import org.apache.pekko.projection.eventsourced.EventEnvelope
        import org.salgar.pekko.fsm.api.UseCaseKeyStrategy
        import org.salgar.fsm.pekko.pekkosystem.ActorService
        import org.salgar.fsm.pekko.projections.OffsetUtility.calculateOffset
        import org.salgar.fsm.pekko.elasticsearch.ElasticsearchRepository
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
              «FOR Event event : findSubMachinePersistEventsRecursive»
                  case «event.name.toFirstLower»@«name».«event.name»(payload)=> {
                    process(envelope, payload, «event.name.toFirstLower»)
                  }
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
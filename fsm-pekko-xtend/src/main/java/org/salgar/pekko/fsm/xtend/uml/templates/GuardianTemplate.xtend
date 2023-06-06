package org.salgar.pekko.fsm.xtend.uml.templates

import javax.inject.Inject
import org.eclipse.xtext.generator.IFileSystemAccess

import org.eclipse.uml2.uml.Transition

class GuardianTemplate {
	@Inject extension Naming
	@Inject extension StateMachineHelper

	def doGenerate (org.eclipse.uml2.uml.StateMachine it, IFileSystemAccess fsa) {
        val content = generate
        fsa.generateFile(packagePath+"/"+name+"Guardian.scala", content)
    }

    private def generate (org.eclipse.uml2.uml.StateMachine it) '''
        package «packageName»

        import org.apache.pekko.NotUsed
        import org.apache.pekko.actor.typed.receptionist.{Receptionist, ServiceKey}
        import org.apache.pekko.actor.typed.scaladsl.Behaviors
        import org.apache.pekko.actor.typed.{ActorRef, ActorSystem, Behavior, SupervisorStrategy}
        import org.apache.pekko.cluster.sharding.external.ExternalShardAllocationStrategy
        import org.apache.pekko.cluster.sharding.typed.{HashCodeNoEnvelopeMessageExtractor, ShardingMessageExtractor}
        import org.apache.pekko.cluster.sharding.typed.scaladsl.{ClusterSharding, Entity, EntityTypeKey}
        import org.apache.pekko.persistence.typed.{EventAdapter, PersistenceId, SnapshotAdapter, SnapshotSelectionCriteria}
        import org.apache.pekko.persistence.typed.scaladsl.{EventSourcedBehavior, Recovery, RetentionCriteria}
        import org.salgar.pekko.fsm.base.eventadapter.NoOpEventAdapter
        import «packageName».«name».{«renderInitialState(it)», «name»Event, Response, PersistEvent, State}
        import «packageName».config.«name»ServiceLocator

        import scala.concurrent.duration._

        object «name»Guardian {
            private val «name.toLowerCase()»Key = ServiceKey[«name»Event]("«name.toLowerCase()»Service")
            private val «name.toLowerCase()»TypeKey = EntityTypeKey[«name»Event]("«name.toLowerCase()»")
            private var _snapshotAdapter: SnapshotAdapter[State] = _
            private var _eventAdapter: EventAdapter[PersistEvent, PersistEvent] = _
            private var _defaultMessageExtractor: ShardingMessageExtractor[«name»Event, «name»Event] = _
            private var _externalShardAllocationStrategy:  ExternalShardAllocationStrategy = _


            sealed trait «name»GuardianEvent
            final case class onReportState(payload: java.util.Map[String, AnyRef], replyTo : ActorRef[«name».Response]) extends «name»GuardianEvent
            «FOR trigger : giveTransitionWithTrigger(allOwnedElements().filter(Transition)).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])»
                final case class «trigger.name»(payload:java.util.Map[String, AnyRef], replyTo: ActorRef[«name».Response]) extends «name»GuardianEvent
                object «trigger.name» {
                    def apply(payload: java.util.Map[String, AnyRef]) : «trigger.name» = {
                        «trigger.name»(payload, null)
                    }
                }
            «ENDFOR»
            final case class onAdd«name»Reference(listing: Receptionist.Listing) extends «name»GuardianEvent

            sealed trait «name»GuardianResponse

            //MessageAdapter
            private final case class WrappedReportStateResponse(reportResponse:  Response) extends «name»GuardianEvent

            var listing: Receptionist.Listing = _

            def apply() (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[«name»GuardianEvent] =
                apply(
                    «name»SnapshotAdapter,
                    NoOpEventAdapter.instance[PersistEvent, PersistEvent],
                    externalAllocationStrategy = false
                )(actorSystem, sharding)

            def apply(
                       shardingMessageExtractor : ShardingMessageExtractor[«name»Event, «name»Event]
                     )
                     (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[«name»GuardianEvent] =
                apply(
                    «name»SnapshotAdapter,
                    NoOpEventAdapter.instance[PersistEvent, PersistEvent],
                    shardingMessageExtractor,
                    externalAllocationStrategy = false
                )(actorSystem, sharding)

            def apply(
                       shardingMessageExtractor : ShardingMessageExtractor[«name»Event, «name»Event],
                       externalAllocationStrategy: Boolean)
                     (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[«name»GuardianEvent] =
                apply(
                    «name»SnapshotAdapter,
                    NoOpEventAdapter.instance[PersistEvent, PersistEvent],
                    shardingMessageExtractor,
                    externalAllocationStrategy = externalAllocationStrategy
                )(actorSystem, sharding)

            def apply(snapshotAdapter: SnapshotAdapter[State])
                     (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[«name»GuardianEvent] =
                apply(
                    snapshotAdapter,
                    NoOpEventAdapter.instance[PersistEvent, PersistEvent],
                    externalAllocationStrategy = false
                )(actorSystem, sharding)

            def apply(eventAdapter: EventAdapter[PersistEvent, PersistEvent])
                     (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[«name»GuardianEvent] =
                apply(
                    «name»SnapshotAdapter,
                    eventAdapter,
                    externalAllocationStrategy = false
                )(actorSystem, sharding)

            def apply(
                       snapshotAdapter: SnapshotAdapter[State],
                       eventAdapter: EventAdapter[PersistEvent, PersistEvent],
                       shardingMessageExtractor : ShardingMessageExtractor[«name»Event, «name»Event])
                     (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[«name»GuardianEvent] =
                apply(
                    snapshotAdapter,
                    eventAdapter,
                    shardingMessageExtractor,
                    externalAllocationStrategy = false
                )(actorSystem, sharding)

            def apply(
                       snapshotAdapter: SnapshotAdapter[State],
                       eventAdapter: EventAdapter[PersistEvent, PersistEvent])
                     (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[«name»GuardianEvent] = {
                apply(
                    snapshotAdapter,
                    eventAdapter,
                    new HashCodeNoEnvelopeMessageExtractor[«name»Event](numberOfShards = actorSystem.settings.config.getInt("pekko.fsm.numberOfShards")) {
                                                 override def entityId(message: «name»Event): String = message.useCaseKey.getKey
                                             },
                    externalAllocationStrategy = false
                )(actorSystem, sharding)
            }

            def apply(
                       snapshotAdapter: SnapshotAdapter[State],
                       eventAdapter: EventAdapter[PersistEvent, PersistEvent],
                       externalAllocationStrategy: Boolean)
                     (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[«name»GuardianEvent] = {
                apply(
                    snapshotAdapter,
                    eventAdapter,
                    new HashCodeNoEnvelopeMessageExtractor[«name»Event](numberOfShards = actorSystem.settings.config.getInt("pekko.fsm.numberOfShards")) {
                                                 override def entityId(message: «name»Event): String = message.useCaseKey.getKey
                                             },
                    externalAllocationStrategy = externalAllocationStrategy
                )(actorSystem, sharding)
            }

            def apply(
                       snapshotAdapter: SnapshotAdapter[State],
                       eventAdapter: EventAdapter[PersistEvent, PersistEvent],
                       shardingMessageExtractor : ShardingMessageExtractor[«name»Event, «name»Event],
                       externalAllocationStrategy: Boolean
                     )(implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[«name»GuardianEvent] = {
                _snapshotAdapter = snapshotAdapter
                _eventAdapter = eventAdapter
                _externalShardAllocationStrategy = new ExternalShardAllocationStrategy(actorSystem, «name.toLowerCase()»TypeKey.name)
                _defaultMessageExtractor = shardingMessageExtractor
                Behaviors
                    .setup[«name»GuardianEvent] {
                        context =>

                    val responseWrapper : ActorRef[Response] =
                        context.messageAdapter(response => WrappedReportStateResponse(response))

                    val listingAdapter: ActorRef[Receptionist.Listing] = context.messageAdapter {
                        listing => onAdd«name»Reference(listing)
                    }
                    context.system.receptionist ! Receptionist.Subscribe(«name.toLowerCase()»Key, listingAdapter)

                    Behaviors
                        .receivePartial[«name»GuardianEvent] {
                            case (ctx, onReportState(payload, replyTo)) =>
                                ctx.log.debug("We are processing onReportState(payload, replyto): {}, {}", payload.toString, replyTo.toString)
                                val useCaseKey = «name»ServiceLocator.getInstance.useCaseKeyStrategy.getKey(payload)
                                getActor(payload, externalAllocationStrategy) ! «name».onReport(() => useCaseKey, replyTo)
                                Behaviors.same
                            case (ctx, WrappedReportStateResponse(response)) =>
                                ctx.log.debug("We are processing WrappedReportStateResponse(response: {}", response.toString)
                                Behaviors.same
                            «FOR trigger : giveTransitionWithTrigger(allOwnedElements().filter(Transition)).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])»
                                case (ctx, «trigger.name»(payload, replyTo)) =>
                                    ctx.log.debug("We are processing «trigger.name»(payload): {}", payload.toString)
                                    val useCaseKey = «name»ServiceLocator.getInstance.useCaseKeyStrategy.getKey(payload)
                                    getActor(payload, externalAllocationStrategy) ! «name».«trigger.name»(() => useCaseKey, payload, replyTo)
                                    Behaviors.same
                            «ENDFOR»

                            case (ctx, onAdd«name»Reference(listings)) =>
                                listing = listings
                                Behaviors.same
                        }
                    }
         }.narrow

         private def getActor(
                               payload: java.util.Map[String, AnyRef],
                               externalAllocationStrategy: Boolean
                             )
                             (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): ActorRef[«name»Event] = {
            val «name.toLowerCase()»RefOption: Option[ActorRef[«name»Event]] = findActor(payload)
            if («name.toLowerCase()»RefOption.isDefined) {
                «name.toLowerCase()»RefOption.get
            } else {
                val shardRegion = prepare(externalAllocationStrategy)
                shardRegion
            }
        }

        private def prepare(externalAllocationStrategy: Boolean)
                    (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): ActorRef[«name»Event] = {
            val entity: Entity[«name»Event, «name»Event] = createEntity()
            if(externalAllocationStrategy) {
                entity.withAllocationStrategy(_externalShardAllocationStrategy)
            }
            val actorRef: ActorRef[«name»Event] = sharding
                .init(entity)
            actorRef
        }

        private def createEntity(): Entity[«name»Event, «name»Event] = {
            Entity(«name.toLowerCase()»TypeKey)(createBehavior = entityContext =>
                Behaviors
                    .supervise(
                        Behaviors.setup[«name»Event] {
                            context =>
                                context.system.receptionist ! Receptionist.Register(«name.toLowerCase()»Key, context.self)
                                context.system.receptionist ! Receptionist.Register(
                                ServiceKey[«name»Event](
                                    "«name.toLowerCase()»Service_" + entityContext.entityId
                                ),
                                context.self
                        )

                        val «name.toLowerCase()» = new «name»(
                            context,
                            entityContext.entityId
                        )
                        EventSourcedBehavior.withEnforcedReplies[«name»Event, PersistEvent, State](
                            persistenceId = PersistenceId
                                .ofUniqueId("«name.toLowerCase()»" + entityContext.entityId),
                            emptyState = «renderInitialState(it)»(new java.util.HashMap[java.lang.String, AnyRef]),
                            commandHandler = (state, cmd) => «name.toLowerCase()».commandHandler(context, cmd, state),
                            eventHandler = (state, event) => «name.toLowerCase()».eventHandler(context, state, event)
                        )
                        .snapshotWhen {
                            «FOR snapshotEvent : giveSnapshotEventsCollection»
                                case(state, «packageName(snapshotEvent)».«name».«snapshotEvent.name»(_), sequenceNumber) => true
                            «ENDFOR»
                            case(state, event, sequenceNumber) => false
                        }
                        .snapshotAdapter(_snapshotAdapter)
                        .eventAdapter(_eventAdapter)
                        .withRecovery(Recovery.withSnapshotSelectionCriteria(SnapshotSelectionCriteria.latest))
                        .withRetention(RetentionCriteria.snapshotEvery(numberOfEvents = 100, keepNSnapshots = 2))
                        .withTagger(event => Set("«name.toLowerCase»-" + Math.abs(entityContext.entityId.hashCode)%context.system.settings.config.getInt("pekko.fsm.numberOfShards")))
                    }
                 )
                .onFailure[Exception](
                    SupervisorStrategy.restartWithBackoff(minBackoff = 5.seconds, maxBackoff = 1.minute, randomFactor = 0.2)
                )).withMessageExtractor(_defaultMessageExtractor)
        }

        private def findActor(payload: java.util.Map[String, AnyRef]): Option[ActorRef[«name»Event]] = {
            val actorName: String = «name»ServiceLocator.getInstance.useCaseKeyStrategy
                .getKey(payload)
            val listingOption = Option(listing)
            if(listingOption.isDefined)
                listingOption.get.serviceInstances(«name.toLowerCase()»Key).foreach(«name.toLowerCase()» =>
                if («name.toLowerCase()».path.name == actorName) {
                    return Option(«name.toLowerCase()»)
                }
            )
            Option.empty
         }
        }
    '''
}
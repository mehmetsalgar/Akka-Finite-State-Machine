package org.salgar.fsm.pekko.foureyes.fraudprevention

import org.salgar.fsm.pekko.foureyes.fraudprevention.FraudPreventionSM._
import org.salgar.pekko.fsm.api.UseCaseKey
import org.salgar.pekko.fsm.base.eventadapter.NoOpEventAdapter

object FraudPreventionSMGuardian {
    private val fraudpreventionsmKey: ServiceKey[FraudPreventionSMEvent] = ServiceKey[FraudPreventionSMEvent]("fraudpreventionsmService")
    private val fraudpreventionsmTypeKey = EntityTypeKey[FraudPreventionSMEvent]("fraudpreventionsm")
    private var _snapshotAdapter: SnapshotAdapter[State] = _
    private var _eventAdapter: EventAdapter[PersistEvent, PersistEvent] = _
    private var _defaultMessageExtractor: ShardingMessageExtractor[FraudPreventionSMEvent, FraudPreventionSMEvent] = _
    private var _externalShardAllocationStrategy:  ExternalShardAllocationStrategy = _

    sealed trait FraudPreventionSMGuardianEvent
    final case class onReportState(useCaseKey: UseCaseKey, payload: java.util.Map[String, AnyRef], replyTo : ActorRef[FraudPreventionSM.Response]) extends FraudPreventionSMGuardianEvent

    final case class onError(useCaseKey: UseCaseKey, payload:java.util.Map[String, AnyRef], replyTo: ActorRef[FraudPreventionSM.Response]) extends FraudPreventionSMGuardianEvent
    object onError {
        def apply(useCaseKey: UseCaseKey, payload: java.util.Map[String, AnyRef]) : onError = {
            onError(useCaseKey, payload, null)
        }
    }
    final case class onResult(useCaseKey: UseCaseKey, payload:java.util.Map[String, AnyRef], replyTo: ActorRef[FraudPreventionSM.Response]) extends FraudPreventionSMGuardianEvent
    object onResult {
        def apply(useCaseKey: UseCaseKey, payload: java.util.Map[String, AnyRef]) : onResult = {
            onResult(useCaseKey, payload, null)
        }
    }
    final case class onRetry(useCaseKey: UseCaseKey, payload:java.util.Map[String, AnyRef], replyTo: ActorRef[FraudPreventionSM.Response]) extends FraudPreventionSMGuardianEvent
    object onRetry {
        def apply(useCaseKey: UseCaseKey, payload: java.util.Map[String, AnyRef]) : onRetry = {
            onRetry(useCaseKey, payload, null)
        }
    }
    final case class onStartFraudPreventionEvaluation(useCaseKey: UseCaseKey, payload:java.util.Map[String, AnyRef], replyTo: ActorRef[FraudPreventionSM.Response]) extends FraudPreventionSMGuardianEvent
    object onStartFraudPreventionEvaluation {
        def apply(useCaseKey: UseCaseKey, payload: java.util.Map[String, AnyRef]) : onStartFraudPreventionEvaluation = {
            onStartFraudPreventionEvaluation(useCaseKey, payload, null)
        }
    }

    //Message Adapter
    private final case class WrappedReportStateResponse(reportResponse: Response) extends FraudPreventionSMGuardianEvent

    sealed trait ResponseState
    final case class ReportResponseState(state : FraudPreventionSM.State) extends ResponseState

    final case class onAddFraudPreventionSMReference(listing: Receptionist.Listing) extends FraudPreventionSMGuardianEvent
    var listing: Receptionist.Listing = _

    def apply()
            (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[FraudPreventionSMGuardianEvent] = {
        apply(
        FraudPreventionSMSnapshotAdapter,
        NoOpEventAdapter.instance[PersistEvent, PersistEvent],
        externalAllocationStrategy = false)(actorSystem, sharding)
    }

    def apply(
                shardingMessageExtractor : ShardingMessageExtractor[FraudPreventionSMEvent, FraudPreventionSMEvent]
            )
            (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[FraudPreventionSMGuardianEvent] = {
        apply(
        FraudPreventionSMSnapshotAdapter,
        NoOpEventAdapter.instance[PersistEvent, PersistEvent],
        shardingMessageExtractor,
        externalAllocationStrategy = false)(actorSystem, sharding)
    }

    def apply(
           shardingMessageExtractor : ShardingMessageExtractor[FraudPreventionSMEvent, FraudPreventionSMEvent],
           externalAllocationStrategy: Boolean)
         (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[FraudPreventionSMGuardianEvent] = {
        apply(
            FraudPreventionSMSnapshotAdapter,
            NoOpEventAdapter.instance[PersistEvent, PersistEvent],
            shardingMessageExtractor,
            externalAllocationStrategy = externalAllocationStrategy
        )(actorSystem, sharding)
    }

    def apply(
                snapshotAdapter: SnapshotAdapter[State])
            (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[FraudPreventionSMGuardianEvent] = {
        apply(
                snapshotAdapter,
                NoOpEventAdapter.instance[PersistEvent, PersistEvent],
                externalAllocationStrategy = false
            )(actorSystem, sharding)
    }

    def apply(
                eventAdapter: EventAdapter[PersistEvent, PersistEvent]
            )
            (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[FraudPreventionSMGuardianEvent] = {
        apply(
                FraudPreventionSMSnapshotAdapter,
                eventAdapter,
                externalAllocationStrategy = false
             )(actorSystem, sharding)
    }

    def apply(
               snapshotAdapter: SnapshotAdapter[State],
               eventAdapter: EventAdapter[PersistEvent, PersistEvent],
               externalAllocationStrategy: Boolean)
             (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[FraudPreventionSMGuardianEvent] = {
        apply(
            snapshotAdapter,
            eventAdapter,
            new HashCodeNoEnvelopeMessageExtractor[FraudPreventionSMEvent](numberOfShards = actorSystem.settings.config.getInt("pekko.fsm.numberOfShards")) {
                override def entityId(message: FraudPreventionSMEvent): String = message.useCaseKey.getKey
            },
            externalAllocationStrategy = false
        )(actorSystem, sharding)
    }

    def apply(
                snapshotAdapter: SnapshotAdapter[State],
                eventAdapter: EventAdapter[PersistEvent, PersistEvent],
                shardingMessageExtractor : ShardingMessageExtractor[FraudPreventionSMEvent, FraudPreventionSMEvent],
                externalAllocationStrategy: Boolean)
            (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[FraudPreventionSMGuardianEvent] = {
        _snapshotAdapter = snapshotAdapter
        _eventAdapter = eventAdapter
        _externalShardAllocationStrategy = new ExternalShardAllocationStrategy(actorSystem, fraudpreventionsmTypeKey.name)
        _defaultMessageExtractor = shardingMessageExtractor
        Behaviors
        .setup[FraudPreventionSMGuardianEvent] {
            context =>
                val baseActorResponseWrapper : ActorRef[Response] =
                  context.messageAdapter(response => WrappedReportStateResponse(response))

                val listingAdapter: ActorRef[Receptionist.Listing] = context.messageAdapter {
                    listing => onAddFraudPreventionSMReference(listing)
                }
                context.system.receptionist ! Receptionist.Subscribe(fraudpreventionsmKey, listingAdapter)

            Behaviors
                .receivePartial[FraudPreventionSMGuardianEvent] {
                    case (ctx, onReportState(useCaseKey, payload, replyTo)) =>
                        ctx.log.debug("We are processing onReportState(payload, replyto): {}, {}", payload.toString, replyTo.toString)
                        val fraudpreventionsmOption: Option[ActorRef[FraudPreventionSMEvent]] = findActor(useCaseKey)
                        if(fraudpreventionsmOption.isDefined) {
                            fraudpreventionsmOption.get ! FraudPreventionSM.onReport(() => useCaseKey.getKey, replyTo)
                        } else {
                            ctx.log.warn("A fraudpreventionsm actor should exist for key: {}", useCaseKey.getKey)
                        }
                        Behaviors.same
                    case (ctx, WrappedReportStateResponse(response)) =>
                        ctx.log.debug("We are processing WrappedReportStateResponse(response: {}", response.toString)
                        Behaviors.same

                    case (ctx, onStartFraudPreventionEvaluation(useCaseKey, payload, replyTo))  =>
                        ctx.log.debug("We are processing onStartFraudPreventionEvaluation(payload): {}", payload.toString)
                        getActor(useCaseKey, externalAllocationStrategy) ! FraudPreventionSM.onStartFraudPreventionEvaluation(useCaseKey, payload, replyTo)
                        Behaviors.same
                    case (ctx, onRetry(useCaseKey, payload, replyTo))  =>
                        ctx.log.debug("We are processing onRetry(payload): {}", payload.toString)
                        getActor(useCaseKey, externalAllocationStrategy) ! FraudPreventionSM.onRetry(useCaseKey, payload, replyTo)
                        Behaviors.same
                    case (ctx, onError(useCaseKey, payload, replyTo))  =>
                        ctx.log.debug("We are processing onError(payload): {}", payload.toString)
                        getActor(useCaseKey, externalAllocationStrategy) ! FraudPreventionSM.onError(useCaseKey, payload, replyTo)
                        Behaviors.same
                    case (ctx, onResult(useCaseKey, payload, replyTo))  =>
                        ctx.log.debug("We are processing onResult(payload): {}", payload.toString)
                        getActor(useCaseKey, externalAllocationStrategy) ! FraudPreventionSM.onResult(useCaseKey, payload, replyTo)
                        Behaviors.same

                    case (ctx, onAddFraudPreventionSMReference(listings)) =>
                        listing = listings
                        Behaviors.same
                }
        }
    }.narrow

    private def getActor(useCaseKey: UseCaseKey, externalAllocationStrategy: Boolean)
                        (implicit sharding: ClusterSharding, actorSystem: ActorSystem[NotUsed]): ActorRef[FraudPreventionSMEvent] = {
        val fraudpreventionsmOption: Option[ActorRef[FraudPreventionSMEvent]] = findActor(useCaseKey)
        if (fraudpreventionsmOption.isDefined) {
            fraudpreventionsmOption.get
        } else {
            shardInit(externalAllocationStrategy)
        }
    }

    def shardInit()(implicit sharding: ClusterSharding, actorSystem: ActorSystem[NotUsed]): ActorRef[FraudPreventionSMEvent] = {
        shardInit(false)
    }

    def shardInit(externalAllocationStrategy: Boolean)(implicit sharding: ClusterSharding, actorSystem: ActorSystem[NotUsed]): ActorRef[FraudPreventionSMEvent] = {
        val entity: Entity[FraudPreventionSMEvent, FraudPreventionSMEvent] = createEntity()
        if(externalAllocationStrategy) {
            entity.withAllocationStrategy(_externalShardAllocationStrategy)
        }
        sharding.init(entity)
    }

    private def createEntity(): Entity[FraudPreventionSMEvent, FraudPreventionSMEvent] = {
        Entity(fraudpreventionsmTypeKey) (createBehavior =
            entityContext =>
                Behaviors.supervise(
                    Behaviors.setup[FraudPreventionSMEvent] {
                        ctx => {
                            ctx.system.receptionist ! Receptionist.Register(FraudPreventionSMGuardian.fraudpreventionsmKey, ctx.self)
                            ctx.system.receptionist ! Receptionist.Register(
                                ServiceKey[FraudPreventionSMEvent](
                                    "fraudpreventionsmService_" + entityContext.entityId
                                ),
                                ctx.self
                            )

                            val fraudpreventionsm: FraudPreventionSM = new FraudPreventionSM(
                                ctx,
                                entityContext.entityId
                            )

                            EventSourcedBehavior.withEnforcedReplies[FraudPreventionSMEvent, PersistEvent, State](
                                persistenceId = PersistenceId.ofUniqueId("fraudpreventionsm" + entityContext.entityId),
                                emptyState = 
                                INITIAL
                                (new java.util.HashMap[java.lang.String, AnyRef]),
                                commandHandler = (state, cmd) => fraudpreventionsm.commandHandler(ctx, cmd, state),
                                eventHandler = (state, event) => fraudpreventionsm.eventHandler(ctx, state, event)
                            )
                            .snapshotWhen {
                                case(state, event, sequenceNumber) => false
                            }
                            .snapshotAdapter(_snapshotAdapter)
                            .eventAdapter(_eventAdapter)
                            .withRecovery(Recovery.withSnapshotSelectionCriteria(SnapshotSelectionCriteria.latest))
                            .withRetention(RetentionCriteria.snapshotEvery(numberOfEvents = 100, keepNSnapshots = 2))
                            .withTagger(event => Set("fraudpreventionsm-" + Math.abs(entityContext.entityId.hashCode)%ctx.system.settings.config.getInt("pekko.fsm.numberOfShards")))
                        }
                    }
                ).onFailure(
                    SupervisorStrategy.restartWithBackoff(minBackoff = 5.seconds, maxBackoff = 1.minute, randomFactor = 0.2)
                )
        ).withMessageExtractor(_defaultMessageExtractor)
    }

    private def findActor(useCaseKey: UseCaseKey): Option[ActorRef[FraudPreventionSMEvent]] = {
        val listingOption = Option(listing)
        if (listingOption.isDefined)
            listingOption.get.serviceInstances(fraudpreventionsmKey).foreach(fraudpreventionsm =>
                if (fraudpreventionsm.path.name == useCaseKey.getKey) {
                    return Option(fraudpreventionsm)
                })
                Option.empty
    }
}

package org.salgar.fsm.pekko.foureyes.creditscore

import org.salgar.fsm.pekko.foureyes.creditscore.MultiTenantCreditScoreSM._
import org.salgar.pekko.fsm.api.UseCaseKey
import org.salgar.pekko.fsm.base.eventadapter.NoOpEventAdapter

object MultiTenantCreditScoreSMGuardian {
    private val multitenantcreditscoresmKey: ServiceKey[MultiTenantCreditScoreSMEvent] = ServiceKey[MultiTenantCreditScoreSMEvent]("multitenantcreditscoresmService")
    private val multitenantcreditscoresmTypeKey = EntityTypeKey[MultiTenantCreditScoreSMEvent]("multitenantcreditscoresm")
    private var _snapshotAdapter: SnapshotAdapter[State] = _
    private var _eventAdapter: EventAdapter[PersistEvent, PersistEvent] = _
    private var _defaultMessageExtractor: ShardingMessageExtractor[MultiTenantCreditScoreSMEvent, MultiTenantCreditScoreSMEvent] = _
    private var _externalShardAllocationStrategy:  ExternalShardAllocationStrategy = _

    sealed trait MultiTenantCreditScoreSMGuardianEvent
    final case class onReportState(useCaseKey: UseCaseKey, payload: java.util.Map[String, AnyRef], replyTo : ActorRef[MultiTenantCreditScoreSM.Response]) extends MultiTenantCreditScoreSMGuardianEvent

    final case class onCreditScoreReceived(useCaseKey: UseCaseKey, payload:java.util.Map[String, AnyRef], replyTo: ActorRef[MultiTenantCreditScoreSM.Response]) extends MultiTenantCreditScoreSMGuardianEvent
    object onCreditScoreReceived {
        def apply(useCaseKey: UseCaseKey, payload: java.util.Map[String, AnyRef]) : onCreditScoreReceived = {
            onCreditScoreReceived(useCaseKey, payload, null)
        }
    }
    final case class onStartMultiTenantCreditScoreResearch(useCaseKey: UseCaseKey, payload:java.util.Map[String, AnyRef], replyTo: ActorRef[MultiTenantCreditScoreSM.Response]) extends MultiTenantCreditScoreSMGuardianEvent
    object onStartMultiTenantCreditScoreResearch {
        def apply(useCaseKey: UseCaseKey, payload: java.util.Map[String, AnyRef]) : onStartMultiTenantCreditScoreResearch = {
            onStartMultiTenantCreditScoreResearch(useCaseKey, payload, null)
        }
    }

    //Message Adapter
    private final case class WrappedReportStateResponse(reportResponse: Response) extends MultiTenantCreditScoreSMGuardianEvent

    sealed trait ResponseState
    final case class ReportResponseState(state : MultiTenantCreditScoreSM.State) extends ResponseState

    final case class onAddMultiTenantCreditScoreSMReference(listing: Receptionist.Listing) extends MultiTenantCreditScoreSMGuardianEvent
    var listing: Receptionist.Listing = _

    def apply()
            (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[MultiTenantCreditScoreSMGuardianEvent] = {
        apply(
        MultiTenantCreditScoreSMSnapshotAdapter,
        NoOpEventAdapter.instance[PersistEvent, PersistEvent],
        externalAllocationStrategy = false)(actorSystem, sharding)
    }

    def apply(
                shardingMessageExtractor : ShardingMessageExtractor[MultiTenantCreditScoreSMEvent, MultiTenantCreditScoreSMEvent]
            )
            (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[MultiTenantCreditScoreSMGuardianEvent] = {
        apply(
        MultiTenantCreditScoreSMSnapshotAdapter,
        NoOpEventAdapter.instance[PersistEvent, PersistEvent],
        shardingMessageExtractor,
        externalAllocationStrategy = false)(actorSystem, sharding)
    }

    def apply(
           shardingMessageExtractor : ShardingMessageExtractor[MultiTenantCreditScoreSMEvent, MultiTenantCreditScoreSMEvent],
           externalAllocationStrategy: Boolean)
         (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[MultiTenantCreditScoreSMGuardianEvent] = {
        apply(
            MultiTenantCreditScoreSMSnapshotAdapter,
            NoOpEventAdapter.instance[PersistEvent, PersistEvent],
            shardingMessageExtractor,
            externalAllocationStrategy = externalAllocationStrategy
        )(actorSystem, sharding)
    }

    def apply(
                snapshotAdapter: SnapshotAdapter[State])
            (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[MultiTenantCreditScoreSMGuardianEvent] = {
        apply(
                snapshotAdapter,
                NoOpEventAdapter.instance[PersistEvent, PersistEvent],
                externalAllocationStrategy = false
            )(actorSystem, sharding)
    }

    def apply(
                eventAdapter: EventAdapter[PersistEvent, PersistEvent]
            )
            (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[MultiTenantCreditScoreSMGuardianEvent] = {
        apply(
                MultiTenantCreditScoreSMSnapshotAdapter,
                eventAdapter,
                externalAllocationStrategy = false
             )(actorSystem, sharding)
    }

    def apply(
               snapshotAdapter: SnapshotAdapter[State],
               eventAdapter: EventAdapter[PersistEvent, PersistEvent],
               externalAllocationStrategy: Boolean)
             (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[MultiTenantCreditScoreSMGuardianEvent] = {
        apply(
            snapshotAdapter,
            eventAdapter,
            new HashCodeNoEnvelopeMessageExtractor[MultiTenantCreditScoreSMEvent](numberOfShards = actorSystem.settings.config.getInt("pekko.fsm.numberOfShards")) {
                override def entityId(message: MultiTenantCreditScoreSMEvent): String = message.useCaseKey.getKey
            },
            externalAllocationStrategy = false
        )(actorSystem, sharding)
    }

    def apply(
                snapshotAdapter: SnapshotAdapter[State],
                eventAdapter: EventAdapter[PersistEvent, PersistEvent],
                shardingMessageExtractor : ShardingMessageExtractor[MultiTenantCreditScoreSMEvent, MultiTenantCreditScoreSMEvent],
                externalAllocationStrategy: Boolean)
            (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[MultiTenantCreditScoreSMGuardianEvent] = {
        _snapshotAdapter = snapshotAdapter
        _eventAdapter = eventAdapter
        _externalShardAllocationStrategy = new ExternalShardAllocationStrategy(actorSystem, multitenantcreditscoresmTypeKey.name)
        _defaultMessageExtractor = shardingMessageExtractor
        Behaviors
        .setup[MultiTenantCreditScoreSMGuardianEvent] {
            context =>
                val baseActorResponseWrapper : ActorRef[Response] =
                  context.messageAdapter(response => WrappedReportStateResponse(response))

                val listingAdapter: ActorRef[Receptionist.Listing] = context.messageAdapter {
                    listing => onAddMultiTenantCreditScoreSMReference(listing)
                }
                context.system.receptionist ! Receptionist.Subscribe(multitenantcreditscoresmKey, listingAdapter)

            Behaviors
                .receivePartial[MultiTenantCreditScoreSMGuardianEvent] {
                    case (ctx, onReportState(useCaseKey, payload, replyTo)) =>
                        ctx.log.debug("We are processing onReportState(payload, replyto): {}, {}", payload.toString, replyTo.toString)
                        val multitenantcreditscoresmOption: Option[ActorRef[MultiTenantCreditScoreSMEvent]] = findActor(useCaseKey)
                        if(multitenantcreditscoresmOption.isDefined) {
                            multitenantcreditscoresmOption.get ! MultiTenantCreditScoreSM.onReport(() => useCaseKey.getKey, replyTo)
                        } else {
                            ctx.log.warn("A multitenantcreditscoresm actor should exist for key: {}", useCaseKey.getKey)
                        }
                        Behaviors.same
                    case (ctx, WrappedReportStateResponse(response)) =>
                        ctx.log.debug("We are processing WrappedReportStateResponse(response: {}", response.toString)
                        Behaviors.same

                    case (ctx, onCreditScoreReceived(useCaseKey, payload, replyTo))  =>
                        ctx.log.debug("We are processing onCreditScoreReceived(payload): {}", payload.toString)
                        getActor(useCaseKey, externalAllocationStrategy) ! MultiTenantCreditScoreSM.onCreditScoreReceived(useCaseKey, payload, replyTo)
                        Behaviors.same
                    case (ctx, onStartMultiTenantCreditScoreResearch(useCaseKey, payload, replyTo))  =>
                        ctx.log.debug("We are processing onStartMultiTenantCreditScoreResearch(payload): {}", payload.toString)
                        getActor(useCaseKey, externalAllocationStrategy) ! MultiTenantCreditScoreSM.onStartMultiTenantCreditScoreResearch(useCaseKey, payload, replyTo)
                        Behaviors.same

                    case (ctx, onAddMultiTenantCreditScoreSMReference(listings)) =>
                        listing = listings
                        Behaviors.same
                }
        }
    }.narrow

    private def getActor(useCaseKey: UseCaseKey, externalAllocationStrategy: Boolean)
                        (implicit sharding: ClusterSharding, actorSystem: ActorSystem[NotUsed]): ActorRef[MultiTenantCreditScoreSMEvent] = {
        val multitenantcreditscoresmOption: Option[ActorRef[MultiTenantCreditScoreSMEvent]] = findActor(useCaseKey)
        if (multitenantcreditscoresmOption.isDefined) {
            multitenantcreditscoresmOption.get
        } else {
            shardInit(externalAllocationStrategy)
        }
    }

    def shardInit()(implicit sharding: ClusterSharding, actorSystem: ActorSystem[NotUsed]): ActorRef[MultiTenantCreditScoreSMEvent] = {
        shardInit(false)
    }

    def shardInit(externalAllocationStrategy: Boolean)(implicit sharding: ClusterSharding, actorSystem: ActorSystem[NotUsed]): ActorRef[MultiTenantCreditScoreSMEvent] = {
        val entity: Entity[MultiTenantCreditScoreSMEvent, MultiTenantCreditScoreSMEvent] = createEntity()
        if(externalAllocationStrategy) {
            entity.withAllocationStrategy(_externalShardAllocationStrategy)
        }
        sharding.init(entity)
    }

    private def createEntity(): Entity[MultiTenantCreditScoreSMEvent, MultiTenantCreditScoreSMEvent] = {
        Entity(multitenantcreditscoresmTypeKey) (createBehavior =
            entityContext =>
                Behaviors.supervise(
                    Behaviors.setup[MultiTenantCreditScoreSMEvent] {
                        ctx => {
                            ctx.system.receptionist ! Receptionist.Register(MultiTenantCreditScoreSMGuardian.multitenantcreditscoresmKey, ctx.self)
                            ctx.system.receptionist ! Receptionist.Register(
                                ServiceKey[MultiTenantCreditScoreSMEvent](
                                    "multitenantcreditscoresmService_" + entityContext.entityId
                                ),
                                ctx.self
                            )

                            val multitenantcreditscoresm: MultiTenantCreditScoreSM = new MultiTenantCreditScoreSM(
                                ctx,
                                entityContext.entityId
                            )

                            EventSourcedBehavior.withEnforcedReplies[MultiTenantCreditScoreSMEvent, PersistEvent, State](
                                persistenceId = PersistenceId.ofUniqueId("multitenantcreditscoresm" + entityContext.entityId),
                                emptyState = 
                                INITIAL_MTCS
                                (new java.util.HashMap[java.lang.String, AnyRef]),
                                commandHandler = (state, cmd) => multitenantcreditscoresm.commandHandler(ctx, cmd, state),
                                eventHandler = (state, event) => multitenantcreditscoresm.eventHandler(ctx, state, event)
                            )
                            .snapshotWhen {
                                case(state, event, sequenceNumber) => false
                            }
                            .snapshotAdapter(_snapshotAdapter)
                            .eventAdapter(_eventAdapter)
                            .withRecovery(Recovery.withSnapshotSelectionCriteria(SnapshotSelectionCriteria.latest))
                            .withRetention(RetentionCriteria.snapshotEvery(numberOfEvents = 100, keepNSnapshots = 2))
                            .withTagger(event => Set("multitenantcreditscoresm-" + Math.abs(entityContext.entityId.hashCode)%ctx.system.settings.config.getInt("pekko.fsm.numberOfShards")))
                        }
                    }
                ).onFailure(
                    SupervisorStrategy.restartWithBackoff(minBackoff = 5.seconds, maxBackoff = 1.minute, randomFactor = 0.2)
                )
        ).withMessageExtractor(_defaultMessageExtractor)
    }

    private def findActor(useCaseKey: UseCaseKey): Option[ActorRef[MultiTenantCreditScoreSMEvent]] = {
        val listingOption = Option(listing)
        if (listingOption.isDefined)
            listingOption.get.serviceInstances(multitenantcreditscoresmKey).foreach(multitenantcreditscoresm =>
                if (multitenantcreditscoresm.path.name == useCaseKey.getKey) {
                    return Option(multitenantcreditscoresm)
                })
                Option.empty
    }
}

package org.salgar.fsm.pekko.foureyes.creditscore

import org.salgar.fsm.pekko.foureyes.creditscore.CreditScoreSM._
import org.salgar.pekko.fsm.api.UseCaseKey
import org.salgar.pekko.fsm.base.eventadapter.NoOpEventAdapter

object CreditScoreSMGuardian {
    private val creditscoresmKey: ServiceKey[CreditScoreSMEvent] = ServiceKey[CreditScoreSMEvent]("creditscoresmService")
    private val creditscoresmTypeKey = EntityTypeKey[CreditScoreSMEvent]("creditscoresm")
    private var _snapshotAdapter: SnapshotAdapter[State] = _
    private var _eventAdapter: EventAdapter[PersistEvent, PersistEvent] = _
    private var _defaultMessageExtractor: ShardingMessageExtractor[CreditScoreSMEvent, CreditScoreSMEvent] = _
    private var _externalShardAllocationStrategy:  ExternalShardAllocationStrategy = _

    sealed trait CreditScoreSMGuardianEvent
    final case class onReportState(useCaseKey: UseCaseKey, payload: java.util.Map[String, AnyRef], replyTo : ActorRef[CreditScoreSM.Response]) extends CreditScoreSMGuardianEvent

    final case class onError(useCaseKey: UseCaseKey, payload:java.util.Map[String, AnyRef], replyTo: ActorRef[CreditScoreSM.Response]) extends CreditScoreSMGuardianEvent
    object onError {
        def apply(useCaseKey: UseCaseKey, payload: java.util.Map[String, AnyRef]) : onError = {
            onError(useCaseKey, payload, null)
        }
    }
    final case class onResultReceived(useCaseKey: UseCaseKey, payload:java.util.Map[String, AnyRef], replyTo: ActorRef[CreditScoreSM.Response]) extends CreditScoreSMGuardianEvent
    object onResultReceived {
        def apply(useCaseKey: UseCaseKey, payload: java.util.Map[String, AnyRef]) : onResultReceived = {
            onResultReceived(useCaseKey, payload, null)
        }
    }
    final case class onRetry(useCaseKey: UseCaseKey, payload:java.util.Map[String, AnyRef], replyTo: ActorRef[CreditScoreSM.Response]) extends CreditScoreSMGuardianEvent
    object onRetry {
        def apply(useCaseKey: UseCaseKey, payload: java.util.Map[String, AnyRef]) : onRetry = {
            onRetry(useCaseKey, payload, null)
        }
    }
    final case class onStartCreditScoreResearch(useCaseKey: UseCaseKey, payload:java.util.Map[String, AnyRef], replyTo: ActorRef[CreditScoreSM.Response]) extends CreditScoreSMGuardianEvent
    object onStartCreditScoreResearch {
        def apply(useCaseKey: UseCaseKey, payload: java.util.Map[String, AnyRef]) : onStartCreditScoreResearch = {
            onStartCreditScoreResearch(useCaseKey, payload, null)
        }
    }

    //Message Adapter
    private final case class WrappedReportStateResponse(reportResponse: Response) extends CreditScoreSMGuardianEvent

    sealed trait ResponseState
    final case class ReportResponseState(state : CreditScoreSM.State) extends ResponseState

    final case class onAddCreditScoreSMReference(listing: Receptionist.Listing) extends CreditScoreSMGuardianEvent
    var listing: Receptionist.Listing = _

    def apply()
            (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[CreditScoreSMGuardianEvent] = {
        apply(
        CreditScoreSMSnapshotAdapter,
        NoOpEventAdapter.instance[PersistEvent, PersistEvent],
        externalAllocationStrategy = false)(actorSystem, sharding)
    }

    def apply(
                shardingMessageExtractor : ShardingMessageExtractor[CreditScoreSMEvent, CreditScoreSMEvent]
            )
            (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[CreditScoreSMGuardianEvent] = {
        apply(
        CreditScoreSMSnapshotAdapter,
        NoOpEventAdapter.instance[PersistEvent, PersistEvent],
        shardingMessageExtractor,
        externalAllocationStrategy = false)(actorSystem, sharding)
    }

    def apply(
           shardingMessageExtractor : ShardingMessageExtractor[CreditScoreSMEvent, CreditScoreSMEvent],
           externalAllocationStrategy: Boolean)
         (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[CreditScoreSMGuardianEvent] = {
        apply(
            CreditScoreSMSnapshotAdapter,
            NoOpEventAdapter.instance[PersistEvent, PersistEvent],
            shardingMessageExtractor,
            externalAllocationStrategy = externalAllocationStrategy
        )(actorSystem, sharding)
    }

    def apply(
                snapshotAdapter: SnapshotAdapter[State])
            (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[CreditScoreSMGuardianEvent] = {
        apply(
                snapshotAdapter,
                NoOpEventAdapter.instance[PersistEvent, PersistEvent],
                externalAllocationStrategy = false
            )(actorSystem, sharding)
    }

    def apply(
                eventAdapter: EventAdapter[PersistEvent, PersistEvent]
            )
            (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[CreditScoreSMGuardianEvent] = {
        apply(
                CreditScoreSMSnapshotAdapter,
                eventAdapter,
                externalAllocationStrategy = false
             )(actorSystem, sharding)
    }

    def apply(
               snapshotAdapter: SnapshotAdapter[State],
               eventAdapter: EventAdapter[PersistEvent, PersistEvent],
               externalAllocationStrategy: Boolean)
             (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[CreditScoreSMGuardianEvent] = {
        apply(
            snapshotAdapter,
            eventAdapter,
            new HashCodeNoEnvelopeMessageExtractor[CreditScoreSMEvent](numberOfShards = actorSystem.settings.config.getInt("pekko.fsm.numberOfShards")) {
                override def entityId(message: CreditScoreSMEvent): String = message.useCaseKey.getKey
            },
            externalAllocationStrategy = false
        )(actorSystem, sharding)
    }

    def apply(
                snapshotAdapter: SnapshotAdapter[State],
                eventAdapter: EventAdapter[PersistEvent, PersistEvent],
                shardingMessageExtractor : ShardingMessageExtractor[CreditScoreSMEvent, CreditScoreSMEvent],
                externalAllocationStrategy: Boolean)
            (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[CreditScoreSMGuardianEvent] = {
        _snapshotAdapter = snapshotAdapter
        _eventAdapter = eventAdapter
        _externalShardAllocationStrategy = new ExternalShardAllocationStrategy(actorSystem, creditscoresmTypeKey.name)
        _defaultMessageExtractor = shardingMessageExtractor
        Behaviors
        .setup[CreditScoreSMGuardianEvent] {
            context =>
                val baseActorResponseWrapper : ActorRef[Response] =
                  context.messageAdapter(response => WrappedReportStateResponse(response))

                val listingAdapter: ActorRef[Receptionist.Listing] = context.messageAdapter {
                    listing => onAddCreditScoreSMReference(listing)
                }
                context.system.receptionist ! Receptionist.Subscribe(creditscoresmKey, listingAdapter)

            Behaviors
                .receivePartial[CreditScoreSMGuardianEvent] {
                    case (ctx, onReportState(useCaseKey, payload, replyTo)) =>
                        ctx.log.debug("We are processing onReportState(payload, replyto): {}, {}", payload.toString, replyTo.toString)
                        val creditscoresmOption: Option[ActorRef[CreditScoreSMEvent]] = findActor(useCaseKey)
                        if(creditscoresmOption.isDefined) {
                            creditscoresmOption.get ! CreditScoreSM.onReport(() => useCaseKey.getKey, replyTo)
                        } else {
                            ctx.log.warn("A creditscoresm actor should exist for key: {}", useCaseKey.getKey)
                        }
                        Behaviors.same
                    case (ctx, WrappedReportStateResponse(response)) =>
                        ctx.log.debug("We are processing WrappedReportStateResponse(response: {}", response.toString)
                        Behaviors.same

                    case (ctx, onRetry(useCaseKey, payload, replyTo))  =>
                        ctx.log.debug("We are processing onRetry(payload): {}", payload.toString)
                        getActor(useCaseKey, externalAllocationStrategy) ! CreditScoreSM.onRetry(useCaseKey, payload, replyTo)
                        Behaviors.same
                    case (ctx, onError(useCaseKey, payload, replyTo))  =>
                        ctx.log.debug("We are processing onError(payload): {}", payload.toString)
                        getActor(useCaseKey, externalAllocationStrategy) ! CreditScoreSM.onError(useCaseKey, payload, replyTo)
                        Behaviors.same
                    case (ctx, onResultReceived(useCaseKey, payload, replyTo))  =>
                        ctx.log.debug("We are processing onResultReceived(payload): {}", payload.toString)
                        getActor(useCaseKey, externalAllocationStrategy) ! CreditScoreSM.onResultReceived(useCaseKey, payload, replyTo)
                        Behaviors.same
                    case (ctx, onStartCreditScoreResearch(useCaseKey, payload, replyTo))  =>
                        ctx.log.debug("We are processing onStartCreditScoreResearch(payload): {}", payload.toString)
                        getActor(useCaseKey, externalAllocationStrategy) ! CreditScoreSM.onStartCreditScoreResearch(useCaseKey, payload, replyTo)
                        Behaviors.same

                    case (ctx, onAddCreditScoreSMReference(listings)) =>
                        listing = listings
                        Behaviors.same
                }
        }
    }.narrow

    private def getActor(useCaseKey: UseCaseKey, externalAllocationStrategy: Boolean)
                        (implicit sharding: ClusterSharding, actorSystem: ActorSystem[NotUsed]): ActorRef[CreditScoreSMEvent] = {
        val creditscoresmOption: Option[ActorRef[CreditScoreSMEvent]] = findActor(useCaseKey)
        if (creditscoresmOption.isDefined) {
            creditscoresmOption.get
        } else {
            shardInit(externalAllocationStrategy)
        }
    }

    def shardInit()(implicit sharding: ClusterSharding, actorSystem: ActorSystem[NotUsed]): ActorRef[CreditScoreSMEvent] = {
        shardInit(false)
    }

    def shardInit(externalAllocationStrategy: Boolean)(implicit sharding: ClusterSharding, actorSystem: ActorSystem[NotUsed]): ActorRef[CreditScoreSMEvent] = {
        val entity: Entity[CreditScoreSMEvent, CreditScoreSMEvent] = createEntity()
        if(externalAllocationStrategy) {
            entity.withAllocationStrategy(_externalShardAllocationStrategy)
        }
        sharding.init(entity)
    }

    private def createEntity(): Entity[CreditScoreSMEvent, CreditScoreSMEvent] = {
        Entity(creditscoresmTypeKey) (createBehavior =
            entityContext =>
                Behaviors.supervise(
                    Behaviors.setup[CreditScoreSMEvent] {
                        ctx => {
                            ctx.system.receptionist ! Receptionist.Register(CreditScoreSMGuardian.creditscoresmKey, ctx.self)
                            ctx.system.receptionist ! Receptionist.Register(
                                ServiceKey[CreditScoreSMEvent](
                                    "creditscoresmService_" + entityContext.entityId
                                ),
                                ctx.self
                            )

                            val creditscoresm: CreditScoreSM = new CreditScoreSM(
                                ctx,
                                entityContext.entityId
                            )

                            EventSourcedBehavior.withEnforcedReplies[CreditScoreSMEvent, PersistEvent, State](
                                persistenceId = PersistenceId.ofUniqueId("creditscoresm" + entityContext.entityId),
                                emptyState = 
                                INITIAL
                                (new java.util.HashMap[java.lang.String, AnyRef]),
                                commandHandler = (state, cmd) => creditscoresm.commandHandler(ctx, cmd, state),
                                eventHandler = (state, event) => creditscoresm.eventHandler(ctx, state, event)
                            )
                            .snapshotWhen {
                                case(state, event, sequenceNumber) => false
                            }
                            .snapshotAdapter(_snapshotAdapter)
                            .eventAdapter(_eventAdapter)
                            .withRecovery(Recovery.withSnapshotSelectionCriteria(SnapshotSelectionCriteria.latest))
                            .withRetention(RetentionCriteria.snapshotEvery(numberOfEvents = 100, keepNSnapshots = 2))
                            .withTagger(event => Set("creditscoresm-" + Math.abs(entityContext.entityId.hashCode)%ctx.system.settings.config.getInt("pekko.fsm.numberOfShards")))
                        }
                    }
                ).onFailure(
                    SupervisorStrategy.restartWithBackoff(minBackoff = 5.seconds, maxBackoff = 1.minute, randomFactor = 0.2)
                )
        ).withMessageExtractor(_defaultMessageExtractor)
    }

    private def findActor(useCaseKey: UseCaseKey): Option[ActorRef[CreditScoreSMEvent]] = {
        val listingOption = Option(listing)
        if (listingOption.isDefined)
            listingOption.get.serviceInstances(creditscoresmKey).foreach(creditscoresm =>
                if (creditscoresm.path.name == useCaseKey.getKey) {
                    return Option(creditscoresm)
                })
                Option.empty
    }
}

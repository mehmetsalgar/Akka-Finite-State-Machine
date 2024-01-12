package org.salgar.fsm.pekko.foureyes.addresscheck

import org.salgar.fsm.pekko.foureyes.addresscheck.AdressCheckSM._
import org.salgar.pekko.fsm.api.UseCaseKey
import org.salgar.pekko.fsm.base.eventadapter.NoOpEventAdapter

object AdressCheckSMGuardian {
    private val adresschecksmKey: ServiceKey[AdressCheckSMEvent] = ServiceKey[AdressCheckSMEvent]("adresschecksmService")
    private val adresschecksmTypeKey = EntityTypeKey[AdressCheckSMEvent]("adresschecksm")
    private var _snapshotAdapter: SnapshotAdapter[State] = _
    private var _eventAdapter: EventAdapter[PersistEvent, PersistEvent] = _
    private var _defaultMessageExtractor: ShardingMessageExtractor[AdressCheckSMEvent, AdressCheckSMEvent] = _
    private var _externalShardAllocationStrategy:  ExternalShardAllocationStrategy = _

    sealed trait AdressCheckSMGuardianEvent
    final case class onReportState(useCaseKey: UseCaseKey, payload: java.util.Map[String, AnyRef], replyTo : ActorRef[AdressCheckSM.Response]) extends AdressCheckSMGuardianEvent

    final case class onError(useCaseKey: UseCaseKey, payload:java.util.Map[String, AnyRef], replyTo: ActorRef[AdressCheckSM.Response]) extends AdressCheckSMGuardianEvent
    object onError {
        def apply(useCaseKey: UseCaseKey, payload: java.util.Map[String, AnyRef]) : onError = {
            onError(useCaseKey, payload, null)
        }
    }
    final case class onResult(useCaseKey: UseCaseKey, payload:java.util.Map[String, AnyRef], replyTo: ActorRef[AdressCheckSM.Response]) extends AdressCheckSMGuardianEvent
    object onResult {
        def apply(useCaseKey: UseCaseKey, payload: java.util.Map[String, AnyRef]) : onResult = {
            onResult(useCaseKey, payload, null)
        }
    }
    final case class onRetry(useCaseKey: UseCaseKey, payload:java.util.Map[String, AnyRef], replyTo: ActorRef[AdressCheckSM.Response]) extends AdressCheckSMGuardianEvent
    object onRetry {
        def apply(useCaseKey: UseCaseKey, payload: java.util.Map[String, AnyRef]) : onRetry = {
            onRetry(useCaseKey, payload, null)
        }
    }
    final case class onStartAdressCheckResearch(useCaseKey: UseCaseKey, payload:java.util.Map[String, AnyRef], replyTo: ActorRef[AdressCheckSM.Response]) extends AdressCheckSMGuardianEvent
    object onStartAdressCheckResearch {
        def apply(useCaseKey: UseCaseKey, payload: java.util.Map[String, AnyRef]) : onStartAdressCheckResearch = {
            onStartAdressCheckResearch(useCaseKey, payload, null)
        }
    }

    //Message Adapter
    private final case class WrappedReportStateResponse(reportResponse: Response) extends AdressCheckSMGuardianEvent

    sealed trait ResponseState
    final case class ReportResponseState(state : AdressCheckSM.State) extends ResponseState

    final case class onAddAdressCheckSMReference(listing: Receptionist.Listing) extends AdressCheckSMGuardianEvent
    var listing: Receptionist.Listing = _

    def apply()
            (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[AdressCheckSMGuardianEvent] = {
        apply(
        AdressCheckSMSnapshotAdapter,
        NoOpEventAdapter.instance[PersistEvent, PersistEvent],
        externalAllocationStrategy = false)(actorSystem, sharding)
    }

    def apply(
                shardingMessageExtractor : ShardingMessageExtractor[AdressCheckSMEvent, AdressCheckSMEvent]
            )
            (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[AdressCheckSMGuardianEvent] = {
        apply(
        AdressCheckSMSnapshotAdapter,
        NoOpEventAdapter.instance[PersistEvent, PersistEvent],
        shardingMessageExtractor,
        externalAllocationStrategy = false)(actorSystem, sharding)
    }

    def apply(
           shardingMessageExtractor : ShardingMessageExtractor[AdressCheckSMEvent, AdressCheckSMEvent],
           externalAllocationStrategy: Boolean)
         (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[AdressCheckSMGuardianEvent] = {
        apply(
            AdressCheckSMSnapshotAdapter,
            NoOpEventAdapter.instance[PersistEvent, PersistEvent],
            shardingMessageExtractor,
            externalAllocationStrategy = externalAllocationStrategy
        )(actorSystem, sharding)
    }

    def apply(
                snapshotAdapter: SnapshotAdapter[State])
            (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[AdressCheckSMGuardianEvent] = {
        apply(
                snapshotAdapter,
                NoOpEventAdapter.instance[PersistEvent, PersistEvent],
                externalAllocationStrategy = false
            )(actorSystem, sharding)
    }

    def apply(
                eventAdapter: EventAdapter[PersistEvent, PersistEvent]
            )
            (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[AdressCheckSMGuardianEvent] = {
        apply(
                AdressCheckSMSnapshotAdapter,
                eventAdapter,
                externalAllocationStrategy = false
             )(actorSystem, sharding)
    }

    def apply(
               snapshotAdapter: SnapshotAdapter[State],
               eventAdapter: EventAdapter[PersistEvent, PersistEvent],
               externalAllocationStrategy: Boolean)
             (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[AdressCheckSMGuardianEvent] = {
        apply(
            snapshotAdapter,
            eventAdapter,
            new HashCodeNoEnvelopeMessageExtractor[AdressCheckSMEvent](numberOfShards = actorSystem.settings.config.getInt("pekko.fsm.numberOfShards")) {
                override def entityId(message: AdressCheckSMEvent): String = message.useCaseKey.getKey
            },
            externalAllocationStrategy = false
        )(actorSystem, sharding)
    }

    def apply(
                snapshotAdapter: SnapshotAdapter[State],
                eventAdapter: EventAdapter[PersistEvent, PersistEvent],
                shardingMessageExtractor : ShardingMessageExtractor[AdressCheckSMEvent, AdressCheckSMEvent],
                externalAllocationStrategy: Boolean)
            (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[AdressCheckSMGuardianEvent] = {
        _snapshotAdapter = snapshotAdapter
        _eventAdapter = eventAdapter
        _externalShardAllocationStrategy = new ExternalShardAllocationStrategy(actorSystem, adresschecksmTypeKey.name)
        _defaultMessageExtractor = shardingMessageExtractor
        Behaviors
        .setup[AdressCheckSMGuardianEvent] {
            context =>
                val baseActorResponseWrapper : ActorRef[Response] =
                  context.messageAdapter(response => WrappedReportStateResponse(response))

                val listingAdapter: ActorRef[Receptionist.Listing] = context.messageAdapter {
                    listing => onAddAdressCheckSMReference(listing)
                }
                context.system.receptionist ! Receptionist.Subscribe(adresschecksmKey, listingAdapter)

            Behaviors
                .receivePartial[AdressCheckSMGuardianEvent] {
                    case (ctx, onReportState(useCaseKey, payload, replyTo)) =>
                        ctx.log.debug("We are processing onReportState(payload, replyto): {}, {}", payload.toString, replyTo.toString)
                        val adresschecksmOption: Option[ActorRef[AdressCheckSMEvent]] = findActor(useCaseKey)
                        if(adresschecksmOption.isDefined) {
                            adresschecksmOption.get ! AdressCheckSM.onReport(() => useCaseKey.getKey, replyTo)
                        } else {
                            ctx.log.warn("A adresschecksm actor should exist for key: {}", useCaseKey.getKey)
                        }
                        Behaviors.same
                    case (ctx, WrappedReportStateResponse(response)) =>
                        ctx.log.debug("We are processing WrappedReportStateResponse(response: {}", response.toString)
                        Behaviors.same

                    case (ctx, onRetry(useCaseKey, payload, replyTo))  =>
                        ctx.log.debug("We are processing onRetry(payload): {}", payload.toString)
                        getActor(useCaseKey, externalAllocationStrategy) ! AdressCheckSM.onRetry(useCaseKey, payload, replyTo)
                        Behaviors.same
                    case (ctx, onStartAdressCheckResearch(useCaseKey, payload, replyTo))  =>
                        ctx.log.debug("We are processing onStartAdressCheckResearch(payload): {}", payload.toString)
                        getActor(useCaseKey, externalAllocationStrategy) ! AdressCheckSM.onStartAdressCheckResearch(useCaseKey, payload, replyTo)
                        Behaviors.same
                    case (ctx, onError(useCaseKey, payload, replyTo))  =>
                        ctx.log.debug("We are processing onError(payload): {}", payload.toString)
                        getActor(useCaseKey, externalAllocationStrategy) ! AdressCheckSM.onError(useCaseKey, payload, replyTo)
                        Behaviors.same
                    case (ctx, onResult(useCaseKey, payload, replyTo))  =>
                        ctx.log.debug("We are processing onResult(payload): {}", payload.toString)
                        getActor(useCaseKey, externalAllocationStrategy) ! AdressCheckSM.onResult(useCaseKey, payload, replyTo)
                        Behaviors.same

                    case (ctx, onAddAdressCheckSMReference(listings)) =>
                        listing = listings
                        Behaviors.same
                }
        }
    }.narrow

    private def getActor(useCaseKey: UseCaseKey, externalAllocationStrategy: Boolean)
                        (implicit sharding: ClusterSharding, actorSystem: ActorSystem[NotUsed]): ActorRef[AdressCheckSMEvent] = {
        val adresschecksmOption: Option[ActorRef[AdressCheckSMEvent]] = findActor(useCaseKey)
        if (adresschecksmOption.isDefined) {
            adresschecksmOption.get
        } else {
            shardInit(externalAllocationStrategy)
        }
    }

    def shardInit()(implicit sharding: ClusterSharding, actorSystem: ActorSystem[NotUsed]): ActorRef[AdressCheckSMEvent] = {
        shardInit(false)
    }

    def shardInit(externalAllocationStrategy: Boolean)(implicit sharding: ClusterSharding, actorSystem: ActorSystem[NotUsed]): ActorRef[AdressCheckSMEvent] = {
        val entity: Entity[AdressCheckSMEvent, AdressCheckSMEvent] = createEntity()
        if(externalAllocationStrategy) {
            entity.withAllocationStrategy(_externalShardAllocationStrategy)
        }
        sharding.init(entity)
    }

    private def createEntity(): Entity[AdressCheckSMEvent, AdressCheckSMEvent] = {
        Entity(adresschecksmTypeKey) (createBehavior =
            entityContext =>
                Behaviors.supervise(
                    Behaviors.setup[AdressCheckSMEvent] {
                        ctx => {
                            ctx.system.receptionist ! Receptionist.Register(AdressCheckSMGuardian.adresschecksmKey, ctx.self)
                            ctx.system.receptionist ! Receptionist.Register(
                                ServiceKey[AdressCheckSMEvent](
                                    "adresschecksmService_" + entityContext.entityId
                                ),
                                ctx.self
                            )

                            val adresschecksm: AdressCheckSM = new AdressCheckSM(
                                ctx,
                                entityContext.entityId
                            )

                            EventSourcedBehavior.withEnforcedReplies[AdressCheckSMEvent, PersistEvent, State](
                                persistenceId = PersistenceId.ofUniqueId("adresschecksm" + entityContext.entityId),
                                emptyState = 
                                INITIAL
                                (new java.util.HashMap[java.lang.String, AnyRef]),
                                commandHandler = (state, cmd) => adresschecksm.commandHandler(ctx, cmd, state),
                                eventHandler = (state, event) => adresschecksm.eventHandler(ctx, state, event)
                            )
                            .snapshotWhen {
                                case(state, event, sequenceNumber) => false
                            }
                            .snapshotAdapter(_snapshotAdapter)
                            .eventAdapter(_eventAdapter)
                            .withRecovery(Recovery.withSnapshotSelectionCriteria(SnapshotSelectionCriteria.latest))
                            .withRetention(RetentionCriteria.snapshotEvery(numberOfEvents = 100, keepNSnapshots = 2))
                            .withTagger(event => Set("adresschecksm-" + Math.abs(entityContext.entityId.hashCode)%ctx.system.settings.config.getInt("pekko.fsm.numberOfShards")))
                        }
                    }
                ).onFailure(
                    SupervisorStrategy.restartWithBackoff(minBackoff = 5.seconds, maxBackoff = 1.minute, randomFactor = 0.2)
                )
        ).withMessageExtractor(_defaultMessageExtractor)
    }

    private def findActor(useCaseKey: UseCaseKey): Option[ActorRef[AdressCheckSMEvent]] = {
        val listingOption = Option(listing)
        if (listingOption.isDefined)
            listingOption.get.serviceInstances(adresschecksmKey).foreach(adresschecksm =>
                if (adresschecksm.path.name == useCaseKey.getKey) {
                    return Option(adresschecksm)
                })
                Option.empty
    }
}

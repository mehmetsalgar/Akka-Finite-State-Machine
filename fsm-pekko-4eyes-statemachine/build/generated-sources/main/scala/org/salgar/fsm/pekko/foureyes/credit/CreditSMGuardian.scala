package org.salgar.fsm.pekko.foureyes.credit

import org.salgar.fsm.pekko.foureyes.credit.CreditSM._
import org.salgar.fsm.pekko.foureyes.credit.config.CreditSMServiceLocator
import org.salgar.pekko.fsm.base.eventadapter.NoOpEventAdapter

object CreditSMGuardian {
    private val creditsmKey = ServiceKey[CreditSMEvent]("creditsmService")
    private val creditsmTypeKey = EntityTypeKey[CreditSMEvent]("creditsm")
    private var _snapshotAdapter: SnapshotAdapter[State] = _
    private var _eventAdapter: EventAdapter[PersistEvent, PersistEvent] = _
    private var _defaultMessageExtractor: ShardingMessageExtractor[CreditSMEvent, CreditSMEvent] = _
    private var _externalShardAllocationStrategy:  ExternalShardAllocationStrategy = _


    sealed trait CreditSMGuardianEvent
    final case class onReportState(payload: java.util.Map[String, AnyRef], replyTo : ActorRef[CreditSM.Response]) extends CreditSMGuardianEvent
    final case class onAcceptableScore(payload:java.util.Map[String, AnyRef], replyTo: ActorRef[CreditSM.Response]) extends CreditSMGuardianEvent
    object onAcceptableScore {
        def apply(payload: java.util.Map[String, AnyRef]) : onAcceptableScore = {
            onAcceptableScore(payload, null)
        }
    }
    final case class onAccepted(payload:java.util.Map[String, AnyRef], replyTo: ActorRef[CreditSM.Response]) extends CreditSMGuardianEvent
    object onAccepted {
        def apply(payload: java.util.Map[String, AnyRef]) : onAccepted = {
            onAccepted(payload, null)
        }
    }
    final case class onCustomerUpdated(payload:java.util.Map[String, AnyRef], replyTo: ActorRef[CreditSM.Response]) extends CreditSMGuardianEvent
    object onCustomerUpdated {
        def apply(payload: java.util.Map[String, AnyRef]) : onCustomerUpdated = {
            onCustomerUpdated(payload, null)
        }
    }
    final case class onRejected(payload:java.util.Map[String, AnyRef], replyTo: ActorRef[CreditSM.Response]) extends CreditSMGuardianEvent
    object onRejected {
        def apply(payload: java.util.Map[String, AnyRef]) : onRejected = {
            onRejected(payload, null)
        }
    }
    final case class onRelationshipManagerApproved(payload:java.util.Map[String, AnyRef], replyTo: ActorRef[CreditSM.Response]) extends CreditSMGuardianEvent
    object onRelationshipManagerApproved {
        def apply(payload: java.util.Map[String, AnyRef]) : onRelationshipManagerApproved = {
            onRelationshipManagerApproved(payload, null)
        }
    }
    final case class onResultReceived(payload:java.util.Map[String, AnyRef], replyTo: ActorRef[CreditSM.Response]) extends CreditSMGuardianEvent
    object onResultReceived {
        def apply(payload: java.util.Map[String, AnyRef]) : onResultReceived = {
            onResultReceived(payload, null)
        }
    }
    final case class onSalesManagerApproved(payload:java.util.Map[String, AnyRef], replyTo: ActorRef[CreditSM.Response]) extends CreditSMGuardianEvent
    object onSalesManagerApproved {
        def apply(payload: java.util.Map[String, AnyRef]) : onSalesManagerApproved = {
            onSalesManagerApproved(payload, null)
        }
    }
    final case class onSubmit(payload:java.util.Map[String, AnyRef], replyTo: ActorRef[CreditSM.Response]) extends CreditSMGuardianEvent
    object onSubmit {
        def apply(payload: java.util.Map[String, AnyRef]) : onSubmit = {
            onSubmit(payload, null)
        }
    }
    final case class onAddCreditSMReference(listing: Receptionist.Listing) extends CreditSMGuardianEvent

    sealed trait CreditSMGuardianResponse

    //MessageAdapter
    private final case class WrappedReportStateResponse(reportResponse:  Response) extends CreditSMGuardianEvent

    var listing: Receptionist.Listing = _

    def apply() (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[CreditSMGuardianEvent] =
        apply(
            CreditSMSnapshotAdapter,
            NoOpEventAdapter.instance[PersistEvent, PersistEvent],
            externalAllocationStrategy = false
        )(actorSystem, sharding)

    def apply(
               shardingMessageExtractor : ShardingMessageExtractor[CreditSMEvent, CreditSMEvent]
             )
             (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[CreditSMGuardianEvent] =
        apply(
            CreditSMSnapshotAdapter,
            NoOpEventAdapter.instance[PersistEvent, PersistEvent],
            shardingMessageExtractor,
            externalAllocationStrategy = false
        )(actorSystem, sharding)

    def apply(
               shardingMessageExtractor : ShardingMessageExtractor[CreditSMEvent, CreditSMEvent],
               externalAllocationStrategy: Boolean)
             (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[CreditSMGuardianEvent] =
        apply(
            CreditSMSnapshotAdapter,
            NoOpEventAdapter.instance[PersistEvent, PersistEvent],
            shardingMessageExtractor,
            externalAllocationStrategy = externalAllocationStrategy
        )(actorSystem, sharding)

    def apply(snapshotAdapter: SnapshotAdapter[State])
             (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[CreditSMGuardianEvent] =
        apply(
            snapshotAdapter,
            NoOpEventAdapter.instance[PersistEvent, PersistEvent],
            externalAllocationStrategy = false
        )(actorSystem, sharding)

    def apply(eventAdapter: EventAdapter[PersistEvent, PersistEvent])
             (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[CreditSMGuardianEvent] =
        apply(
            CreditSMSnapshotAdapter,
            eventAdapter,
            externalAllocationStrategy = false
        )(actorSystem, sharding)

    def apply(
               snapshotAdapter: SnapshotAdapter[State],
               eventAdapter: EventAdapter[PersistEvent, PersistEvent],
               shardingMessageExtractor : ShardingMessageExtractor[CreditSMEvent, CreditSMEvent])
             (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[CreditSMGuardianEvent] =
        apply(
            snapshotAdapter,
            eventAdapter,
            shardingMessageExtractor,
            externalAllocationStrategy = false
        )(actorSystem, sharding)

    def apply(
               snapshotAdapter: SnapshotAdapter[State],
               eventAdapter: EventAdapter[PersistEvent, PersistEvent])
             (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[CreditSMGuardianEvent] = {
        apply(
            snapshotAdapter,
            eventAdapter,
            new HashCodeNoEnvelopeMessageExtractor[CreditSMEvent](numberOfShards = actorSystem.settings.config.getInt("pekko.fsm.numberOfShards")) {
                                         override def entityId(message: CreditSMEvent): String = message.useCaseKey.getKey
                                     },
            externalAllocationStrategy = false
        )(actorSystem, sharding)
    }

    def apply(
               snapshotAdapter: SnapshotAdapter[State],
               eventAdapter: EventAdapter[PersistEvent, PersistEvent],
               externalAllocationStrategy: Boolean)
             (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[CreditSMGuardianEvent] = {
        apply(
            snapshotAdapter,
            eventAdapter,
            new HashCodeNoEnvelopeMessageExtractor[CreditSMEvent](numberOfShards = actorSystem.settings.config.getInt("pekko.fsm.numberOfShards")) {
                                         override def entityId(message: CreditSMEvent): String = message.useCaseKey.getKey
                                     },
            externalAllocationStrategy = externalAllocationStrategy
        )(actorSystem, sharding)
    }

    def apply(
               snapshotAdapter: SnapshotAdapter[State],
               eventAdapter: EventAdapter[PersistEvent, PersistEvent],
               shardingMessageExtractor : ShardingMessageExtractor[CreditSMEvent, CreditSMEvent],
               externalAllocationStrategy: Boolean
             )(implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): Behavior[CreditSMGuardianEvent] = {
        _snapshotAdapter = snapshotAdapter
        _eventAdapter = eventAdapter
        _externalShardAllocationStrategy = new ExternalShardAllocationStrategy(actorSystem, creditsmTypeKey.name)
        _defaultMessageExtractor = shardingMessageExtractor
        Behaviors
            .setup[CreditSMGuardianEvent] {
                context =>

            val responseWrapper : ActorRef[Response] =
                context.messageAdapter(response => WrappedReportStateResponse(response))

            val listingAdapter: ActorRef[Receptionist.Listing] = context.messageAdapter {
                listing => onAddCreditSMReference(listing)
            }
            context.system.receptionist ! Receptionist.Subscribe(creditsmKey, listingAdapter)

            Behaviors
                .receivePartial[CreditSMGuardianEvent] {
                    case (ctx, onReportState(payload, replyTo)) =>
                        ctx.log.debug("We are processing onReportState(payload, replyto): {}, {}", payload.toString, replyTo.toString)
                        val useCaseKey = CreditSMServiceLocator.getInstance.useCaseKeyStrategy.getKey(payload)
                        getActor(payload, externalAllocationStrategy) ! CreditSM.onReport(() => useCaseKey, replyTo)
                        Behaviors.same
                    case (ctx, WrappedReportStateResponse(response)) =>
                        ctx.log.debug("We are processing WrappedReportStateResponse(response: {}", response.toString)
                        Behaviors.same
                    case (ctx, onAcceptableScore(payload, replyTo)) =>
                        ctx.log.debug("We are processing onAcceptableScore(payload): {}", payload.toString)
                        val useCaseKey = CreditSMServiceLocator.getInstance.useCaseKeyStrategy.getKey(payload)
                        getActor(payload, externalAllocationStrategy) ! CreditSM.onAcceptableScore(() => useCaseKey, payload, replyTo)
                        Behaviors.same
                    case (ctx, onAccepted(payload, replyTo)) =>
                        ctx.log.debug("We are processing onAccepted(payload): {}", payload.toString)
                        val useCaseKey = CreditSMServiceLocator.getInstance.useCaseKeyStrategy.getKey(payload)
                        getActor(payload, externalAllocationStrategy) ! CreditSM.onAccepted(() => useCaseKey, payload, replyTo)
                        Behaviors.same
                    case (ctx, onCustomerUpdated(payload, replyTo)) =>
                        ctx.log.debug("We are processing onCustomerUpdated(payload): {}", payload.toString)
                        val useCaseKey = CreditSMServiceLocator.getInstance.useCaseKeyStrategy.getKey(payload)
                        getActor(payload, externalAllocationStrategy) ! CreditSM.onCustomerUpdated(() => useCaseKey, payload, replyTo)
                        Behaviors.same
                    case (ctx, onRejected(payload, replyTo)) =>
                        ctx.log.debug("We are processing onRejected(payload): {}", payload.toString)
                        val useCaseKey = CreditSMServiceLocator.getInstance.useCaseKeyStrategy.getKey(payload)
                        getActor(payload, externalAllocationStrategy) ! CreditSM.onRejected(() => useCaseKey, payload, replyTo)
                        Behaviors.same
                    case (ctx, onRelationshipManagerApproved(payload, replyTo)) =>
                        ctx.log.debug("We are processing onRelationshipManagerApproved(payload): {}", payload.toString)
                        val useCaseKey = CreditSMServiceLocator.getInstance.useCaseKeyStrategy.getKey(payload)
                        getActor(payload, externalAllocationStrategy) ! CreditSM.onRelationshipManagerApproved(() => useCaseKey, payload, replyTo)
                        Behaviors.same
                    case (ctx, onResultReceived(payload, replyTo)) =>
                        ctx.log.debug("We are processing onResultReceived(payload): {}", payload.toString)
                        val useCaseKey = CreditSMServiceLocator.getInstance.useCaseKeyStrategy.getKey(payload)
                        getActor(payload, externalAllocationStrategy) ! CreditSM.onResultReceived(() => useCaseKey, payload, replyTo)
                        Behaviors.same
                    case (ctx, onSalesManagerApproved(payload, replyTo)) =>
                        ctx.log.debug("We are processing onSalesManagerApproved(payload): {}", payload.toString)
                        val useCaseKey = CreditSMServiceLocator.getInstance.useCaseKeyStrategy.getKey(payload)
                        getActor(payload, externalAllocationStrategy) ! CreditSM.onSalesManagerApproved(() => useCaseKey, payload, replyTo)
                        Behaviors.same
                    case (ctx, onSubmit(payload, replyTo)) =>
                        ctx.log.debug("We are processing onSubmit(payload): {}", payload.toString)
                        val useCaseKey = CreditSMServiceLocator.getInstance.useCaseKeyStrategy.getKey(payload)
                        getActor(payload, externalAllocationStrategy) ! CreditSM.onSubmit(() => useCaseKey, payload, replyTo)
                        Behaviors.same

                    case (ctx, onAddCreditSMReference(listings)) =>
                        listing = listings
                        Behaviors.same
                }
            }
 }.narrow

 private def getActor(
                       payload: java.util.Map[String, AnyRef],
                       externalAllocationStrategy: Boolean
                     )
                     (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): ActorRef[CreditSMEvent] = {
    val creditsmRefOption: Option[ActorRef[CreditSMEvent]] = findActor(payload)
    if (creditsmRefOption.isDefined) {
        creditsmRefOption.get
    } else {
        val shardRegion = prepare(externalAllocationStrategy)
        shardRegion
    }
}

private def prepare(externalAllocationStrategy: Boolean)
            (implicit actorSystem: ActorSystem[NotUsed], sharding: ClusterSharding): ActorRef[CreditSMEvent] = {
    val entity: Entity[CreditSMEvent, CreditSMEvent] = createEntity()
    if(externalAllocationStrategy) {
        entity.withAllocationStrategy(_externalShardAllocationStrategy)
    }
    val actorRef: ActorRef[CreditSMEvent] = sharding
        .init(entity)
    actorRef
}

private def createEntity(): Entity[CreditSMEvent, CreditSMEvent] = {
    Entity(creditsmTypeKey)(createBehavior = entityContext =>
        Behaviors
            .supervise(
                Behaviors.setup[CreditSMEvent] {
                    context =>
                        context.system.receptionist ! Receptionist.Register(creditsmKey, context.self)
                        context.system.receptionist ! Receptionist.Register(
                        ServiceKey[CreditSMEvent](
                            "creditsmService_" + entityContext.entityId
                        ),
                        context.self
                )

                val creditsm = new CreditSM(
                    context,
                    entityContext.entityId
                )
                EventSourcedBehavior.withEnforcedReplies[CreditSMEvent, PersistEvent, State](
                    persistenceId = PersistenceId
                        .ofUniqueId("creditsm" + entityContext.entityId),
                    emptyState = 
                    INITIAL
                    (new java.util.HashMap[java.lang.String, AnyRef]),
                    commandHandler = (state, cmd) => creditsm.commandHandler(context, cmd, state),
                    eventHandler = (state, event) => creditsm.eventHandler(context, state, event)
                )
                .snapshotWhen {
                    case(state, org.salgar.fsm.pekko.foureyes.credit.CreditSM.RelationshipManagerApprovedPersistEvent(_), sequenceNumber) => true
                    case(state, event, sequenceNumber) => false
                }
                .snapshotAdapter(_snapshotAdapter)
                .eventAdapter(_eventAdapter)
                .withRecovery(Recovery.withSnapshotSelectionCriteria(SnapshotSelectionCriteria.latest))
                .withRetention(RetentionCriteria.snapshotEvery(numberOfEvents = 100, keepNSnapshots = 2))
                .withTagger(event => Set("creditsm-" + Math.abs(entityContext.entityId.hashCode)%context.system.settings.config.getInt("pekko.fsm.numberOfShards")))
            }
         )
        .onFailure[Exception](
            SupervisorStrategy.restartWithBackoff(minBackoff = 5.seconds, maxBackoff = 1.minute, randomFactor = 0.2)
        )).withMessageExtractor(_defaultMessageExtractor)
}

private def findActor(payload: java.util.Map[String, AnyRef]): Option[ActorRef[CreditSMEvent]] = {
    val actorName: String = CreditSMServiceLocator.getInstance.useCaseKeyStrategy
        .getKey(payload)
    val listingOption = Option(listing)
    if(listingOption.isDefined)
        listingOption.get.serviceInstances(creditsmKey).foreach(creditsm =>
        if (creditsm.path.name == actorName) {
            return Option(creditsm)
        }
    )
    Option.empty
 }
}

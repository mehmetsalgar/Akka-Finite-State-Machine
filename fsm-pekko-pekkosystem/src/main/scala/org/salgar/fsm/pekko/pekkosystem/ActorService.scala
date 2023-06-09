package org.salgar.fsm.pekko.pekkosystem

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import org.apache.pekko.NotUsed
import org.apache.pekko.actor.BootstrapSetup
import org.apache.pekko.actor.setup.ActorSystemSetup
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.apache.pekko.actor.typed.{ActorSystem, Behavior, Scheduler}
import org.apache.pekko.cluster.sharding.typed.scaladsl.ClusterSharding
import org.apache.pekko.serialization.jackson.{JacksonObjectMapperFactory, JacksonObjectMapperProviderSetup}
import org.apache.pekko.util.Timeout
import org.salgar.fsm.pekko.pekkosystem.ActorService.{customJacksonObjectMapperFactory, log, mainBehavior}
import org.salgar.fsm.pekko.pekkosystem.config.PekkoApplicationProperty
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.{PostConstruct, PreDestroy}
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

object ActorService {
  private val log : Logger = LoggerFactory.getLogger(ActorService.getClass)

  val customJacksonObjectMapperFactory = new JacksonObjectMapperFactory {
    override def newObjectMapper(bindingName: String, jsonFactory: JsonFactory): ObjectMapper =
    {
      if(bindingName == "jackson-json" || bindingName == "jackson-cbor") {
        val mapper = new ObjectMapper(jsonFactory)
        val pvt =  BasicPolymorphicTypeValidator
          .builder()
          .allowIfBaseType(classOf[Object])
          .build()
        mapper.activateDefaultTyping(pvt)
      } else {
        super.newObjectMapper(bindingName, jsonFactory)
      }
    }
  }

  def mainBehavior: Behavior[NotUsed] =
    Behaviors.setup {
      context =>
        Behaviors.empty
    }

  implicit var timeout: Timeout = 30.seconds
}

@Component
class ActorService (
                     @Autowired pekkoApplicationProperty: PekkoApplicationProperty,
                     @Autowired additionalService: AdditionalService){
  private var _sharding: ClusterSharding = null
  private var _ec: ExecutionContext = null
  private var _scheduler : Scheduler = null
  private var _actorSystem: ActorSystem[NotUsed] = null

  def sharding() = _sharding

  def ec() = _ec

  def scheduler() = _scheduler

  def actorSystem() = _actorSystem

  @PostConstruct
  def init(): Unit = {
    log.info("We are initializing the Actor System!")

    val actorSystemSetup = ActorSystemSetup()
      .withSetup(JacksonObjectMapperProviderSetup(customJacksonObjectMapperFactory))
      .withSetup(BootstrapSetup(additionalService.configureFromFiles()))
    _actorSystem = ActorSystem(mainBehavior, pekkoApplicationProperty.getApplicationName(), actorSystemSetup)

    additionalService.config(actorSystem)

    _sharding = ClusterSharding(actorSystem)
    _ec = actorSystem.executionContext
    _scheduler = actorSystem.scheduler

    log.info("Initialization of the Actor System complete!")
  }

  @PreDestroy
  def destroy() {
    _actorSystem.terminate()
  }
}
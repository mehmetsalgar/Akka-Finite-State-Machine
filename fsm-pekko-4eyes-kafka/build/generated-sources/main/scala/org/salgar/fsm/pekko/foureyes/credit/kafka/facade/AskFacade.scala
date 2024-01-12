package org.salgar.fsm.pekko.foureyes.credit.kafka.facade

import org.salgar.fsm.pekko.command.CommandHandler
import org.salgar.fsm.pekko.foureyes.addresscheck.AdressCheckSM
import org.salgar.fsm.pekko.foureyes.addresscheck.protobuf.AdressCheckSMCommand
import org.salgar.fsm.pekko.foureyes.credit.CreditSM
import org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditSMCommand
import org.salgar.fsm.pekko.foureyes.creditscore.{CreditScoreSM, MultiTenantCreditScoreSM}
import org.salgar.fsm.pekko.foureyes.creditscore.protobuf.{CreditScoreSMCommand, MultiTenantCreditScoreSMCommand}
import org.salgar.fsm.pekko.foureyes.fraudprevention.FraudPreventionSM
import org.salgar.fsm.pekko.foureyes.fraudprevention.protobuf.FraudPreventionSMCommand
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

@Component
class AskFacade(
    creditSMCommandHandlers: java.util.List[CommandHandler[CreditSMCommand, CreditSM.Response]],
    creditScoreSMCommandHandlers: java.util.List[CommandHandler[CreditScoreSMCommand, CreditScoreSM.Response]],
    multiTenantCreditScoreSMCommandHandlers: java.util.List[
      CommandHandler[MultiTenantCreditScoreSMCommand, MultiTenantCreditScoreSM.Response]
    ],
    fraudPreventionSMCommandHandlers: java.util.List[
      CommandHandler[FraudPreventionSMCommand, FraudPreventionSM.Response]
    ],
    adressCheckSMCommandHandlers: java.util.List[CommandHandler[AdressCheckSMCommand, AdressCheckSM.Response]]
) {
  private var _creditSMCommandHandlers: Map[String, CommandHandler[CreditSMCommand, CreditSM.Response]] = Map()
  private var _creditScoreSMCommandHandlers: Map[String, CommandHandler[CreditScoreSMCommand, CreditScoreSM.Response]] =
    Map()
  private var _multiTenantCreditScoreSMCommandHandlers
      : Map[String, CommandHandler[MultiTenantCreditScoreSMCommand, MultiTenantCreditScoreSM.Response]] = Map()
  private var _fraudPreventionSMCommandHandlers
      : Map[String, CommandHandler[FraudPreventionSMCommand, FraudPreventionSM.Response]] = Map()
  private var _adressCheckSMCommandHandlers: Map[String, CommandHandler[AdressCheckSMCommand, AdressCheckSM.Response]] =
    Map()

  def askCreditSMCommand(creditSMCommand: CreditSMCommand): Future[CreditSM.Response] = {
    val commandHandler: CommandHandler[CreditSMCommand, CreditSM.Response] = _creditSMCommandHandlers(
      creditSMCommand.getCommand
    )
    commandHandler.handleCommand(creditSMCommand)
  }
  def askCreditScoreSMCommand(creditScoreSMCommand: CreditScoreSMCommand): Future[CreditScoreSM.Response] = {
    val commandHandler: CommandHandler[CreditScoreSMCommand, CreditScoreSM.Response] = _creditScoreSMCommandHandlers(
      creditScoreSMCommand.getCommand
    )
    commandHandler.handleCommand(creditScoreSMCommand)
  }
  def askMultiTenantCreditScoreSMCommand(
      multiTenantCreditScoreSMCommand: MultiTenantCreditScoreSMCommand
  ): Future[MultiTenantCreditScoreSM.Response] = {
    val commandHandler: CommandHandler[MultiTenantCreditScoreSMCommand, MultiTenantCreditScoreSM.Response] =
      _multiTenantCreditScoreSMCommandHandlers(
        multiTenantCreditScoreSMCommand.getCommand
      )
    commandHandler.handleCommand(multiTenantCreditScoreSMCommand)
  }
  def askFraudPreventionSMCommand(
      fraudPreventionSMCommand: FraudPreventionSMCommand
  ): Future[FraudPreventionSM.Response] = {
    val commandHandler: CommandHandler[FraudPreventionSMCommand, FraudPreventionSM.Response] =
      _fraudPreventionSMCommandHandlers(
        fraudPreventionSMCommand.getCommand
      )
    commandHandler.handleCommand(fraudPreventionSMCommand)
  }
  def askAdressCheckSMCommand(adressCheckSMCommand: AdressCheckSMCommand): Future[AdressCheckSM.Response] = {
    val commandHandler: CommandHandler[AdressCheckSMCommand, AdressCheckSM.Response] = _adressCheckSMCommandHandlers(
      adressCheckSMCommand.getCommand
    )
    commandHandler.handleCommand(adressCheckSMCommand)
  }

  @PostConstruct
  private def init() = {
    creditSMCommandHandlers
      .stream()
      .forEach(commandHandler => {
        _creditSMCommandHandlers += (commandHandler.`type`() -> commandHandler)
      })
    creditScoreSMCommandHandlers
      .stream()
      .forEach(commandHandler => {
        _creditScoreSMCommandHandlers += (commandHandler.`type`() -> commandHandler)
      })
    multiTenantCreditScoreSMCommandHandlers
      .stream()
      .forEach(commandHandler => {
        _multiTenantCreditScoreSMCommandHandlers += (commandHandler.`type`() -> commandHandler)
      })
    fraudPreventionSMCommandHandlers
      .stream()
      .forEach(commandHandler => {
        _fraudPreventionSMCommandHandlers += (commandHandler.`type`() -> commandHandler)
      })
    adressCheckSMCommandHandlers
      .stream()
      .forEach(commandHandler => {
        _adressCheckSMCommandHandlers += (commandHandler.`type`() -> commandHandler)
      })
  }
}

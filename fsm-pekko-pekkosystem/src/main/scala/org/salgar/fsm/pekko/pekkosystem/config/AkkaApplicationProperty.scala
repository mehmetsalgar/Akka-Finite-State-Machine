package org.salgar.fsm.pekko.pekkosystem.config

class AkkaApplicationProperty {
  private var _applicationName: String = null

  def setApplicationName(applicationName: String) = {
    _applicationName = applicationName
  }

  def getApplicationName() = {
    _applicationName
  }
}
package org.salgar.fsm.akka.kafka.config

import akka.kafka.ConsumerSettings

trait ConsumerConfig[KEY, VALUE] {
  def consumerSettings(): ConsumerSettings[KEY, VALUE]
}

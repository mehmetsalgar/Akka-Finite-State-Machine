package org.salgar.fsm.pekko.kafka.config

import org.apache.pekko.kafka.ConsumerSettings

trait ConsumerConfig[KEY, VALUE] {
  def consumerSettings(): ConsumerSettings[KEY, VALUE]
}

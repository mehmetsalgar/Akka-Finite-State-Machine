package org.salgar.akka.fsm.base.eventadapter

import akka.persistence.typed.{EventAdapter, EventSeq}

object NoOpEventAdapter {
  private val i = new NoOpEventAdapter[Nothing, Nothing]
  def instance[E, P]: NoOpEventAdapter[E, P] = i.asInstanceOf[NoOpEventAdapter[E, P]]
}

/**
 * INTERNAL API
 */
class NoOpEventAdapter[E, P] extends EventAdapter[E, P] {
  override def toJournal(e: E): P = e.asInstanceOf[P]
  override def fromJournal(p: P, manifest: String): EventSeq[E] = EventSeq.single(p.asInstanceOf[E])
  override def manifest(event: E): String = ""
}
package com.barclays.taxis

import akka.actor.{ActorContext, ActorRef, ActorSystem, Scheduler}
import akka.event.LoggingAdapter
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar

/**
 * Mocks the actor system. This can be used in unit tests requiring a mock actor system and actor context
 *
 * @author	kostas.kougios
 *            Date: 08/12/14
 */
trait ActorMocks extends MockitoSugar
{
	val self = mock[ActorRef]
	val context = mock[ActorContext]
	when(context.self).thenReturn(self)

	val actorSystem = mock[ActorSystem]
	when(context.system).thenReturn(actorSystem)
	val scheduler = mock[Scheduler]
	when(actorSystem.scheduler).thenReturn(scheduler)

	val log = mock[LoggingAdapter]
}

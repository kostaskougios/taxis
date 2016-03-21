package com.barclays.taxis.actors

import akka.actor.ActorSystem
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike, Matchers}

/**
 * A base test class to be used when testing actors, both in unit and integration tests.
 *
 * @author	kostas.kougios
 *            Date: 08/12/14
 */
class BaseSuite extends TestKit(ActorSystem("testSystem")) with DefaultTimeout with ImplicitSender with FunSuiteLike with Matchers with BeforeAndAfterAll
{
	override def afterAll {
		TestKit.shutdownActorSystem(system)
	}
}

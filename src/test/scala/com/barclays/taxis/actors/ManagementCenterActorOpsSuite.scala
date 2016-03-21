package com.barclays.taxis.actors

import com.barclays.taxis.ActorMocks
import com.barclays.taxis.TestData._
import com.barclays.taxis.service.GPSService
import org.mockito.Matchers.{eq => equ, _}
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{FunSuite, Matchers}

import scala.concurrent.duration._
import scala.language.postfixOps

/**
 * This unit tests the Management Center Operations class which helps split the functionality
 * to more manageable and testable units.
 *
 * @author	kostas.kougios
 *            Date: 08/12/14
 */
class ManagementCenterActorOpsSuite extends FunSuite with Matchers with MockitoSugar
{
	test("creates the actor") {
		new ActorMocks
		{
			val reg = new ManagementCenterActorOpsImpl(mock[GPSService])
			reg.register(Taxi1, context)
			verify(context).actorOf(anyObject())
		}
	}

	ignore("schedules the taxi to report once per second") {
		new ActorMocks
		{
			val reg = new ManagementCenterActorOpsImpl(mock[GPSService])
			reg.register(Taxi1, context)

			// TODO: this fails as the 3rd param is a f: =>Unit and currently can't find a way to mock it (scala passes the anyObject() by name!)
			verify(scheduler).schedule(equ(1 second), equ(1 second))(anyObject())(equ(scala.concurrent.ExecutionContext.Implicits.global))
		}
	}

	test("logs taxi reports") {
		new ActorMocks
		{
			val reg = new ManagementCenterActorOpsImpl(mock[GPSService])
			reg.report(log, Taxi1)
			verify(log).info("taxi {} reports location {}", Taxi1.name, Taxi1.location)
		}
	}
}

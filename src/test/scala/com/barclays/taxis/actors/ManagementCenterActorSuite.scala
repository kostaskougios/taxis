package com.barclays.taxis.actors

import akka.actor.Props
import akka.testkit.TestActorRef
import com.barclays.taxis.TestData._
import com.barclays.taxis.messages.{NewTaxi, TaxiReport}
import com.barclays.taxis.service.GPSService
import org.mockito.Matchers.{eq => equ, _}
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar

/**
 * unit testing the actor
 *
 * @author	kostas.kougios
 *            Date: 08/12/14
 */
class ManagementCenterActorSuite extends BaseSuite with MockitoSugar
{

	trait TestSetup
	{
		// since all the work is done via side effects in ManagementCenterActorOps, we
		// just need to mock ManagementCenterActorOps and see if the actor
		// is correctly invoking it.
		val ops = mock[ManagementCenterActorOps]
		val gpsService = mock[GPSService]
		val managementCenterActor: TestActorRef[ManagementCenterActor] = TestActorRef(Props(ManagementCenterActor(gpsService, ops)))
	}

	test("registers new taxi") {
		new TestSetup
		{
			managementCenterActor ! NewTaxi(Taxi1)
			verify(ops).register(equ(Taxi1), anyObject())
		}
	}

	test("taxi reports location") {
		new TestSetup
		{
			managementCenterActor ! TaxiReport(Taxi1)
			verify(ops).report(anyObject(), equ(Taxi1))
		}
	}
}

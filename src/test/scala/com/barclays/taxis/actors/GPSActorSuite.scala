package com.barclays.taxis.actors

import akka.actor.Props
import akka.testkit.TestActorRef
import com.barclays.taxis.TestData._
import com.barclays.taxis.messages.{GPSOf, TaxiLocation}
import com.barclays.taxis.service.GPSService
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar

/**
 * unit testing the actor
 *
 * @author	kostas.kougios
 *            Date: 08/12/14
 */
class GPSActorSuite extends BaseSuite with MockitoSugar
{
	trait TestSetup
	{
		// we setup our test with a mock GPSService and our actor
		val gpsService = mock[GPSService]
		val gpsActor = TestActorRef(Props(GPSActor(gpsService)))
	}

	test("sends new location") {
		new TestSetup
		{
			// Taxi1 moves from CanaryWarf to LondonBridge
			when(gpsService.measureLocation(Taxi1)).thenReturn(LondonBridge)

			gpsActor ! GPSOf(Taxi1)
			expectMsg(TaxiLocation(LondonBridge))
		}
	}
}

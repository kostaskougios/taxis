package com.barclays.taxis.actors

import akka.actor.Props
import akka.testkit.TestActorRef
import com.barclays.taxis.TestData._
import com.barclays.taxis.messages.{TaxiLocation, TaxiReport}
import com.barclays.taxis.service.GPSService
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar

/**
 * unit testing the actor
 *
 * @author	kostas.kougios
 *            Date: 08/12/14
 */
class TaxiActorSuite extends BaseSuite with MockitoSugar
{

	trait TestSetup
	{
		// we setup our test with a mock GPSService and our actor
		val gpsService = mock[GPSService]
		val taxiActor = TestActorRef(Props(TaxiActor(gpsService, Taxi1, testActor)))
	}

	test("sends new location if GPSActor responds with the Location") {
		new TestSetup
		{
			// Taxi1 moves from CanaryWarf to LondonBridge
			when(gpsService.measureLocation(Taxi1)).thenReturn(LondonBridge)

			taxiActor ! TaxiLocation(LondonBridge)

			expectMsg(TaxiReport(Taxi1.copy(location = LondonBridge)))
		}
	}

	test("sends new location if asked to CheckLocation") {
		new TestSetup
		{
			// Taxi1 moves from CanaryWarf to LondonBridge
			when(gpsService.measureLocation(Taxi1)).thenReturn(LondonBridge)

			taxiActor ! TaxiActor.ReportLocation

			expectMsg(TaxiReport(Taxi1.copy(location = LondonBridge)))
		}
	}
}

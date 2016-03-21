package com.barclays.taxis.actors

import akka.actor.{ActorContext, Props}
import akka.event.LoggingAdapter
import com.barclays.taxis.TestData._
import com.barclays.taxis.messages.NewTaxi
import com.barclays.taxis.model.{Location, Taxi}
import com.barclays.taxis.service.GPSService

import scala.concurrent.duration._
import scala.language.postfixOps

/**
 * End to end test which tests the whole system
 *
 * @author	kostas.kougios
 *            Date: 09/12/14
 */
class ManagementCenterEndToEndSuite extends BaseSuite
{
	// we need a predictable gps service
	val gpsService = new GPSService
	{
		override def measureLocation(taxi: Taxi) = Location(taxi.location.latitude + 1, taxi.location.longitude + 1f)
	}

	// we also need some messages that will be send when ManagementCenterActor receives a message
	case class ReportReceived(taxi: Taxi)

	case class RegistrationReceived(taxi: Taxi)

	// now we create the management center, making sure we report all events to the testActor so
	// that we can assert.
	val managementCenter = system.actorOf(Props(ManagementCenterActor(gpsService, new ManagementCenterActorOpsImpl(gpsService)
	{
		override def report(log: LoggingAdapter, taxi: Taxi) = {
			// we'll skip the logging for the purposes of this test, so no call to super.report()

			// let our testActor know about it!
			testActor ! ReportReceived(taxi)
		}

		override def register(taxi: Taxi, context: ActorContext) = {
			// do normal taxi registration
			super.register(taxi, context)
			// and let our testActor know about it!
			testActor ! RegistrationReceived(taxi)
		}
	})))

	test("taxi registration") {
		within(500 millis) {
			managementCenter ! NewTaxi(Taxi1)
			expectMsg(RegistrationReceived(Taxi1))
		}
	}

	test("taxi reports location after the 1st second") {
		within(1500 millis) {
			// we need to ignore registration messages
			ignoreMsg {
				case _: RegistrationReceived => true
			}

			// register the taxi
			managementCenter ! NewTaxi(Taxi1)
			// now expect the first report of the taxi to be on the new location
			expectMsg(ReportReceived(Taxi1.copy(location = gpsService.measureLocation(Taxi1))))
		}
	}

	test("taxi reports location continuously, once per second") {
		within(2500 millis) {
			// we need to ignore registration messages
			ignoreMsg {
				case _: RegistrationReceived => true
			}

			// register the taxi
			managementCenter ! NewTaxi(Taxi1)

			// now track the taxi movement
			val taxiNewLoc = Taxi1.copy(location = gpsService.measureLocation(Taxi1))
			expectMsg(ReportReceived(taxiNewLoc))

			// and after 1 second it will have moved a bit more
			expectMsg(ReportReceived(taxiNewLoc.copy(location = gpsService.measureLocation(taxiNewLoc))))
		}
	}
}

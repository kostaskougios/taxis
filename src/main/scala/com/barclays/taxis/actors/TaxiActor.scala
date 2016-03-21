package com.barclays.taxis.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.barclays.taxis.messages.{GPSOf, TaxiLocation, TaxiReport}
import com.barclays.taxis.model.Taxi
import com.barclays.taxis.service.GPSService

/**
 * A Taxi actor. It has a child gps actor and accepts the management actor it
 * has to report to.
 *
 * This actor accepts a TaxiActor.ReportLocation and will then send a report
 * back to the reportTo actor.
 *
 * @author	kostas.kougios
 *            Date: 08/12/14
 */
class TaxiActor private(
	gpsService: GPSService,
	private var taxi: Taxi,
	reportTo: ActorRef
	) extends Actor with ActorLogging
{
	// we need a gps actor for this taxi
	private val gpsActor = context.actorOf(Props(GPSActor(gpsService)))

	override def receive = {
		// this will be received every 1 second by the scheduler
		case TaxiActor.ReportLocation =>
			gpsActor ! GPSOf(taxi)
		// this will be received by the GPS actor
		case TaxiLocation(newLocation) =>
			// our taxi has a new location
			taxi = taxi.copy(location = newLocation)
			// send our report
			reportTo ! TaxiReport(taxi)
	}
}

object TaxiActor
{

	// this message is send by the scheduler to trigger a location check for a taxi
	object ReportLocation

	def apply(gpsService: GPSService, taxi: Taxi, reportTo: ActorRef) = new TaxiActor(gpsService, taxi, reportTo)

}
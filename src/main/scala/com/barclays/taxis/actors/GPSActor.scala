package com.barclays.taxis.actors

import akka.actor.Actor
import com.barclays.taxis.messages.{GPSOf, TaxiLocation}
import com.barclays.taxis.service.GPSService

/**
 * Receives GPS requests from Taxi's and responds with the gps location
 *
 * @author	kostas.kougios
 *            Date: 08/12/14
 */
class GPSActor private(gpsService: GPSService) extends Actor
{
	override def receive = {
		case GPSOf(taxi) =>
			// it's a good thing that we have the gpsService doing all the gps
			// logic as it let's us externalize it and helps with testing too (and
			// gpsService is reusable)
			sender ! TaxiLocation(gpsService.measureLocation(taxi))
	}
}

object GPSActor
{
	def apply(gpsService: GPSService) = new GPSActor(gpsService)
}
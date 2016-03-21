package com.barclays.taxis

import akka.actor.{ActorSystem, Props}
import com.barclays.taxis.actors.ManagementCenterActor
import com.barclays.taxis.messages.NewTaxi
import com.barclays.taxis.model.{Location, Taxi}
import com.barclays.taxis.service.GPSService
import com.typesafe.config.ConfigFactory

import scala.util.Random

/**
 * The demo application, it simulates 4 taxi's starting from (1.0,1.0) and randomly moving around.
 *
 * @author	kostas.kougios
 *            Date: 08/12/14
 */
object Application extends App
{
	// initialize the actor system
	val config = ConfigFactory.parseString( """akka { loglevel = "INFO" }""")
	val system = ActorSystem("TestCluster", config)

	println("----- SIMULATION STARTED----")

	// a gps service that randomly moves the taxi's location - for demo purposes
	val gpsService = new GPSService
	{
		override def measureLocation(taxi: Taxi) = Location(
			taxi.location.latitude + Random.nextFloat - 0.5f,
			taxi.location.longitude + Random.nextFloat - 0.5f
		)
	}

	try {
		// the management center
		val managementCenter = system.actorOf(Props(ManagementCenterActor(gpsService)))

		// our 4 taxis, all starting from the same location each morning :)
		for (i <- 1 to 4) {
			val taxi = Taxi("taxi " + i, Location(1.0f, 1.0f))
			// register the new taxi with the management center
			managementCenter ! NewTaxi(taxi)
		}

		// wait for some time for the taxis to move around.
		Thread.sleep(10000)
	} finally {
		// exit gracefully
		system.shutdown()
		println("----- SIMULATION COMPLETED ----")
	}
}

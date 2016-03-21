package com.barclays.taxis.actors

import akka.actor.{Actor, ActorContext, ActorLogging, Props}
import akka.event.LoggingAdapter
import com.barclays.taxis.messages.{NewTaxi, TaxiReport}
import com.barclays.taxis.model.Taxi
import com.barclays.taxis.service.GPSService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps

/**
 * The management center. It just logs the reports from each taxi and
 * accepts new taxis.
 *
 * Having taxi actors created here means that they are children of the
 * management center, effectively allowing us to supervise them (I
 * accept the default supervision config which will restart the actors
 * in case of TaxiActor throwing an exception)
 *
 * @author	kostas.kougios
 *            Date: 08/12/14
 */
abstract class ManagementCenterActor private(val gpsService: GPSService) extends Actor with ActorLogging
{
	// we inject this as required.
	val ops: ManagementCenterActorOps

	override def receive = {
		case TaxiReport(taxi) =>
			// a taxi just send us their report, log it
			ops.report(log, taxi)

		case NewTaxi(taxi) =>
			ops.register(taxi, context)
	}
}

object ManagementCenterActor
{
	def apply(gpsService: GPSService) = new ManagementCenterActor(gpsService)
	{
		val ops: ManagementCenterActorOps = new ManagementCenterActorOpsImpl(gpsService)
	}

	def apply(gpsService: GPSService, actorOps: ManagementCenterActorOps) = new ManagementCenterActor(gpsService)
	{
		val ops = actorOps
	}
}

trait ManagementCenterActorOps
{

	def report(log: LoggingAdapter, taxi: Taxi): Unit

	def register(taxi: Taxi, context: ActorContext): Unit
}
/**
 * We separated the actual logic from the actor in order to easier manage it
 * and test it (see ManagementCenterActorOpsSuite)
 *
 * @param gpsService	the gps service to be used by the taxis
 */
class ManagementCenterActorOpsImpl(gpsService: GPSService) extends ManagementCenterActorOps
{
	def report(log: LoggingAdapter, taxi: Taxi): Unit = {
		log.info("taxi {} reports location {}", taxi.name, taxi.location)
	}

	def register(taxi: Taxi, context: ActorContext): Unit = {
		val taxiActor = context.actorOf(Props(TaxiActor(gpsService, taxi, context.self)))
		// now make sure all taxis report their location each second
		context.system.scheduler.schedule(1 second, 1 second) {
			taxiActor ! TaxiActor.ReportLocation
		}
	}
}

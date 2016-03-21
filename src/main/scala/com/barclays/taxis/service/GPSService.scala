package com.barclays.taxis.service

import com.barclays.taxis.model.{Location, Taxi}

/**
 * Provides GPS services, i.e. current location of a taxi
 *
 * Please note: there is no concrete impl of this class other than inside Application class. A real
 * impl would use GPS to figure out the taxi's location.
 *
 * @author	kostas.kougios
 *            Date: 08/12/14
 */
trait GPSService
{
	/**
	 * finds out the current location for this taxi
	 *
	 * @param taxi the taxi
	 * @return the calculated Location of this taxi
	 */
	def measureLocation(taxi: Taxi): Location
}

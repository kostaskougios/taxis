package com.barclays.taxis.messages

import com.barclays.taxis.model.Taxi

/**
 * a report for a taxi
 *
 * We could break down this class to contain only the taxi name and location, but
 * the Taxi class is compact so far and it will do.
 *
 * @author	kostas.kougios
 *            Date: 09/12/14
 */
case class TaxiReport(taxi: Taxi)

package com.barclays.taxis.messages

import com.barclays.taxis.model.Taxi

/**
 * A request for the gps location of a taxi
 *
 * @author	kostas.kougios
 *            Date: 09/12/14
 */
case class GPSOf(taxi: Taxi)

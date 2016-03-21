package com.barclays.taxis.messages

import com.barclays.taxis.model.Taxi

/**
 * Send to the management center to add a new taxi to
 * the list of taxi's it manages
 *
 * @author	kostas.kougios
 *            Date: 08/12/14
 */
case class NewTaxi(taxi: Taxi)

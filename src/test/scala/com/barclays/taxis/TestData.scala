package com.barclays.taxis

import com.barclays.taxis.model.{Location, Taxi}

/**
 * test data to be reused by test cases
 *
 * @author	kostas.kougios
 *            Date: 08/12/14
 */
object TestData
{
	// we can safely reuse the immutable domain model

	val CanaryWarf = Location(1.0f, 0.5f)
	val LondonBridge = Location(0.5f, 1.0f)

	val Taxi1 = Taxi("taxi 1", CanaryWarf)
}

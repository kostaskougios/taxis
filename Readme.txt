Taxi akka app.

Please note: class names in double quotes:
A "GPSActor" waits for messages and uses the "GPSService" to acquire the "Location" of a "Taxi". The "TaxiActor" has a "GPSActor" child which is
used to figure out the GPS location of the "Taxi".

The "ManagementCenterActor" accepts "NewTaxi" registration requests. It then creates a "TaxiActor" child and schedules it's report each second.

This implementation uses sbt to manage dependencies and run tests & the application.

To run the app
---------------------------------------------------------

sbt run
(it will run a simulation with 4 taxis for 10 seconds. Each taxi reports its gps location once per second)

to run the tests
---------------------------------------------------------

sbt test
(please note, 1 test is ignored because it fails, in ManagementCenterActorOpsSuite. Due to a mockito problem expecting anyObject() for a by-name =>Unit parameter,
more inside the test itself. I am not sure if there is currently a solution but I couldn't find anything on the internet. Maybe a missing
feature? Anyway I left this as is due to time constraints.)


Some generic notes about the code
---------------------------------------------------------

- actor dependencies are passed on via constructor arguments. The call

context.actorOf(Props(new GPSActor(gpsService)))

is safe for non-remote actors. (For a real system we probably want to investigate DI)

- Some tests use fixture context objects, http://www.scalatest.org/user_guide/sharing_fixtures

- Using a service layer helps us minimize the code inside actors, making the actors simpler and more testable. Also
	breaking up the functionality of the actors into multiple classes help us simplify the code and ease testing.

- Actor classes have private constructors and a companion object to control instance creation and help extending
	the code later on (if it was actually required).

- Ideally an end to end test should exist for the TaxiActor (and maybe one for the GPSActor) but due to time
	constraints, I've implemented the most important: ManagementCenterEndToEndSuite . This tests end to end
	the whole system.

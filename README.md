# RouteTracker API
RouteTracker API's purpose is to encapsulate calls to the Chicago Transit Authority's bus and train APIs. It creates objects that are more easily consumable for a mobile app. Additionally, it helps cache results to minimize exhausting the limit of calls per day set by the CTA. Deployable format is a fat jar with embedded Tomcat server.

Powered by Spring, there are two primary endpoints supported by the api: "/getbuslines" and "/getpredictions":

- "/getbuslines" encapsulates many calls to the CTA API to build usable "BusLine" objects. Buslines consist of the route information, the directions it travels, and the stops that are serviced along each direction. Each of these represent 3 different endpoints from the CTA API: "/getroutes", "/getdirections", and "/getstops".
- "/getpredictions" is a direct translation of the CTA's endpoint of the same path. This endpoint exists solely to abstract the CTA's developer key which is required for API use. The additional network time is worth the added key security.



*Implementation of the train tracker API is planned. The API has a similar implementation to Bus Tracker.

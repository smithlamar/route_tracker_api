# RouteTracker API
RouteTracker API's purpose is to encapsulate calls to the Chicago Transit Authority's bus and train APIs. It creates objects that are more easily consumable for a mobile app. Additionally, it helps cache results to minimize exhausting the limit of calls per day set by the CTA. Deployable format is a fat jar with embedded Tomcat server.

Powered by Spring, there are two primary endpoints supported by the api: "/getbuslines" and "/getpredictions":

- "/getbuslines" encapsulates many calls to the CTA Bus Tracker API to build usable "BusLine" objects. Buslines consist of the route information, the directions it travels, and the stops that are serviced along each direction. Each of these represent 3 different endpoints from the CTA API: "/getroutes", "/getdirections", and "/getstops".

- "/getpredictions" is a direct translation of the CTA's endpoint of the same path. This endpoint exists solely to abstract the CTA's developer key which is required for API use. The additional network time is worth the added key security.

"/getpredictions" has one required parameter and two optional ones.
1. stpids=_string_ (Required): Comma-separated list of stpid values that each represent a unique stop along a route.
2. rts=_string_ (optional): Comma-separated list of rt values that represent unique routes. The list of stpids should correspond to these routes if included.
3. top=_int_ (optional): Sets the maximum number of results to return.

For more details on the data that is being encapsulated: see: http://www.transitchicago.com/assets/1/developer_center/cta_Bus_Tracker_API_Developer_Guide_and_Documentation_20160929.pdf

*Implementation of the train tracker API is planned. The API has a similar implementation to Bus Tracker.

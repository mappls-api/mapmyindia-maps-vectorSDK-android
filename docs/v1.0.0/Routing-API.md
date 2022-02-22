﻿![MapmyIndia APIs](https://www.mapmyindia.com/api/img/mapmyindia-api.png)


# Routes & Navigation API
## Routing API
Routing and displaying driving directions on map, including instructions for navigation, distance to destination, traffic etc. are few of the most important parts of developing a map based application. This REST API calculates driving routes between specified locations including via points based on route type(fastest or shortest), includes delays for traffic congestion , and is capable of handling additional route parameters like: type of roads to avoid, travelling vehicle type etc.
#### Java
~~~java
MapmyIndiaDirections.builder()  
        .origin(startPointLocal)  
        .steps(true)  
        .resource(DirectionsCriteria.RESOURCE_ROUTE_ETA)  
        .profile(DirectionsCriteria.PROFILE_DRIVING)  
        .overview(DirectionsCriteria.OVERVIEW_FULL)  
        .destination(endPointLocal)
        .build()  
        .enqueueCall(new Callback<DirectionsResponse>() {  
            @Override  
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {  
                //handle response   
	    }  
  
            @Override  
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {  
                 t.printStackTrace();  
	    }  
        });
~~~
#### Kotlin
~~~kotlin
MapmyIndiaDirections.builder()  
        .origin(startPointLocal)  
        .steps(true)  
        .resource(DirectionsCriteria.RESOURCE_ROUTE_ETA)  
        .profile(DirectionsCriteria.PROFILE_DRIVING)  
        .overview(DirectionsCriteria.OVERVIEW_FULL)  
        .destination(endPointLocal)  
        .build()  
        .enqueueCall(object : Callback<DirectionsResponse> {  
  
            override fun onResponse(call: Call<DirectionsResponse>, response: Response<DirectionsResponse>) {  
                //handle response  
            }  
            override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {  
                t.printStackTrace()  
            }  
        })
~~~
### Request Parameters
#### Mandatory Parameter
1. `origin(Point)`: pass the origin point
	- origin can also takes eLoc in parameter eg., `origin("17ZUL7")`
2. `destination(Point)`: pass the destination point
	- destination can also takes eLoc in parameter eg., `destination("MMI000")`
#### Optional Parameter
1. `profile(String)`: **Below are the available profile:**
   - DirectionsCriteria.PROFILE_DRIVING **(Default)**:Meant for car routing
   - DirectionsCriteria.PROFILE_WALKING:  Meant for pedestrian routing. Routing with this profile is restricted to route_adv only. region & rtype request parameters are not supported in pedestrian routing
   - DirectionsCriteria.PROFILE_BIKING:Meant for two-wheeler routing. Routing with this profile is restricted to route_adv only. region & rtype request parameters are not supported in two-wheeler routing.
   - DirectionsCriteria.PROFILE_TRUCKING:Meant for Truck routing. Routing with this profile is restricted to route_adv only. region & rtype request parameters are not supported in truck routing.
2. `resource(String)`:  **Below are the available resource:**
    - DirectionsCriteria.RESOURCE_ROUTE **(Default)**: to calculate a route & its duration without considering traffic conditions.
    - DirectionsCriteria.RESOURCE_ROUTE_ETA get the updated duration of a route considering live traffic; Applicable for India only "region=ind" and "rtype=1" is not supported. This is different from route_traffic; since this doesn't search for a route considering traffic, it only applies delays to the default route.
   - DirectionsCriteria.RESOURCE_ROUTE_TRAFFIC:  
to search for routes considering live traffic; Applicable for India only “region=ind” and “rtype=1” is not supported
3. ``steps(Boolean)``:Return route steps for each route leg. Possible values are true/false. By default it will be used as false.
4. ``overView(String)``: Add overview geometry either full, simplified according to highest zoom level it could be display on, or not at all. **Below are the available value:**
	- DirectionsCriteria.OVERVIEW_FULL
	- DirectionsCriteria.OVERVIEW_FALSE
	- DirectionsCriteria.OVERVIEW_SIMPLIFIED
5. ``excludes(String...)``: Additive list of road classes to avoid, order does not matter. **Below are the available value:**
    - DirectionsCriteria.EXCLUDE_FERRY
    - DirectionsCriteria.EXCLUDE_MOTORWAY
    - DirectionsCriteria.EXCLUDE_TOLL
6. ``addWaypoint(Point)``: pass single waypoint at a time
	- addWapoint also takes eLoc in parameter eg., `addWaypoint("MMI000")`
7. ``alternatives(Boolean)``: Search for alternative routes.
8. ``radiuses(double...)``: Limits the search to given radius in meters. For all way-points including start and end points.
9. ``geometries(String)``: This parameter used to change the route geometry format/density (influences overview and per step).**Below are the available value:**
	-  DirectionsCriteria.GEOMETRY_POLYLINE: with 5 digit precision
	- DirectionsCriteria.GEOMETRY_POLYLINE6 **(Default)**: with 6 digit precision
### Response Code (as HTTP response code)
#### Success:
1.  200: To denote a successful API call.
2.  204: To denote the API was a success but no results were found.
#### Client side issues:
1.  400: Bad Request, User made an error while creating a valid request.
2.  401: Unauthorized, Developer’s key is not allowed to send a request with restricted parameters.
3.  403: Forbidden, Developer’s key has hit its daily/hourly limit.
#### Server-Side Issues:
1.  500: Internal Server Error, the request caused an error in our systems.
2.  503: Service Unavailable, during our maintenance break or server downtimes.
###  Response Messages (as HTTP response message)
1.  200: Success.
2.  204: No matches were found for the provided query.
3.  400: Something’s just not right with the request.
4.  401: Access Denied.
5.  403: Services for this key has been suspended due to daily/hourly transactions limit.
6.  500: Something went wrong.
7.  503: Maintenance Break.
### Response Parameters
1. `code()` (String): if request is successful, response is .ok.. Else, see the service dependent and general status codes. In case of error, .NoRoute. code is supported (in addition to the general ones) which means .no route found..
2. `routes()`(List`<DirectionsRoute>`): A list of DirectionsRoute objects, each having potentially multiple via points
3. `waypoints()`(List`<DirectionsWaypoint>`): Array of DirectionsWaypoint objects representing all waypoints in order
#### DirectionsRoute Response result parameters:
1. ``distance()`` (Double): The distance travelled by the route; unit is meter.
2. `duration()` (Double): The estimated travel time; unit is second.
3. ``geometry()`` (String): returns the whole geometry of the route as per given parameter .geometries. default is encoded .polyline6. with 6 digit accuracy for positional coordinates.
4. `legs()` (List`<RouteLeg>`): The legs between the given waypoints, representing an array of RouteLeg between two waypoints.
##### Route Leg result parameters:
1. ``distance()`` (Double): The distance of travel to the subsequent legs, in meters
2. ``duration()`` (Double): The estimated travel time, in seconds
3. ``steps()`` (List`<LegStep>`): Return route steps for each route leg depending upon steps parameter.
##### LegStep result parameters:
1. `distance()` (double) :The distance of travel to the subsequent step, in meters
2. `duration()` (double): The estimated travel time, in seconds
3. `geometry()` (double): The un-simplified geometry of the route segment, depends on the given geometries parameter.
4. `name()` (String): The name of the way along which travel proceeds.
5. `mode()` (String): signifies the mode of transportation; driving as default.
6. `maneuver()` (StepManeuver): A StepManeuver object representing a maneuver
7. `drivingSide()` (String): .Left. (default) for India, Sri Lanka, Nepal, Bangladesh & Bhutan.
8. `intersections()` (List`<StepIntersection>`): A list of StepIntersection objects that are passed along the segment, the very first belonging to the StepManeuver
##### StepManeuver result parameters:
1. `location()`(Point): A Point describing the location of the turn
2. bearingBefore() (Double): The clockwise angle from true north to the direction of travel immediately before the maneuver.
3. bearingAfter() (Double): The clockwise angle from true north to the direction of travel immediately after the maneuver.
4. type() (String): An optional string indicating the direction change of the maneuver.
5. modifier() (String): A string indicating the type of maneuver. New identifiers might be introduced without API change. Types unknown to the client should be handled like the .turn. type, the existence of correct modifier values is guaranteed.
##### StepIntersection result parameters:
1. `location()` (Point): point describing the location of the turn.
2. `bearings()` (List`<Integer>`): A list of bearing values (e.g. [0,90,180,270]) thxat are available at the intersection. The bearings describe all available roads at the intersection.
3. `classes()` (List`<String>`): Categorised types of road segments e.g. Motorway
4. `entry()` (List`<Boolean>`): A list of entry flags, corresponding in a 1:1 relationship to the bearings. A value of true indicates that the respective road could be entered on a valid route. false indicates that the turn onto the respective road would violate a restriction.
5. `in() `(Integer): index into bearings/entry array. Used to calculate the bearing just before the turn. Namely, the clockwise angle from true north to the direction of travel immediately before the maneuver/passing the intersection. Bearings are given relative to the intersection. To get the bearing in the direction of driving, the bearing has to be rotated by a value of 180. The value is not supplied for depart maneuvers.
6. `out()` (Integer): index into the bearings/entry array. Used to extract the bearing just after the turn. Namely, The clockwise angle from true north to the direction of travel immediately after the maneuver/passing the intersection. The value is not supplied for arrive maneuvers.
7. `lanes()` (List`<IntersectionLanes>`Array of IntersectionLanes objects that denote the available turn lanes at the intersection. If no lane information is available for an intersection, the lanes property will not be present.): 
		- `valid()`(Boolean): verifying lane info.
		- `indications()` (List`<String>`): Indicating a sign of directions like Straight, Slight Left, Right, etc.
#### DirectionsWaypoint Response result parameters:
1. `name()` (String): Name of the street the coordinate snapped to.
2. `location()` (Point): Point describing the snapped location of the waypoint.
## Driving Distance Matrix API
Adding driving directions API would help to add predicted travel time & duration from a given origin point to a number of points. The Driving Distance Matrix API provides driving distance and estimated time to go from a start point to multiple destination points, based on recommended routes from MapmyIndia Maps and traffic flow conditions
Get driving time and distance between a center point and up to 10 destination points using MapmyIndia Maps [Distance API](http://www.mapmyindia.com/api/v3/docs/distance-api).
#### Java
~~~java
MapmyIndiaDistanceMatrix.builder()  
        .profile(DirectionsCriteria.PROFILE_DRIVING)  
        .resource(DirectionsCriteria.RESOURCE_DISTANCE)  
        .coordinate(Point.fromLngLat(80.502113, 8.916787))  
        .coordinate(Point.fromLngLat(28.5505073, 77.2689367))  
        .build()  
        .enqueueCall(new Callback<DistanceResponse>() {  
            @Override  
	    public void onResponse(Call<DistanceResponse> call, Response<DistanceResponse> response) {  
                //handle response    
	    }  
  
            @Override  
	    public void onFailure(Call<DistanceResponse> call, Throwable t) {  
                t.printStackTrace();  
	    }  
        });
~~~
#### Kotlin
~~~kotlin
MapmyIndiaDistanceMatrix.builder()  
        .profile(DirectionsCriteria.PROFILE_DRIVING)  
        .resource(DirectionsCriteria.RESOURCE_DISTANCE)  
        .coordinate(Point.fromLngLat(80.502113, 8.916787))  
        .coordinate(Point.fromLngLat(28.5505073, 77.2689367))  
        .build()  
        .enqueueCall(object : Callback<DistanceResponse> {  
  
            override fun onResponse(call: Call<DistanceResponse>, response: Response<DistanceResponse>) {  
                //handle response  
            }  
            override fun onFailure(call: Call<DistanceResponse>, t: Throwable) {  
                t.printStackTrace()  
            }  
        })
~~~
### Request Parameter
#### Manadatory  Parameter

1.  `coordinates(List<Point>)`: A List full of {@link Point}s which define the points to perform the matrix.
2. `coordinate(Point)`: To pass signgle coordinate at a time
	- coordinate can also takes eLoc eg., `coordinate("MMI000")`
3. `coordinateList(List<String>)`: To pass the full list of elocs
#### Optional Parameter
1.  `profile(String)`  : **Only supports** DirectionsCriteria.PROFILE_DRIVING.
2. `resource(Strng)`:  **Below are the available value:**
	- DirectionsCriteria.RESOURCE_DISTANCE **(Default)**: to calculate the route & duration without considering traffic conditions.
	- DirectionsCriteria.RESOURCE_DISTANCE_ETA: to get the updated duration considering live traffic; Applicable for India only “region=ind” and “rtype=1” is not supported. This is different from distance_matrix_traffic; since this doesn't search for a route considering traffic, it only applies delays to the default route.
	- DirectionsCriteria.RESOURCE_DISTANCE_TRAFFIC: to search for routes considering live traffic; Applicable for India only “region=ind” and “rtype=1” is not supported
#### Parameters for Many-To-Many Distance Matrix
1. `sources(List<Integer>)`: To specify which of the coordinates supplied in the URL are to be treated as _source_ position for many to many distance matrix calculation, indicate that coordinate pair's index. E.g. 0;1 means that 0th and 1st coordinate pairs are source points.
4. `destinations(List<integer>)`: To specify which of the coordinates supplied in the URL are to be treated as _destination_ position for many to many distance matrix calculation, indicate that coordinate pair's index. E.g. 2;3 means that 2nd and 3rd coordinate pairs are destination points.
### Response Code (as HTTP response code)
#### Success:
1.  200: To denote a successful API call.
2.  204: To denote the API was a success but no results were found.
#### Client side issues:
1.  400: Bad Request, User made an error while creating a valid request.
2.  401: Unauthorized, Developer’s key is not allowed to send a request with restricted parameters.
3.  403: Forbidden, Developer’s key has hit its daily/hourly limit.
#### Server-Side Issues:
1.  500: Internal Server Error, the request caused an error in our systems.
2.  503: Service Unavailable, during our maintenance break or server downtimes.
###  Response Messages (as HTTP response message)
1.  200: Success.
2.  204: No matches were found for the provided query.
3.  400: Something’s just not right with the request.
4.  401: Access Denied.
5.  403: Services for this key has been suspended due to daily/hourly transactions limit.
6.  500: Something went wrong.
7.  503: Maintenance Break.
### Response Parameters
1.  `responseCode`(Long): Response codes of the api.
2.  `version`(String): Version of the Api.
3.  `results`(DistanceResults): Array of results, each consisting of the following parameters
#### DistanceResults Response Result Parameters
1.  `code`  : if the request was successful, code is ok.
2.  `durations`(List`<Double[]>`): Duration in seconds for source to source (default 0), 1st, 2nd, 3rd secondary locations... from source.
3. `distances`(List`<Double[]>`): Distance in meters for source to source (default 0), 1st, 2nd, 3rd secondary locations... from source.
For any queries and support, please contact: 
![Email](https://www.google.com/a/cpanel/mapmyindia.co.in/images/logo.gif?service=google_gsuite) 
Email us at [apisupport@mapmyindia.com](mailto:apisupport@mapmyindia.com)
![](https://www.mapmyindia.com/api/img/icons/stack-overflow.png)
[Stack Overflow](https://stackoverflow.com/questions/tagged/mapmyindia-api)
Ask a question under the mapmyindia-api
![](https://www.mapmyindia.com/api/img/icons/support.png)
[Support](https://www.mapmyindia.com/api/index.php#f_cont)
Need support? contact us!
![](https://www.mapmyindia.com/api/img/icons/blog.png)
[Blog](http://www.mapmyindia.com/blog/)
Read about the latest updates & customer stories


> © Copyright 2021. CE Info Systems Ltd. All Rights Reserved. | [Terms & Conditions](http://www.mapmyindia.com/api/terms-&-conditions).
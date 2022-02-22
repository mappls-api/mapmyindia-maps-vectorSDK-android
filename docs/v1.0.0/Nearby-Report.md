
![MapmyIndia APIs](https://www.mapmyindia.com/api/img/mapmyindia-api.png)
# Nearby Reports
MapmyIndia Reports APIs enable the user to report any incidents at any place with predefined categories and
get that information on the Map and during Navigation.

Our SDK allows you the developers to get smooth way to access these reports.

**This feature is available from v6.8.15**

#### Java
~~~java
MapmyIndiaNearbyReport mapmyIndiaNearbyReport = MapmyIndiaNearbyReport.builder()  
        .topLeft(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()))  
        .bottomRight(Point.fromLngLat(latLng2.getLongitude(), latLng2.getLatitude()))  
        .build();  
new MapmyIndiaNearbyReportManager(mapmyIndiaNearbyReport).call(new OnResponseCallback<NearbyReportResponse>() {  
    @Override  
  public void onSuccess(NearbyReportResponse nearbyReportResponse) {  
        
    }  
  
    @Override  
  public void onError(int code, String message) {  
        // Request failed with code and message   
 }  
});
~~~
### Request Parameters
#### Mandatory Parameter
1. ``topLeft(Point)``: Point with top left latitude longitude
2. ``bottomRight(Point)``:Point with bottom right latitude longitude
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
###
### 	Response Parameter
#### NearbyReport Response Result Parameters
1. ``id``         (String): id of the report
2.  ``createdOn``  (Long): Timestamp when event created
3.  ``latitude``  (Double): latitude of the report.
4.  ``longitude`` (Double): longitude of the report.
5.  ``category``  (String): report category.
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
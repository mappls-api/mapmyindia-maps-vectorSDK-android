![MapmyIndia APIs](https://www.mapmyindia.com/api/img/mapmyindia-api.png)

# Feedback API

If you want to send feedback for search and selection user made you can use this api and it will improve your search result.

#### Java
~~~java
MapmyIndiaFeedback mapmyIndiaFeedback = MapmyIndiaFeedback.builder()  
        .typedKeyword("Map")  
        .eLoc("mmi000")  
        .locationName("Map")
        .index(1)  
        .longitude(77.0)  
        .latitude(28.0)  
        .userName(userName)  
        .appVersion(BuildConfig.VERSION_NAME)  
        .build();  
MapmyIndiaFeedbackManager.newInstance(mapmyIndiaFeedback).call(new OnResponseCallback<Void>() {  
    @Override  
    public void onSuccess(Void response) {  
        //Handle response  
    }  
  
    @Override  
    public void onError(int code, String message) {  
        //Handle error  
    }  
});
~~~
#### Kotlin
~~~kotlin
val mapmyIndiaFeedback = MapmyIndiaFeedback.builder()
    .typedKeyword("Map")
    .eLoc("mmi000")
    .locationName("Map")
    .index(1)
    .longitude(77.0)
    .latitude(28.0)
    .userName(userName)
    .appVersion(BuildConfig.VERSION_NAME)
    .build()
MapmyIndiaFeedbackManager.newInstance(mapmyIndiaFeedback).call(object : OnResponseCallback<Void?> {
    override fun onSuccess(response: Void?) {
        //Success  
    }

    override fun onError(code: Int, message: String) {
        //Handle Error  
    }
})
~~~
### Request Parameters

#### Mandatory Parameter
1.  `typedKeyword (String)` : The string that was searched. Must be 2 characters or more.
2. `eLoc (String)` : eLoc of the location that was selected. Must be exactly 6 characters.
3. `index (Integer)` : the index of the selected object that was returned from the search.
4. `userName (String)` : the username of the user that’s logged in.
5. `appVersion (String)` : the version of the app that was used to make the request.

#### Optional Parameter
1. `longitude (Double)` : the longitude of the location from where the search is made. The longitude must be a double value, must not start with 0.
2.  `latitude (Double)` : the latitude of the location from where the search is made. The latitude must be a double value, must not start with 0 and must not be larger than the longitude.
3.  `locationName (String )` : name of the location that was selected.

### Response Code (as HTTP response code)

#### Success:

1.  201: To denote that the feedback was successfully created.

#### Client side issues:

1.  400: Bad Request, User made an error while creating a valid request.
2.  401: Unauthorized, Developer’s key is not allowed to send a request with restricted parameters.
3.  403: Forbidden, Developer’s key has hit its daily/hourly limit.

#### Server-Side Issues:

1.  500: Internal Server Error, the request caused an error in our systems.
2.  503: Service Unavailable, during our maintenance break or server downtimes.

### Response Messages (as HTTP response message)

1.  201: Feedback submitted.
2.  400: Something’s just not right with the request.
3.  401: Access Denied.
4.  403: Services for this key has been suspended due to daily/hourly transactions limit.
5.  500: Something went wrong.
6.  503: Maintenance Break.

### Response parameters
The response of this API would be empty. Success would be denoted by the response codes and error would be denoted with the response codes.


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


> © Copyright 2021. CE Info Systems Ltd. All Rights Reserved. | [Terms & Conditions](http://www.mapmyindia.com/api/terms-&-conditions)



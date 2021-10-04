![MapmyIndia APIs](https://www.mapmyindia.com/api/img/mapmyindia-api.png)
# MapmyIndia Safety Plugin
MapmyIndia Safety Plugin will alert a user who is in or near to a containment zone. If the app is running, a callback method will be called to get the containment zone information. Safety plugin will push a local notification when the user goes near to the containment zone or in a containment zone, which can be seen from the notification panel.

A few extra step you have to follow in order to integrate MapmyIndia Safety Plugin in your application.

## Steps to Integrate SDK in an application.


## 1. ​ Add following lines to your application build.gradle:
``` groovy
dependencies {
    classpath ​'com.google.gms:google-services:4.3.3'
}
```

## 2. Initialize plugin

#### Java
``` java
MapmyIndiaSafetyPlugin  
        .getInstance()  
        .initialize(new IAuthListener() {  
        @Override  
        public void onSuccess() {  
                // After successful authentication onSuccess() will be called.  
        }  
  
         @Override  
          public void onError(String reason, String errorIdentifier, String errorDescription) {  
                // reason gives the error type.  
  
               // errorIdentifier gives information about error code.  
              // errorDescription gives a message for a particular error. 
        }  
});
```

#### Kotlin
```kotlin
 MapmyIndiaSafetyPlugin.getInstance().initialize(object : IAuthListener {  
      override fun onSuccess() {  
            // After successful authentication onSuccess() will be called.    
      }  
  
      override fun onError(reason: String, errorIdentifier: String, errorDescription: String) {  
            // reason gives the error type.  
  
           // errorIdentifier gives information about error code.  
           // errorDescription gives a message for a particular error
    }  
})
```
## 3. Start plugin

#### Java
```java
MapmyIndiaSafetyPlugin  
        .getInstance()  
        .startSafetyPlugin(new ISafetyListener() {  
        @Override  
        public void onResult(ContainmentZoneInfo zoneInfo) {  
  
               // onResult will return a ContainmentZoneInfo object.  
  
              //zoneInfo.isInsideContainmentZone() --True if user stays inside the containment zone else false.  
              //zoneInfo.getDistanceToNearestZone() --Distance in meters to the nearest containment zone.  
              //zoneInfo.getMapLink() --Map link for containment zone.  
              //zoneInfo.getDistrictName() --Name of the district.  
              //zoneInfo.getZoneType() -- District Zone current type like red, orange and green zone.  
         }   
  
        @Override  
         public void onError(String reason, String errorIdentifier, String errorDescription) {  
               
              // reason gives the error type.  
              // errorIdentifier gives information about error code.  
              // error description gives a message for a particular error.  
        }  
});
```

#### Kotlin
```kotlin
MapmyIndiaSafetyPlugin.getInstance().startSafetyPlugin(object : ISafetyListener {  
    override fun onResult(zoneInfo: ContainmentZoneInfo) {  
   
        //zoneInfo.isInsideContainmentZone --True if user stays inside the containment zone else false.  
        //zoneInfo.distanceToNearestZone --Distance in meters to the nearest containment zone.    
        // zoneInfo.mapLink --Map link for containment zone.  
        //zoneInfo.districtName --Name of the district.  
        //zoneInfo.zoneType -- District Zone current type like red, orange and green zone. 
    }  
  
    override fun onError(reason: String, errorIdentifier: String, errorDescription: String) {  
        Timber.e("OnError:" + reason + "errorIdentifier:- " + errorIdentifier)  
    }  
})
```
## 4. Stop Safety plugin

#### Java
```java
MapmyIndiaSafetyPlugin.getInstance().stopSafetyPlugin();
```

#### Kotlin
```kotlin
MapmyIndiaSafetyPlugin.getInstance().stopSafetyPlugin()
```
## 5. Get Current Location Safety

#### Java
```java
// Get the corona safety information for the current location by calling following method.
MapmyIndiaSafetyPlugin  
        .getInstance()  
        .getCurrentLocationSafety(new ISafetyListener() {  
        @Override  
        public void onResult(ContainmentZoneInfo zoneInfo) {  
 
             // onResult will return a ContainmentZoneInfo object.  
  
            //zoneInfo.isInsideContainmentZone() --True if user stays inside the containment zone else false.  
            //zoneInfo.getDistanceToNearestZone() --Distance in meters to the nearest containment zone.  
            //zoneInfo.getMapLink() --Map link for containment zone.  
            //zoneInfo.getDistrictName() --Name of the district.  
            //zoneInfo.getZoneType() -- District Zone current type like red, orange and green zone.  
         }  
  
        @Override  
        public void onError(String reason, String errorIdentifier, String errorDescription) {  
            // reason gives the error type.  
  
           // errorIdentifier gives information about error code.  
           // error description gives a message for a particular error.  
        }  
});
```

#### Kotlin
```kotlin
MapmyIndiaSafetyPlugin.getInstance().getCurrentLocationSafety(object : ISafetyListener {  
    override fun onResult(zoneInfo: ContainmentZoneInfo) {  
            // onResult will return a ContainmentZoneInfo object.  
  
            //zoneInfo.isInsideContainmentZone --True if user stays inside the containment zone else false.  
            //zoneInfo.distanceToNearestZone --Distance in meters to the nearest containment zone.  
            //zoneInfo.mapLink --Map link for containment zone.  
            //zoneInfo.districtName --Name of the district.  
            //zoneInfo.zoneType -- District Zone current type like red, orange and green zone.
    }  
  
    override fun onError(reason: String, errorIdentifier: String, errorDescription: String) {  
        // reason gives the error type.  
  
       // errorIdentifier gives information about error code.  
       // error description gives a message for a particular error.    
    }  
})
```
## ContainmentZoneInfo ​Result Parameters:-

- `isInsideContainmentZone (boolean)`​ - True if user stays inside the containment zone else false.
- `containmentZoneName (String)`​ ​- Name of the containment zone.
- `mapLink (String)` ​- Map link for containment zone.
- `distanceToNearestZone (Long`) ​- Distance to the nearest containment zone.
- `districtName (String)`​ - Name of the district.
- `zoneType (String)`​ - District Zone current type like red, orange and green zone.

## Error Description:-

    - 204: No matches were found for the provided query.
    - 400: Bad request.
    - 401: unauthorized request. Access to API is forbidden.
    - 500: Something went wrong.
    - 503: Maintenance Break

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


> © Copyright 2020. CE Info Systems Pvt. Ltd. All Rights Reserved. | [Terms & Conditions](http://www.mapmyindia.com/api/terms-&-conditions).



![MapmyIndia APIs](https://www.mapmyindia.com/api/img/mapmyindia-api.png)  
  
# Direction Widget  
  
A ready to use Fragment to show the Routes in an Android platform.  It offers the following basic functionalities:

1. Takes support of MapmyIndia Place search for searching locations of origin, destinations and via points.
2. It allows to use origin and destinations in MapmyIndia's digital address (semicolon separated) eLoc or WGS 84 geographical coordinates both.
3.  The ability to set the vehicle profile like driving, and biking.
4. Easily set the resource for traffic and ETA information.

For more details, please contact apisupport@mapmyindia.com.

## Implementation

### Setup your project	

Follow these steps to add the SDK to your project –	

-   Create a new project in Android Studio	
- Add MapmyIndia repository in your project level build.gradle	
~~~groovy	
 allprojects {  	
    repositories {  	
    	
        maven {  	
            url 'https://maven.mapmyindia.com/repository/mapmyindia/' 	
        }  	
    }  	
}	
~~~	

- Add below dependency in your app-level build.gradle	
~~~groovy	
    implementation 'com.mapmyindia.sdk:direction-ui:0.2.0'
~~~	

### Add your API keys to the SDK	

_Add your API keys to the SDK (in your application's onCreate() or before using map)_	
#### Java	
```java	
MapmyIndiaAccountManager.getInstance().setRestAPIKey(getRestAPIKey());  	
MapmyIndiaAccountManager.getInstance().setMapSDKKey(getMapSDKKey());  		
MapmyIndiaAccountManager.getInstance().setAtlasClientId(getAtlasClientId());  	
MapmyIndiaAccountManager.getInstance().setAtlasClientSecret(getAtlasClientSecret());  	
```	
#### Kotlin	
```kotlin	
MapmyIndiaAccountManager.getInstance().restAPIKey = getRestAPIKey()  	
MapmyIndiaAccountManager.getInstance().mapSDKKey = getMapSDKKey()  		
MapmyIndiaAccountManager.getInstance().atlasClientId = getAtlasClientId()  	
MapmyIndiaAccountManager.getInstance().atlasClientSecret = getAtlasClientSecret()	
```	

### Add Direction Widget
  
#### Java  
```java  
DirectionFragment directionFragment = DirectionFragment.newInstance();  
  
getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, directionFragment, DirectionFragment.class.getSimpleName())    
        .commit();  
  
                            //OR  
DirectionFragment directionFragment = DirectionFragment.newInstance(directionOptions);  
  
getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, directionFragment, DirectionFragment.class.getSimpleName())    
        .commit();  
```  
#### Kotlin  
~~~kotlin  
val directionFragment: DirectionFragment = DirectionFragment.newInstance()  
supportFragmentManager.beginTransaction().add(R.id.fragment_container, directionFragment, DirectionFragment::class.java.simpleName)    
        .commit()  
                                 
                               //OR  
val directionFragment: DirectionFragment = DirectionFragment.newInstance(directionOptions)  
supportFragmentManager.beginTransaction().add(R.id.fragment_container, placeAutocompleteFragment, PlaceAutocompleteFragment::class.java.simpleName)    
        .commit()                                 
~~~  
  
You can use `DirectionOptions` to set the properties of direction widget:  
  
 1. resource(String)`:  **Below are the available resource:**  
    - DirectionsCriteria.RESOURCE_ROUTE **(Default)**: to calculate a route & its duration without considering traffic conditions.  
    - DirectionsCriteria.RESOURCE_ROUTE_ETA get the updated duration of a route considering live traffic; Applicable for India only "region=ind" and "rtype=1" is not supported. This is different from route_traffic; since this doesn't search for a route considering traffic, it only applies delays to the default route.  
   - DirectionsCriteria.RESOURCE_ROUTE_TRAFFIC:    
to search for routes considering live traffic; Applicable for India only “region=ind” and “rtype=1” is not supported  
2. `showAlternative(Boolean)`: Show alternative routes.  
3. `profile(String)`: **Below are the available profile:**  
   - DirectionsCriteria.PROFILE_DRIVING **(Default)**:Meant for car routing  
   - DirectionsCriteria.PROFILE_WALKING:  Meant for pedestrian routing. Routing with this profile is restricted to route_adv only. region & rtype request parameters are not supported in pedestrian routing  
   - DirectionsCriteria.PROFILE_BIKING:Meant for two-wheeler routing. Routing with this profile is restricted to route_adv only. region & rtype request parameters are not supported in two-wheeler routing.  
   - DirectionsCriteria.PROFILE_TRUCKING:Meant for Truck routing. Routing with this profile is restricted to route_adv only. region & rtype request parameters are not supported in truck routing.  
4. `overview(String)`:  Add overview geometry either full, simplified according to highest zoom level it could be display on, or not at all. **Below are the available value:**  
   - DirectionsCriteria.OVERVIEW_FULL  
   - DirectionsCriteria.OVERVIEW_FALSE  
   - DirectionsCriteria.OVERVIEW_SIMPLIFIED  
5. `steps(Boolean)`: Return route steps for each route leg. Possible values are true/false. By default it will be used as false.  
6. `excludes(List<String>)` : Additive list of road classes to avoid, order does not matter. **Below are the available value:**  
    - DirectionsCriteria.EXCLUDE_FERRY  
    - DirectionsCriteria.EXCLUDE_MOTORWAY  
    - DirectionsCriteria.EXCLUDE_TOLL  
7. `showStartNavigation(Boolean)`: To show the Start Navigation button if the origin is current location.  
8. `showDefaultMap(Boolean)`: To add the option to show default map  
9. `destination(DirectionPoint)`: You can use `DirectionPoint` to pass the destination in direction widget:  
    - `setDirection(Point, String, String)`: It takes coordinate, place name and place address  
    - `setDirection(String, String, String)`: It takes eloc, place name and place address  
10. `searchPlaceOption(PlaceOptions`): To set the properties of search widget  
  
### To pass the MapView  
~~~java  
directionFragment.provideMap(MapView)  
~~~  
  
### Callbacks getting from Direction Fagment  
Implement from DirectionCallback interface:  
  
~~~java  
directionFragment.setDirectionCallback(new DirectionCallback() {    
    @Override    
  public void onCancel() {    
        //on Click of back button  
  }    
    
    @Override    
  public void onStartNavigation(DirectionPoint origin, DirectionPoint destination, List<DirectionPoint> waypoints, DirectionsResponse directionsResponse, int selectedIndex) {    
        //Get the origin, destination, waypoints, directionsResponse and the selected Index  
  }    
});  
~~~  
  
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
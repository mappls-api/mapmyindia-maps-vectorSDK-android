  
  

![MapmyIndia APIs](https://www.mapmyindia.com/api/img/mapmyindia-api.png)
# MapmyIndia Vector Map Android SDK

**Easy To Integrate Maps & Location APIs & SDKs For Web & Mobile Applications**

Powered with India's most comprehensive and robust mapping functionalities.
**Now Available**  for Srilanka, Nepal, Bhutan and Bangladesh

1. You can get your api key to be used in this document here: [https://www.mapmyindia.com/api/signup](https://www.mapmyindia.com/api/signup)

2. The sample code is provided to help you understand the basic functionality of MapmyIndia maps & REST APIs working on **Android** native development platform.  

## [Version History](#Version-History)


| Version | Last Updated | Author |  Release Note|
| ---- | ---- | ---- | ---- |
| v6.8.2 | 16 June 2020 | MapmyIndia API Team ([MA](https://github.com/mdakram)) ([ST](https://github.com/saksham66)) |   Added Options to check current location safety |
| v6.8.1 | 31 May 2020 | MapmyIndia API Team ([MA](https://github.com/mdakram)) ([ST](https://github.com/saksham66)) |   🐞 Bug fixes|
| v6.8.0 | 27 May 2020 | MapmyIndia API Team ([MA](https://github.com/mdakram)) ([ST](https://github.com/saksham66)) |   Now enable/disable interactive layer using our maps sdk |
| v6.7.14 | 28 April 2020 | MapmyIndia API Team ([MA](https://github.com/mdakram)) ([ST](https://github.com/saksham66)) |  Added MapmyIndia Safety plugin |
|v6.7.13 | 12 May 2020| MapmyIndia API Team ([MA](https://github.com/mdakram)) ([ST](https://github.com/saksham66)) | Functions added to enable/disable dem and monument icons
|  v6.7.12 | 26 March 2020 | MapmyIndia API Team ([MA](https://github.com/mdakram)) ([ST](https://github.com/saksham66)) | Indoor related bug fixes and bug fixes in rest api calls
| v6.7.10| 29 Jan 2020 | MapmyIndia API Team ([MA](https://github.com/mdakram)) ([ST](https://github.com/saksham66)) | Changed rest API calls for better kotlin support
| v6.7.9 |  6 Dec 2019 | MapmyIndia API Team ([MA](https://github.com/mdakram)) ([ST](https://github.com/saksham66)) |  Indoor maps are now supported with controls and callbacks for switching between different levels as well as getting the layer control only when the map has an indoor component within viewbounds.
|v6.7.8 | 25 Oct 2019 | MapmyIndia API Team ([MA](https://github.com/mdakram)) |  Added support for Myanmar throughout all components: Maps, RESTful API methods available through the SDK> Available in both Vector & legacy Raster SDK.
| v6.7.7| 29 Sep 2019 | MapmyIndia API Team ([MA](https://github.com/mdakram)) | - Retrofit  v2.6 Support  <br/>- Pedestrian Routing API integration in SDK <br/> - Biking Routing API integration in SDK <br/>- Aggressive Routing Resource integrated in both Routing & Distance Matrix methods.<br/>- Able to show flight path polyline via dashed style<br/>- Polyline with gradient colors now available.<br/>- Marker position Animation: Let's a developer animate the transition of marker's position from 1 geocoordinate to another.<br/>- Marker Rotation Animation: Let's a developer animate the rotation of marker, rather than using several markers of different heading for pseudo-animation of marker.<br/>- Marker icon mutation > animated transition to another icon
| v6.7.6 | 29 Sep 2019 | MapmyIndia API Team ([MA](https://github.com/mdakram)) | Fixes Unknown pbf field type exception
|v6.7.5 | 5 Sep 2019 | MapmyIndia API Team ([MA](https://github.com/mdakram)) |  Fixes native library loading error




## [Getting Started](#getting-started)

MapmyIndia Maps SDK for Android lets you easily add MapmyIndia Maps and web services to your own Android application. The SDK for Android supports API 14+. You can have a look at the map and features you will get in your own app by using the MapmyIndia Maps SDK for Android.

Through customized tiles, you can add different map layers to your application and add bunch of controls and gestures to enhance map usability thus creating potent map based solutions for your customers. The SDK handles downloading of map tiles and their display along with a bunch of controls and native gestures.

## [API Usage](#api-usage)

Your MapmyIndia Maps SDK usage needs a set of license keys (get them  [here](http://www.mapmyindia.com/api/signup)) and is governed by the API  [terms and conditions](http://www.mapmyindia.com/api/terms-&-conditions). As part of the terms and conditions,  **you cannot remove or hide the MapmyIndia logo and copyright information** in your project.

The allowed SDK hits are described on the user dashboard (http://www.mapmyindia.com/api/dashboard) page. Note that your usage is shared between platforms, so the API hits you make from a web application, Android app or an iOS app all add up to your allowed daily limit.

## [Setup your project](#setup-your-project)

Follow these steps to add the SDK to your project –

-   Create a new project in Android Studio
-  Add MapmyIndia repository in your project level `build.gradle`
~~~groovy
 allprojects {  
    repositories {  
    
        maven {  
            url 'https://maven.mapmyindia.com/repository/mapmyindia/' 
        }  
    }  
}
~~~
-   Add below dependency in your app-level `build.gradle`

```groovy
implementation 'com.mapmyindia.sdk:mapmyindia-android-sdk:6.8.2'
```
- Add these permissions in your project
```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.INTERNET"/>
```
### [Add Java 8 Support to the project](#add-java-8-support-to-the-project)

 *add following lines in your app module's build.gradle*

```groovy
compileOptions {
      sourceCompatibility 1.8
      targetCompatibility 1.8
  }
  ```
    
### [Add your API keys to the SDK](#add-your-api-keys-to-the-sdk)
*Add your API keys to the SDK (in your application's onCreate() or before using map)*

#### Java
```java
MapmyIndiaAccountManager.getInstance().setRestAPIKey(getRestAPIKey());  
MapmyIndiaAccountManager.getInstance().setMapSDKKey(getMapSDKKey());  
MapmyIndiaAccountManager.getInstance().setAtlasGrantType(getAtlasGrantType());  
MapmyIndiaAccountManager.getInstance().setAtlasClientId(getAtlasClientId());  
MapmyIndiaAccountManager.getInstance().setAtlasClientSecret(getAtlasClientSecret());  
MapmyIndia.getInstance(getApplicationContext());
```
#### Kotlin
```kotlin
MapmyIndiaAccountManager.getInstance().restAPIKey = getRestAPIKey()  
MapmyIndiaAccountManager.getInstance().mapSDKKey = getMapSDKKey()  
MapmyIndiaAccountManager.getInstance().atlasGrantType = getAtlasGrantType()  
MapmyIndiaAccountManager.getInstance().atlasClientId = getAtlasClientId()  
MapmyIndiaAccountManager.getInstance().atlasClientSecret = getAtlasClientSecret()
MapmyIndia.getInstance(applicationContext)
```
*You cannot use the MapmyIndia Map Mobile SDK without these function calls. You will find your keys in your API Dashboard.*


## [Add a MapmyIndia Map to your application](#add-a-mapmyindia-map-to-your-application)


```xml
<com.mapbox.mapboxsdk.maps.MapView  
  android:id="@id/map_view"  
  android:layout_width="match_parent"  
  android:layout_height="match_parent" />
```
##### NOTE: All the lifecycle methods that need to be overridden:

Initialize the mapView 
#### Java
```java
@Override  
protected void onCreate(@Nullable Bundle savedInstanceState) {  
    super.onCreate(savedInstanceState);  
    setContentView(R.layout.base_layout);  
    mapView = findViewById(R.id.map_view);  
    mapView.onCreate(savedInstanceState);  
}

@Override  
protected void onStart() {  
    super.onStart();  
    mapView.onStart();  
}  
  
@Override  
protected void onStop() {  
    super.onStop();  
    mapView.onStop();  
}  
  
@Override  
protected void onDestroy() {  
    super.onDestroy();  
    mapView.onDestroy();
}  
  
@Override  
protected void onPause() {  
    super.onPause();  
    mapView.onPause();  
}  
  
@Override  
protected void onResume() {  
    super.onResume();  
    mapView.onResume();  
}  
  
@Override  
public void onLowMemory() {  
    super.onLowMemory();  
    mapView.onLowMemory();  
}  
  
@Override  
protected void onSaveInstanceState(Bundle outState) {  
    super.onSaveInstanceState(outState);  
    mapView.onSaveInstanceState(outState);  
}
```

#### Kotlin
~~~kotlin
  
override fun onCreate(savedInstanceState: Bundle?) {  
    super.onCreate(savedInstanceState)  
    setContentView(R.layout.base_layout) 
    mapView = findViewById(R.id.map_view)  
    mapView.onCreate(savedInstanceState)
}

  
override fun onStart() {  
    super.onStart()  
    mapView.onStart()  
}  
  
override fun onResume() {  
    super.onResume()  
    mapView.onResume()  
}  
  
override fun onPause() {  
    super.onPause()  
    mapView.onPause()  
}  
  
override fun onStop() {  
    super.onStop()  
    mapView.onStop()  
}  
  
override fun onDestroy() {  
    super.onDestroy()  
    mapView.onDestroy()  
}  
  
override fun onLowMemory() {  
    super.onLowMemory()  
    mapView.onLowMemory()  
}  
  
override fun onSaveInstanceState(outState: Bundle) {  
    super.onSaveInstanceState(outState)  
    map_view.onSaveInstanceState(outState)  
}
~~~
## [Map Interactions](#map-interactions)

The MapmyIndia Maps Android SDK allows you to define interactions that you can activate on the map to enable gestures and click events. The following interactions are supported –

### [Zoom Controls](#zoom-controls)

The map supports the familiar two-finger pinch and zooms to change zoom level as well as double tap to zoom in. Set zoom to 4 for country level display and 18 for house number display. In this SDK the camera position plays an important role

And following operations can be performed using the CameraPosition

### [Target](#target)

The target is single latitude and longitude coordinate that the camera centers it on. Changing the camera's target will move the camera to the inputted coordinates. The target is a LatLng object. The target coordinate is always  _at the center of the viewport_.

### [Tilt](#tilt)

Tilt is the camera's angle from the nadir (directly facing the Earth) and uses unit degrees. The camera's minimum (default) tilt is 0 degrees, and the maximum tilt is 60. Tilt levels use six decimal point of precision, which enables you to restrict/set/lock a map's bearing with extreme precision.

The map camera tilt can also adjust by placing two fingertips on the map and moving both fingers up and down in parallel at the same time or

### [Bearing](#bearing)

Bearing represents the direction that the camera is pointing in and measured in degrees  _clockwise from north_.

The camera's default bearing is 0 degrees (i.e. "true north") causing the map compass to hide until the camera bearing becomes a non-zero value. Bearing levels use six decimal point precision, which enables you to restrict/set/lock a map's bearing with extreme precision. In addition to programmatically adjusting the camera bearing, the user can place two fingertips on the map and rotate their fingers.

### [Zoom](#zoom)

Zoom controls scale of the map and consumes any value between 0 and 22. At zoom level 0, viewport shows continents and other world features. A middle value of 11 will show city level details.At a higher zoom level, map will begin to show buildings and points of interest. Camera can zoom in following ways:

-   Pinch motion two fingers to zoom in and out.
-   Quickly tap twice on the map with a single finger to zoom in.
-   Quickly tap twice on the map with a single finger and hold your finger down on the screen after the second tap.
-   Then slide the finger up to zoom out and down to zoom out.

 Sdk provides a OnMapReadyCallback, implements this callback and override it's onMapReady() and set the Camera position inside this method 
#### Java
```java
CameraPosition position = new CameraPosition.Builder()
      .target(new LatLng(22.8978, 77.3245)) // Sets the new camera position
      .zoom(14) // Sets the zoom to level 14
      .tilt(45) // Set the camera tilt to 45 degrees
      .build();
mapmyIndiaMap.setCameraPosition(position)
```

#### Kotlin
```kotlin
val cameraPosition = CameraPosition.Builder()  
        .target(LatLng(22.8978, 77.3245))  
        .zoom(10.0)  
        .tilt(0.0)  
        .build()  
mapmyIndiaMap?.cameraPosition = cameraPosition
```
##### Sdk allows various method to Move, ease,animate Camera to a particular location  :
#### Java
```java
mapmyIndiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.8978,77.3245),14);
mapmyIndiaMap.easeCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.8978,77.3245),14);
mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.8978,77.3245),14);
```
#### Kotlin
~~~kotlin
mapmyIndiaMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(22.8978,77.3245), 14.0))  
mapmyIndiaMap?.easeCamera(CameraUpdateFactory.newLatLngZoom(LatLng(22.8978,77.3245), 14.0))
mapmyIndiaMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(22.8978,77.3245), 14.0))
~~~

## [Map Events](#map-events)

##### **The SDK allows you to listen to certain events on the map. It sets a callback that is invoked when camera movement has started.**
#### Java
```java
mapmyIndiaMap.addOnCameraMoveStartedListener(new MapboxMap.OnCameraMoveStartedListener() {  
    private final String[] REASONS = {  
            "REASON_API_GESTURE",  
            "REASON_DEVELOPER_ANIMATION",  
            "REASON_API_ANIMATION"};  
  
  @Override  
  public void onCameraMoveStarted(int reason) {  
        String string = String.format(Locale.US, "OnCameraMoveStarted: %s", REASONS[reason - 1]);  
        Toast.makeText(MainActivity.this, string, Toast.LENGTH_LONG).show();  
  }  
});
```

#### Kotlin
~~~kotlin
mapmyIndiaMap.addOnCameraMoveStartedListener(object : MapboxMap.OnCameraMoveStartedListener {  
    private val REASONS: Array<String> = arrayOf(
               "REASON_API_GESTURE",  
               "REASON_DEVELOPER_ANIMATION",  
               "REASON_API_ANIMATION"
               )  
    override fun onCameraMoveStarted(i: Int) {  
        var string: String = String.format("OnCameraMoveStarted: %s", REASONS[i - 1])  
        Toast.makeText(this@MainActivity, string, Toast.LENGTH_SHORT).show()  
    }
})
~~~
##### It sets a callback that is invoked when camera movement was cancelled.
#### Java
```java
mapmyIndiaMap.addOnCameraMoveCancelListener(new MapboxMap.OnCameraMoveCanceledListener() {  
  @Override  
  public void onCameraMoveCanceled() {  
    Toast.makeText(MainActivity.this, "onCameraMoveCanceled", Toast.LENGTH_LONG).show();  
  }  
});
```

#### Kotlin
~~~kotlin
mapmyIndiaMap.addOnCameraMoveCancelListener(object : MapboxMap.OnCameraMoveCanceledListener {  
    override fun onCameraMoveCanceled() {  
        Toast.makeText(this@MainActivity, "onCameraMoveCanceled", Toast.LENGTH_SHORT).show()  
    }  
})
~~~
##### It sets a callback that is invoked when camera movement has ended.
#### Java
```java
 mapmyIndiaMap.addOnCameraIdleListener(new MapboxMap.OnCameraIdleListener() {  
  @Override  
  public void onCameraIdle() {  
    Toast.makeText(MainActivity.this, "onCameraIdle", Toast.LENGTH_LONG).show();  
  }  
});
```
#### Kotlin
~~~kotlin
mapmyIndiaMap.addOnCameraIdleListener(object : MapboxMap.OnCameraIdleListener {  
    override fun onCameraIdle() {  
        Toast.makeText(this@MainActivity, "onCameraIdle", Toast.LENGTH_SHORT).show()  
    }  
})
~~~
### [Map Click/Long Press](#map-click-long-press)

If you want to respond to a user tapping on a point on the map, you can use a MapEventsOverlay which you need to add on the map as an Overlay.

It sets a callback that's invoked when the user clicks on the map view.
#### Java
```java
mapmyIndiaMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {  
  @Override  
  public void onMapClick(@NonNull LatLng point) {  
    String string = String.format(Locale.US, "User clicked at: %s", point.toString())  
    Toast.makeText(MainActivity.this, string, Toast.LENGTH_LONG).show();  
  }  
});
```
####  Kotlin
~~~kotlin
mapmyIndiaMap.addOnMapClickListener(object: MapboxMap.OnMapClickListener {  
    override fun onMapClick(latLng: LatLng) { 
        val string: String = String.format("User clicked at: %s", latLng.toString()) 
        Toast.makeText(this@MainActivity, string, Toast.LENGTH_LONG).show()   
    }  
})
~~~
##### Sets a callback that's invoked when the user long clicks on the map view.
#### Java
```java
mapmyIndiaMap.addOnMapLongClickListener(new MapboxMap.OnMapLongClickListener() {  
  @Override  
  public void onMapLongClick(@NonNull LatLng point) {  
    String string = String.format(Locale.US, "User long clicked at: %s", point.toString());  
    Toast.makeText(MainActivity.this, string, Toast.LENGTH_LONG).show();  
  }  
});
```

#### Kotlin
~~~kotlin
mapmyIndiaMap.addOnMapLongClickListener(object: MapboxMap.OnMapLongClickListener {  
    override fun onMapLongClick(latLng: LatLng) {
        var string: String = String.format("User long clicked at: %s", latLng.toString())  
        Toast.makeText(this@MainActivity, string, Toast.LENGTH_LONG).show()  
    }  
})
~~~
## [Map Overlays](#map-overlays)

### [Add A Marker](#add-a-marker)

 ##### Add markers to the map by following these steps –
 #### Java
```java
MarkerOptions markerOptions = new MarkerOptions().position(point).icon(IconFactory.getInstance(SimpleMapActivity.this).fromResource(R.drawable.ic_android));  
markerOptions.setTitle("Marker");  
markerOptions.setSnippet("This is a Marker");  
Marker marker = mapmyIndiaMap.addMarker(markerOptions);
```
#### Kotlin
~~~kotlin
val markerOptions: MarkerOptions = MarkerOptions().position(point).icon(IconFactory.getInstance(this@SimpleMapActivity).fromResource(R.drawable.ic_android))  
markerOptions.title= "Marker"  
markerOptions.snippet = "This is a Marker"  
mapmyIndiaMap?.addMarker(markerOptions)
~~~
### [Remove A Marker](#remove-a-marker)
#### Java
```java
mapmyIndiaMap.removeMarker(marker)
```
#### Kotlin
~~~kotlin
mapmyIndiaMap?.removeMarker(marker)
~~~
### [Customize A Marker](#customize-a-marker)
#### Java
```java
MarkerOptions markerOptions = new MarkerOptions().position(point).icon(IconFactory.getInstance(context).fromResource(R.drawable.ic_android));  
Marker marker = mapmyIndiaMap.addMarker(markerOptions);  
marker.setTitle("title");  
mapmyIndiaMap.setInfoWindowAdapter(new MapboxMap.InfoWindowAdapter() {  
  @Nullable  
  @Override
  public View getInfoWindow(@NonNull Marker marker) {  
     View view = getLayoutInflater().inflate(R.layout.layout, null);  
     TextView text = view.findViewById(R.id.text);  
     text.setText(marker.getTitle());  
     return view;  
  }  
});
```
#### Kotlin
~~~kotlin
val markerOptions: MarkerOptions = MarkerOptions().position(point).icon(IconFactory.getInstance(context).fromResource(R.drawable.ic_android))  
markerOptions.title= "Marker"    
mapmyIndiaMap?.addMarker(markerOptions)  
mapmyIndiaMap?.setInfoWindowAdapter {  
  val view: View? = getLayoutInflater().inflate(R.layout.layout, null)  
    val textView: TextView = view?.findViewById(R.id.text)!!  
    textView.text = it.title  
  return@setInfoWindowAdapter view  
}
~~~
### [Add A Polyline](#add-a-polyline)
##### Draw polyline on the map
#### Java
```java
mapmyIndiaMap.addPolyline(new PolylineOptions()  
  .addAll(points)//list of LatLng   
  .color(Color.parseColor("#3bb2d0"))  
  .width(2));
```
#### Kotlin
~~~kotlin
mapmyIndiaMap.addPolyline(PolylineOptions()  
        .addAll(points)  
        .color(Color.parseColor("#3bb2d0"))  
        .width(2f))
~~~
### [Remove Polyline](remove-polyline)
To remove a polyline from map:
#### Java
```java
mapmyIndiaMap.removePolyline(polyline);
```
#### Kotlin
~~~kotlin
mapmyIndiaMap.removePolyline(polyLine!!)
~~~
### [Add A Polygon](#add-a-polygon)

##### Draw a polygon on the map
#### Java
```java
mapmyIndiaMap.addPolygon(new PolygonOptions()  
    .addAll(polygon)//list of LatLng.  
    .fillColor(Color.parseColor("#3bb2d0")));
```
#### Kotlin
~~~kotlin
mapmyIndiaMap.addPolygon(PolygonOptions()  
        .addAll(polygon)  
        .fillColor(Color.parseColor("#3bb2d0")))
~~~
### [Remove Polygon](#remove-polygon)
To remove a polygon from map:
#### Java
```java
mapmyIndiaMap.removePolygon(polygon);
```
#### Kotlin
~~~kotlin
mapmyIndiaMap.removePolygon(polygon!!)
~~~
### [Show User Location](#show-user-location)

##### Show the current user location 

Implement LocationEngineListener and override it's method 
#### Java
```java
LocationComponentOptions options = LocationComponentOptions.builder(context)  
  .trackingGesturesManagement(true)  
  .accuracyColor(ContextCompat.getColor(this, R.color.colorAccent))  
  .build();  
// Get an instance of the component LocationComponent  
locationComponent = mapmyIndiaMap.getLocationComponent();  
// Activate with options  
locationComponent.activateLocationComponent(context, options);  
// Enable to make component visible  
locationComponent.setLocationComponentEnabled(true);  
locationEngine = locationComponent.getLocationEngine();  
  
locationEngine.addLocationEngineListener(this);  
// Set the component's camera mode  
locationComponent.setCameraMode(CameraMode.TRACKING);  
locationComponent.setRenderMode(RenderMode.COMPASS);

@Override  
public void onConnected() {  
  locationEngine.requestLocationUpdates();  
}  
  
@Override  
public void onLocationChanged(Location location) {  
  mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));  
}  
  
@Override  
protected void onResume() {  
  super.onResume();  
  if (locationEngine != null) {  
    locationEngine.removeLocationEngineListener(this);  
    locationEngine.addLocationEngineListener(this);  
  }  
}  
  
@Override  
protected void onPause() {  
  super.onPause();  
  if (locationEngine != null)  
    locationEngine.removeLocationEngineListener(this);  
  } 
} 
  
@Override  
protected void onStop() {  
  super.onStop();  
  if (locationEngine != null) {  
    locationEngine.removeLocationEngineListener(this);  
    locationEngine.removeLocationUpdates();  
  }  
}  
  
@Override  
protected void onDestroy() {  
  super.onDestroy();  
  if (locationEngine != null) {  
    locationEngine.deactivate();  
  }  
}
```

#### Kotlin
~~~kotlin
val options: LocationComponentOptions = LocationComponentOptions.builder(context)  
        .trackingGesturesManagement(true)  
        .accuracyColor(ContextCompat.getColor(this, R.color.colorAccent))  
        .build()  
locationComponent = mapmyIndiaMap.locationComponent  
locationComponent.activateLocationComponent(this, options)  
locationComponent.isLocationComponentEnabled = true  
locationEngine = locationComponent.locationEngine!!  
locationEngine.addLocationEngineListener(this)  
locationComponent.cameraMode = CameraMode.TRACKING  
locationComponent.renderMode = RenderMode.COMPASS

override fun onConnected() {  
    locationEngine?.requestLocationUpdates()  
}  
  
override fun onLocationChanged(location: Location) {  
   mapmyIndiaMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(  
            LatLng(location.latitude, location.longitude), 16.0))  
}

override fun onResume() {  
    super.onResume()  
    if (locationEngine != null) {  
        locationEngine?.removeLocationEngineListener(this);  
  locationEngine?.addLocationEngineListener(this);  
  }  
}

override fun onPause() {  
    super.onPause()  
    if (locationEngine != null)  
        locationEngine?.removeLocationEngineListener(this);  
}

override fun onStop() {  
    super.onStop()  
    if (locationEngine != null) {  
        locationEngine?.removeLocationEngineListener(this);  
  locationEngine?.removeLocationUpdates();  
  }  
}  

override fun onDestroy() {  
    super.onDestroy()  
    if (locationEngine != null) {  
        locationEngine?.deactivate();  
  }  
}
~~~

### [Dashed PolyLine](#dashed-polyline)
DashPolyline Plugin provides option to add or remove dash polyline.
 ##### [Add Dashed PolyLine](#add-dashed-polyline)
 #### Java
~~~java 
 DashedPolylinePlugin dashedPolylinePlugin = new DashedPolylinePlugin(mapmyIndiaMap, mapView);  
dashedPolylinePlugin.createPolyline(listOfLatLng);
~~~
#### Kotlin
~~~kotlin
val dashedPolylinePlugin = DashedPolylinePlugin(mapmyIndiaMap, map_view!!)  
dashedPolylinePlugin.createPolyline(listOfLatLng!!)
~~~
 ##### [Remove Dashed PolyLine](#remove-dashed-polyline)
  #### Java

~~~java 
dashedPolylinePlugin.clear();
~~~
#### Kotlin

~~~java 
dashedPolylinePlugin.clear();
~~~
### [Calculate distance between two points](#calculate-distance-between-points)
To calculate aerial distance between two points:
#### Java
~~~java
LatLng firstLatLng = new LatLng(28, 77);  
LatLng secondLatLng = new LatLng(28.67, 77.65);  
firstLatLng.distanceTo(secondLatLng);
~~~
#### Kotlin
~~~kotlin
val firstLatLng = LatLng(28.0, 77.0)
val secondLatLng = LatLng(28.67, 77.65)
firstLatLng.distanceTo(secondLatLng)
~~~

### [Proguard](#proguard)
```
# Retrofit 2 
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform 
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor 
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8 
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes *Annotation*,Signature 
# Retain declared checked exceptions for use by a Proxy instance. 
 -keepattributes Exceptions 
# For using GSON @Expose annotation
-keepattributes *Annotation* 
# Gson specific classes 
-dontwarn sun.misc.** 
-dontwarn okio.** 
-dontwarn okhttp3.**
-keep class retrofit.** 
-keep class retrofit.** { *; } 
-keepclasseswithmembers class * { 
 @retrofit.http.* <methods>;
}

-keep class com.mmi.services.account.** {
    <fields>;
    <methods>;
}
-keep class com.mmi.services.api.** {
    <fields>;
    <methods>;
}
-keep class com.mmi.services.utils.** {
    <fields>;
    <methods>;
}
```

## [REST APIs](#rest-apis)
For information on the REST API module, please read repository wiki [here](https://github.com/MapmyIndia/mapmyindia-maps-vectorSDK-android/wiki).

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

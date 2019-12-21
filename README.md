![MapmyIndia APIs](https://www.mapmyindia.com/api/img/mapmyindia-api.png)
# MapmyIndia Vector Map Android SDK

**Easy To Integrate Maps & Location APIs & SDKs For Web & Mobile Applications**

Powered with India's most comprehensive and robust mapping functionalities.
**Now Available**  for Srilanka, Nepal, Bhutan and Bangladesh

1. You can get your api key to be used in this document here: [https://www.mapmyindia.com/api/signup](https://www.mapmyindia.com/api/signup)

2. The sample code is provided to help you understand the basic functionality of MapmyIndia maps & REST APIs working on **Android** native development platform.  

## Getting Started

MapmyIndia Maps SDK for Android lets you easily add MapmyIndia Maps and web services to your own Android application. The SDK for Android supports API 14+. You can have a look at the map and features you will get in your own app by using the MapmyIndia Maps SDK for Android.

Through customized tiles, you can add different map layers to your application and add bunch of controls and gestures to enhance map usability thus creating potent map based solutions for your customers. The SDK handles downloading of map tiles and their display along with a bunch of controls and native gestures.

## API Usage

Your MapmyIndia Maps SDK usage needs a set of license keys (get them  [here](http://www.mapmyindia.com/api/signup)) and is governed by the API  [terms and conditions](http://www.mapmyindia.com/api/terms-&-conditions). As part of the terms and conditions,  **you cannot remove or hide the MapmyIndia logo and copyright information** in your project.

The allowed SDK hits are described on the user dashboard (http://www.mapmyindia.com/api/dashboard) page. Note that your usage is shared between platforms, so the API hits you make from a web application, Android app or an iOS app all add up to your allowed daily limit.

## Setup your project

Follow these steps to add the SDK to your project –

-   Create a new project in Android Studio
-   Import MapmyIndiaGLAndroidSDK_v{version}.aar file in your project.
-   Add Following dependencies for the implementation

```java
  
implementation 'com.jakewharton.timber:timber:4.5.0'
implementation 'com.google.code.gson:gson:2.8.0'
implementation 'com.squareup.okhttp3:okhttp:3.10.0'
implementation 'com.squareup.retrofit2:retrofit:2.4.0'
implementation 'com.squareup.retrofit2:converter-gson:2.4.0'
implementation 'com.squareup.retrofit2:converter-scalars:2.3.0'
implementation 'com.squareup.okhttp3:logging-interceptor:3.10.0'
implementation 'com.google.android.gms:play-services-location:15.0.1' (New)
implementation 'com.mapbox.mapboxsdk:mapbox-sdk-turf:3.1.0'
implementation 'com.mapbox.mapboxsdk:mapbox-android-gestures:0.3.0'
implementation 'com.mapbox.mapboxsdk:mapbox-sdk-geojson:3.3.0'
implementation 'com.mapbox.mapboxsdk:mapbox-android-core:0.2.1'(New)
implementation 'com.facebook.soloader:soloader:0.6.1'
```
- Add these permissions in your project
```java
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"   >
<uses-permission android:name="android.permission.INTERNET" >
```
### Add Java 8 Support to the project

 *add following lines in your app module's build.gradle*

```
compileOptions {
      sourceCompatibility 1.8
      targetCompatibility 1.8
  }
  ```
    
### Add your API keys to the SDK
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
```
*You cannot use the MapmyIndia Map Mobile SDK without these function calls. You will find your keys in your API Dashboard.*


## Add a MapmyIndia Map to your application
```java
<com.mapbox.mapboxsdk.maps.MapView
android:id="@+id/mapView" 
android:layout_width="match_parent"
android:layout_height="match_parent" 
/>
```
##### NOTE: All the lifecycle methods that need to be overridden:

```java
onCreate(); 
onStart(); 
onResume(); 
onPause(); 
onStop(); 
onSaveInstanceState(); 
onLowMemory(); 
onDestroy();
```
## Map Interactions

The MapmyIndia Maps Android SDK allows you to define interactions that you can activate on the map to enable gestures and click events. The following interactions are supported –

### Zoom Controls

The map supports the familiar two-finger pinch and zooms to change zoom level as well as double tap to zoom in. Set zoom to 4 for country level display and 18 for house number display. In this SDK the camera position plays an important role

And following operations can be performed using the CameraPosition

### Target

The target is single latitude and longitude coordinate that the camera centers it on. Changing the camera's target will move the camera to the inputted coordinates. The target is a LatLng object. The target coordinate is always  _at the center of the viewport_.

### Tilt

Tilt is the camera's angle from the nadir (directly facing the Earth) and uses unit degrees. The camera's minimum (default) tilt is 0 degrees, and the maximum tilt is 60. Tilt levels use six decimal point of precision, which enables you to restrict/set/lock a map's bearing with extreme precision.

The map camera tilt can also adjust by placing two fingertips on the map and moving both fingers up and down in parallel at the same time or

### Bearing

Bearing represents the direction that the camera is pointing in and measured in degrees  _clockwise from north_.

The camera's default bearing is 0 degrees (i.e. "true north") causing the map compass to hide until the camera bearing becomes a non-zero value. Bearing levels use six decimal point precision, which enables you to restrict/set/lock a map's bearing with extreme precision. In addition to programmatically adjusting the camera bearing, the user can place two fingertips on the map and rotate their fingers.

### Zoom

Zoom controls the scale of the map and consumes any value between 0 and 22. At zoom level 0, the viewport shows continents and other world features. A middle value of 11 will show city level details, and at a higher zoom level, the map will begin to show buildings and points of interest. The camera can zoom in the following ways:

-   Pinch motion two fingers to zoom in and out.
-   Quickly tap twice on the map with a single finger to zoom in.
-   Quickly tap twice on the map with a single finger and hold your finger down on the screen after the second tap.
-   Then slide the finger up to zoom out and down to zoom out.
```java
CameraPosition position = new CameraPosition.Builder() 
target(new LatLng(22.8978, 77.3245)) // Sets the new camera position
zoom(14) // Sets the zoom to level 14 
tilt(45) // Set the camera tilt to 45 degrees 
build();
```
##### We can pass this camera positions to following functions
```java
moveCamera() 
easeCamera() 
animateCamera()
```

## Map Events

##### **The SDK allows you to listen to certain events on the map. It sets a callback that is invoked when camera movement has started.**
```java
mapboxMap.setOnCameraMoveStartedListener(new MapboxMap.OnCameraMoveStartedListener()
{ 
private final String[] REASONS = {"REASON_API_GESTURE", "REASON_DEVELOPER_ANIMATION", "REASON_API_ANIMATION"
  }; 
@Override 
public void onCameraMoveStarted(int reason) { 
String string = String.format(Locale.US, 
"OnCameraMoveStarted: %s", REASONS[reason - 1])
Toast.makeText(MainActivity.this, string, Toast.LENGTH_LONG).show();
}
}); 
```
##### It sets a callback that is invoked when camera movement was cancelled.
```json
mapboxMap.setOnCameraMoveCancelListener(new
MapboxMap.OnCameraMoveCanceledListener() 
{ 
@Override
public void onCameraMoveCanceled() { 
Toast.makeText(MainActivity.this, "onCameraMoveCanceled", Toast.LENGTH_LONG).show(); 
       } 
 });
```
##### It sets a callback that is invoked when camera movement has ended.
```json
  
mapboxMap.setOnCameraIdleListener(new MapboxMap.OnCameraIdleListener()
 { 
@Override
public void onCameraIdle() { 
Toast.makeText(MainActivity.this, "onCameraIdle", Toast.LENGTH_LONG).show(); 
        } 
 });
```
### Map Click/Long Press

If you want to respond to a user tapping on a point on the map, you can use a MapEventsOverlay which you need to add on the map as an Overlay.

It sets a callback that's invoked when the user clicks on the map view.
```json
mapboxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() 
  { 
@Override 
public void onMapClick(@NonNull LatLng point) { 
String string = String.format(Locale.US, "User clicked at: %s", point.toString()) 
Toast.makeText(MainActivity.this, string, Toast.LENGTH_LONG).show();
   } 
   });
```
###### Sets a callback that's invoked when the user long clicks on the map view.
```json
mapboxMap.setOnCameraIdleListener(new MapboxMap.OnCameraIdleListener()
  { 
@Override 
public void onCameraIdle() { 
Toast.makeText(MainActivity.this, "onCameraIdle", Toast.LENGTH_LONG).show(); 
       } 
 });
```
### Map Click/Long Press

##### If you want to respond to a user tapping on a point on the map, you can use a MapEventsOverlay which you need to add on the map as an Overlay.

##### It sets a callback that's invoked when the user clicks on the map view.
```json
mapboxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() { 
@Override 
public void onMapClick(@NonNull LatLng point) { 
String string = String.format(Locale.US, "User clicked at: %s", point.toString()) 
Toast.makeText(MainActivity.this, string, Toast.LENGTH_LONG).show(); 
 } 
 });
```
##### Sets a callback that's invoked when the user long clicks on the map view.
```json
mapboxMap.setOnMapLongClickListener(new MapboxMap.OnMapLongClickListener() { 
@Override 
public void onMapLongClick(@NonNull LatLng point) { 
String string = String.format(Locale.US, "User clicked at: %s", point.toString()) 
Toast.makeText(MainActivity.this, string, Toast.LENGTH_LONG).show(); 
 } 
 });
```
## Map Overlays

### Add A Marker

##### Add markers to the map by following these steps –
```json
MarkerOptions markerOptions = new MarkerOptions().position(point).icon(icon); 
Marker marker1 = map.addMarker(markerOptions); 
markerOptions.setTitle(""); 
markerOptions.setSnippet("");
```
### Remove A Marker
```json
mapboxMap.removeMarker()
```
### Customize A Marker
```json
MarkerOptions markerOptions = new   MarkerOptions().position(point).icon(icon); 
Marker marker = mapBoxMap.addMarker(markerOptions); 
String tittle = “abc”; 
marker.setTitle(tittle);
mapboxMap.setInfoWindowAdapter(new MapboxMap.InfoWindowAdapter() { 
@Nullable 
@Override 
public View getInfoWindow(@NonNull Marker marker) { 
View view = (context).getLayoutInflater() 
.inflate(R.layout.layout, null); 
TextView text = (TextView)view.findViewById(R.id.text); 
text.setText(marker.getTitle()); 
return view; 
 } 
 });
```
### Add A Polyline
##### Draw polyline on the map
```json
mapboxMap.addPolyline(new PolylineOptions() 
.addAll(points)//list of LatLng 
.color(Color.parseColor("#3bb2d0")) 
.width(2));
```
### Add A Polygon

##### Draw a polygon on the map
```json
mapboxMap.addPolygon(new PolygonOptions() 
.addAll(polygon)//list of LatLng.fillColor(Color.parseColor("#3bb2d0")));
```
### Show User Location

##### Show the current user location
```json
LocationComponentOptions options = LocationComponentOptions.builder(this)
.trackingGesturesManagement(true)
.accuracyColor(ContextCompat.getColor(this, R.color.colorAccent))
.build();
// Get an instance of the component LocationComponent 
locationComponent = mapboxMap.getLocationComponent(); 
// Activate with options 
locationComponent.activateLocationComponent(this, options); 
// Enable to make component visible 
locationComponent.setLocationComponentEnabled(true); 
locationEngine = locationComponent.getLocationEngine(); '
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
mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom( 
new LatLng(location.getLatitude(), location.getLongitude()), 16)); 
locationEngine.removeLocationEngineListener(this); 
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
```
### Proguard
```json
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

## REST APIs
For information on the REST API module, please read repository wiki [here](https://github.com/MapmyIndia/mapmyindia-maps-vectorSDK-android-sample-withREST-beta/wiki).

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


> © Copyright 2019. CE Info Systems Pvt. Ltd. All Rights Reserved. | [Terms & Conditions](http://www.mapmyindia.com/api/terms-&-conditions)
> mapbox-gl-native copyright (c) 2014-2019 Mapbox.

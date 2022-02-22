
![MapmyIndia APIs](https://www.mapmyindia.com/api/img/mapmyindia-api.png)

# MapmyIndiaDrivingRangePlugin for Android

## [Introduction](#Introduction)

The MapmyIndiaDrivingRangePlugin allows you to plot driving range area to drive based on time or distance on MapmyIndia's vector map component.

The plugin offers the following basic functionalities:

- Get and Plot driving range as polygon on map.
- Update driving range on map.
- Clear already plotted driving range on map.

**This can be done by following simple steps.**


## [Implementation](#Implementation)

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
implementation 'com.mapmyindia.sdk:driving-range-plugin:0.0.1'
~~~	


## [Initialise Plugin](#Initialise-Plugin)

### [MapmyIndiaDrivingRangePlugin](#MapmyIndiaDrivingRangePlugin)

`MapmyIndiaDrivingRangePlugin` is the main class which is required to be initialized to use different functionalities of plugin. 

It allows you to plot driving range area based on time or distance.
#### Java
~~~java
MapmyIndiaDrivingRangePlugin mapmyIndiaDrivingRangePlugin = new MapmyIndiaDrivingRangePlugin(mapView, mMapmyIndiaMap);
~~~
#### Kotlin
```kotlin
var mapmyIndiaDrivingRangePlugin = MapmyIndiaDrivingRangePlugin(mapView, mMapmyIndiaMap)
```

## [Plot Driving Range](#Plot-Driving-Range)

A function `drawDrivingRange` of instance of `MapmyIndiaDrivingRangePlugin` will be used to get driving range and plot on map. This function will accept an instance of `MapmyIndiaDrivingRangeOption` as request to get driving range.

Below is code for reference:
#### Java
~~~java
MapmyIndiaDrivingRangeContour rangeContour = MapmyIndiaDrivingRangeContour.builder()  
        .value(50)  
        .color("ff0000")  
        .build();  
List<MapmyIndiaDrivingRangeContour> list = new ArrayList<>();  
list.add(rangeContour);  
MapmyIndiaDrivingRangeOption option = MapmyIndiaDrivingRangeOption.builder()  
        .location(location)  
        .rangeTypeInfo(  
                MapmyIndiaDrivingRangeTypeInfo.builder()  
                        .rangeType(DrivingRangeCriteria.RANGE_TYPE_TIME)  
                        .contours(list)  
                        .build()  
        ).build(); 
mapmyIndiaDrivingRangePlugin.drawDrivingRange(option)
           
           //Or with callback
mapmyIndiaDrivingRangePlugin.drawDrivingRange(option, new IDrivingRangeListener() {  
    @Override  
  public void onSuccess() {  
          //Success
    }  
  
    @Override  
  public void onFailure(int code, @NonNull String message) {  
		  // Failure
    }  
});
~~~
#### Kotlin
```kotlin
val range = MapmyIndiaDrivingRangeContour.builder()  
    .value(50)  
    .color("ff0000")  
    .build();  
val option = MapmyIndiaDrivingRangeOption.builder()  
    .location(location)  
    .rangeTypeInfo(  
        MapmyIndiaDrivingRangeTypeInfo.builder()  
            .rangeType(DrivingRangeCriteria.RANGE_TYPE_TIME)  
            .contours(listOf(range))  
            .build()  
    ).build()
mapmyIndiaDrivingRangePlugin?.drawDrivingRange(option)

                 //OR with callback
mapmyIndiaDrivingRangePlugin?.drawDrivingRange(option, object :  
    IDrivingRangeListener {  
    override fun onSuccess() {  
        Toast.makeText(this@DrivingRangeActivity, "Success", Toast.LENGTH_SHORT).show()  
    }  
  
    override fun onFailure(code: Int, message: String) {  
        Toast.makeText(this@DrivingRangeActivity, message, Toast.LENGTH_SHORT).show()  
    }  
})
```
### [Request Parameters](#Request-Parameters)

**MapmyIndiaDrivingRangeOptions**

It is a structure that specifies the criteria for request for getting polygons as range to drive based on time or distance.

It contains following properties.

- `name[String]`: It is used for name of the driving range request. If name is specified, the name is returned with the response.

- `location[Point](Mandatory)`: It is center point for range area that will surrounds the roads which can be reached from this point in specified time range(s).

- `drivingProfile[String]`:  Driving profile for routing engine. Default value is `auto`.

- `speedTypeInfo[MapmyIndiaDrivingRangeSpeed]`: It is used to calculate the polygon.  Default value is `MapmyIndiaDrivingRangeSpeed.optimal()`'

- `rangeTypeInfo[MapmyIndiaDrivingRangeTypeInfo](Mandatory)`:- It is used to specify the type of range which is used to calculate the polygon.

- `isForPolygons[Boolean]`:  It is used to determine whether to return geojson polygons or linestrings. The default is true.

- `showLocations[Boolean]`: It indicating whether the input locations should be returned as MultiPoint features. The default is false. It returns
  -  one point feature for the exact input coordinates
  -  one point feature for the coordinates of the network node it snapped to.

- `denoise[Float]`: A floating point value from 0 to 1 (default of 1) which will be used to remove smaller contours. Default value is 0.5.

- `generalize[Float]`: A floating point value in meters used as the tolerance for Douglas-Peucker generalization. Default value is 1.2


**MapmyIndiaDrivingRangeTypeInfo**

It contains following properties.

- `rangeType[String]`: It specify the type of range which is used to calculate the polygon. Possible values are:
  - DrivingRangeCriteria.RANGE_TYPE_TIME (Default): It means it takes time in min.
  - DrivingRangeCriteria.RANGE_TYPE_DISTANCE: It means it takes distance in Km.

- `contours[List<MapmyIndiaDrivingRangeContour>]`: Each contour object is combination of value and colorHexString.

**MapmyIndiaDrivingRangeContour**

It contains following properties.

- `value(Integer)`: value taken as time or distance defined in instance of `MapmyIndiaDrivingRangeRangeTypeInfo`.
  - If type of range is time in minutes(default = 15, maximum = 80)
  - If type of range is distance in kilometers(default = 1, maximum = 80)

- `color[String]`: It specify the color for the output of the contour. Pass a hex value, such as `ff0000` for red.


**MapmyIndiaDrivingRangeOptimalSpeed**

- For optimal Speed type.
~~~java
MapmyIndiaDrivingRangeSpeed.optimal();
~~~

- For predictive speed type with current timestamp
~~~java
MapmyIndiaDrivingRangeSpeed.predictiveSpeedFromCurrentTime();
~~~

- For predictive speed type with custom timestamp:
~~~java
MapmyIndiaDrivingRangeSpeed.predictiveSpeedFromCustomTime(time)
~~~


## [Additional Features](#Additional-Features)

We have provided some features with the plugin which will help to further enhance the experience.

### [Auto Fit Bounds](#Auto-Fit-Bounds)

To fit the bounds of the map for plotted driving range, user has to set camera.
#### Java
~~~java
mapmyIndiaDrivingRangePlugin.isSetMapBoundForDrivingRange(false);
~~~
#### Kotlin
```kotlin
mapmyIndiaDrivingRangePlugin?.isSetMapBoundForDrivingRange(false)
```
On setting it `true`, plugin will set camera to fit bound. It's default value is `true`.


### [Clear Driving Range](#Clear-Driving-Range)

Plotted driving range on Map can be removed by calling function `clearDrivingRangeFromMap`.

Refer to the code below:
#### Java
~~~java
mapmyIndiaDrivingRangePlugin.clearDrivingRangeFromMap();
~~~

#### Kotlin
```kotlin
mapmyIndiaDrivingRangePlugin?.clearDrivingRangeFromMap()
```



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


> © Copyright 2022. CE Info Systems Ltd. All rights reserved. | [Terms & Conditions](http://www.mapmyindia.com/api/terms-&-conditions)


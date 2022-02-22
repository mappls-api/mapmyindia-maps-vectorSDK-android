
![MapmyIndia APIs](https://www.mapmyindia.com/api/img/mapmyindia-api.png)

# MapmyIndia Place Picker Widget

##  Introduction
MapmyIndia Place Picker Plugin can be used to choose a specific location.


## Add the dependency
Add below dependency in app level build.gradle file
~~~groovy	
implementation 'com.mapmyindia.sdk:place-widget:1.4.0'	
~~~	

## Add Place Picker

To add the place picker widget
####  java

~~~java
   Intent intent = new PlacePicker.IntentBuilder()  
                .placeOptions(placePickerOptions).build(this);  
     startActivityForResult(intent, 101);
~~~

####  Kotlin

~~~kotlin
  val intent = PlacePicker.IntentBuilder()  
            .placeOptions(placePickerOptions).build(this)  

  startActivityForResult(intent, 101)
~~~
You can use `PlacePickerOptions` to set the following properties:
1. `toolbarColor(Integer)`:  To set the toolbar color of place widget
2. `startingBounds(LatLngBounds)`: To open a map in a bound
3. `statingCameraPosition(CameraPosition)`: To open a map that sets in camera poition you can set zoom, centre, bearing etc.,
4. `includeDeviceLocationButton(Boolean)`: To enable/ disable current location functionality
5. `marker(Integer)`: To change the marker image which is visible in the centre of a map
6. `mapMaxZoom(Double)`: To set maximum zoom level of the map
7. `mapMinZoom(Double)`: To set minimum zoom level of the map
8. `includeSearch(Boolean)`: To provide opions for search locations
9. `searchPlaceOption(PlaceOptions)`: To set all the properties of search widget
   ​

##  Get Result

To get the pick place:
####  java

~~~java
@Override  
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {  
       super.onActivityResult(requestCode, resultCode, data);  
       if (requestCode == 101 && resultCode == RESULT_OK) {  
        Place place = PlacePicker.getPlace(data);  
  }  
}
~~~

####  Kotlin

~~~kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {  
      super.onActivityResult(requestCode, resultCode, data)  
      if(requestCode == 101 && resultCode == Activity.RESULT_OK) {  
            val place: Place? = PlacePicker.getPlace(data!!)            
    }  
}
~~~

![Email](https://www.google.com/a/cpanel/mapmyindia.co.in/images/logo.gif?service=google_gsuite)

Email us at [apisupport@mapmyindia.com](mailto:apisupport@mapmyindia.com)

​

![](https://www.mapmyindia.com/api/img/icons/stack-overflow.png)

[Stack  Overflow](https://stackoverflow.com/questions/tagged/mapmyindia-api)

Ask a question under the mapmyindia-api

​

![](https://www.mapmyindia.com/api/img/icons/support.png)

[Support](https://www.mapmyindia.com/api/index.php#f_cont)

Need support? contact us!

​

![](https://www.mapmyindia.com/api/img/icons/blog.png)

[Blog](http://www.mapmyindia.com/blog/)

Read about the latest updates & customer stories

​

​

> © Copyright 2021. CE Info Systems Ltd. All Rights Reserved. | [Terms & Conditions](http://www.mapmyindia.com/api/terms-&-conditions).
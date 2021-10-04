![MapmyIndia APIs](https://www.mapmyindia.com/api/img/mapmyindia-api.png)

##  MapmyIndia  Scalebar  Plugin

### Introduction
MapmyIndia Scalebar  Plugin can be used to add a scale bar on map view to determine distance based on zoom level.

Initalize ScaleBarPlugin in  onMapReady()  

## Add the dependency
Add below dependency in app level build.gradle file
~~~groovy	
implementation 'com.mapmyindia.sdk:scalebar-plugin:0.1.0'	
~~~	

####  java

~~~java
   ScaleBarPlugin scaleBarPlugin = new ScaleBarPlugin(mapView, mapmyIndiaMap);  
   ScaleBarOptions scaleBarOptions = new ScaleBarOptions(this)  
        .setTextColor(android.R.color.black)  
        .setTextSize(40f)  
        .setBarHeight(5f)
        
  scaleBarPlugin.create(scaleBarOptions);
~~~

####  Kotlin

~~~kotlin

  val scaleBarPlugin = ScaleBarPlugin(mapView, mapmyIndiaMap!!)  
  val scalebarOptions = ScaleBarOptions(this)  
        .setTextColor(android.R.color.black)  
        .setTextSize(40f)  
        .setBarHeight(5f)
        
  scaleBarPlugin.create(scalebarOptions)
~~~

You can use `ScaleBarOptions` to set the following properties:

*  `setPrimaryColor(Integer)`  :  To  set  primary  color  of  scalebar
*  `setSecondaryColor(Integer)`:  To  set  secondary  color  of  scalebar
*  `setTextColor(Integer)`:  To  set  the  text  color  of  text  in  scalebar
*  `setTextSize(float)`:  To  set  the  text  size  in  scalebar
*  `setBarHeight(float)`:  To  set  the  height  of  scalebar
*  `setBorderWidth(float)`:  To  set  border  width  of  scalebar
*  `setRefreshInterval(Integer)`:  To  set  refresh  interval  of  scalebar
*  `setMarginTop(Integer)`:  To  set  top  margin  of  scalebar
*  `setMarginLeft(Integer)`:  To  set  left  margin  of  scalebar
*  `setTextBarMargin(float)`:  To  set  margin  between  text  and  scalebar
*  `setMaxWidthRatio(float)`  :  To  set  width  ratio  of  scalebar
*  `setShowTextBorder(Boolean)`:  To  show  text  border  or  not
*  `setMetricUnit(Boolean)  `:  To  use  metric  unit
*  `setTextBorderWidth(float)`:  To  set  border  width  of  text


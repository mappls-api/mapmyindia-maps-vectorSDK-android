

![MapmyIndia APIs](https://www.mapmyindia.com/api/img/mapmyindia-api.png)

# MapmyIndia GeoAnalytics

## Introduction

This API gives the users the power to display, style, and edit the data which is archived in  
MapmyIndia’s Database and overlay it on the user created maps.
- Enables data selection, visualization, queries & styling with rich base map available at various level of granularity
- Build maps quickly and easily without coordinates, using country, pin codes or simply place names
- Drill and analyze different type of map layers such as districts, pincode, villages, city etc at Pan India level or restricted to limited bound /Area of interest
- Allows user to set their own styling parameters such as label color scheme, geometry color schemes, opacity, width, stroke etc.
- Allows to create rich thematic visuals using combination of value ranges

This API gets the layer specified which is stored in MapmyIndia’s Database and gives a WMS layer as an output with any filters or styles specified by the user.

## Add the dependency
Add below dependency in app level build.gradle file
~~~groovy
implementation 'com.mapmyindia.sdk:geoanalytics-plugin:0.1.0'
~~~

## Available Geoanalytics Layer
1. `MapmyIndiaGeoAnalyticsType.STATE`
2. `MapmyIndiaGeoAnalyticsType.DISTRICT`
3. `MapmyIndiaGeoAnalyticsType.SUB_DISTRICT`
4. `MapmyIndiaGeoAnalyticsType.WARD`
5. `MapmyIndiaGeoAnalyticsType.LOCALITY`
6. `MapmyIndiaGeoAnalyticsType.PANCHAYAT`
7. `MapmyIndiaGeoAnalyticsType.BLOCK`
8. `MapmyIndiaGeoAnalyticsType.PINCODE`
9. `MapmyIndiaGeoAnalyticsType.TOWN`
10. `MapmyIndiaGeoAnalyticsType.CITY `
11. `MapmyIndiaGeoAnalyticsType.VILLAGE`
12. `MapmyIndiaGeoAnalyticsType.SUB_LOCALITY`
13. `MapmyIndiaGeoAnalyticsType.SUB_SUB_LOCALITY`

#### To get the detailed list of available layer's and attribute's names, please use the [Listing API](https://github.com/MapmyIndia/mapmyindia-maps-vectorSDK-android/wiki/MapmyIndia-GeoAnalytics#mapmyindia-geoanalytics-listing-api) available here.

Now that you’re all caught up with the features, let's get down right to them and look at how you can integrate our Geoanlytics API to add data on your map in few simple steps.



## Step-1 Initialize GeoAnalytics Manager
#### Java
~~~java  
MapmyIndiaGeoAnalyticsPlugin geoAnalyticsPlugin = new MapmyIndiaGeoAnalyticsPlugin(mapView, mapmyIndiaMap);  
~~~  
#### Kotlin
~~~kotlin  
val geoAnalyticsPlugin = MapmyIndiaGeoAnalyticsPlugin(mapView, mapmyIndiaMap)  
~~~  
## Step-2 Show GeoAnalytics layer

#### Java
~~~java  
geoAnalyticsPlugin.showGeoAnalytics(MapmyIndiaGeoAnalyticsType.STATE, MapmyIndiaGeoAnalyticsRequest.builder().attribute("t_p").query(">0").geoboundType("stt_nme").geoBound("HARYANA", "KERALA")  
        .propertyNames("stt_nme", "stt_id").style(new GeoAnalyticsAppearanceOption().fillColor("42a5f4").fillOpacity(0.5).strokeColor("000000").labelSize(10).strokeWidth(0).labelColor("000000")).build());  
~~~  
#### Kotlin
~~~kotlin  
geoAnalyticsPlugin.showGeoAnalytics(MapmyIndiaGeoAnalyticsType.STATE, MapmyIndiaGeoAnalyticsRequest.builder().attribute("t_p").query(">0").geoboundType("stt_nme").geoBound("HARYANA", "KERALA")  
        .propertyNames("stt_nme", "stt_id").style(new GeoAnalyticsAppearanceOption().fillColor("42a5f4").fillOpacity(0.5).strokeColor("000000").labelSize(10).strokeWidth(0).labelColor("000000")).build()))  
~~~  



## Step-3 MapmyIndiaGeoAnalyticsRequest
1. `geoboundType(String...)`**(Mandatory)**: The type of geographical extents on which data would be bound, i.e. the parent layer types (India, State, District, Sub District, etc.)

2. `geoBound(String...)`**(Mandatory)**: The values of the extent depending on the GeoBoundType.

3. `propertyNames(String...)`**(Mandatory)**:  Attribute names that user wants to show in info window or in click callback
4. `attribute(String)`: The name of Attribute to filter the output, such as population or number of households.

5.  `query(String)`: A string containing an operator and a value which would be applied to the attribute filter. Applicable queries include < (Less than) OR > (Greater then) OR <> (Between). Example 1: ‘> 10000’ Example 2: BETWEEN value1 AND value2  
    ***Note**: Query is mandatory if Attribute is given.
6. `style(GeoAnalyticsStyleOption)`: Options to set the style of Layer

   ### GeoAnalyticsAppearanceOption
    1. `labelColor(String)`: Value of the color of label.
    2. `labelSize(Integer)`:  Size of labels to be displayed.
    3. `fillColor(String)`: Value of the polygon/point color. e.g., fillColor("42a5f4")
    4. `pointSize(Integer)`: Size of point data. (Applicable for Point geometry only)
    5. `strokeColor(String)`: Value of the color of the label.
    6. `strokeWidth(Double)`: Width of the polygon border.
    7. `fillOpacity(Double)`: Opacity value of whole layer. (Any range between 0 & 1) ***Note**: All starred parameters are mandatory if Style is given.

That's All!

## Other Methods Available are:

## 1. Remove Geoanalytics Layer
#### Java
~~~java  
geoAnalyticsPlugin.removeGeoAnalytics(MapmyIndiaGeoAnalyticsType.STATE);  
~~~  
#### Kotlin
~~~kotlin  
geoAnalyticsPlugin.removeGeoAnalytics(MapmyIndiaGeoAnalyticsType.STATE)  
~~~  
## 2. GeoAnalytics Layer Click
~~~java  
geoAnalyticsPlugin.setGeoAnalyticsCallback(new MapmyIndiaGeoAnalyticsCallbcak() {  
    @Override  
  public void getSelectedFeatures(List<MapmyIndiaGeoAnalyticsDetail> result) {  
  
    }  
});  
~~~  
## 3. Custom Info Window
~~~java  
geoAnalyticsPlugin.setCustomGeoAnalyticsInfoWindowAdapter(new CustomGeoAnalyticsInfoWindowAdapter() {  
    @Override  
  public View setCustomInfoWindow(List<MapmyIndiaGeoAnalyticsDetail> result) {  
        return view;  
  }  
});  
~~~  
# MapmyIndia GeoAnalytics Listing API

Geo-Analytics API is an API set that gives the users the power to display & style the data which is archived in MapmyIndia’s pan-India database and overlay it on MapmyIndia’s Interactive Maps JS API for web.

Listing API is an API that gives the users information on the different layers & attributes available within Geo-Analytics Core APIs. This API acts as an assisting API to quickly get the necessary details that are required to accurately fetch the required overlays from the core Geo-Analytics APIs.

It that provide list of attributes along with unique ID. User can get bounding box of the required feature/area as well.

~~~java  
MapmyIndiaGeoAnalyticsList mapmyIndiaGeoAnalyticsList = MapmyIndiaGeoAnalyticsList.builder()  
        .api("state")  
        .attributes("b_box")  
        .geoBound(geobound)  
        .geoBoundType(geoboundType)  
        .build();
MapmyIndiaGeoAnalyticsListManager.newInstance(mapmyIndiaGeoAnalyticsList).call(new OnResponseCallback<GeoAnalyticsListResponse>() {  
    @Override  
  public void onSuccess(GeoAnalyticsListResponse geoAnalyticsListResponse) {
  //Handle response
  } 
    
    @Override  
    public void onError(int i, String s) {  
  
    }  
});
~~~  
### Request Parameters
1. `api(String)`: api layer name (such as state, district, subdistrict, village, pincode etc)
2. `geoBoundType(String)`: Single valued parent type, for example: stt_nme, dst_nme, sdb_nme etc.  
   **Note**: For parent type reference, contact [apisupport@mapmyindia.com](mailto:apisupport@mapmyindia.com)
3. `geoBound(String...)`: child values, for example: Haryana, Maharashtra, Goa etc
4. `attributes(String...)`: field name/bounding Box requested w.r.t api (api) & parent type (geo_bound_type). Bounding box can be requested as "b_box" variable.

### Response Parameters
1. `responseCode(Integer)`: The response code of the operation. The 400 series is for client side (application end) error while 500 series is for server side (MapmyIndia) error, 200 series is for success.
2. `version(String)`: The version of the API you’re connected to.
3. `totalFeatureCount(Integer)`: total number of features in the request.
4. `results(GeoAnalyticsListResult)`

#### GeoAnalyticsListResult
1. `apiName(String)`: as requested
2. `attribute(String)`: as requested
3. `getAttrValues(List<GeoAnalyticsValue>)`

#### GeoAnalyticsValue
1. `geoBound(String)`: child value
2. `getAttrValues(List<Map<String, Object>>)`: list of names/b_box requested


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


> © Copyright 2020. CE Info Systems Pvt. Ltd. All Rights Reserved. | [Terms & Conditions](http://www.mapmyindia.com/api/terms-&-conditions)
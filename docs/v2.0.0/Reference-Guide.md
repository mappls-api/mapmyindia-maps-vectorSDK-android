
﻿![MapmyIndia APIs](https://www.mapmyindia.com/api/img/mapmyindia-api.png)

# Refrence Guide to update SDK from v6+ to v7.0.0
## Map SDK
### Change Imports
1. Change imports `com.mapbox.mapboxsdk.*` to `com.mapmyindia.sdk.maps.*`
2. Change imports `com.mapbox.mapboxsdk.maps.*` to `com.mapmyindia.sdk.maps.*`
3. Change imports `com.mapbox.geojson.*` to `com.mapmyindia.sdk.geojson.*`
4. Change imports `com.mapbox.turf.*` to `com.mapmyindia.sdk.turf.*`
5. Change imports `com.mapbox.android.gestures.*` to `com.mapmyindia.sdk.gestures.*`

### Change Classes
1. Change class `MapboxMap` to `MapmyIndiaMap`

### Change in XML
#### Old SDK
~~~xml
<com.mapbox.mapboxsdk.maps.MapView  
  android:id="@+id/map_view"  
  android:layout_width="match_parent"  
  android:layout_height="match_parent"/>
~~~

#### New SDK
~~~xml
<com.mapmyindia.sdk.maps.MapView  
  android:id="@+id/map_view"  
  android:layout_width="match_parent"  
  android:layout_height="match_parent"/>
~~~

**Change all attributes from mapbox_ to mapmyindia_maps_**

### Set Camera using ELoc
#### Old SDK
~~~java
mapmyIndiaMap.moveCamera("MMI000", 14);
mapmyIndiaMap.easeCamera("MMI000", 14);
mapmyIndiaMap.animateCamera("MMI000", 14);
~~~
#### New SDK
~~~java
mapmyIndiaMap.moveCamera(CameraELocUpdateFactory.newELocZoom("MMI000", 14));
mapmyIndiaMap.easeCamera(CameraELocUpdateFactory.newELocZoom("MMI000", 14));
mapmyIndiaMap.animateCamera(CameraELocUpdateFactory.newELocZoom("MMI000", 14));
~~~
### Set Camera to Particular Eloc Bounds
#### Old SDK
~~~java
mapmyIndiaMap.moveCamera(eLocList, 10, 10, 10, 10);
mapmyIndiaMap.easeCamera(eLocList, 10, 10, 10, 10);
mapmyIndiaMap.animateCamera(eLocList, 10, 10, 10, 10);
~~~
#### New SDK
~~~java
mapmyIndiaMap.moveCamera(CameraELocUpdateFactory.newELocBounds(eLocs, 10 , 100, 10, 10));
mapmyIndiaMap.easeCamera(CameraELocUpdateFactory.newELocBounds(eLocs, 10 , 100, 10, 10));
mapmyIndiaMap.animateCamera(CameraELocUpdateFactory.newELocBounds(eLocs, 10 , 100, 10, 10));
~~~
### Map Click/Long click
#### Old SDK
~~~java
//Map click
mapmyIndiaMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {  
  @Override  
  public void onMapClick(@NonNull LatLng point) {  
    
  }  
});
//Map long click
mapmyIndiaMap.addOnMapLongClickListener(new MapboxMap.OnMapLongClickListener() {  
  @Override  
  public void onMapLongClick(@NonNull LatLng point) {  
    
  }  
});
~~~
#### New SDK
~~~java
//Map click
mapmyIndiaMap.addOnMapClickListener(new MapmyIndiaMap.OnMapClickListener() {  
    @Override  
  public boolean onMapClick(@NonNull LatLng latLng) {  
    return false;  
 }  
});
//Map long click
mapmyIndiaMap.addOnMapLongClickListener(new MapmyIndiaMap.OnMapLongClickListener() {  
    @Override  
  public boolean onMapLongClick(@NonNull LatLng latLng) {   
        return false;  
  }  
});
~~~

### Current Location
#### Old SDK
~~~java
//Current Location Activation 
LocationComponentOptions options = LocationComponentOptions.builder(this)
                .foregroundDrawable(R.drawable.location_pointer)
                .build();
// Get an instance of the component LocationComponent
locationComponent = mapmyIndiaMaps.getLocationComponent();
// Activate with options
locationComponent.activateLocationComponent(this, options);
//LocationChange Listener
LocationEngineListener listener = new LocationEngineListener() {
	  
@Override  
public void onConnected() {  
  
}  
  
@Override  
public void onLocationChanged(Location location) {  
   
	}
};
//Add Location Change listener
locationEngine.addLocationEngineListener(this);
//Remove Location Change listener
locationEngine.removeLocationEngineListener(this);
//Request Location Update
locationEngine.requestLocationUpdates();
//Remove location update
locationEngine.removeLocationUpdates();
~~~
#### New SDK
~~~java
//Current Location Activation 
LocationComponentOptions options = LocationComponentOptions.builder(this) 
                .foregroundDrawable(R.drawable.location_pointer)  
                .build();  
// Get an instance of the component LocationComponent  
  locationComponent = mapmyIndiaMap.getLocationComponent();  
  LocationComponentActivationOptions locationComponentActivationOptions = LocationComponentActivationOptions.builder(this, style)  
                .locationComponentOptions(options)  
                .build();  
// Activate with options  
locationComponent.activateLocationComponent(locationComponentActivationOptions);
//LocationChange Listener
LocationEngineCallback<LocationEngineResult> locationEngineCallback = new LocationEngineCallback<LocationEngineResult>() {  
    @Override  
  public void onSuccess(LocationEngineResult locationEngineResult) {  
        if(locationEngineResult.getLastLocation() != null) {  
            Location location = locationEngineResult.getLastLocation();    
        }  
    }  
  
    @Override  
  public void onFailure(@NonNull Exception e) {  
  
    }  
};
LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)  
        .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)  
        .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();  
 ////Request Location Update & add location change callback
locationEngine.requestLocationUpdates(request, locationEngineCallback, getMainLooper());
//Remove location update & callback
locationEngine.removeLocationUpdates(locationEngineCallback);
~~~


## Rest API
### AutoSuggest
#### Old SDK
~~~java
MapmyIndiaAutoSuggest.builder()  
        .query(searchText)  
        .build()  
        .enqueueCall(new Callback<AutoSuggestAtlasResponse>() {  
            @Override  
            public void onResponse(@NonNull Call<AutoSuggestAtlasResponse> call, @NonNull Response<AutoSuggestAtlasResponse> response) {  
                //handle response 
            }  
  
            @Override  
            public void onFailure(@NonNull Call<AutoSuggestAtlasResponse> call, @NonNull Throwable t) {  
                t.printStackTrace();  
            }  
        });
~~~
#### New SDK
~~~java
MapmyIndiaAutoSuggest mapmyIndiaAutoSuggest = MapmyIndiaAutoSuggest.builder()  
        .query(searchString)  
        .build();  
MapmyIndiaAutosuggestManager.newInstance(mapmyIndiaAutoSuggest).call(new OnResponseCallback<AutoSuggestAtlasResponse>() {  
    @Override  
  public void onSuccess(AutoSuggestAtlasResponse autoSuggestAtlasResponse) {  
          //handle response 
    }  
  
    @Override  
  public void onError(int i, String s) {  
  
    }  
});
~~~
### Geocoding API
#### Old SDK
~~~java
MapmyIndiaGeoCoding.builder()  
        .setAddress("Delhi")  
        .build()  
        .enqueueCall(new Callback<GeoCodeResponse>() {  
            @Override  
            public void onResponse(Call<GeoCodeResponse> call, Response<GeoCodeResponse> response) {  
                //handle response 
            }     
  
            @Override  
            public void onFailure(Call<GeoCodeResponse> call, Throwable t) {  
                t.printStackTrace();  
            }  
        });
~~~
#### New SDK
~~~java
MapmyIndiaGeoCoding mapmyIndiaGeoCoding = MapmyIndiaGeoCoding.builder()  
        .setAddress("Delhi")  
        .build();  
MapmyIndiaGeoCodingManager.newInstance(mapmyIndiaGeoCoding).call(new OnResponseCallback<GeoCodeResponse>() {  
    @Override  
    public void onSuccess(GeoCodeResponse geoCodeResponse) {  
          
    }  
  
    @Override  
    public void onError(int i, String s) {  
  
    }  
});
~~~
### Reverse Geocode
#### Old SDK
~~~java
MapmyIndiaReverseGeoCode.builder()  
        .setLocation(28, 77)  
        .build()  
        .enqueueCall(new Callback<PlaceResponse>() {  
            @Override  
            public void onResponse(Call<PlaceResponse> call,Response<PlaceResponse> response) {  
                //handle response 
           }  
  
            @Override  
            public void onFailure(Call<PlaceResponse> call, Throwable t) {  
                t.printStackTrace();  
            }  
        });
~~~
#### New SDK
~~~java
MapmyIndiaReverseGeoCode mapmyIndiaReverseGeoCode = MapmyIndiaReverseGeoCode.builder()    
        .setLocation(28,77)    
        .build();    
MapmyIndiaReverseGeoCodeManager.newInstance(mapmyIndiaReverseGeoCode).call(new OnResponseCallback<PlaceResponse>() {    
    @Override    
  public void onSuccess(PlaceResponse response) {    
        //Handle Response  
  }    
    
    @Override    
  public void onError(int code, String message) {    
        //Handle Error  
  }    
});
~~~

### Nearby API
#### Old SDK
~~~java
MapmyIndiaNearby.builder()  
        .keyword("Parking")  
        .setLocation(28d, 77d)  
        .build()  
        .enqueueCall(new Callback<NearbyAtlasResponse>() {  
            @Override  
            public void onResponse(Call<NearbyAtlasResponse> call, Response<NearbyAtlasResponse> response) {  
                //handle response 
            }  
  
            @Override  
            public void onFailure(Call<NearbyAtlasResponse> call, Throwable t) {  
                t.printStackTrace();  
            }  
        });
~~~
#### New SDK
~~~java
MapmyIndiaNearby mapmyIndiaNearby = MapmyIndiaNearby.builder()  
        .setLocation(28,77)    
        .keyword("Parking")    
        .build();    
MapmyIndiaNearbyManager.newInstance(mapmyIndiaNearby).call(new OnResponseCallback<NearbyAtlasResponse>() {    
    @Override    
  public void onSuccess(NearbyAtlasResponse response) {    
        //Handle Response  
  }    
    
    @Override    
  public void onError(int code, String message) {    
       //Handle Error  
  }    
});
~~~
### eLoc
#### Old SDK
~~~java
MapmyIndiaELoc.builder()  
        .setELoc("mmi000")  
        .build()  
        .enqueueCall(new Callback<PlaceResponse>() {  
            @Override  
            public void onResponse(Call<PlaceResponse> call, Response<PlaceResponse> response) {  
                //handle response 
            }  
  
            @Override  
            public void onFailure(Call<PlaceResponse> call, Throwable t) {  
                t.printStackTrace();  
            }  
        });
~~~
#### New SDK
~~~java  
MapmyIndiaELoc mapmyIndiaELoc = MapmyIndiaELoc.builder()    
        .setELoc("mmi000")    
        .build();    
MapmyIndiaELocManager.newInstance(mapmyIndiaELoc).call(new OnResponseCallback<PlaceResponse>() {    
    @Override    
  public void onSuccess(PlaceResponse response) {    
        //Handle Response   
  }    
    
    @Override    
  public void onError(int code, String message) {    
        //Handle Error  
  }    
});  
~~~
### Place Detail
#### Old SDK
~~~java
MapmyIndiaPlaceDetail.builder()  
        .eLoc("mmi000")  
        .build()  
        .enqueueCall(new Callback<PlaceDetailResponse>() {  
            @Override  
            public void onResponse(Call<PlaceDetailResponse> call, Response<PlaceDetailResponse> response) {  
                //handle response 
            }  
  
            @Override  
            public void onFailure(Call<PlaceDetailResponse> call, Throwable t) {  
                t.printStackTrace();  
            }  
        });
~~~
#### New SDK
~~~java
MapmyIndiaPlaceDetail mapmyIndiaPlaceDetail = MapmyIndiaPlaceDetail.builder()    
        .eLoc("mmi000")    
        .build();    
MapmyIndiaPlaceDetailManager.newInstance(mapmyIndiaPlaceDetail).call(new OnResponseCallback<PlaceDetailResponse>() {    
    @Override    
  public void onSuccess(PlaceDetailResponse response) {    
        //Handle Response    
  }    
    
    @Override    
  public void onError(int code, String message) {    
       //Handle Error  
  }    
});
~~~
### POI Along the Route
#### Old SDK
~~~java
MapmyIndiaPOIAlongRoute.builder()  
        .category(catCode)  
        .path(path)  
        .build().enqueueCall(new Callback<POIAlongRouteResponse>() {  
            @Override  
            public void onResponse(Call<POIAlongRouteResponse> call, Response<POIAlongRouteResponse> response) {  
                //handle response
            }  
  
            @Override  
            public void onFailure(Call<POIAlongRouteResponse> call, Throwable t) {  
               t.printStackTrace();
            }  
        });
~~~
#### New SDK
~~~java
MapmyIndiaPOIAlongRoute poiAlongRoute = MapmyIndiaPOIAlongRoute.builder()    
        .category(catCode)  
        .path(path)    
        .build();    
MapmyIndiaPOIAlongRouteManager.newInstance(poiAlongRoute).call(new OnResponseCallback<POIAlongRouteResponse>() {    
    @Override    
  public void onSuccess(POIAlongRouteResponse response) {    
        //Handle Response    
  }    
    
    @Override    
  public void onError(int code, String message) {    
        //Handle Error  
  }    
});
~~~

### Routing API
#### Old SDK
~~~java
MapmyIndiaDirections.builder()  
        .origin(startPointLocal)  
        .steps(true)  
        .resource(DirectionsCriteria.RESOURCE_ROUTE_ETA)  
        .profile(DirectionsCriteria.PROFILE_DRIVING)  
        .overview(DirectionsCriteria.OVERVIEW_FULL)  
        .destination(endPointLocal)
        .build()  
        .enqueueCall(new Callback<DirectionsResponse>() {  
            @Override  
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {  
                //handle response 
	        }  
  
            @Override  
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {  
                 t.printStackTrace();  
	        }  
        });
~~~
#### New SDK
~~~java
MapmyIndiaDirections directions = MapmyIndiaDirections.builder()  
        .origin(startPointLocal)  
        .steps(true)  
        .resource(DirectionsCriteria.RESOURCE_ROUTE_ETA)  
        .profile(DirectionsCriteria.PROFILE_DRIVING)  
        .overview(DirectionsCriteria.OVERVIEW_FULL)  
        .destination(endPointLocal)
        .build();
MapmyIndiaDirectionManager.newInstance(directions).call(new OnResponseCallback<DirectionsResponse>() {  
    @Override  
    public void onSuccess(DirectionsResponse directionsResponse) {  
         
    }  
  
    @Override  
    public void onError(int i, String s) {  
         
    }  
});
~~~
### Driving Distance Matrix
#### Old SDK
~~~java
MapmyIndiaDistanceMatrix.builder()  
        .profile(DirectionsCriteria.PROFILE_DRIVING)  
        .resource(DirectionsCriteria.RESOURCE_DISTANCE)  
        .coordinate(Point.fromLngLat(80.502113, 8.916787))  
        .coordinate(Point.fromLngLat(28.5505073, 77.2689367))  
        .build()  
        .enqueueCall(new Callback<DistanceResponse>() {  
            @Override  
	        public void onResponse(Call<DistanceResponse> call, Response<DistanceResponse> response) {  
                //handle response 
	        }  
  
            @Override  
	        public void onFailure(Call<DistanceResponse> call, Throwable t) {  
                t.printStackTrace();  
	        }
        });
~~~

#### New SDK
~~~java
MapmyIndiaDistanceMatrix distanceMatrix = MapmyIndiaDistanceMatrix.builder()  
        .profile(DirectionsCriteria.PROFILE_DRIVING)  
        .resource(DirectionsCriteria.RESOURCE_DISTANCE)  
        .coordinate(Point.fromLngLat(80.502113, 8.916787))  
        .coordinate(Point.fromLngLat(28.5505073, 77.2689367))  
        .build(); 
MapmyIndiaDistanceMatrixManager.newInstance(distanceMatrix).call(new OnResponseCallback<DistanceResponse>() {  
    @Override  
  public void onSuccess(DistanceResponse distanceResponse) {  
        
    }  
  
    @Override  
  public void onError(int i, String s) {  
        
  }  
});
~~~

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



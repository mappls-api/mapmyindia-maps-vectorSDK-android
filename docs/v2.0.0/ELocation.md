


![MapmyIndia APIs](https://www.mapmyindia.com/api/img/mapmyindia-api.png)
# ELocation Strategy

## Add Marker
To add marker using eLoc:
~~~java
mapmyIndiaMap.addMarker(new MarkerOptions().eLoc(eLoc));
~~~

To get the callback that marker is added or not:
~~~java
mapmyIndiaMap.addMarker(new MarkerOptions().eLoc(eLoc), new OnMarkerAddedListener() {  
    @Override  
  public void onSuccess() {  
        //On Marker Added Successfully  
    }  
  
    @Override  
  public void onFailure() {  
	  //Failure
    }  
});
~~~


## To set Camera to particular eLoc

##### Sdk allows various method to Move, ease,animate Camera to a particular location :

#### Java
~~~java
mapmyIndiaMap.moveCamera(CameraELocUpdateFactory.newELocZoom("MMI000", 14));  
mapmyIndiaMap.easeCamera(CameraELocUpdateFactory.newELocZoom("MMI000", 14));  
mapmyIndiaMap.animateCamera(CameraELocUpdateFactory.newELocZoom("MMI000", 14));
~~~
#### Kotlin
~~~kotlin
mapmyIndiaMap?.moveCamera(CameraELocUpdateFactory.newELocZoom("MMI000", 14));  
mapmyIndiaMap?.easeCamera(CameraELocUpdateFactory.newELocZoom("MMI000", 14));  
mapmyIndiaMap?.animateCamera(CameraELocUpdateFactory.newELocZoom("MMI000", 14));
~~~
## To set Camera to particular eLoc Bound

##### Sdk allows various method to Move, ease,animate Camera to a particular eLoc Bound :

#### Java
~~~java
mapmyIndiaMap.moveCamera(CameraELocUpdateFactory.newELocBounds(eLocs, 10 , 100, 10, 10));  
mapmyIndiaMap.easeCamera(CameraELocUpdateFactory.newELocBounds(eLocs, 10 , 100, 10, 10));  
mapmyIndiaMap.animateCamera(CameraELocUpdateFactory.newELocBounds(eLocs, 10 , 100, 10, 10));
~~~
#### Kotlin
~~~kotlin
mapmyIndiaMap?.moveCamera(CameraELocUpdateFactory.newELocBounds(eLocs, 10 , 100, 10, 10));  
mapmyIndiaMap?.easeCamera(CameraELocUpdateFactory.newELocBounds(eLocs, 10 , 100, 10, 10));  
mapmyIndiaMap?.animateCamera(CameraELocUpdateFactory.newELocBounds(eLocs, 10 , 100, 10, 10));
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


> © Copyright 2021. CE Info Systems Ltd. All Rights Reserved. | [Terms & Conditions](http://www.mapmyindia.com/api/terms-&-conditions).



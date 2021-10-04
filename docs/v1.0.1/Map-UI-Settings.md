

![MapmyIndia APIs](https://www.mapmyindia.com/api/img/mapmyindia-api.png)
# Map UI settings

## Compass Settings
To change compass related settings:
### Enable/ Disable compass
To enable or disable the compass:
#### Java
~~~java
mapmyIndiaMap.getUiSettings().setCompassEnabled(true);
~~~

#### Kotlin
~~~kotlin
mapmyIndiaMap?.uiSettings?.isCompassEnabled = true
~~~
### Enable/Disable Fading of the compass when facing north
To enable or disable fading of the compass when facing north:
#### Java
~~~java
mapmyIndiaMap.getUiSettings().setCompassFadeFacingNorth(true);
~~~
#### Kotlin
~~~kotlin
mapmyIndiaMap?.uiSettings?.setCompassFadeFacingNorth(true)
~~~
### Gravity of Compass
To set the gravity of compass view:
#### Java
~~~java
mapmyIndiaMap.getUiSettings().setCompassGravity(Gravity.TOP);
~~~
#### Kotlin
~~~kotlin
mapmyIndiaMap?.uiSettings?.compassGravity = Gravity.TOP
~~~
### Margins of compass
To set the margin of compass view:
#### Java
~~~java
mapmyIndiaMap.getUiSettings().setCompassMargins(left, top, right, bottom);
~~~

#### Kotlin
~~~kotlin
mapmyIndiaMap?.uiSettings?.setCompassMargins(left, top, right, bottom)
~~~

## Enable/Disable zoom of map on double tap
To enable or disable zoom on double tap:
#### Java
~~~java
mapmyIndiaMap.getUiSettings().setDoubleTapGesturesEnabled(false);
~~~
#### Kotlin
~~~kotlin
mapmyIndiaMap?.uiSettings?.isDoubleTapGesturesEnabled = false
~~~

## Logo Settings
To change the positions of logo:
### Gravity of Logo
To set the gravity of Logo view:
#### Java
~~~java
mapmyIndiaMap.getUiSettings().setLogoGravity(Gravity.TOP);
~~~

#### Kotlin
~~~kotlin
mapmyIndiaMap?.uiSettings?.logoGravity = Gravity.TOP
~~~

### Margin of Logo
To set the margins of Logo view
#### Java
~~~java
mapmyIndiaMap.getUiSettings().setLogoMargins(left, top, right, bottom);
~~~
#### Kotlin
~~~kotlin
mapmyIndiaMap?.uiSettings?.setLogoMargins(left, top, right, bottom)
~~~
## Enable/ Disable Map Rotation Gesture
To enable or disable the map rotation:
#### Java
~~~java
mapmyIndiaMap.getUiSettings().setRotateGesturesEnabled(false);
~~~

#### Kotlin
~~~kotlin
mapmyIndiaMap?.uiSettings?.isRotateGesturesEnabled = false
~~~
## Enable/Disable Map Scrolling Gesture
To enable or disable the map scrolling:
#### Java
~~~java
mapmyIndiaMap.getUiSettings().setScrollGesturesEnabled(false);
~~~ 
#### Kotlin
~~~kotlin
mapmyIndiaMap?.uiSettings?.isScrollGesturesEnabled = false
~~~
## Enable/ Disable Map Tilt Gesture
To enable or disable map tilt:
#### Java
~~~java
mapmyIndiaMap.getUiSettings().setTiltGesturesEnabled(false);
~~~
#### Kotlin
~~~kotlin
mapmyIndiaMap?.uiSettings?.isTiltGesturesEnabled = false
~~~

## Enable/Disable Zoom Gesture
To enable or disable zoom gesture
#### Java
~~~java
mapmyIndiaMap.getUiSettings().setZoomGesturesEnabled(false);
~~~
#### Kotlin
~~~kotlin
mapmyIndiaMap?.uiSettings?.isZoomGesturesEnabled = false
~~~
## SDK Error Codes
Map will fail with following error codes
* MAPS_SDK_KEY_MISSING=1; //when map sdk key is not passed
* REST_API_KEY_MISSING=2; //when rest api key is not passed
* ATLAS_CLIENT_ID_MISSING=3; //when atlas client id is not passed
* ATLAS_CLIENT_SECRET_MISSING=4; //when atlas client secret is not passed
* ATLAS_GRANT_TYPE_MISSING=5; //* //when atlas grant type is not passed
* AUTHENTICATION_FAILED=7; //when map fails to authenticate itself with MapmyIndia server
* UNKNOWN=8; // when map loading failed due to unknown error

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



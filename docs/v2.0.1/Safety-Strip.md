
![MapmyIndia APIs](https://www.mapmyindia.com/api/img/mapmyindia-api.png)

# COVID-19 Safety Status on MapmyIndia's Map

## Introduction

It is a guide to display a user's safety status for COVID-19 on a map. It will show whether user is in a containment zone or not if yes how much is distance from current location.

Following this guide it will be possible to display user's safety status on MapmyIndia's Map  in the form of a safety strip depending upon the user's current location.

A method need to be called to check safety status. SDK has inbuild view with button which can be used to call that method.


## Getting Started

### Get Safety Status

After loading of Map, a mthod can be called to get safety status.  `showCurrentLocationSafety`  is the method which helps to find user's safety status. On successfully fetching of status a safety strip will be shown on map.

#### Java
~~~java
mapmyIndiaMap.showCurrentLocationSafety();
~~~

#### Kotlin
~~~kotlin
mapmyIndiaMap?.showCurrentLocationSafety()
~~~


### Safety Strip

On successfully receiving status based on location from server, a view will be shown on map with safety information like distance, containment zone location etc.

### Safety Strip Position

By default safety status strip will be shown on top of map.

#### Java
~~~java
mapmyIndiaMap.getUiSettings().setSafetyStripGravity(Gravity.TOP);
~~~

#### Kotlin
~~~kotlin
mapmyIndiaMap?.uiSettings?.safetyStripGravity = Gravity.TOP
~~~
### Safety Strip Margins
By default margins of safety status is zero.

#### Java
~~~java
mapmyIndiaMap.getUiSettings().setSafetyStripMargins(left, top, right, bottom);
~~~

#### Kotlin
~~~kotlin
mapmyIndiaMap?.uiSettings?.setSafetyStripMargins(left, top, right, bottom)
~~~
### Hide Safety Status

#### Java
~~~java
mapmyIndiaMap.getUiSettings().hideSafetyStrip();
~~~
#### Kotin
~~~kotlin
mapmyIndiaMap?.uiSettings?.hideSafetyStrip()
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


> © Copyright 2022. CE Info Systems Ltd. All Rights Reserved. | [Terms & Conditions](http://www.mapmyindia.com/api/terms-&-conditions).



![MapmyIndia APIs](https://www.mapmyindia.com/api/img/mapmyindia-api.png)  
  

# MapmyIndia Nearby Search Widget

## [Introduction](#Introduction)

The MapmyIndia Nearby Widget makes it easy to integrate the functionality to search nearby POIs for selected categories in your Android application. The Nearby Search widget provided as a means to enable radially search for Nearby Places on MapmyIndia Maps.

The widget offers the following basic functionalities:

- Ability to search for nearby places directly with MapmyIndia Maps visual interface.

- A single method to initiate nearby search across all categories of places available on MapmyIndia.

- Ability to get information from MapmyIndia Nearby Search widget through a callback.

This can be done by following simple steps.

	
## [Dependencies](#Dependencies)

- Add below dependency in your app-level build.gradle	
~~~groovy	
   implementation 'com.mapmyindia.sdk:nearby-ui:0.2.0'
~~~	

## Step 2 :-  [Adding Credentials](#Adding-Credentials)

Add your API keys to the SDK (in your application's onCreate() or before using map)

#### Java	
```java	
MapmyIndiaAccountManager.getInstance().setRestAPIKey(getRestAPIKey());  	
MapmyIndiaAccountManager.getInstance().setMapSDKKey(getMapSDKKey());  		
MapmyIndiaAccountManager.getInstance().setAtlasClientId(getAtlasClientId());  	
MapmyIndiaAccountManager.getInstance().setAtlasClientSecret(getAtlasClientSecret());  	
MapmyIndia.getInstance(applicationContext);
```	
#### Kotlin	
```kotlin	
MapmyIndiaAccountManager.getInstance().restAPIKey = getRestAPIKey()  	
MapmyIndiaAccountManager.getInstance().mapSDKKey = getMapSDKKey()  		
MapmyIndiaAccountManager.getInstance().atlasClientId = getAtlasClientId()  	
MapmyIndiaAccountManager.getInstance().atlasClientSecret = getAtlasClientSecret()	
MapmyIndia.getInstance(applicationContext)
```	

  
## Step 3 :-  [Launching Nearby Widget](#Launching-Nearby-Widget)

There are two ways to implement  `Nearby Search`  widget:

-   Using `MapmyIndiaNearbyFragment`
-   Using  `MapmyIndiaNearbyActivity`

### [MapmyIndiaNearbyFragment](#nearby-fragment)
Add MapmyIndiaNearbyFragment in your activity:

#### java
~~~java
MapmyIndiaNearbyFragment nearbyFragment = MapmyIndiaNearbyFragment.newInstance();

getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, nearbyFragment, MapmyIndiaNearbyFragment.class.getSimpleName())  
        .commit();  
~~~

#### Kotlin
~~~kotlin
val nearbyFragment: MapmyIndiaNearbyFragment= MapmyIndiaNearbyFragment.newInstance()
supportFragmentManager.beginTransaction().add(R.id.fragment_container, nearbyFragment, MapmyIndiaNearbyFragment::class.java.simpleName)  
        .commit()
~~~

To get the selected nearby place use IMapmyIndiaNearbyCallback interface:
#### Java
~~~java
nearbyFragment.setMapmyIndiaNearbyCallback(new IMapmyIndiaNearbyCallback() {  
    @Override  
    public void getNearbyCallback(NearbyAtlasResult nearbyAtlasResult) {  
       // Select place
    }    
});
~~~
#### Kotlin
~~~kotlin
nearbyFragment.setMapmyIndiaNearbyCallback(object : IMapmyIndiaNearbyCallback {  
    override fun getNearbyCallback(nearbyCallback: NearbyAtlasResult) {  
        //Select place  
    }  
})
~~~

### [MapmyIndiaNearbyActivity](#nearby-activity)
Add MapmyIndiaNearbyActivity in your activity:  
####  java  
~~~java  
 Intent intent = new MapmyIndiaNearbyWidget.IntentBuilder().build(this);   
 startActivityForResult(intent, 101);  
~~~  
  
####  Kotlin  
  
~~~kotlin  
val intent = MapmyIndiaNearbyWidget.IntentBuilder().build(this)   
startActivityForResult(intent, 101)  
~~~  
  
To get the selected POI:  
####  java  
  
~~~java  
@Override protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {    
       super.onActivityResult(requestCode, resultCode, data);    
       if (requestCode == 101 && resultCode == RESULT_OK) {    
        NearbyAtlasResult result = MapmyIndiaNearbyWidget.getNearbyResponse(data);    
} }  
~~~  
  
####  Kotlin  
  
~~~kotlin  
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {    
      super.onActivityResult(requestCode, resultCode, data)    
      if(requestCode == 101 && resultCode == Activity.RESULT_OK) {    
            val place: NearbyAtlasResult? = MapmyIndiaNearbyWidget.getNearbyResponse(data!!)              
} }  
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
  
>  ©  Copyright  2021.  CE  Info  Systems  Ltd.  All  Rights  Reserved.  |  [Terms  &  Conditions](http://www.mapmyindia.com/api/terms-&-conditions).

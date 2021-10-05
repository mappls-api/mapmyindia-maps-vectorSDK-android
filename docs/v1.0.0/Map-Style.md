
![MapmyIndia APIs](https://www.mapmyindia.com/api/img/mapmyindia-api.png)

# [Set MapmyIndiaMaps style](#Set-MapmyIndiaMaps-style)

MapmyIndia offers a range of preset styles to rendering the map. The user has to retrieve a list of styles for a specific account. 
The listing api would help in rendering specific style as well as facilitate the switching of style themes. 

From the below reference code it would become quite clear that user has to specify style names and not URLs to use them. 
A default style is set for all account users to start with. 
To know more about available styles, kindly contact apisupport@mapmyindia.com

**This feature is available from version v6.8.14**

## [List of Available Styles](#list-of-available-styles)

To get the list of available styles:

#### Java
~~~java
List<MapmyIndiaStyle> styleList = mapmyIndiaMap.getMapmyIndiaAvailableStyles();
~~~

#### Kotlin
~~~kotlin
val styleList = mapmyIndiaMap.getMapmyIndiaAvailableStyles()
~~~

`MapmyIndiaStyle` contains below parameters:

 1. `description(String)`: Description of the style
 2. `displayName(string)`: Generic Name of style mostly used in MapmyIndia content.
 3. `imageUrl(String)`: Preview Image of style
 4. `name(String)`: Name of style used to change the style.

## [Set MapmyIndia Style](#set-mapmyindia-style)
To set MapmyIndiaMaps style reference code is below:
#### Java
~~~java
mapmyIndiaMap.setMapmyIndiaStyle(name, new OnStyleLoadListener() {  
       @Override  
       public void onError(String error) {  
            Toast.makeText(MapActivity.this, error, Toast.LENGTH_SHORT).show();  
       }  
  
       @Override  
       public void onStyleLoaded() {   
           Toast.makeText(MapActivity.this, "onStyleLoaded", Toast.LENGTH_SHORT).show();  
  
       }  
});
                           //OR
mapmyIndiaMap.setMapmyIndiaStyle(name);
~~~

#### Kotlin
~~~kotlin
mapmyIndiaMap.setMapmyIndiaStyle(style, object : OnStyleLoadListener {
            override fun onError(p0: String?) {
                Toast.makeText(this@MainActivity, p0, Toast.LENGTH_LONG).show()
            }
            override fun onStyleLoaded() {
                Toast.makeText(this@MainActivity, "Style loaded Successfully", Toast.LENGTH_LONG).show()
            }
        })
                           //OR
mapmyIndiaMap.setMapmyIndiaStyle(name)
~~~

## [To enable/disable last selected style](#To-enable-last-selected-style)
To enable/disable loading of last selected style:

#### Java
~~~java
MapmyIndiaMapConfiguration.getInstance().setShowLastSelectedStyle(false); //true is enable & false is disable(default value is true) 
~~~ 
#### Kotlin
~~~kotlin
MapmyIndiaMapConfiguration.getInstance().setShowLastSelectedStyle(false) //true is enable & false is disable(default value is true)
~~~

## [Get Selected style](#get-selected-style)
To get the currently selected style name:
#### Java
~~~java
mapmyIndiaMap.getMapmyIndiaStyle();
~~~
#### Kotlin
~~~kotlin
mapmyIndiaMap.getMapmyIndiaStyle()
~~~


For any queries and support, please contact: 

![Email](https://www.google.com/a/cpanel/mapmyindia.co.in/images/logo.gif?service=google_gsuite)

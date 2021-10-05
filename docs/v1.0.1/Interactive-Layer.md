
![MapmyIndia APIs](https://www.mapmyindia.com/api/img/mapmyindia-api.png)
# MapmyIndia Interactive Layer Plugin

## Introduction

In the current scenario, where social distancing is paramount, technology is playing a key role in maintaining daily communication and information transfer. MapmyIndia is serving individuals, companies and the government alike, to spread critical information related to COVID-19 through deeply informative, useful and free-to-integrate geo-visualized COVID-19 Layers in your application.

Simply plug these easy-to-integrate and COVID-19 Layers into your applications, and offer a seamless information experience to your users.

## Get Interactive Layers

To get all interactive layers:
#### Java
~~~java
mapmyIndiaMap.getInteractiveLayer(new MapboxMap.InteractiveLayerLoadingListener() {  
    @Override  
    public void onLayersLoaded(List<InteractiveLayer> list) {  
  
    }  
});
~~~

#### Kotlin
~~~kotlin
mapmyIndiaMap?.getInteractiveLayer(object: MapboxMap.InteractiveLayerLoadingListener {  
    override fun onLayersLoaded(list: List<InteractiveLayer>?) {  
        
    }  
})
~~~

## Show/ Hide Interactive Layers

To show Interactive Layers:
#### Java
~~~java
mapmyIndiaMap.showInteractiveLayer(interactiveLayer);
~~~

#### Kotlin
~~~kotlin
mapmyIndiaMap?.showInteractiveLayer(interactiveLayer)
~~~
To hide Interactive Layers:
#### Java
~~~java
mapmyIndiaMap.hideInteractiveLayer(interactiveLayer);
~~~
#### Kotlin
~~~kotlin
mapmyIndiaMap?.hideInteractiveLayer(interactiveLayer)
~~~

## Enable/ Disable Infowindow
To Enable/Disable info window on click of Interactive Layer

#### Java
~~~java
mapmyIndiaMap.showInteractiveLayerInfoWindow(false);
~~~
#### Kotlin
~~~kotlin
mapmyIndiaMap?.showInteractiveLayerInfoWindow(false)
~~~

## Get Interactive Layer Details
To get the Interactive Layer Detail when clicked on Layer
#### Java
~~~java
mapmyIndiaMap.setOnInteractiveLayerClickListener(new MapboxMap.OnInteractiveLayerClickListener() {  
    @Override  
  public void onInteractiveLayerClicked(InteractiveItemDetails interactiveItemDetails) {  
  
    }  
});
~~~
#### Kotlin
~~~kotlin
  
mapmyIndiaMap?.setOnInteractiveLayerClickListener(object : MapboxMap.OnInteractiveLayerClickListener {  
    override fun onInteractiveLayerClicked(interactiveItemDetails: InteractiveItemDetails?) {  
  
    }  
})
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


> © Copyright 2020. CE Info Systems Pvt. Ltd. All Rights Reserved. | [Terms & Conditions](http://www.mapmyindia.com/api/terms-&-conditions).
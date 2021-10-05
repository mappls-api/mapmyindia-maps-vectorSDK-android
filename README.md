![MapmyIndia APIs](https://www.mapmyindia.com/api/img/mapmyindia-api.png)
# MapmyIndia Vector Map Android SDK

**Easy To Integrate Maps & Location APIs & SDKs For Web & Mobile Applications**

Powered with India's most comprehensive and robust mapping functionalities.
**Now Available**  for Srilanka, Nepal, Bhutan and Bangladesh

1. You can get your api key to be used in this document here: [https://www.mapmyindia.com/api/signup](https://www.mapmyindia.com/api/signup)

2. The sample code is provided to help you understand the basic functionality of MapmyIndia maps & REST APIs working on **Android** native development platform.

4. Explore through [238 nations](https://github.com/MapmyIndia/mapmyindia-rest-api/blob/master/docs/countryISO.md) with **Global Search, Routing and Mapping APIs & SDKs** by MapmyIndia.

## [Documentation History](#Documentation-History)

| Version | Supported SDK Version |
| ---- | ---- | 
| [v1.0.1](./README.md) | - Map SDK v6.9.0 <br/> - Geo Analytics v0.1.0 <br/> - Place Search Widget v1.4.0 <br/> - GeoFence Widget v0.9.5 <br/> - Scalebar Plugin v0.1.0 <br/> - Direction Widget v0.2.0 <br/> - Nearby UI Widget v0.1.0 |
| [v1.0.0](./docs/v1.0.0/README.md) | - Map SDK v6.8.16 <br/> - Geo Analytics v0.1.0 <br/> - Place Search Widget v1.4.0 <br/> - GeoFence Widget v0.9.5 <br/> - Scalebar Plugin v0.1.0 <br/> - Direction Widget v0.2.0 <br/> - Nearby UI Widget v0.1.0 |

## [Version History](#Version-History)


| Version | Last Updated | Author |  Release Note|
| ---- | ---- | ---- | ---- |
| v6.9.0 | 07 September 2021 | MapmyIndia API Team ([MA](https://github.com/mdakram)) ([ST](https://github.com/saksham66)) |   Bug Fixes |
| v6.8.16 | 03 September 2021 | MapmyIndia API Team ([MA](https://github.com/mdakram)) ([ST](https://github.com/saksham66)) |   Added distance in Autosuggest API response |
| v6.8.15 | 18 August 2021 | MapmyIndia API Team ([MA](https://github.com/mdakram)) ([ST](https://github.com/saksham66)) |  -Added Nearby Report API call<br/>-Added hyperlocal in Autosuggest |
| v6.8.14 | 24 May 2021 | MapmyIndia API Team ([MA](https://github.com/mdakram)) ([ST](https://github.com/saksham66)) |  Added MapmyIndia Style support |
| v6.8.13 | 19 May 2021 | MapmyIndia API Team ([MA](https://github.com/mdakram)) ([ST](https://github.com/saksham66)) |  Fixes Android Auto related Issue |
| v6.8.12 | 07 May 2021 | MapmyIndia API Team ([MA](https://github.com/mdakram)) ([ST](https://github.com/saksham66)) |  Fixes On Map Ready related Issue |


For more details of previous versions , [click here](./Version-History.md).

## [Table Of Content](#Table-Of-Content)
- [Vector Android Map](./docs/v1.0.1/Getting-Started.md)
    * [Getting Started](./docs/v1.0.1/Getting-Started.md#getting-started)
    * [Setup your project](./docs/v1.0.1/Getting-Started.md#setup-your-project)
    * [Add your API keys to the SDK](./docs/v1.0.1/Getting-Started.md#add-your-api-keys-to-the-sdk)
    * [Add a MapmyIndia Map to your application](./docs/v1.0.1/Getting-Started.md#add-a-mapmyindia-map-to-your-application)
    * [Map Interactions](./docs/v1.0.1/Getting-Started.md#map-interactions)
    * [Map Events](./docs/v1.0.1/Getting-Started.md#map-events)
    * [Map Click/Long Press](./docs/v1.0.1/Getting-Started.md#map-click-long-press)
    * [Map Overlays](./docs/v1.0.1/Getting-Started.md#map-overlays)
        - [Add A Marker](./docs/v1.0.1/Getting-Started.md#add-a-marker)
        - [Remove A Marker](./docs/v1.0.1/Getting-Started.md#remove-a-marker)
        - [Customize A Marker](./docs/v1.0.1/Getting-Started.md#customize-a-marker)
        - [Add A Polyline](./docs/v1.0.1/Getting-Started.md#add-a-polyline)
        - [Remove Polyline](./docs/v1.0.1/Getting-Started.md#remove-polyline)
        - [Add A Polygon](./docs/v1.0.1/Getting-Started.md#add-a-polygon)
        - [Remove Polygon](./docs/v1.0.1/Getting-Started.md#remove-polygon)
    * [Show User Location](./docs/v1.0.1/Getting-Started.md#show-user-location)
    * [Calculate distance between two points](./docs/v1.0.1/Getting-Started.md#calculate-distance-between-points)
    * [Proguard](./docs/v1.0.1/Getting-Started.md#proguard)
- [Map UI Settings](./docs/v1.0.1/Map-UI-Settings.md)
    * [Compass Settings](./docs/v1.0.1/Map-UI-Settings.md#Compass-Settings)
    * [Enable/Disable Zoom on double tap](./docs/v1.0.1/Map-UI-Settings.md#Enable-disable-zoom)
    * [Logo Settings](./docs/v1.0.1/Map-UI-Settings.md/#Logo-settings)
    * [Enable/Disable Map Rotation Gessture](./docs/v1.0.1/Map-UI-Settings.md#Enable-disable-rotation)
    * [Enable/Disable Map Scrolling Gesture](./docs/v1.0.1/Map-UI-Settings.md#Enable-disable-scrolling)
    * [Enable/Disable Map Tilt Gesture](./docs/v1.0.1/Map-UI-Settings.md#Enable-disable-tilt)
    * [Enable/Disable Zoom Gestures](./docs/v1.0.1/Map-UI-Settings.md#Enable-disable-zoom-gesture)
- [SDK Error Codes](./docs/v1.0.1/SDK-Error-code.md)
- [Set Country Regions](./docs/v1.0.1/Set-Regions.md)
- [Set MapmyIndia MapStyle](./docs/v1.0.1/Map-Style.md)
    * [List of Available Styles](./docs/v1.0.1/Map-Style.md#list-of-available-styles)
    * [Set MapmyIndia Style](./docs/v1.0.1/Map-Style.md#set-mapmyindia-style)
    * [To enable/disable last selected style](./docs/v1.0.1/Map-Style.md#To-enable-last-selected-style)
    * [Get selected style](./docs/v1.0.1/Map-Style.md#get-selected-style)
- REST API Kit
    * [Search API's](./docs/v1.0.1/Search-Api.md)
        - [Auto Suggest](./docs/v1.0.1/Search-Api.md#auto-suggest)
        - [Geocoding](./docs/v1.0.1/Search-Api.md#geocoding)
        - [Reverse Geocoding](./docs/v1.0.1/Search-Api.md#reverse-geocoding)
        - [Nearby Places](./docs/v1.0.1/Search-Api.md#nearby-places)
        - [eLoc](./docs/v1.0.1/Search-Api.md#eloc)
        - [Place Detail](./docs/v1.0.1/Search-Api.md#place-detail)
        - [POI Along the Route](./docs/v1.0.1/Search-Api.md#poi-along-route)

    * [Routes & Navigation API](./docs/v1.0.1/Routing-API.md)
        - [Routing API](./docs/v1.0.1/Routing-API.md#routing-api)
        - [Driving Distance Matrix API](./docs/v1.0.1/Routing-API.md#distance-api)
    * [Feedback API](./docs/v1.0.1/Feedback.md)
    * [Nearby Reports](./docs/v1.0.1/Nearby-Report.md)
- [MapmyIndia GeoAnalytics](./docs/v1.0.1/Geoanalytics.md)
- [Place Autocomple Widget](./docs/v1.0.1/Place-Autocomplete.md)
    * [PlaceAutocompleteFragment](./docs/v1.0.1/Place-Autocomplete.md#place-autocomplete-fragment)
    * [PlaceAutocompleteActivity](./docs/v1.0.1/Place-Autocomplete.md#place-autocomplete-activity)
- [MapmyIndia Safety Plugin](./docs/v1.0.1/Safety-Plugin.md)
- [MapmyIndia Interactive Layer](./docs/v1.0.1/Interactive-Layer.md)
- [MapmyIndia GeoFence View](./docs/v1.0.1/GeoFence-View.md)
- [MapmyIndia Safety Strip](./docs/v1.0.1/Safety-Strip.md)
- [MapmyIndia Place Picker](./docs/v1.0.1/Place-Picker.md)
- [MapmyIndia Scalebar Plugin](./docs/v1.0.1/Scalebar-Plugin.md)
- [ELocation Strategy](./docs/v1.0.1/ELocation.md)
- [MapmyIndia Direction Widget](./docs/v1.0.1/Direction-Widget.md)
- [MapmyIndia Nearby Search Widget](./docs/v1.0.1/Nearby-Widget.md)
    * [Introduction](./docs/v1.0.1/Nearby-Widget.md#Introduction)
    * [Adding Credentials](./docs/v1.0.1/Nearby-Widget.md#Adding-Credentials)
    * [Launching Nearby Widget](./docs/v1.0.1/Nearby-Widget.md#Launching-Nearby-Widget)
        - [MapmyIndiaNearbyFragment](./docs/v1.0.1/Nearby-Widget.md#nearby-fragment)
        - [MapmyIndiaNearbyActivity](./docs/v1.0.1/Nearby-Widget.md#nearby-activity)
- [Version History](./docs/v1.0.1/Version-History.md)
- [Country List](https://github.com/MapmyIndia/mapmyindia-rest-api/blob/master/docs/countryISO.md)


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


> © Copyright 2021. CE Info Systems Pvt. Ltd. All Rights Reserved. | [Terms & Conditions](http://www.mapmyindia.com/api/terms-&-conditions).

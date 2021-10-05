![MapmyIndia APIs](https://www.mapmyindia.com/api/img/mapmyindia-api.png)

# Setting Regions - limiting geographic area

Source data can often contain more information than what's needed in your map.
For example, an administrative places dataset may contain data for a country OR set of countries OR contain places for the entire world; when you may only need political boundaries for North America or the USA.
In this scenario, you can specify a region in SDK to limit the geographic extent of the data that will be tiled.

As of now, **the tiles for global maps are enabled by default** for all users as a means for users to visually experience our international maps.

Search and Routing APIs are available for the entire world as well; but they are enabled by default for India only for all customers.
If any customer wishes to use our Search and Routing Stacks of SDKs & APIs in their apps, please contact our API Support team.

Why limit access?

By reducing the amount of data querying & transference required, you can reduce your API transaction costs and also reduce the amount of time it takes for your job to process.

Now, what you as a developer need to do is to specify a region in our APIs/SDKs from the list of globally applicable region values and that's it !! [See Country List](https://github.com/MapmyIndia/mapmyindia-rest-api/blob/master/docs/countryISO.md)

To set the region, refer to the below code. Default is India ( "IND")

```js
MapmyIndiaAccountManager.getInstance().setRegion("KWT");
```

For any queries and support, please contact:

![Email](https://www.google.com/a/cpanel/mapmyindia.co.in/images/logo.gif?service=google_gsuite)
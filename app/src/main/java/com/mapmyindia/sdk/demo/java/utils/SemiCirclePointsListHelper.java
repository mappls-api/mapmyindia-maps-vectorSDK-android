package com.mapmyindia.sdk.demo.java.utils;


import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saksham on 18-07-2019
 */
public class SemiCirclePointsListHelper {

    //Radius of the earth in meters
    private static final double EARTH_RADIUS = 6371009.0D;

    /**
     * Compute the list of points to draw a semicircle
     *
     * @param p1 Start Point
     * @param p2 End Point
     * @param k  Radius of semicircle
     * @return the list of points
     */
    public static List<LatLng> showCurvedPolyline(LatLng p1, LatLng p2, double k) {
        List<LatLng> latLngs = new ArrayList<>();
        //Calculate distance and heading between two points
        double d = computeDistanceBetween(p1, p2);
        double h = computeHeading(p1, p2);

        //Midpoint position
        LatLng p = computeOffset(p1, d * 0.5, h);

        //Apply some mathematics to calculate position of the circle center
        double x = (1 - k * k) * d * 0.5 / (2 * k);
        double r = (1 + k * k) * d * 0.5 / (2 * k);

        LatLng c = computeOffset(p, x, h + 90.0);

        //Calculate heading between circle center and two points
        double h1 = computeHeading(c, p1);
        double h2 = computeHeading(c, p2);

        //Calculate positions of points on circle border and add them to polyline options

        int numpoints = 100;
        double step = (h2 - h1) / numpoints;
        for (int i = 0; i < numpoints; i++) {
            LatLng pi = computeOffset(c, r, h1 + i * step);
            latLngs.add(pi);
        }

        //Draw polyline
        return latLngs;
    }

    /**
     * Compute the headings from one point to another. Headings are expressed in degrees clockwise from North within the range [-180,180).
     *
     * @param from first point
     * @param to second point
     * @return The heading in degrees clockwise from north
     */
    private static double computeHeading(LatLng from, LatLng to) {
        double fromLat = Math.toRadians(from.getLatitude());
        double fromLng = Math.toRadians(from.getLongitude());
        double toLat = Math.toRadians(to.getLatitude());
        double toLng = Math.toRadians(to.getLongitude());
        double dLng = toLng - fromLng;
        double heading = Math.atan2(Math.sin(dLng) * Math.cos(toLat), Math.cos(fromLat) * Math.sin(toLat) - Math.sin(fromLat) * Math.cos(toLat) * Math.cos(dLng));
        return wrap(Math.toDegrees(heading), -180.0D, 180.0D);
    }


    /**
     *  Wraps the given value into the inclusive-exclusive interval between min and max.
     *
     * @param n The value to wrap
     * @param min minimum value
     * @param max maximum value
     * @return this value should be between minimum value and maximum value
     */
    private static double wrap(double n, double min, double max) {
        return n >= min && n < max ? n : mod(n - min, max - min) + min;
    }

    /**
     * Non-negative value of x mod m
     *
     * @param x the operand
     * @param m the modulus
     * @return this is non negative remainder of x/m
     */
    private static double mod(double x, double m) {
        return (x % m + m) % m;
    }

    /**
     * Compute the point resulting from moving a distance from an origin in the specified heading (expressed in degrees clockwise from north).
     *
     * @param from the point from which to start
     * @param distance distance to travel
     * @param heading heading in degrees from clockwise north
     * @return Resulting point
     */
    private static LatLng computeOffset(LatLng from, double distance, double heading) {
        distance /= EARTH_RADIUS;
        heading = Math.toRadians(heading);
        double fromLat = Math.toRadians(from.getLatitude());
        double fromLng = Math.toRadians(from.getLongitude());
        double cosDistance = Math.cos(distance);
        double sinDistance = Math.sin(distance);
        double sinFromLat = Math.sin(fromLat);
        double cosFromLat = Math.cos(fromLat);
        double sinLat = cosDistance * sinFromLat + sinDistance * cosFromLat * Math.cos(heading);
        double dLng = Math.atan2(sinDistance * cosFromLat * Math.sin(heading), cosDistance - sinFromLat * sinLat);
        return new LatLng(Math.toDegrees(Math.asin(sinLat)), Math.toDegrees(fromLng + dLng));
    }

    /**
     * Calculate distance in radian
     *
     * @param lat1  Latitude of start point in radian
     * @param lng1  Longitude of start point in radian
     * @param lat2  Latitude of destination point in radian
     * @param lng2  Longitude of destination point in radian
     * @return Distance from start point to destination point in radian
     */
    private static double distanceRadians(double lat1, double lng1, double lat2, double lng2) {
        return arcHav(havDistance(lat1, lat2, lng1 - lng2));
    }

    /**
     * Determines the great-circle distance between two points on a sphere given their longitudes and latitudes
     * This is also called Haversine Formula
     *
     * @param lat1 Latitude of first point
     * @param lat2 Latitude of second point
     * @param dLng difference between two longitude
     * @return haversine distance
     */
    private static double havDistance(double lat1, double lat2, double dLng) {
        return hav(lat1 - lat2) + hav(dLng) * Math.cos(lat1) * Math.cos(lat2);
    }

    /**
     * Computes half a versine of the angle.
     * hav(x) == (1 - cos(x)) / 2 == sin(x / 2)^2.
     *
     * @param x Parameter of which we find haversine
     * @return haversine(angle-in-radians)
     */
    private static double hav(double x) {
        double sinHalf = Math.sin(x * 0.5D);
        return sinHalf * sinHalf;
    }

    /**
     * Computes inverse haversine. Has good numerical stability around 0.
     * arcHav(x) == acos(1 - 2 * x) == 2 * asin(sqrt(x)).
     * The argument must be in [0, 1], and the result is positive.
     *
     * @param x must be in [0, 1]
     * @return inverse haversine
     */
    private static double arcHav(double x) {
        return 2.0D * Math.asin(Math.sqrt(x));
    }

    /**
     * Compute the angle between two points in radian
     *
     * @param from Start Point
     * @param to Last Point
     * @return angle between the points
     */
    private static double computeAngleBetween(LatLng from, LatLng to) {
        return distanceRadians(Math.toRadians(from.getLatitude()), Math.toRadians(from.getLongitude()), Math.toRadians(to.getLatitude()), Math.toRadians(to.getLongitude()));
    }

    /**
     * Compute the Distance between two points in meters
     *
     * @param from Start Point
     * @param to Last Point
     * @return distance between the points
     */
    private static double computeDistanceBetween(LatLng from, LatLng to) {
        return computeAngleBetween(from, to) * EARTH_RADIUS;
    }

}

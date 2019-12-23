package com.mapmyindia.sdk.demo.kotlin.utility

import com.mapbox.mapboxsdk.geometry.LatLng
import java.util.*
import kotlin.math.*

/**
 * Created by Saksham on 20/9/19.
 */
class SemiCirclePointsListHelper {

    companion object {

        //Radius of the earth in meters
        private const val EARTH_RADIUS: Double = 6371009.0

        /**
         * Compute the list of points to draw a semicircle
         *
         * @param p1 Start Point
         * @param p2 End Point
         * @param k  Radius of semicircle
         * @return the list of points
         */
        fun showCurvedPolyline(p1: LatLng, p2: LatLng, k: Double): List<LatLng> {
            val latLngs = ArrayList<LatLng>()
            //Calculate distance and heading between two points
            val d = computeDistanceBetween(p1, p2)
            val h = computeHeading(p1, p2)

            //Midpoint position
            val p = computeOffset(p1, d * 0.5, h)

            //Apply some mathematics to calculate position of the circle center
            val x = (1 - k * k) * d * 0.5 / (2 * k)
            val r = (1 + k * k) * d * 0.5 / (2 * k)

            val c = computeOffset(p, x, h + 90.0)

            //Calculate heading between circle center and two points
            val h1 = computeHeading(c, p1)
            val h2 = computeHeading(c, p2)

            //Calculate positions of points on circle border and add them to polyline options

            val numpoints = 100
            val step = (h2 - h1) / numpoints
            for (i in 0 until numpoints) {
                val pi = computeOffset(c, r, h1 + i * step)
                latLngs.add(pi)
            }

            //Draw polyline
            return latLngs
        }

        /**
         * Compute the headings from one point to another. Headings are expressed in degrees clockwise from North within the range [-180,180).
         *
         * @param from first point
         * @param to second point
         * @return The heading in degrees clockwise from north
         */
        private fun computeHeading(from: LatLng, to: LatLng): Double {
            val fromLat = Math.toRadians(from.latitude)
            val fromLng = Math.toRadians(from.longitude)
            val toLat = Math.toRadians(to.latitude)
            val toLng = Math.toRadians(to.longitude)
            val dLng = toLng - fromLng
            val heading = atan2(sin(dLng) * cos(toLat), cos(fromLat) * sin(toLat) - sin(fromLat) * cos(toLat) * cos(dLng))
            return wrap(Math.toDegrees(heading), -180.0, 180.0)
        }

        /**
         *  Wraps the given value into the inclusive-exclusive interval between min and max.
         *
         * @param n The value to wrap
         * @param min minimum value
         * @param max maximum value
         * @return this value should be between minimum value and maximum value
         */
        private fun wrap(n: Double, min: Double, max: Double): Double {
            return if (n >= min && n < max) n else mod(n - min, max - min) + min
        }

        /**
         * Non-negative value of x mod m
         *
         * @param x the operand
         * @param m the modulus
         * @return this is non negative remainder of x/m
         */
        private fun mod(x: Double, m: Double): Double {
            return (x % m + m) % m
        }

        /**
         * Compute the point resulting from moving a distance from an origin in the specified heading (expressed in degrees clockwise from north).
         *
         * @param from the point from which to start
         * @param distance distance to travel
         * @param heading heading in degrees from clockwise north
         * @return Resulting point
         */
        private fun computeOffset(from: LatLng, distance: Double, heading: Double): LatLng {
            var distance = distance
            var heading = heading
            distance /= EARTH_RADIUS
            heading = Math.toRadians(heading)
            val fromLat = Math.toRadians(from.latitude)
            val fromLng = Math.toRadians(from.longitude)
            val cosDistance = cos(distance)
            val sinDistance = sin(distance)
            val sinFromLat = sin(fromLat)
            val cosFromLat = cos(fromLat)
            val sinLat = cosDistance * sinFromLat + sinDistance * cosFromLat * cos(heading)
            val dLng = atan2(sinDistance * cosFromLat * sin(heading), cosDistance - sinFromLat * sinLat)
            return LatLng(Math.toDegrees(asin(sinLat)), Math.toDegrees(fromLng + dLng))
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
        private fun distanceRadians(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
            return arcHav(havDistance(lat1, lat2, lng1 - lng2))
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
        private fun havDistance(lat1: Double, lat2: Double, dLng: Double): Double {
            return hav(lat1 - lat2) + hav(dLng) * cos(lat1) * cos(lat2)
        }

        /**
         * Computes half a versine of the angle.
         * hav(x) == (1 - cos(x)) / 2 == sin(x / 2)^2.
         *
         * @param x Parameter of which we find haversine
         * @return haversine(angle-in-radians)
         */
        private fun hav(x: Double): Double {
            val sinHalf = sin(x * 0.5)
            return sinHalf * sinHalf
        }

        /**
         * Computes inverse haversine. Has good numerical stability around 0.
         * arcHav(x) == acos(1 - 2 * x) == 2 * asin(sqrt(x)).
         * The argument must be in [0, 1], and the result is positive.
         *
         * @param x must be in [0, 1]
         * @return inverse haversine
         */
        private fun arcHav(x: Double): Double {
            return 2.0 * asin(sqrt(x))
        }

        /**
         * Compute the angle between two points in radian
         *
         * @param from Start Point
         * @param to Last Point
         * @return angle between the points
         */
        private fun computeAngleBetween(from: LatLng, to: LatLng): Double {
            return distanceRadians(Math.toRadians(from.latitude), Math.toRadians(from.longitude), Math.toRadians(to.latitude), Math.toRadians(to.longitude))
        }

        /**
         * Compute the Distance between two points in meters
         *
         * @param from Start Point
         * @param to Last Point
         * @return distance between the points
         */
        private fun computeDistanceBetween(from: LatLng, to: LatLng): Double {
            return computeAngleBetween(from, to) * EARTH_RADIUS
        }
    }

}

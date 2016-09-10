package ru.ps.geoloc.utils;

/**
 * Spherical Pseudo-Mercator projection
 */
public class LonLatToMetersConverter {
    public static final double RADIUS = 30.0; //Earth radius 6378137.0

    public static double lon2x(double lon) {
        return lon/180.0*Math.PI * RADIUS;
    }
    public static double lat2y(double aLat) {
        return Math.log(Math.tan(Math.PI / 4 + Math.toRadians(aLat) / 2)) * RADIUS;
    }

    public static int lon2xCell(double lon) {
        return new Double(lon/180.0*Math.PI * RADIUS).intValue();
    }
    public static int lat2yCell(double aLat) {
        return new Double(Math.log(Math.tan(Math.PI / 4 + Math.toRadians(aLat) / 2)) * RADIUS).intValue();
    }

    public static double x2lon(double aX) {
        return Math.toDegrees(aX / RADIUS);
    }
    public static double y2lat(double aY) {
        return Math.toDegrees(2* Math.atan(Math.exp(Math.toRadians(aY / RADIUS))) - Math.PI/2);
    }
}

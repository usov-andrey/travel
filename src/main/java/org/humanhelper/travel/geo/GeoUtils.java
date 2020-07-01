package org.humanhelper.travel.geo;

/**
 * @author Андрей
 * @since 16.01.15
 */
public class GeoUtils {

    /**
     * Расстояние в километрах
     */
    public static double distance(float lat1, float lng1, float lat2, float lng2) {
        //Java implementation of Haversine formula
        double earthRadius = 6371;//В киломентрах
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }

    /**
     * За сколько минут можно пройти km километров двигаясь со скоростью velocity километров в час
     */
    public static int minutesWithVelocity(double km, double velocityKmPerHour) {
        //km = (time / 60) * velocityKmPerHour;
        return new Double(km / velocityKmPerHour * 60).intValue();
    }
}

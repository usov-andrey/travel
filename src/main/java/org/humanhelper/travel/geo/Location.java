package org.humanhelper.travel.geo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.humanhelper.service.utils.RandomUtils;

import java.io.Serializable;

/**
 * Вначале идет Lat после Lng
 *
 * @author Андрей
 * @since 17.01.15
 */
public class Location implements Serializable {

    public static final String LOCATION_FIELD = "loc";

    @JsonIgnore
    private float lat;
    @JsonIgnore
    private float lng;

    public Location() {
    }

    public Location(double lat, double lng) {
        this.lat = (float) lat;
        this.lng = (float) lng;
    }

    public Location(float lat, float lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public static Location latLng(double lat, double lng) {
        return new Location(lat, lng);
    }

    public static Location latLng(float lat, float lng) {
        return new Location(lat, lng);
    }

    public static Location lngLat(float lng, float lat) {
        return new Location(lat, lng);
    }

    public static Location latLng(String value) {
        String[] values = value.split(",");
        return new Location(Double.parseDouble(values[0]), Double.parseDouble(values[1]));
    }

    public static Location lngLat(String value) {
        String[] values = value.split(",");
        return new Location(Double.parseDouble(values[1]), Double.parseDouble(values[0]));
    }

    public static Location rnd() {
        return new Location(RandomUtils.nextDouble(), RandomUtils.nextDouble());
    }

    public static Location rnd(Location location, double distance) {
        return Location.latLng(location.getLat() - (distance / 2), location.getLng() + (distance / 2));
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    //для хранения в Elastic и вывода в файл
    //@JsonProperty(LOCATION_FIELD)
    public float[] getLoc() {
        float[] location = new float[2];
        location[0] = getLng();
        location[1] = getLat();
        return location;
    }

    //@JsonProperty(LOCATION_FIELD)
    public void setLoc(float[] location) {
        lng = location[0];
        lat = location[1];
    }

    public String toString() {
        return latLng();
    }

    public String latLng() {
        return getLat() + "," + getLng();
    }

    public String lngLat() {
        return getLng() + "," + getLat();
    }

    public boolean empty() {
        return getLng() == 0 && getLat() == 0;
    }

    /**
     * @return Расстояние между текущей точкой и location в километрах
     */
    public double getDistance(Location location) {
        return GeoUtils.distance(getLat(), getLng(), location.getLat(), location.getLng());
    }

    public boolean inRadius(Location location, double radiusInKm) {
        return getDistance(location) < radiusInKm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;

        Location location = (Location) o;

        if (Float.compare(location.lat, lat) != 0) return false;
        return Float.compare(location.lng, lng) == 0;

    }

    @Override
    public int hashCode() {
        int result = (lat != +0.0f ? Float.floatToIntBits(lat) : 0);
        result = 31 * result + (lng != +0.0f ? Float.floatToIntBits(lng) : 0);
        return result;
    }
}

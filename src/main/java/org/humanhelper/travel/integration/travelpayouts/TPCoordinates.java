package org.humanhelper.travel.integration.travelpayouts;

/**
 * @author Андрей
 * @since 08.01.15
 */
public class TPCoordinates {

    private float lon;
    private float lat;

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return lat + "," + lon;
    }
}

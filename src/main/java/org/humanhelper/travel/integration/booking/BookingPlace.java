package org.humanhelper.travel.integration.booking;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Андрей
 * @since 07.09.15
 */
public class BookingPlace {

    @JsonProperty(value = "b_latitude")
    private float lat;
    @JsonProperty(value = "b_longitude")
    private float lon;
    @JsonProperty(value = "b_url")
    private String url;
    @JsonProperty(value = "b_name")
    private String name;

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BookingPlace{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

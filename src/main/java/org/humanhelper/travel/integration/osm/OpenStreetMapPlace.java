package org.humanhelper.travel.integration.osm;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Андрей
 * @since 19.01.15
 */
public class OpenStreetMapPlace {

    @JsonProperty(value = "place_id")
    private String id;
    private float lat;
    private float lon;
    @JsonProperty(value = "display_name")
    private String displayName;
    private OpenStreetMapPlaceType type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public OpenStreetMapPlaceType getType() {
        return type;
    }

    public void setType(OpenStreetMapPlaceType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "OpenStreetMapPlace{" +
                "id='" + id + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", displayName='" + displayName + '\'' +
                ", type=" + type +
                '}';
    }
}

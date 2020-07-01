package org.humanhelper.travel.place.createdb.location;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.humanhelper.travel.geo.Location;
import org.humanhelper.travel.place.Place;

/**
 * @author Андрей
 * @since 23.01.15
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = OSMPlaceLocationResolver.class, name = "osm"),
        @JsonSubTypes.Type(value = ManualPlaceLocationResolver.class, name = "manual")
})
public abstract class PlaceLocationResolver {

    private String placeName;
    private Class<? extends Place> placeClass;

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public Class<? extends Place> getPlaceClass() {
        return placeClass;
    }

    public void setPlaceClass(Class<? extends Place> placeClass) {
        this.placeClass = placeClass;
    }

    abstract public Location resolve();
}

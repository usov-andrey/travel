package org.humanhelper.travel.place.createdb.location;

import org.humanhelper.service.singleton.SingletonLocator;
import org.humanhelper.travel.geo.Location;
import org.humanhelper.travel.integration.osm.OpenStreetMapGeoCoder;
import org.humanhelper.travel.integration.osm.OpenStreetMapPlaceType;

import java.util.List;

/**
 * Определяем местоположение места через openstreetmap
 *
 * @author Андрей
 * @since 23.01.15
 */
public class OSMPlaceLocationResolver extends PlaceLocationResolver {

    private String name;

    public Location resolve() {
        List<OpenStreetMapPlaceType> types = OpenStreetMapPlaceType.getByPlaceClass(getPlaceClass());
        Location location = SingletonLocator.get(OpenStreetMapGeoCoder.class).getLocation(this.name != null ? this.name : getPlaceName(), types);
        if (location == null) {
            throw new IllegalStateException("Not found location in open street map by name " + name + " and types " + types);
        }
        return location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

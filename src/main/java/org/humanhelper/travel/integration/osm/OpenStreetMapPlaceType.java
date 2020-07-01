package org.humanhelper.travel.integration.osm;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.type.region.Region;
import org.humanhelper.travel.place.type.transport.BusStation;
import org.humanhelper.travel.place.type.transport.SeaPort;

import java.util.Arrays;
import java.util.List;

/**
 * @author Андрей
 * @since 19.01.15
 */
public enum OpenStreetMapPlaceType {
    ferry_terminal, pier, dock, marina, service, town, city, motorway_junction, yes/* building*/,
    bus_station, village, residential, tertiary, townhall, police, picnic_site;

    public static List<OpenStreetMapPlaceType> getByPlaceClass(Class<? extends Place> placeClass) {
        if (SeaPort.class.isAssignableFrom(placeClass)) {
            return Arrays.asList(ferry_terminal, dock, marina, pier);
        }
        if (Region.class.isAssignableFrom(placeClass)) {
            return Arrays.asList(town, city, village);
        }
        if (BusStation.class.isAssignableFrom(placeClass)) {
            return Arrays.asList(bus_station);
        }
        throw new IllegalStateException("Not found type by placeClass:" + placeClass);
    }
}

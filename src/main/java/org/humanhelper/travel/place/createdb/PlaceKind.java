package org.humanhelper.travel.place.createdb;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.type.accomodation.Hotel;
import org.humanhelper.travel.place.type.region.Region;
import org.humanhelper.travel.place.type.transport.Airport;
import org.humanhelper.travel.place.type.transport.BusStation;
import org.humanhelper.travel.place.type.transport.SeaPort;

/**
 * @author Андрей
 * @since 09.01.15
 */
public enum PlaceKind {

    airport(Airport.class),
    seaport(SeaPort.class),
    bus(BusStation.class),
    region(Region.class),
    hotel(Hotel.class);

    private Class<? extends Place> clazz;

    PlaceKind(Class<? extends Place> clazz) {
        this.clazz = clazz;
    }

    public static String getName(Class<? extends Place> placeClass) {
        return getKind(placeClass).getName();
    }

    public static PlaceKind getKind(Class<? extends Place> placeClass) {
        for (PlaceKind placeKind : values()) {
            if (placeKind.getPlaceClass().equals(placeClass)) {
                return placeKind;
            }
        }
        throw new IllegalArgumentException("Not found PlaceKind by class:" + placeClass);
    }

    public Class<? extends Place> getPlaceClass() {
        return clazz;
    }

    public String getName() {
        return toString();
    }
}

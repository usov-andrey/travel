package org.humanhelper.travel.place.type.transport;

import org.humanhelper.travel.place.Place;

/**
 * @author Андрей
 * @since 06.01.15
 */
public abstract class TransportPlace extends Place {

    public static boolean isTransportPlace(Place place) {
        return place instanceof TransportPlace;
    }


}

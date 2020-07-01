package org.humanhelper.travel.route.routesearch.road;

import org.humanhelper.travel.place.Place;

import java.util.Set;

/**
 * @author Андрей
 * @since 14.10.15
 */
public class TransportPlaceSet {

    private Set<Place> transportPlaces;
    private Place parentRegion;

    public TransportPlaceSet(Set<Place> transportPlaces, Place parentRegion) {
        this.transportPlaces = transportPlaces;
        this.parentRegion = parentRegion;
    }

    public Set<Place> getTransportPlaces() {
        return transportPlaces;
    }

    /**
     * если место среди транспортных, то возвращаем parentRegion
     */
    public Place getPlace(Place place) {
        return transportPlaces.contains(place) ? parentRegion : place;
    }

}

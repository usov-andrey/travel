package org.humanhelper.travel.route.routesearch.road;

import org.humanhelper.travel.geo.BoundingBox;
import org.humanhelper.travel.geo.Location;
import org.humanhelper.travel.place.Place;

import java.util.Comparator;
import java.util.Set;

/**
 * @author Андрей
 * @since 10.10.15
 */
public class LocationPlaceComparator implements Comparator<Place> {

    private Location endCenter;

    public LocationPlaceComparator(Set<Place> places) {
        this.endCenter = getCenterOfEndPlacesLocations(places);
    }

    @Override
    public int compare(Place o1, Place o2) {
        return Double.compare(o1.getLocation().getDistance(endCenter), o2.getLocation().getDistance(endCenter));
    }

    private Location getCenterOfEndPlacesLocations(Set<Place> endPlaces) {
        BoundingBox boundingBox = new BoundingBox();
        for (Place place : endPlaces) {
            if (place.getLocation() == null) {
                throw new IllegalArgumentException("Place location is null:" + place);
            }
            boundingBox.addLocation(place.getLocation());
        }
        return boundingBox.center();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationPlaceComparator)) return false;

        LocationPlaceComparator that = (LocationPlaceComparator) o;

        return endCenter.equals(that.endCenter);

    }

    @Override
    public int hashCode() {
        return endCenter.hashCode();
    }
}

package org.humanhelper.travel.route.routesearch.road;

import org.humanhelper.travel.place.Place;

import java.util.HashMap;
import java.util.Set;

/**
 * @author Андрей
 * @since 14.10.15
 */
public class DijkstraRouteMap extends HashMap<Place, DijkstraRouteList> {

    private Set<Place> targets;

    public DijkstraRouteMap(Set<Place> targets) {
        this.targets = targets;
    }

    public void addRoute(Place source, Place target, float price) {
        DijkstraRouteList routeList = computeIfAbsent(target, place -> new DijkstraRouteList());
        routeList.addRoute(source, price);
    }

    /**
     * Нашли путь или нет
     */
    public boolean isPathFounded() {
        for (Place target : targets) {
            if (containsKey(target)) {
                return true;
            }
        }
        return false;
    }


    private DijkstraRouteList safeGet(Place place) {
        if (place == null) {
            return null;
        }
        return get(place);
    }

    public void fillPlaceSetRouteMap(PlaceRouteNetwork result, TransportPlaceSet sourcePS, TransportPlaceSet targetPS) {
        for (Place targetTransportPlace : targets) {
            fillPlaceSetRouteMap(result, targetTransportPlace, Float.MAX_VALUE, sourcePS, targetPS);
        }
    }

    private void fillPlaceSetRouteMap(PlaceRouteNetwork result, Place place, float price, TransportPlaceSet sourcePS, TransportPlaceSet targetPS) {
        DijkstraRouteList routes = safeGet(place);
        if (routes != null) {
            for (DijkstraRoute route : routes) {
                Place target = route.getPrevious();
                if (target != null) {
                    if (route.getPrice() <= price) {
                        result.addRoute(sourcePS.getPlace(target), targetPS.getPlace(place));
                        fillPlaceSetRouteMap(result, target, route.getPrice(), sourcePS, targetPS);
                    }
                }
            }
        }
    }
}

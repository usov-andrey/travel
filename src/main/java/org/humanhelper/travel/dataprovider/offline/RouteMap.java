package org.humanhelper.travel.dataprovider.offline;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.fordelete.OldPlaceService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Андрей
 * @since 27.01.15
 */
public class RouteMap {

    private List<RouteMapEntry> routes = new ArrayList<>();

    public void add(RouteMapEntry route) {
        routes.add(route);
    }

    public void add(RouteMapEntry... routes) {
        for (RouteMapEntry route : routes) {
            add(route);
        }
    }

    public void addRound(RouteMapEntry... routes) {
        add(routes);
        for (RouteMapEntry route : routes) {
            RouteMapEntry exchangeRoute = new RouteMapEntry(route);
            add(exchangeRoute);
        }
    }

    public List<RouteMapEntry> getRoutes() {
        return routes;
    }

    public RouteMapEntry getRoute(Place source, Place target, OldPlaceService placeResolver) {
        for (RouteMapEntry route : routes) {
            if (route.getSource(placeResolver).equals(source) &&
                    route.getTarget(placeResolver).equals(target)) {
                return route;
            }
        }
        throw new IllegalStateException("Not found route by source " + source + " and target " + target + " in route list:" + routes);
    }

}

package org.humanhelper.travel.route.routesearch.road;

import org.humanhelper.travel.place.Place;

import java.util.ArrayList;

/**
 * @author Андрей
 * @since 14.10.15
 */
public class DijkstraRouteList extends ArrayList<DijkstraRoute> {

    private DijkstraRoute best;

    public float getBestPrice() {
        return best.getPrice();
    }

    public DijkstraRoute getBest() {
        return best;
    }


    public void addRoute(Place source, float price) {
        DijkstraRoute route = new DijkstraRoute(price, source);
        if (best == null || best.getPrice() > price) {
            best = route;
        }
        add(route);
    }

    public boolean containsSource(Place source) {
        for (DijkstraRoute route : this) {
            if (route.getPrevious().equals(source)) {
                return true;
            }
        }
        return false;
    }
}

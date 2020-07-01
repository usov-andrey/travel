package org.humanhelper.travel.route.type.sourcetarget;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.sourcetarget.SourceTarget;
import org.humanhelper.travel.price.PriceAgent;
import org.humanhelper.travel.route.type.Route;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Список перемещений из source в target
 *
 * @author Андрей
 * @since 26.12.14
 */
public class SourceTargetRouteList<T extends Route> extends SourceTarget implements Iterable<T> {

    private PriceAgent priceAgent;

    private List<T> wayRoutes = new ArrayList<>();

    public SourceTargetRouteList(Place source, Place target, PriceAgent priceAgent) {
        super(source, target);
        this.priceAgent = priceAgent;
    }

    public void addRoute(T route) {
        wayRoutes.add(route);
    }

    public T getRoute(T route) {
        for (T wayRoute : wayRoutes) {
            if (wayRoute.equals(route)) {
                return wayRoute;
            }
        }
        return null;
    }

    public List<T> getWayRoutes() {
        return wayRoutes;
    }

    public PriceAgent getPriceAgent() {
        return priceAgent;
    }

    public void remove(T wayRoute) {
        wayRoutes.remove(wayRoute);
    }

    @Override
    public Iterator<T> iterator() {
        return wayRoutes.iterator();
    }

    @Override
    public String toString() {
        return "RouteList{" +
                "wayRoutes=" + wayRoutes +
                '}';
    }
}

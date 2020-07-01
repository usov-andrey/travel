package org.humanhelper.travel.dao.memory.dataprovider;

import org.humanhelper.travel.dataprovider.AnyTimeDataProvider;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.type.AnyTimeRoute;
import org.humanhelper.travel.route.type.sourcetarget.SourceTargetRouteList;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Андрей
 * @since 20.01.15
 */
public class MemoryFixedPriceDataProvider extends AnyTimeDataProvider {
    private String name;
    private Collection<AnyTimeRoute> routes = new ArrayList<>();

    public MemoryFixedPriceDataProvider(String name) {
        this.name = name;
        clearPlaces();
    }

    @Override
    public SourceTargetRouteList<AnyTimeRoute> getWays(Place source, Place target) {
        SourceTargetRouteList<AnyTimeRoute> result = new SourceTargetRouteList<>(source, target, this);
        for (AnyTimeRoute route : routes) {
            if (route.getSource().equals(source) && route.getTarget().equals(target)) {
                result.addRoute(route);
            }
        }
        return result;
    }

    public void addRoute(AnyTimeRoute route) {
        routes.add(route);
        addOneWayRoute(route.getSource(), route.getTarget());
    }

    @Override
    public void updatePlaces() {

    }

    @Override
    public String getName() {
        return name;
    }
}

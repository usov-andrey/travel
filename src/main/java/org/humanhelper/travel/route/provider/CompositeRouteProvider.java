package org.humanhelper.travel.route.provider;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.resolver.RoutesRequest;
import org.humanhelper.travel.route.type.AnyTimeRoute;
import org.humanhelper.travel.route.type.TimeRoute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Обьединяет маршруты из дочерних RouteProvider
 *
 * @author Андрей
 * @since 05.10.15
 */
public class CompositeRouteProvider<T extends RouteProvider> extends ArrayList<T> implements RouteProvider {

    @SafeVarargs
    public final void add(T... routeProviders) {
        for (T routeProvider : routeProviders) {
            add(routeProvider);
        }
    }

    @Override
    public Collection<TimeRoute> getTimeRoutes(RoutesRequest request) {
        Collection<TimeRoute> result = new HashSet<>();
        for (RouteProvider routeProvider : this) {
            result.addAll(routeProvider.getTimeRoutes(request));
        }
        return result;
    }

    @Override
    public Collection<AnyTimeRoute> getAnyTimeRoutes(RoutesRequest request) {
        Collection<AnyTimeRoute> result = new HashSet<>();
        for (RouteProvider routeProvider : this) {
            result.addAll(routeProvider.getAnyTimeRoutes(request));
        }
        return result;
    }

    @Override
    public Set<Place> getTargets(Place source) {
        Set<Place> result = new HashSet<>();
        for (RouteProvider routeProvider : this) {
            result.addAll(routeProvider.getTargets(source));
        }
        return result;
    }
}

package org.humanhelper.travel.route.provider;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.resolver.RoutesRequest;
import org.humanhelper.travel.route.type.AnyTimeRoute;
import org.humanhelper.travel.route.type.TimeRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

/**
 * Кэш перед каким-то RouteProvider
 *
 * @author Андрей
 * @since 15.10.15
 */
@Cacheable("CacheRoute")
@Service(CacheRouteProvider.NAME)
public class CacheRouteProvider implements RouteProvider {

    public static final String NAME = "CacheRouteProvider";//Имя класса

    @Autowired
    private MainRouteProvider provider;

    @Override
    public Collection<TimeRoute> getTimeRoutes(RoutesRequest request) {
        return provider.getTimeRoutes(request);
    }

    @Override
    public Collection<AnyTimeRoute> getAnyTimeRoutes(RoutesRequest request) {
        return provider.getAnyTimeRoutes(request);
    }

    @Override
    public Set<? extends Place> getTargets(Place source) {
        return provider.getTargets(source);
    }
}

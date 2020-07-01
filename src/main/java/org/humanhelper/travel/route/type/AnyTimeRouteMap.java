package org.humanhelper.travel.route.type;

import org.humanhelper.service.utils.CollectionHelper;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.resolver.RoutesRequest;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Хранение anytime маршрутов
 * Используется структура, где для каждого source хранится Map<Target, список маршрутов>
 * для более быстрого доступа
 *
 * @author Андрей
 * @since 05.10.15
 */
public abstract class AnyTimeRouteMap {

    private Map<Place, Entry> map = new ConcurrentHashMap<>();

    public Set<Place> getTargets(Place source) {
        return getEntry(source).getTargets();
    }

    public List<AnyTimeRoute> getRoutes(RoutesRequest request) {
        List<AnyTimeRoute> routes = new ArrayList<>();
        request.process((source, target, period) ->
                routes.addAll(getEntry(source).getRoutes(target)));
        return routes;
    }

    private Entry getEntry(Place source) {
        return map.computeIfAbsent(source, key -> {
            List<AnyTimeRoute> routes = createRoutes(key);
            Entry entry = new Entry();
            for (AnyTimeRoute route : routes) {
                Place target = route.getTarget();
                entry.targets.add(target);
                CollectionHelper.getArrayListOrCreate(entry.routesByTarget, target).add(route);
            }
            return entry;
        });
    }

    abstract protected List<AnyTimeRoute> createRoutes(Place place);

    private static class Entry {

        private Set<Place> targets = new HashSet<>();
        //Для более быстрого доступа, храним для каждого target списко маршрутов
        private Map<Place, List<AnyTimeRoute>> routesByTarget = new HashMap<>();

        public Set<Place> getTargets() {
            return targets;
        }

        public List<AnyTimeRoute> getRoutes(Place target) {
            return CollectionHelper.getSafeArrayList(routesByTarget, target);
        }
    }
}

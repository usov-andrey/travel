package org.humanhelper.travel.route.dao;

import org.humanhelper.service.utils.CollectionHelper;
import org.humanhelper.service.utils.DateHelper;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.sourcetarget.SourceTarget;
import org.humanhelper.travel.route.resolver.RoutesRequest;
import org.humanhelper.travel.route.type.AnyTimeRoute;
import org.humanhelper.travel.route.type.Route;
import org.humanhelper.travel.route.type.ScheduleTimeRoute;
import org.humanhelper.travel.route.type.TimeRoute;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Андрей
 * @since 22.12.15
 */

public class MemoryRouteDao extends AbstractOldDao {

    private Map<SourceTarget, List<AnyTimeRoute>> anyTimeWayRouteMap;
    private Map<SourceTarget, List<TimeRoute>> timeRouteMap;
    private Map<SourceTarget, List<ScheduleTimeRoute>> scheduleTimeRouteMap;
    private Map<Place, Set<Place>> targets;

    public MemoryRouteDao() {
        clear();
    }

    private <T extends Route> void add(Map<SourceTarget, List<T>> map, T route) {
        CollectionHelper.getArrayListOrCreate(map, SourceTarget.get(route)).add(route);
        CollectionHelper.getHashSetOrCreate(targets, route.getSource()).add(route.getTarget());
    }

    public void add(TimeRoute route) {
        add(timeRouteMap, route);
    }

    public void add(AnyTimeRoute route) {
        add(anyTimeWayRouteMap, route);
    }

    @Override
    public Set<Place> getTargets(Place source) {
        return CollectionHelper.getSafeHashSet(targets, source);
    }

    public void clear() {
        timeRouteMap = new ConcurrentHashMap<>();
        anyTimeWayRouteMap = new ConcurrentHashMap<>();
        scheduleTimeRouteMap = new ConcurrentHashMap<>();
        targets = new HashMap<>();
    }

    @Override
    public Collection<TimeRoute> getTimeRoutes(RoutesRequest request) {
        List<TimeRoute> result = new ArrayList<>();
        request.process((source, target, period) -> {
            for (Date day : period.dayIterator()) {
                Date nextDay = DateHelper.incDays(day, 1);
                SourceTarget sourceTarget = new SourceTarget(source, target);
                if (timeRouteMap.containsKey(sourceTarget)) {//Добавляем маршруты по времени
                    for (TimeRoute route : timeRouteMap.get(sourceTarget)) {
                        long ms = route.getStartTime().getTime();
                        if (day.getTime() < ms && nextDay.getTime() > ms) {
                            result.add(route);
                        }
                    }
                }

                //Добавляем маршруты по расписанию
                if (scheduleTimeRouteMap.containsKey(sourceTarget)) {//Добавляем маршруты по времени
                    for (ScheduleTimeRoute route : scheduleTimeRouteMap.get(sourceTarget)) {
                        TimeRoute timeRoute = route.getTimeWayRoute(day);
                        if (timeRoute != null) {
                            result.add(timeRoute);
                        }
                    }
                }
            }
        });
        return result;
    }

    @Override
    public Collection<AnyTimeRoute> getAnyTimeRoutes(RoutesRequest request) {
        List<AnyTimeRoute> result = new ArrayList<>();
        request.process((source, target, period) -> {
            SourceTarget sourceTarget = new SourceTarget(source, target);
            result.addAll(CollectionHelper.getSafeArrayList(anyTimeWayRouteMap, sourceTarget));
        });
        return result;
    }

}

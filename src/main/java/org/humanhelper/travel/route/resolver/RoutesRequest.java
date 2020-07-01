package org.humanhelper.travel.route.resolver;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.PlaceMap;
import org.humanhelper.travel.route.type.Route;
import org.humanhelper.travel.route.type.SingleRoute;
import org.humanhelper.travel.route.type.TimeRoute;
import org.humanhelper.travel.service.period.DatePeriod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Андрей
 * @since 16.12.15
 */
public class RoutesRequest {

    private Map<Place, Map<Place, DatePeriod>> entryMap = new HashMap<>();
    private DatePeriod maxPeriod;//Максимально возможный период
    private Set<String> sourceIds = new HashSet<>();
    private Set<String> targetIds = new HashSet<>();
    private PlaceMap placeMap = new PlaceMap();

    public Map<Place, Map<Place, DatePeriod>> getEntryMap() {
        return entryMap;
    }

    public void addSourceTarget(Place source, Place target, DatePeriod period) {
        if (maxPeriod == null) {
            maxPeriod = new DatePeriod(period.getStart(), period.getEnd());
        } else {
            maxPeriod.setMax(period);
        }
        sourceIds.add(source.getId());
        targetIds.add(target.getId());
        placeMap.add(source);
        placeMap.add(target);
        entryMap.computeIfAbsent(source, place -> new HashMap<>()).put(target, period);
    }

    public void process(RoutesRequestConsumer consumer) {
        for (Map.Entry<Place, Map<Place, DatePeriod>> entry : entryMap.entrySet()) {
            for (Map.Entry<Place, DatePeriod> targetEntry : entry.getValue().entrySet()) {
                consumer.accept(entry.getKey(), targetEntry.getKey(), targetEntry.getValue());
            }
        }
    }

    public String[] getSourceIds() {
        return sourceIds.toArray(new String[sourceIds.size()]);
    }

    public String[] getTargetIds() {
        return targetIds.toArray(new String[targetIds.size()]);
    }

    public boolean isValidRoute(Route route) {
        return getRoutePeriod(route.getSource(), route.getTarget()) != null;
    }

    /**
     * true, если время начала маршрута из source в target попадает в возможный период перемещения
     */
    public boolean isValidTimeRoute(TimeRoute route) {
        DatePeriod period = getRoutePeriod(route.getSource(), route.getTarget());
        return period != null && period.inPeriodDateTime(route.getStartTime());
    }

    public void fillRouteWithPlaces(SingleRoute route) {
        route.setSource(placeMap.get(route.getSource().getId()));
        route.setTarget(placeMap.get(route.getTarget().getId()));
    }

    public DatePeriod getMaxPeriod() {
        return maxPeriod;
    }

    /**
     * Возвращем period, который есть для
     */
    public DatePeriod getRoutePeriod(Place source, Place target) {
        return entryMap.get(source).get(target);
    }

}

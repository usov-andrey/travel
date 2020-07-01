package org.humanhelper.travel.route.usage;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.routesearch.query.RouteQuery;
import org.humanhelper.travel.route.routesearch.query.RouteQueryStop;
import org.humanhelper.travel.service.period.DatePeriod;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Статистика по использованию системы
 *
 * @author Андрей
 * @since 15.01.15
 */
@Service
public class UsageDao {

    private Map<Date, Integer> dayUsages = new HashMap<>();
    private Map<Place, Integer> placeUsages = new HashMap<>();


    public float getUsageCoefficient(Date date) {
        return getUsage(dayUsages, date);
    }

    public float getUsageCoefficient(Place place) {
        return getUsage(placeUsages, place);
    }

    private <T> float getUsage(Map<T, Integer> usages, T object) {
        Integer usage = usages.get(object);
        return usage != null ? usage / usages.keySet().size() : 0f;
    }

    private <T> void addUsage(Map<T, Integer> usages, T object) {
        Integer usage = usages.get(object);
        if (usage == null) {
            usages.put(object, 1);
        } else {
            usages.put(object, usage + 1);
        }
    }

    public void addUsage(Place place) {
        addUsage(placeUsages, place);
    }

    public void addUsage(Date date) {
        addUsage(dayUsages, date);
    }

    public void addRouteQuery(RouteQuery routeQuery) {
        Date currentDate = new Date();
        //Добавляем места
        Place startPlace = routeQuery.getStart().getPlace();
        Place endPlace = routeQuery.getEnd().getPlace();
        addUsage(startPlace);
        if (!startPlace.equals(endPlace)) {
            addUsage(endPlace);
        }
        for (RouteQueryStop stop : routeQuery.getStops()) {
            addUsage(stop.getPlace());
        }
        DatePeriod period = new DatePeriod(routeQuery.getStart().getPeriod().getStart(), routeQuery.getEnd().getPeriod().getEnd());
        for (Date date : period.dayIterator()) {
            addUsage(date);
        }
    }
}

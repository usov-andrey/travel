package org.humanhelper.travel.dao.memory.dataprovider;

import org.humanhelper.service.utils.DateHelper;
import org.humanhelper.travel.dataprovider.FixedTimeDataProvider;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.type.TimeRoute;
import org.humanhelper.travel.route.type.sourcetarget.SourceTargetRouteList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * @author Андрей
 * @since 20.01.15
 */
public class MemoryFloatPriceDataProvider extends FixedTimeDataProvider {

    private String name;
    private Collection<TimeRoute> routes = new ArrayList<>();

    public MemoryFloatPriceDataProvider(String name) {
        this.name = name;
        clearPlaces();
    }

    @Override
    public SourceTargetRouteList<TimeRoute> getWays(Place source, Place target, Date day) {
        SourceTargetRouteList<TimeRoute> result = new SourceTargetRouteList<>(source, target, this);
        for (TimeRoute route : routes) {
            if (route.getSource().equals(source) && route.getTarget().equals(target) &&
                    inDay(route, day)) {
                result.addRoute(route);
            }
        }
        return result;
    }

    private boolean inDay(TimeRoute route, Date day) {
        return DateHelper.setTime(day, 0, 0).before(route.getStartTime()) && DateHelper.setTime(day, 23, 59).after(route.getStartTime());
    }

    public void addRoute(TimeRoute route) {
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

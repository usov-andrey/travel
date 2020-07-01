package org.humanhelper.travel.route.dao;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.type.AnyTimeRoute;
import org.humanhelper.travel.route.type.RouteBase;
import org.humanhelper.travel.route.type.ScheduleTimeRoute;
import org.humanhelper.travel.route.type.TimeRoute;
import org.humanhelper.travel.route.type.sourcetarget.SourceTargetRouteList;
import org.humanhelper.travel.service.period.DatePeriod;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Содержит пустую реализацию старого варианта, для поддержки успешности
 * компиляции старого кода, который со временем нужно переработать и удалить
 *
 * @author Андрей
 * @since 22.12.15
 */
public abstract class AbstractOldDao implements RouteDao {

    @Override
    public void updateWays(DatePeriod datePeriod, SourceTargetRouteList<TimeRoute> ways) {
        //TODO
    }

    @Override
    public void updateAnyTimeWays(SourceTargetRouteList<AnyTimeRoute> ways) {
        //TODO
    }

    @Override
    public void updateScheduleWays(SourceTargetRouteList<ScheduleTimeRoute> ways) {
        //TODO
    }

    @Override
    public Map<Date, Integer> getFixedTimeRoutesCount() {
        return null;  //TODO
    }

    @Override
    public void delete() {
        //TODO
    }

    @Override
    public long getCount() {
        return 0;  //TODO
    }

    @Override
    public List<TimeRoute> getWays(Place source, Place target, Date startDate, Date endDate) {
        return null;  //TODO
    }

    @Override
    public Collection<AnyTimeRoute> getAnyTimeRoutes(Place source, Place target) {
        return null;  //TODO
    }

    @Override
    public Collection<ScheduleTimeRoute> getScheduleTimeRoutes(Place source, Place target) {
        return null;  //TODO
    }

    @Override
    public Collection<RouteBase> getAll() {
        return null;  //TODO
    }

}

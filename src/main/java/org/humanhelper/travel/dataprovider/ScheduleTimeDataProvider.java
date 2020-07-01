package org.humanhelper.travel.dataprovider;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.type.ScheduleTimeRoute;

import java.util.Collection;

/**
 * Перемещение задается раписанием, цена в зависимости от расписания неизменна
 *
 * @author Андрей
 * @since 24.02.15
 */
public abstract class ScheduleTimeDataProvider extends AbstractFixedPriceDataProvider<ScheduleTimeRoute> {

    @Override
    protected Collection<ScheduleTimeRoute> getExistsRoutes(Place source, Place target) {
        return routeDao.getScheduleTimeRoutes(source, target);
    }

    @Override
    public void updateWays(String source, String target) {
        routeDao.updateScheduleWays(getWays(source, target));
    }
}

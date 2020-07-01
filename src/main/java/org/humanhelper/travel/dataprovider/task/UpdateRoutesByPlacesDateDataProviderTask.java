package org.humanhelper.travel.dataprovider.task;

import org.humanhelper.travel.dataprovider.FixedTimeDataProvider;
import org.humanhelper.travel.route.dao.RouteDao;
import org.humanhelper.travel.route.type.TimeRoute;
import org.humanhelper.travel.route.type.sourcetarget.SourceTargetRouteList;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Обновить информацию
 *
 * @author Андрей
 * @since 30.05.14
 */
public class UpdateRoutesByPlacesDateDataProviderTask extends AbstractDataProviderTask<FixedTimeDataProvider> {

    @Autowired
    private RouteDao routeDao;
    @Autowired
    private UpdateRoutesCounter counter;

    private DayRoutes dayRoutes;

    public UpdateRoutesByPlacesDateDataProviderTask() {
    }

    @Override
    protected void doRun() {
        String source = dayRoutes.getSource();
        String target = dayRoutes.getTarget();
        SourceTargetRouteList<TimeRoute> ways = getDataProvider().getWays(source, target, dayRoutes.getDay());
        counter.addCounter(dayRoutes, !ways.getWayRoutes().isEmpty());
        routeDao.updateWays(dayRoutes.calculateDayDatePeriod(), ways);
    }

    public DayRoutes getDayRoutes() {
        return dayRoutes;
    }

    public void setDayRoutes(DayRoutes dayRoutes) {
        this.dayRoutes = dayRoutes;
    }

    @Override
    public String toString() {
        return "UpdateRoutesByPlacesDateTask{" +
                "dayRoutes=" + dayRoutes +
                '}';
    }
}

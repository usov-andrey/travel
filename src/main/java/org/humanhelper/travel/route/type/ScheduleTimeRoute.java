package org.humanhelper.travel.route.type;

import org.humanhelper.service.utils.DateHelper;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.price.PriceResolver;
import org.humanhelper.travel.service.frequency.Frequency;
import org.humanhelper.travel.service.period.DatePeriod;
import org.humanhelper.travel.transport.Transport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Перемещение по расписанию
 *
 * @author Андрей
 * @since 21.01.15
 */
public class ScheduleTimeRoute extends FixedTimeRoute {

    private Frequency frequency;//Частота по дням

    public ScheduleTimeRoute() {
    }

    public ScheduleTimeRoute(Place source, PriceResolver priceResolver, Place target, Transport transport, Date startTime, Date endTime) {
        super(source, priceResolver, target, transport, startTime, endTime);
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    /**
     * @return Список TimeWayRoute которые удовлетворяют периоду startDate и endDate
     */
    public List<TimeRoute> getTimeWayRoutes(Date startDate, Date endDate) {
        List<TimeRoute> routes = new ArrayList<>();
        for (Date day : new DatePeriod(startDate, endDate).dayIterator()) {
            TimeRoute route = getTimeWayRoute(day);
            if (route != null) {
                routes.add(route);
            }
        }
        return routes;
    }

    /**
     * Если маршрут в этот день есть, то создаем его и возвращем, иначе возвращаем null
     */
    public TimeRoute getTimeWayRoute(Date day) {
        if (frequency.inDay(day)) {
            TimeRoute route = copy();
            //Только у нас хранится в startTime и endTime только время, нужно задать текущйи день
            route.setStartTime(DateHelper.setTime(day, this.startTime));
            route.setEndTime(DateHelper.setTime(day, this.endTime));
            //Если endTime меньше startTime, то это значит, чтобы мы приходим на следующий день
            if (this.endTime.before(this.startTime)) {
                route.setEndTime(DateHelper.incDays(route.getEndTime(), 1));
            }
            return route;
        }
        return null;
    }
}

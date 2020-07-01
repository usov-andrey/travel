package org.humanhelper.travel.route.dao;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.resolver.RoutesRequest;
import org.humanhelper.travel.route.type.AnyTimeRoute;
import org.humanhelper.travel.route.type.RouteBase;
import org.humanhelper.travel.route.type.ScheduleTimeRoute;
import org.humanhelper.travel.route.type.TimeRoute;
import org.humanhelper.travel.route.type.sourcetarget.SourceTargetRouteList;
import org.humanhelper.travel.service.period.DatePeriod;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * У нас есть места где можно останавливаться и есть транзитные места, в которых останавливаться нельзя
 * Для каждого места в котором можно останавливаться задается список ближайщих транзитных мест
 *
 * @author Андрей
 * @since 11.05.14
 */
@RequestMapping("routeDao")
public interface RouteDao {


    /**
     * Нужно найти все перемещения за период datePeriod из source в target.
     * Если найденное перемещение присутствует в ways, то нужно обновить цену и ticketAgent
     * Если найденное перемещение не присутствует в ways и в найденном перемещении
     * агент равен ticketAgent, то нужно обнулить цену(это значит, что билеты от этого агента на этот рейс закончились)
     * Все необработанные ways нужно добавить как новые перемещения
     */
    void updateWays(DatePeriod datePeriod, SourceTargetRouteList<TimeRoute> ways);

    void updateAnyTimeWays(SourceTargetRouteList<AnyTimeRoute> ways);

    void updateScheduleWays(SourceTargetRouteList<ScheduleTimeRoute> ways);

    /**
     * Количество маршрутов за день
     */
    Map<Date, Integer> getFixedTimeRoutesCount();

    void delete();

    long getCount();

    List<TimeRoute> getWays(Place source, Place target, Date startDate, Date endDate);

    Collection<AnyTimeRoute> getAnyTimeRoutes(Place source, Place target);

    Collection<ScheduleTimeRoute> getScheduleTimeRoutes(Place source, Place target);

    Collection<RouteBase> getAll();

    /**
     * Time маршруты согласно запросу request(также должен делаться запрос на получение ScheduledTime маршрутов и
     * на основе них должны создаваться новые Time маршруты)
     */
    Collection<TimeRoute> getTimeRoutes(RoutesRequest request);

    /**
     * AnyTime маршруты согласно запросу request
     */
    Collection<AnyTimeRoute> getAnyTimeRoutes(RoutesRequest request);


    Set<? extends Place> getTargets(Place source);

}

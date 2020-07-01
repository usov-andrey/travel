package org.humanhelper.travel.route.resolver;

import org.humanhelper.service.utils.DateHelper;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.provider.RouteProvider;
import org.humanhelper.travel.route.provider.RouteProviderStore;
import org.humanhelper.travel.route.routesearch.query.RouteQuery;
import org.humanhelper.travel.route.routesearch.road.PlaceRouteNetwork;
import org.humanhelper.travel.route.routesearch.time.FixedTimeWayFromAnyWayRoute;
import org.humanhelper.travel.route.type.AnyTimeRoute;
import org.humanhelper.travel.route.type.TimeRoute;
import org.humanhelper.travel.service.period.DatePeriod;

import java.util.*;

/**
 * Получение маршрутов между набором мест
 *
 * @author Андрей
 * @since 04.10.15
 */
public class PlaceSetRoutesResolver {

    private Map<Place, PlaceTimeRoutes> routesMap = new HashMap<>();
    private RouteProvider routeProvider;

    public PlaceSetRoutesResolver() {
        this.routeProvider = RouteProviderStore.getMainRouteProvider();
    }

    /**
     * В исходном запросе может быть задан регион, а не реальное место из маршрута
     */
    public PlaceTimeRoutes routes(Place place) {
        if (routesMap.containsKey(place)) {
            return routesMap.get(place);
        } else {
            if (place.getRegion() == null) {
                throw new IllegalStateException("Not found routes by place:" + place);
            }
            return routes(place.getRegion());
        }
    }

    public void fillRoutes(RouteQuery query, PlaceRouteNetwork routeNetwork) {
        //Формируем запрос на получение маршрутов
        RoutesRequest routesRequest = getRoutesRequest(query, routeNetwork);
        //Добавляем Time маршруты
        addTimeRoutes(routeProvider.getTimeRoutes(routesRequest));
        //Добавляем AnyTime маршруты
        addAnyTimeRoutes(routeProvider.getAnyTimeRoutes(routesRequest),
                query.getStart().getPeriod().getStart(), query.getEnd().getPeriod().getEnd());
    }

    //------------------------Routes Request ------------------------------

    /**
     * Запрос на получение маршрутов согласно дорожной сетки
     */
    private RoutesRequest getRoutesRequest(RouteQuery query, PlaceRouteNetwork routeNetwork) {
        final DatePeriod startPeriod = query.getStart().getPeriod();
        final DatePeriod endPeriod = query.getEnd().getPeriod();
        final DatePeriod fullPeriod = new DatePeriod(startPeriod.getStart(), endPeriod.getEnd());
        //Проходим по дорожной сетки и формируем запросы на получение маршрутов
        RoutesRequest timeRoutesRequest = new RoutesRequest();
        routeNetwork.process(query.getStart().getPlace(),
                (source, target) -> {
                    if (source.equals(query.getStart().getPlace())) {
                        addSourceTargetToRequest(timeRoutesRequest, source, target, startPeriod);
                    } else if (target.equals(query.getEnd().getPlace())) {
                        addSourceTargetToRequest(timeRoutesRequest, source, target, endPeriod);
                    } else {
                        addSourceTargetToRequest(timeRoutesRequest, source, target, fullPeriod);
                    }
                }
        );
        return timeRoutesRequest;
    }

    private void addSourceTargetToRequest(RoutesRequest timeRoutesRequest, Place source, Place target, DatePeriod period) {
        for (Place sourceTP : source.transportPlaces()) {
            for (Place targetTP : target.transportPlaces()) {
                if (!sourceTP.equals(targetTP)) {
                    timeRoutesRequest.addSourceTarget(source, target, period);
                }
            }
        }
    }

    //--------------Time Routes---------------------------

    private void addTimeRoutes(Collection<TimeRoute> routes) {
        for (TimeRoute route : routes) {
            getOrCreateRoutes(route.getSource()).addOutgoing(route);
            getOrCreateRoutes(route.getTarget()).addIncoming(route);
        }
    }

    private PlaceTimeRoutes getOrCreateRoutes(Place place) {
        return routesMap.computeIfAbsent(place, key -> new PlaceTimeRoutes());
    }

    //------------ Any Time-----------------------

    /**
     * Добавляем AnyTime перемещения
     * Основная идея, что у нас уже есть граф перемещений, нужно в него добавить перемещения AnyTime
     * привязав к уже существующим перемещениям по времени
     */
    private void addAnyTimeRoutes(Collection<AnyTimeRoute> anyTimeWayRoutes, Date start, Date end) {
        for (AnyTimeRoute anyTimeWayRoute : anyTimeWayRoutes) {
            //У нас есть исходная точка и конечная точка
            PlaceTimeRoutes sourceRoutes = routes(anyTimeWayRoute.getSource());
            PlaceTimeRoutes targetRoutes = routes(anyTimeWayRoute.getTarget());

            List<TimeRoute> sourceIncomingRoutes = sourceRoutes.getIncoming();
            List<TimeRoute> targetIncomingRoutes = targetRoutes.getIncoming();
            List<TimeRoute> sourceOutgoingRoutes = sourceRoutes.getOutgoing();
            List<TimeRoute> targetOutgoingRoutes = targetRoutes.getOutgoing();

            //Нужно взять все входящие ребра(incoming) в source,
            //и создать перемещение source(incomingRoute.target) -> target
            //Это перемещение необходимо добавить во входящие ребра(incoming) в target и в исходящие из source
            for (TimeRoute incomingRoute : sourceIncomingRoutes) {
                //Добавлять цикл смысла нет
                if (incomingRoute.getSource().equals(anyTimeWayRoute.getTarget()) || (incomingRoute instanceof FixedTimeWayFromAnyWayRoute)) {
                    continue;
                }
                Date endTime = incomingRoute.getTarget().getFirstDepartTime(incomingRoute.getEndTime());
                if (endTime.before(end)) {
                    FixedTimeWayFromAnyWayRoute newTimedWayRoute = new FixedTimeWayFromAnyWayRoute(anyTimeWayRoute.getSource(), anyTimeWayRoute.getPriceResolver(),
                            anyTimeWayRoute.getTarget(), anyTimeWayRoute.getTransport(), endTime, DateHelper.incMinutes(endTime, anyTimeWayRoute.getDurationInMinutes())
                    );

                    targetIncomingRoutes.add(newTimedWayRoute);
                    sourceOutgoingRoutes.add(newTimedWayRoute);
                }
            }
            //Нужно взять все исходящие ребра(outgoing) из target
            //и создать перемещение source -> target ( outgoing.source)
            //и добавить это перемещение в исходящие из source и во входящие в target
            for (TimeRoute outgoingRoute : targetOutgoingRoutes) {
                //Избегаем цикла
                if (outgoingRoute.getTarget().equals(anyTimeWayRoute.getSource()) || (outgoingRoute instanceof FixedTimeWayFromAnyWayRoute)) {
                    continue;
                }
                Date startTime = outgoingRoute.getSource().getLastArriveTime(outgoingRoute.getStartTime());
                if (startTime.after(start)) {
                    FixedTimeWayFromAnyWayRoute newTimedWayRoute = new FixedTimeWayFromAnyWayRoute(anyTimeWayRoute.getSource(), anyTimeWayRoute.getPriceResolver(),
                            anyTimeWayRoute.getTarget(), anyTimeWayRoute.getTransport(), DateHelper.incMinutes(startTime, -anyTimeWayRoute.getDurationInMinutes()), startTime
                    );

                    sourceOutgoingRoutes.add(newTimedWayRoute);
                    targetIncomingRoutes.add(newTimedWayRoute);
                }
            }
        }
    }

}

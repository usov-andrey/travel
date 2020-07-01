package org.humanhelper.travel.route.dao;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.humanhelper.travel.dao.elastic.AbstractElasticCRUDDao;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.dao.PlaceDao;
import org.humanhelper.travel.price.PriceAgent;
import org.humanhelper.travel.price.PriceResolver;
import org.humanhelper.travel.route.resolver.RoutesRequest;
import org.humanhelper.travel.route.type.*;
import org.humanhelper.travel.route.type.sourcetarget.SourceTargetRouteList;
import org.humanhelper.travel.service.period.DatePeriod;
import org.humanhelper.travel.transport.Transport;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

import static org.elasticsearch.index.query.FilterBuilders.*;

/**
 * @author Андрей
 * @since 14.01.15
 */
public class ElasticRouteDao extends AbstractElasticCRUDDao<RouteBase> implements RouteDao {

    @Autowired
    private PlaceDao placeDao;

    public ElasticRouteDao(Client client) {
        super("routes", "route", RouteBase.class, client);
    }

    @Override
    protected void createMapping(XContentBuilder builder) throws IOException {
        builder.
                startObject("_id").
                field("path", "id").
                endObject().
                startObject("properties").
                startObject("kind").
                field("type", "string").
                //field("store", true).
                        field("index", "not_analyzed").
                endObject().
                startObject(FixedTimeRoute.SOURCE_ID_FIELD).
                field("type", "string").
                //field("store", true).
                        field("index", "not_analyzed").
                endObject().
                startObject(FixedTimeRoute.TARGET_ID_FIELD).
                field("type", "string").
                //field("store", true).
                        field("index", "not_analyzed").
                endObject().
                startObject(FixedTimeRoute.START_TIME_FIELD).
                field("type", "date").
                endObject().
                startObject(FixedTimeRoute.END_TIME_FIELD).
                field("type", "string").
                endObject().
                startObject(FixedTimeRoute.TRANSPORT_FIELD).
                field("type", "nested").
                startObject("properties").
                startObject(Transport.NAME_FIELD).
                field("type", "string").
                //field("store", true).
                        field("index", "not_analyzed").
                endObject().
                startObject(Transport.COMPANY_FIELD).
                field("type", "string").
                //field("store", true).
                        field("index", "not_analyzed").
                endObject().
                endObject().
                endObject().
                startObject(FixedTimeRoute.PRICE_RESOLVER_FIELD).
                field("type", "nested").
                startObject("properties").
                startObject(PriceResolver.PRICE_FIELD).
                field("type", "float").
                endObject().
                startObject(PriceResolver.AGENT_FIELD).
                field("type", "string").
                //field("store", true).
                        field("index", "not_analyzed").
                endObject().
                startObject(PriceResolver.CURRENCY_FIELD).
                field("type", "string").
                //field("store", true).
                        field("index", "not_analyzed").
                endObject().
                endObject().
                endObject().
                endObject()
        ;
    }

    public Collection<ScheduleTimeRoute> getScheduleTimeRoutes(Place source, Place target) {
        return getScrolledCollection(searchRequestBuilder(boolFilter()
                .must(termFilter(ActivityBase.KIND_FIELD, ActivityBase.SCHEDULE_KIND))
                .must(termFilter(FixedTimeRoute.SOURCE_ID_FIELD, source.getId()))
                .must(termFilter(FixedTimeRoute.TARGET_ID_FIELD, target.getId()))
        ));
    }


    public List<TimeRoute> getWays(Place source, Place target, Date startTime, Date endTime) {
        return getScrolledCollection(searchRequestBuilder(boolFilter()
                .must(termFilter(FixedTimeRoute.SOURCE_ID_FIELD, source.getId())).must(rangeFilter(TimeActivity.START_TIME_FIELD).gt(startTime).lt(endTime))
                .must(termFilter(FixedTimeRoute.TARGET_ID_FIELD, target.getId()))
        ));
    }

    @Override
    public Collection<AnyTimeRoute> getAnyTimeRoutes(Place source, Place target) {
        return getScrolledCollection(searchRequestBuilder(boolFilter()
                .must(termFilter(ActivityBase.KIND_FIELD, ActivityBase.ANY_KIND))
                .must(termFilter(FixedTimeRoute.SOURCE_ID_FIELD, source.getId()))
                .must(termFilter(FixedTimeRoute.TARGET_ID_FIELD, target.getId()))
        ));
    }

    @Override
    public void updateAnyTimeWays(SourceTargetRouteList<AnyTimeRoute> ways) {
        Place source = ways.getSource();
        Place target = ways.getTarget();
        //Всегда приходит только один маршрут
        if (ways.getWayRoutes().isEmpty()) {
            throw new IllegalStateException("Illegal ways:" + ways);
        }
        AnyTimeRoute newRoute = ways.getWayRoutes().get(0);
        Collection<AnyTimeRoute> routesInDB = getAnyTimeRoutes(source, target);
        for (AnyTimeRoute route : routesInDB) {
            //От одного агента может быть только одно перемещение между source и target
            if (route.getPriceResolver().getPriceAgent().equals(ways.getPriceAgent())) {
                if (newRoute == null) {
                    throw new IllegalStateException("Founded many routes by source and target and price agent:" + routesInDB + " by ways:" + ways);
                }
                route.setDurationInMinutes(newRoute.getDurationInMinutes());
                route.setPriceResolver(newRoute.getPriceResolver());
                update(route.getId(), route);
                newRoute = null;
            }
        }
        //Если не нашли существующий, то добавляем
        if (newRoute != null) {
            insert(newRoute);
        }
        placeDao.addRoad(source, target);
    }

    @Override
    public void updateScheduleWays(SourceTargetRouteList<ScheduleTimeRoute> ways) {
        //Для ScheduleWays устанавливаем дату минимальной, чтобы она не когда не находилась через поиск по start/endTime
        Place source = ways.getSource();
        Place target = ways.getTarget();
        Collection<ScheduleTimeRoute> routesInDB = getScheduleTimeRoutes(source, target);
        for (ScheduleTimeRoute routeInDB : routesInDB) {
            ScheduleTimeRoute newRoute = ways.getRoute(routeInDB);
            if (newRoute != null) {
                ways.remove(newRoute);
                newRoute.setId(routeInDB.getId());
                update(newRoute.getId(), newRoute);
            } else if (routeInDB.getPriceResolver().equals(ways.getPriceAgent())) {
                throw new IllegalStateException("In db have route " + routeInDB + " but in input ways no have:" + ways);
            }
        }
        //Добавляем новые найденные перемещения
        for (ScheduleTimeRoute way : ways) {
            insert(way);
        }
        placeDao.addRoad(ways.getSource(), ways.getTarget());
    }

    @Override
    public void updateWays(DatePeriod datePeriod, SourceTargetRouteList<TimeRoute> ways) {
        /**
         Нужно найти все перемещения за период datePeriod из source в target.
         * Если найденное перемещение присутствует в ways, то нужно обновить цену и ticketAgent
         * Если найденное перемещение не присутствует в ways и в найденном перемещении
         * агент равен ticketAgent, то нужно обнулить цену(это значит, что билеты от этого агента на этот рейс закончились)
         * Все необработанные ways нужно добавить как новые перемещения
         */
        Place source = ways.getSource();
        Place target = ways.getTarget();

        for (TimeRoute routeInDB : getWays(source, target, datePeriod.getStart(), datePeriod.getEnd())) {
            TimeRoute newRoute = ways.getRoute(routeInDB);
            if (newRoute != null) {
                updateExistsWay(routeInDB, newRoute);
                ways.remove(newRoute);
            } else {
                if (routeInDB.getPriceResolver().equals(ways.getPriceAgent())) {
                    //Билеты закончились, нужно выставить цену 0
                    updatePrice(routeInDB, 0, routeInDB.getPriceResolver().getCurrency());
                }
            }
        }
        //Добавляем новые найденные перемещения
        for (TimeRoute way : ways) {
            insert((RouteBase) way);
        }
        placeDao.addRoad(source, target);
    }


    private void updateExistsWay(TimeRoute routeInDB, TimeRoute newRoute) {
        //Значит уже есть такое перемещение с какой-то ценой
        //Если agent совпадают, то нужно обновить цену
        //Если agent не совпадают, то нужно обновить, только если цена от этого агента меньше
        float newPrice = newRoute.getPriceResolver().getPrice();
        PriceAgent newPriceAgent = newRoute.getPriceResolver().getPriceAgent();
        if (routeInDB.getPriceResolver().equals(newPriceAgent)) {
            updatePrice(routeInDB, newPrice, newRoute.getPriceResolver().getCurrency());
        } else {
            if (newPrice < routeInDB.getPriceResolver().getPrice()) {
                log.debug("Update price agent:" + newPrice + " " + newPriceAgent);
                routeInDB.setPriceResolver(newRoute.getPriceResolver());
                update(routeInDB.getId(), (RouteBase) routeInDB);
            }
        }
    }

    private void updatePrice(TimeRoute routeInDB, float price, Currency currency) {
        routeInDB.getPriceResolver().setPrice(price);
        routeInDB.getPriceResolver().setCurrency(currency);
        update(routeInDB.getId(), (RouteBase) routeInDB);
    }

    @Override
    public Map<Date, Integer> getFixedTimeRoutesCount() {
        return new HashMap<>();
    }

    //--------------------------- Получение Fixed, Any, Schedule маршрутов из базы ---------------------------

    private BoolFilterBuilder getRoutesFilterBuilder(String kind, String[] sourceIds, String[] targetIds) {
        return boolFilter()
                .must(termFilter(ActivityBase.KIND_FIELD, kind))
                .must(idsFilter(FixedTimeRoute.SOURCE_ID_FIELD).addIds(sourceIds))
                .must(idsFilter(FixedTimeRoute.TARGET_ID_FIELD).addIds(targetIds));
    }

    @Override
    public Collection<AnyTimeRoute> getAnyTimeRoutes(RoutesRequest request) {
        //Получаем список всех возможных source и target
        //Получаем данные из базы на основе этой фильтрации
        //Для каждого полученного маршрута проверяем, а точно ли его source+target есть в запросе

        Collection<AnyTimeRoute> routesInDb = getScrolledCollection(searchRequestBuilder(getRoutesFilterBuilder(ActivityBase.ANY_KIND,
                request.getSourceIds(), request.getTargetIds())));
        Collection<AnyTimeRoute> result = new ArrayList<>();
        //Если source и target есть в request
        routesInDb.stream()
                .filter(request::isValidRoute)
                .forEach(route -> {
                    request.fillRouteWithPlaces(route);
                    result.add(route);
                });
        return result;
    }

    @Override
    public Collection<TimeRoute> getTimeRoutes(RoutesRequest request) {
        //Получаем из базы
        DatePeriod period = request.getMaxPeriod();
        Collection<FixedTimeRoute> timeRoutes = getScrolledCollection(searchRequestBuilder(getRoutesFilterBuilder(ActivityBase.FIXED_KIND,
                request.getSourceIds(), request.getTargetIds())
                .must(rangeFilter(TimeActivity.END_TIME_FIELD).gte(period.getStart()).lte(period.getEnd()))
        ));
        Collection<ScheduleTimeRoute> scheduleTimeRoutes = getScrolledCollection(searchRequestBuilder(
                getRoutesFilterBuilder(ActivityBase.SCHEDULE_KIND, request.getSourceIds(), request.getTargetIds())));
        Collection<TimeRoute> result = new ArrayList<>();

        //Добавляем в результат fixed маршруты
        timeRoutes.stream()
                .filter(request::isValidTimeRoute)
                .forEach(route -> {
                    request.fillRouteWithPlaces(route);
                    result.add(route);
                });

        //Добавляем в результат schedule маршруты
        for (ScheduleTimeRoute route : scheduleTimeRoutes) {
            DatePeriod routePeriod = request.getRoutePeriod(route.getSource(), route.getTarget());
            if (routePeriod != null) {
                result.addAll(route.getTimeWayRoutes(routePeriod.getStart(), routePeriod.getEnd()));
            }
        }

        return result;
    }

    @Override
    public Set<? extends Place> getTargets(Place source) {
        //Храним targets в виде поля в source
        Set<? extends Place> targets = source.getTargets();
        return targets != null ? targets : Collections.emptySet();
    }
}

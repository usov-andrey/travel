package org.humanhelper.travel.route.routesearch.time.search;

import org.humanhelper.travel.route.type.CompositeActivity;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Андрей
 * @since 25.10.15
 */
public class BestPriceRouteSearchResult extends PathRouteSearchResult {

    private Map<Integer, CompositeActivity> routes = new LinkedHashMap<>();//Map<route.getStopsHashcode(), routeWithPrice>
    private long time = 0;
    private int count = 0;

    public void addRoute(Path path) {
        long ms = System.nanoTime();
        count++;
        //Теперь нужно определить найденый маршрут лучше или хуже
        //Если уже есть маршрут с такой же последовательностью остановок(без остановок в транзитных местах), то нужно заменить или оставить существующий
        //Если такой последовательности нет, то если найден лучше по стоимости вариант, то он должен вытеснить самый дорогой
        int routeStopsHashCode = path.calculateStopsHashCode();

        CompositeActivity route = path.createRoute();
        //System.out.println("Found route:"+route);
        Float routePrice = path.getPrice();//В принципе нам неважно сколько ночей оставаться, главное оптимизировать стоимость перемещений
        CompositeActivity existsRouteWithPrice = routes.get(routeStopsHashCode);
        if (existsRouteWithPrice != null) {
            if (routePrice >= existsRouteWithPrice.getPrice()) {
                //log.debug("For route with price " + routePrice + " already exists more cheap route " + existsRouteWithPrice.getPrice());
                return;
            } else {
                //System.out.println("Rewrite Route:" + existsRouteWithPrice + " By Route:" + new RouteWithPrice(route, routePrice));
                routes.put(routeStopsHashCode, route);
            }
        } else {
            //System.out.println("Add new Route:" + new RouteWithPrice(route, routePrice));
            routes.put(routeStopsHashCode, route);
        }

        //Новый маршрут или существующий хуже, добавляем

        //addRoute(route);
        time = time + (System.nanoTime() - ms);
    }

    @Override
    protected Collection<CompositeActivity> getRoutes() {
        return routes.values();
    }

    @Override
    public void printStatistics(Logger logger) {
        logger.debug("Time for find:" + (time / 1000000));
        super.printStatistics(logger);
    }
}

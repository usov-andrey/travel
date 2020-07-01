package org.humanhelper.travel.route.routesearch;

import org.humanhelper.travel.route.Activity;
import org.humanhelper.travel.route.type.CompositeActivity;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Результат поиска
 * Для каждой последовательности посещения мест остановок вычисляет минимальные по стоимости маршруты
 *
 * @author Андрей
 * @since 13.05.14
 */
public class RouteSearchResult {

    private float minPriceWithoutStop = Float.MAX_VALUE;
    private List<CompositeActivity> routes = new ArrayList<>();

    public void addRouteWithMinPriceWithoutStop(CompositeActivity route, float priceWithoutStop) {
        routes.add(route);
        if (minPriceWithoutStop > priceWithoutStop) {
            minPriceWithoutStop = priceWithoutStop;
        }
    }

    public void setResult(RouteSearchResult result) {
        this.minPriceWithoutStop = result.minPriceWithoutStop;
        this.routes = result.routes;
    }

    protected Collection<CompositeActivity> getRoutes() {
        return routes;
    }

    public List<CompositeActivity> getRouteList() {
        List<CompositeActivity> result = new ArrayList<>(getRoutes());
        Collections.sort(result, (o1, o2) ->
                new Float(o1.getPrice() - o2.getPrice()).intValue());
        return result;
    }

    public void printStatistics(Logger logger) {
        logger.info("Found Routes:" + getRoutes().size());
        int maxCount = 100;
        int i = 0;
		/*
		//Выводим общую информацию о 100 лучших маршрутах

		for (CompositeActivity route : getRouteList()) {
			logger.info(route.getQuickInfo());
			if (i == maxCount) {
				break;
			}
			i++;
		} */
        //Выводим более подробно о лучших 10 маршрутах
        i = 0;
        maxCount = 10;
        for (CompositeActivity route : getRouteList()) {
            //System.out.println("Route:" + route.getSum());
            logger.info(route.getQuickInfo());
            for (Activity item : route.getRouteItems()) {
                logger.info(item.toString());
            }
            if (i == maxCount) {
                break;
            }
            i++;
        }
    }

    public float getMinPriceWithoutStop() {
        return minPriceWithoutStop;
    }

    public int size() {
        return routes.size();
    }
}

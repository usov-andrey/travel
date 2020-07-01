package org.humanhelper.travel.route.routesearch.time.search;

import org.humanhelper.travel.route.Activity;
import org.humanhelper.travel.route.ActivityList;
import org.humanhelper.travel.route.routesearch.query.RouteQuery;
import org.humanhelper.travel.route.routesearch.query.RouteQueryStop;
import org.humanhelper.travel.route.routesearch.time.search.vertex.PathVertex;
import org.humanhelper.travel.route.type.CompositeActivity;

import java.util.*;

/**
 * Путь из себя представляет последовательность остановок, хранится ссылка на последнюю остановку
 * В остановке хранится ссылка на предыдущую остановку и на подпуть после этой остановки
 * подпуть(SubPath) - последовательность перемещений после этой остновки
 *
 * @author Андрей
 * @since 05.12.14
 */
public class Path {

    protected Set<RouteQueryStop> availableStops;//Остановки, которые необходимо сделать еще в рамках этого маршрута
    private Set<Integer> pathVertexCodes;

    private PathVertex lastStop;

    private Map<Integer, Float> routeStopPricesMap = new HashMap<>();//Map<хэш код предыдущих остановок без дат, стоимость>
    //private Map<Integer, Float> routeStopPricesMap = new HashMap<>();

    public Path(RouteQuery query) {
        availableStops = new HashSet<>(query.getStops());
        pathVertexCodes = new HashSet<>();
    }

    public boolean isVisitedAllPathVertexs() {
        return availableStops.isEmpty() && pathVertexCodes.isEmpty();
    }

    public CompositeActivity createRoute() {
        ActivityList route = new ActivityList();
        lastStop.fillRoute(route);
        return new CompositeActivity(route, getPrice(route));
    }

    private float getPrice(List<Activity> route) {
        float cost = 0;
        for (Activity item : route) {
            cost += item.getPriceResolver().getPrice();
        }
        return cost;
    }

    /**
     * @return если остановка в этом месте еще не делалась
     */
    public boolean addStop(RouteQueryStop stop) {
        return availableStops.remove(stop);
    }

    public void removeStop(RouteQueryStop stop) {
        availableStops.add(stop);
    }

    public boolean addPathVertexForVisit(Integer code) {
        return pathVertexCodes.add(code);
    }

    public boolean removeVisitedPathVertex(Integer code) {
        return pathVertexCodes.remove(code);
    }

    /**
     * Возвращаем предыдущее место остановки
     */
    public PathVertex setStopVertex(PathVertex stopVertex) {
        PathVertex lastStop = this.lastStop;
        this.lastStop = stopVertex;
        return lastStop;
    }

    public SubPath getSubPath() {
        return lastStop.getSubPath();
    }

    /**
     * В подсчете используются только StopPlaceVertex вершины
     */
    public int calculateStopsHashCode() {
        return lastStop.getStopsHashCode();
    }

    /**
     * В подсчете используются все PathVertex вершины(Stop, StartDiscount, EndDiscount)
     */
    public int calculatePathHashCode() {
        return lastStop.getPathHashCode();
    }

    /**
     * @return цена маршрута без учета стоимости количества ночей в отеле(берется одна ночь)
     */
    public float getPrice() {
        return lastStop.getPrice();
    }

    /**
     * Общая стоимость подпутей
     */
    public float getSubPrice() {
        return lastStop.getSubPrice();
    }

    public String toString() {
        return lastStop.pathToString();
    }

    public boolean isBestRouteToThisPlace() {
        //Если мы пришли в какую-то остановку и ранее при обходе мы уже были здесь с такой же последовательностью остановок
        //И если раньше цена была меньше, чем текущая цена, то дальше не идем

        int routeStopsHashCode = calculatePathHashCode();//lastStop.getPlaceHashCodeWithoutDate();
        float price = getSubPrice();//Цена без учета количества ночей
        if (routeStopPricesMap.containsKey(routeStopsHashCode)) {
            Float existsCost = routeStopPricesMap.get(routeStopsHashCode);
            if (existsCost != null) {
                if (price >= existsCost) {
                    //System.out.println("return current cost " + averageCost + " bigger or equals than before " + existsCost);
                    return false;
                }
            }
        }
        routeStopPricesMap.put(routeStopsHashCode, price);
        return true;
    }

}

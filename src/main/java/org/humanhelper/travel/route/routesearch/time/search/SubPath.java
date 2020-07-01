package org.humanhelper.travel.route.routesearch.time.search;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.Activity;
import org.humanhelper.travel.route.routesearch.time.search.vertex.SubPathVertex;
import org.humanhelper.travel.route.routesearch.time.search.vertex.TransitVertex;
import org.humanhelper.travel.route.routesearch.time.search.vertex.WayVertex;
import org.humanhelper.travel.route.type.TimeRoute;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Перемещения между остановками
 *
 * @author Андрей
 * @since 05.12.14
 */
public class SubPath {

    private Set<Place> places;
    private LinkedList<SubPathVertex> vertexes;//Путь после текущей остановки
    private int transitCount;

    public SubPath() {
        vertexes = new LinkedList<>();
        transitCount = 0;
        places = new HashSet<>();
    }

    public void clear() {
        vertexes.clear();
        transitCount = 0;
        places.clear();
    }

    public void addWay(WayVertex vertex) {
        vertexes.add(vertex);
        places.add(vertex.getRouteItem().getSource());
    }

    public void removeWay(WayVertex vertex) {
        vertexes.removeLast();
        places.remove(vertex.getRouteItem().getSource());
    }

    public boolean canUseThisWay(TimeRoute way) {
        return !places.contains(way.getTarget());
    }

    //---------Логика по транзитам----------

    public boolean canTransit() {
        return transitCount < 2;
    }

    public void addTransit(TransitVertex vertex) {
        vertexes.add(vertex);
        transitCount++;
    }

    public void removeTransit(TransitVertex vertex) {
        vertexes.removeLast();
        transitCount--;
    }

    public void fillRoute(Collection<Activity> route) {
        for (SubPathVertex vertex : vertexes) {
            route.add(vertex.getRouteItem());
        }
    }

    public float getPrice() {
        float cost = 0;
        for (SubPathVertex vertex : vertexes) {
            cost += vertex.getRouteItem().getPriceResolver().getPrice();
        }
        return cost;
    }

    @Override
    public String toString() {
        return vertexes.toString();
    }

}

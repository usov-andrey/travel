package org.humanhelper.travel.route.routesearch.road;

import org.humanhelper.travel.place.Place;

/**
 * Найденный путь из текущей вершины во все другие
 *
 * @author Андрей
 * @since 11.12.14
 */
public class DijkstraRoute {

    private float price;
    private Place previous;

    public DijkstraRoute(float price, Place previous) {
        this.price = price;
        this.previous = previous;
    }

    public float getPrice() {
        return price;
    }

    public Place getPrevious() {
        return previous;
    }
}

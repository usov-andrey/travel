package org.humanhelper.travel.route.routesearch.query;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.service.period.DatePeriod;

/**
 * Исходная или конечная точка в запросе маршрута
 *
 * @author Андрей
 * @since 27.12.14
 */
public class RouteQueryPlace {

    private Place place;
    private DatePeriod period;

    public RouteQueryPlace() {
    }

    public RouteQueryPlace(Place place) {
        this.place = place;
    }

    public RouteQueryPlace(Place place, DatePeriod period) {
        this.place = place;
        this.period = period;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public DatePeriod getPeriod() {
        return period;
    }

    public void setPeriod(DatePeriod period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return "RouteQueryPlace{" +
                "place=" + place +
                ", period=" + period +
                '}';
    }
}

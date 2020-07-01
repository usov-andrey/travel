package org.humanhelper.travel.route.routesearch.query;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.service.period.DatePeriod;
import org.humanhelper.travel.service.period.NumberPeriod;

import java.util.Date;

/**
 * место, которое нужно посетить в рамках путешествия
 *
 * @author Андрей
 * @since 08.05.14
 */
public class RouteQueryStop {

    private int id = 0;
    private Place place;
    private DatePeriod arrivePeriod;//Может быть null, если дата не задана. Не должно быть указано время
    private NumberPeriod nights;//Количество ночей проведенных к месте Place

    public RouteQueryStop() {
    }

    public RouteQueryStop(Place place, NumberPeriod nights) {
        setPlace(place);
        setNights(nights);
    }

    public RouteQueryStop(Place place, NumberPeriod nights, DatePeriod arrivePeriod) {
        this(place, nights);
        this.arrivePeriod = arrivePeriod;
    }

    public DatePeriod getArrivePeriod() {
        return arrivePeriod;
    }

    public void setArrivePeriod(DatePeriod arrivePeriod) {
        this.arrivePeriod = arrivePeriod;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
        id = id + place.hashCode();
    }

    public NumberPeriod getNights() {
        return nights;
    }

    public void setNights(NumberPeriod nights) {
        this.nights = nights;
        id = id + nights.hashCode();
    }

    public boolean canArrive(Date arriveDay) {
        return arrivePeriod == null || arrivePeriod.inPeriodDate(arriveDay);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RouteQueryStop)) return false;

        RouteQueryStop that = (RouteQueryStop) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "RouteQueryStop{" +
                "id=" + id +
                ", place=" + place +
                ", nights=" + nights +
                //", prices=" + prices +
                '}';
    }
}

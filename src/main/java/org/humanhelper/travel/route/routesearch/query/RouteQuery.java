package org.humanhelper.travel.route.routesearch.query;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.PlaceSet;
import org.humanhelper.travel.service.period.DatePeriod;
import org.humanhelper.travel.service.period.NumberPeriod;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Запрос на поиск информации о путешествии
 *
 * @author Андрей
 * @since 08.05.14
 */
public class RouteQuery {

    private RouteQueryPlace start;
    private Set<RouteQueryStop> stops;//Места, где нужно остановится на ночь
    private RouteQueryPlace end;
    private Currency currency;

    public RouteQuery() {
        stops = new HashSet<>();
    }

    public RouteQueryPlace getStart() {
        return start;
    }

    public void setStart(RouteQueryPlace start) {
        this.start = start;
    }

    public RouteQueryPlace getEnd() {
        return end;
    }

    public void setEnd(RouteQueryPlace end) {
        this.end = end;
    }

    public Set<RouteQueryStop> getStops() {
        return stops;
    }

    public void setStops(Set<RouteQueryStop> stops) {
        this.stops = stops;
    }

    /**
     * Возвращает null, если останавливаться в этой точке не нужно
     */
    public RouteQueryStop getStop(Place place) {
        for (RouteQueryStop stop : stops) {
            if (stop.getPlace().equals(place)) {
                return stop;
            }
        }
        return null;
    }

    public Map<Place, RouteQueryStop> stopMap() {
        Map<Place, RouteQueryStop> map = new HashMap<>();
        for (RouteQueryStop stop : stops) {
            map.put(stop.getPlace(), stop);
        }
        return map;
    }

    public DatePeriod period() {
        return new DatePeriod(start.getPeriod().getStart(), end.getPeriod().getEnd());
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrencyCode(String currencyCode) {
        currency = Currency.getInstance(currencyCode);
    }

    public RouteQuery currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public RouteQuery currencyUSD() {
        return currency(Currency.getInstance("USD"));
    }

    @Override
    public String toString() {
        return "RouteQuery{" +
                "start=" + start +
                ", stops=" + stops +
                ", end=" + end +
                ", currency=" + currency +
                '}';
    }

    /**
     * @return множество мест, куда нужно будет направляться согласно этому запросу
     */
    public PlaceSet<Place> getPlaces() {
        PlaceSet<Place> places = new PlaceSet<>();
        places.add(getStart().getPlace());
        places.add(getEnd().getPlace());
        places.addAll(stops.stream().map(RouteQueryStop::getPlace).collect(Collectors.toList()));
        return places;
    }

    //Builder

    public RouteQuery startEnd(Place place, DatePeriod datePeriod) {
        setStart(new RouteQueryPlace(place, datePeriod.copy()));
        setEnd(new RouteQueryPlace(place, datePeriod.copy()));
        return this;
    }

    public RouteQuery start(Place place, DatePeriod datePeriod) {
        setStart(new RouteQueryPlace(place, datePeriod.copy()));
        return this;
    }

    public RouteQuery end(Place place, DatePeriod datePeriod) {
        setEnd(new RouteQueryPlace(place, datePeriod.copy()));
        return this;
    }

    public RouteQuery end(Place place) {
        setEnd(new RouteQueryPlace(place));
        return this;
    }

    public RouteQuery stop(Place place, int days) {
        return stop(place, new NumberPeriod(days));
    }

    public RouteQuery stop(Place place, int daysStart, int daysEnd) {
        return stop(place, new NumberPeriod(daysStart, daysEnd));
    }

    public RouteQuery stop(Place place, NumberPeriod period) {
        getStops().add(new RouteQueryStop(place, period));
        return this;
    }

    public RouteQuery stop(Place place, NumberPeriod period, DatePeriod arrivePeriod) {
        getStops().add(new RouteQueryStop(place, period, arrivePeriod));
        return this;
    }
}

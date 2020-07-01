package org.humanhelper.travel.place;

import org.humanhelper.travel.geo.Location;
import org.humanhelper.travel.place.dao.PlaceDao;
import org.humanhelper.travel.place.type.transport.Airport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Умеет искать в dao места
 *
 * @author Андрей
 * @since 11.09.15
 */
@Service
public class PlaceService {

    private static final Logger log = LoggerFactory.getLogger(PlaceService.class);

    @Autowired
    protected PlaceDao placeDao;

    /**
     * Очищаем места в базе
     */
    public void clear() {
        placeDao.delete();
    }

    /**
     * Добавляем места в базу
     */
    public void add(Place... places) {
        for (Place place : places) {
            add(place);
        }
    }

    public void add(Place place) {
        place.save(placeDao);
    }

    /**
     * Получаем аэропорт по коду
     */
    public Airport getAirportByCode(String code) {
        log.debug("Get airport from dao by code:" + code);
        return placeDao.getAirportByCode(code);
    }

    /**
     * @return Ближайщие места в радиусе 300км
     */
    public List<Place> getNearestPlaces(Place source) {
        Location location = source.getLocation();
        if (location == null) {
            throw new IllegalStateException("Empty location for place:" + source);
        }
        List<Place> places = createNearestPlaces(location, 300);
        places.remove(source);
        if (places.isEmpty()) {
            places = createNearestPlaces(location, 600);
            places.remove(source);
            if (places.isEmpty()) {
                throw new IllegalStateException("Not found nearest places");
            }
        }
        //Сортируем в порядке увеличения расстояния
        Collections.sort(places, (o1, o2) -> Double.compare(location.getDistance(o1.getLocation()), location.getDistance(o2.getLocation())));
        //log.debug("Return the nearest places with " + source.simple() + ": " + places.size());
        return places;
    }

    private List<Place> createNearestPlaces(Location location, double radius) {
        List<Place> places = new ArrayList<>();
        places.addAll(placeDao.getInRadius(location, radius));
        return places;

    }

}

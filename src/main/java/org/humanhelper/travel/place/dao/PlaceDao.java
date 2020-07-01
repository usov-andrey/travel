package org.humanhelper.travel.place.dao;

import org.humanhelper.data.dao.CRUDDao;
import org.humanhelper.travel.country.Country;
import org.humanhelper.travel.geo.Location;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.type.region.Region;
import org.humanhelper.travel.place.type.transport.Airport;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

/**
 * @author Андрей
 * @since 10.05.14
 */
@RequestMapping("placeDao")
public interface PlaceDao extends CRUDDao<Place, String> {

    /**
     * AutoComplete
     */
    Collection<Place> getByNamePrefix(String namePrefix, int limitCount);

    <T extends Place> T get(String name, Country country, Class<T> placeClass);

    <T extends Place> T getById(String id);

    /**
     * Добавить возможность перемещения из source в target
     */
    void addRoad(Place source, Place target);

    Collection<Place> get(Collection<String> idSet);

    /**
     * @return все места определенного класса
     */
    <T extends Place> Collection<T> getAll(Class<T> placeClass);

    /**
     * @return Все места определенного класса в какой-то одной стране
     */
    <T extends Place> Collection<T> getByCountry(Class<T> placeClass, Country country);

    <T extends Place> Collection<T> getByRegion(Region region, Class<T> placeClass);

    /**
     * Наиболее популярные в стране регионы(в текущей реализации по количеству отелей)
     */
    Collection<Region> getPopularRegions(Country country, int count);

    <T extends Place> Collection<T> getInRadius(Class<T> placeClass, Country country, Location location, double radiusInKm);

    Collection<Place> getInRadius(Location location, double radiusInKm);

    Airport getAirportByCode(String code);
}

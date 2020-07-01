package org.humanhelper.travel.place.type.transport;

import org.humanhelper.service.utils.DateHelper;
import org.humanhelper.travel.geo.Location;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.dao.PlaceDao;
import org.humanhelper.travel.place.type.region.Region;

import java.util.Date;

/**
 * Аэропорт
 *
 * @author Human Helper
 * @since 30.10.13
 */
public class Airport extends TransportPlace {

    private static final int ONE_HOUR_IN_MS = 1000 * 60 * 60;//1 час

    private String code;

    public static Airport build(String code, Region region) {
        Airport place = build(code);
        place.linkRegion(region);
        //Если мы месту назначаем регион, то и позиция у места всегда тогда должна быть, выбираем случайную позицию
        place.setLocation(Location.rnd());
        place.setCountry(region.getCountry());
        return place;
    }

    public static Airport build(String code) {
        return build(Airport.class, code).code(code);
    }

    public static boolean isAirport(Place place) {
        return place instanceof Airport;
    }

    public static Airport get(Place place) {
        return (Airport) place;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code.toUpperCase();
    }

    public Airport code(String code) {
        setCode(code);
        return this;
    }

    public Airport location(Location location) {
        setLocation(location);
        return this;
    }

    @Override
    public Date getFirstDepartTime(Date arrivalTime) {
        return DateHelper.incHours(arrivalTime, 1);//В итоге пересадка между рейсами одного аэропорта - минимум 2 часа
    }

    @Override
    public Date getLastArriveTime(Date departTime) {
        return new Date(departTime.getTime() - ONE_HOUR_IN_MS);
        //return DateHelper.decHours(departTime, 1);
    }

    @Override
    public String toString() {
        return "Airport{code=" +
                code + ", " +
                super.toString() +
                '}';
    }

    public void join(Airport airport) {
        if (getLocation() == null) {
            setLocation(airport.getLocation());
        }
    }

    @Override
    public String nameOrId() {
        return code;
    }

    @Override
    public String generateId() {
        if (code == null) {
            throw new IllegalStateException("Code is null");
        }
        return code;
    }

    @Override
    public void save(PlaceDao placeDao) {
        //Для поиска аэропорта в базе не стоит искать по локации, достаточно искать по коду
        Airport airportInDB = placeDao.getAirportByCode(getCode());
        if (airportInDB != null) {
            placeDao.update(getId(), this);
        } else {
            placeDao.insert(this);
        }
    }
}

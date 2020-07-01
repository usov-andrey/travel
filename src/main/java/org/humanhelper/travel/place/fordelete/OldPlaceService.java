package org.humanhelper.travel.place.fordelete;

import org.humanhelper.travel.country.Country;
import org.humanhelper.travel.country.dao.CountryDao;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.dao.PlaceDao;
import org.humanhelper.travel.place.type.region.Region;
import org.humanhelper.travel.place.type.transport.Airport;
import org.humanhelper.travel.place.type.transport.TransportPlace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author Андрей
 * @since 21.01.15
 */
@Service
public class OldPlaceService {

    public static final String CITY_SUFFIX = " City";

    @Autowired
    private PlaceDao placeDao;
    @Autowired
    private CountryDao countryDao;

    public Airport getAirportInDB(String code) {
        return placeDao.getById(code);
    }

    public Airport getAirport(String code) {
        Airport airport = getAirportInDB(code);
        //Если в базе еще нет такого аэропорта, то нужно его создать
        if (airport == null) {
            throw new IllegalStateException("Not found airport with code " + code);
        }
        return airport;
    }

    public Place getPlace(String placeName, Region region, Class<? extends Place> placeClass) {
        Place place = getPlaceInDB(placeName, region, placeClass);
        if (place == null) {
            throw new IllegalStateException("Not found place " + placeName + " in region " + region + " with placeClass " + placeClass);
        }
        return place;
    }

    public Place getPlaceInDB(String placeName, Region region, Class<? extends Place> placeClass) {
        return placeDao.get(placeName, region.getCountry(), placeClass);
    }

    /**
     * Считаем, что в городе транспортная компания может обладать только одной остановкой, если остановок несколько
     * то нужно искать по имени используя метод getByName
     */
    public <T extends TransportPlace> T getTransportPlace(Region region, Class<T> placeClass) {
        Collection<T> places = placeDao.getByRegion(region, placeClass);
        if (places.isEmpty()) {
            throw new IllegalStateException("Region " + region + " don't have places with placeClass " + placeClass);
        }
        T result = null;
        //В базе найдено несколько мест, необходимо найти единственное, этого типа
        for (T place : places) {
            if (place.getClass().equals(placeClass)) {
                if (result == null) {
                    result = place;
                } else {
                    throw new IllegalStateException("In db founded several places in region:" + region + " with placeClass:" + placeClass);
                }
            }
        }
        if (result == null) {
            throw new IllegalStateException("In db not found place in region:" + region + " by placeClass:" + placeClass + ", places:" + places);
        }
        return result;
    }

    //----------Region

    public Region getRegion(String regionName, Country country) {
        Region region = getRegionInDB(regionName, country);
        if (region == null) {
            throw new IllegalStateException("Not found region " + regionName + " in country " + country);
        }
        return region;
    }

    public Region getRegionWithSuffixCity(String regionName, Country country) {
        Region regionInDB = getRegionInDB(regionName, country);
        if (regionInDB == null) {
            regionInDB = getRegionInDB(regionName + CITY_SUFFIX, country);
        }
        if (regionInDB == null) {
            throw new IllegalStateException("Not found region with suffix " + regionName + " in country " + country);
        }
        return regionInDB;
    }

    public Region getRegionInDB(String regionName, Country country) {
        return placeDao.get(regionName, country, Region.class);
    }


    /**
     * @param source - Маркер из какого источника был создано это место
     */
    public <T extends Place> T insert(T place, String source) {
        if (place.getId() == null) {
            Region region = place.getRegion();
            String id = (region == null ? place.getCountry().getId() : region.getId()) + "." + place.getName() + source;
            place.setId(id);
        }
        return (T) placeDao.insert(place);
    }

    public Country getCountry(String code) {
        Country country = countryDao.get(code);
        if (country == null) {
            throw new IllegalStateException("Not found country by code:" + code);
        }
        return country;
    }
}

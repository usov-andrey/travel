package org.humanhelper.travel.place.dao;

import org.humanhelper.travel.country.Country;
import org.humanhelper.travel.dao.memory.MemoryCRUDDao;
import org.humanhelper.travel.geo.Location;
import org.humanhelper.travel.place.AbstractPlaceList;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.type.region.Region;
import org.humanhelper.travel.place.type.transport.Airport;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Андрей
 * @since 13.05.14
 */
public class MemoryPlaceDao extends MemoryCRUDDao<Place> implements PlaceDao {

    @Override
    protected void generateId(Place object) {
        object.fillId();
    }

    private Place copy(Place place) {
        try {
            return (Place) place.clone();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<Place> getByNamePrefix(String namePrefix, int limitCount) {
        List<Place> placeList = new ArrayList<>();
        for (Place place : values.values()) {
            if (place.getName().startsWith(namePrefix)) {
                placeList.add(copy(place));
            }
            if (placeList.size() == limitCount) {
                return placeList;
            }
        }
        return placeList;
    }

    @Override
    public <T extends Place> T getById(String id) {
        return (T) get(id);
    }

    @Override
    public <T extends Place> T get(String name, Country country, Class<T> placeClass) {
        for (Place place : values.values()) {
            if (place.equalsPlace(placeClass, country) && place.getName().equalsIgnoreCase(name)) {
                return (T) place;
            }
        }
        return null;
    }

    @Override
    public void addRoad(Place source, Place target) {
		/*
		if (source.addTarget(target)) {
			log.debug("Add road from " + source + " to " + target);
		} */
    }

    @Override
    public Collection<Place> get(Collection<String> idSet) {
        Set<Place> places = new HashSet<>();
        for (Place place : values.values()) {
            if (idSet.contains(place.getId())) {
                places.add(place);
            }
        }
        return places;
    }

    @Override
    public <T extends Place> Collection<T> getAll(Class<T> placeClass) {
        List<T> result = new ArrayList<>();
        for (Place place : values.values()) {
            if (place.equalsPlace(placeClass)) {
                result.add((T) place);
            }
        }
        return result;
    }

    @Override
    public <T extends Place> Collection<T> getByCountry(Class<T> placeClass, Country country) {
        List<T> result = new ArrayList<>();
        for (Place place : values.values()) {
            if (place.equalsPlace(placeClass, country)) {
                result.add((T) place);
            }
        }
        return result;
    }

    @Override
    public <T extends Place> Collection<T> getByRegion(Region region, Class<T> placeClass) {
        List<T> result = new ArrayList<>();
        for (Place place : values.values()) {
            if (place.equalsPlace(placeClass, region)) {
                result.add((T) place);
            }
        }
        return result;
    }

    @Override
    public Collection<Region> getPopularRegions(Country country, int count) {
        List<Region> result = new ArrayList<>();
        for (Place place : values.values()) {
            if (Region.isRegion(place)) {
                if (place.getCountry().equals(country)) {
                    result.add((Region) place);
                }
                if (result.size() == count) {
                    return result;
                }
            }
        }
        return result;
    }

    @Override
    public <T extends Place> Collection<T> getInRadius(Class<T> placeClass, Country country, Location location, double radiusInKm) {
        AbstractPlaceList<T> placeList = new AbstractPlaceList<>();
        for (Place place : values.values()) {
            if (place.equalsPlace(placeClass, country) && place.getLocation().inRadius(location, radiusInKm)) {
                placeList.add((T) place);
            }
        }
        return placeList;

    }

    @Override
    public Collection<Place> getInRadius(Location location, double radiusInKm) {
        return values.values().stream().filter(place -> place.getLocation() != null && place.getLocation()
                .inRadius(location, radiusInKm)).collect(Collectors.toList());
    }

    @Override
    public Airport getAirportByCode(String code) {
        for (Place place : values.values()) {
            if (Airport.isAirport(place) && Airport.get(place).getCode().equals(code)) {
                return Airport.get(place);
            }
        }
        return null;
    }
}

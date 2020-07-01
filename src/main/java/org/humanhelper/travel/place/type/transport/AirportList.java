package org.humanhelper.travel.place.type.transport;

import org.humanhelper.travel.place.AbstractPlaceList;
import org.humanhelper.travel.place.Place;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Андрей
 * @since 06.09.15
 */
public class AirportList extends AbstractPlaceList<Airport> {

    public static Airport getByCode(Collection<Place> places, String code) {
        for (Place place : places) {
            if (Airport.isAirport(place) && Airport.get(place).getCode().equals(code)) {
                return Airport.get(place);
            }
        }
        return null;
    }

    public Airport getByCode(String code) {
        for (Airport object : this) {
            if (object.getCode().equals(code)) {
                return object;
            }
        }
        return null;
    }

    /**
     * @return map (код аэропорта, аэропорт)
     */
    public Map<String, Airport> createMapByCode() {
        Map<String, Airport> airportMap = new HashMap<>();
        for (Airport airport : this) {
            airportMap.put(airport.getCode(), airport);
        }
        return airportMap;
    }
}

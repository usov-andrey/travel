package org.humanhelper.travel.integration.booking;

import java.util.Map;

/**
 * @author Андрей
 * @since 23.01.15
 */
public class BookingCountries {

    private Map<String, BookingCities> cities;

    public Map<String, BookingCities> getCities() {
        return cities;
    }

    public void setCities(Map<String, BookingCities> cities) {
        this.cities = cities;
    }
}

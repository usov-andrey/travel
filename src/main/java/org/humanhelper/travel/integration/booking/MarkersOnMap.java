package org.humanhelper.travel.integration.booking;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Андрей
 * @since 07.09.15
 */
public class MarkersOnMap {

    @JsonProperty(value = "b_airports")
    private List<BookingPlace> airports;
    @JsonProperty(value = "b_cities")
    private List<BookingPlace> cities;
    @JsonProperty(value = "b_hotels")
    private List<BookingPlace> hotels;

    public List<BookingPlace> getAirports() {
        return airports;
    }

    public void setAirports(List<BookingPlace> airports) {
        this.airports = airports;
    }

    public List<BookingPlace> getCities() {
        return cities;
    }

    public void setCities(List<BookingPlace> cities) {
        this.cities = cities;
    }

    public List<BookingPlace> getHotels() {
        return hotels;
    }

    public void setHotels(List<BookingPlace> hotels) {
        this.hotels = hotels;
    }

    @Override
    public String toString() {
        return "MarkersOnMap{" +
                "airports=" + airports +
                ", cities=" + cities +
                ", hotels=" + hotels +
                '}';
    }
}

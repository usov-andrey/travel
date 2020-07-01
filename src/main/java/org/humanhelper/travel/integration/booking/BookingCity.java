package org.humanhelper.travel.integration.booking;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Андрей
 * @since 16.01.15
 */
public class BookingCity extends BookingPlace {

    @JsonProperty(value = "b_cc1")
    private String country;
    @JsonProperty(value = "b_city_name")
    private String cityName;

    @JsonProperty(value = "b_nr_hotels")
    private int hotelsCount;

    @JsonProperty(value = "b_city_url")
    private String cityUrl;

    @JsonProperty(value = "b_ufi")
    private String ufi;


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getHotelsCount() {
        return hotelsCount;
    }

    public void setHotelsCount(int hotelsCount) {
        this.hotelsCount = hotelsCount;
    }

    public String getCityUrl() {
        return cityUrl;
    }

    public void setCityUrl(String cityUrl) {
        this.cityUrl = cityUrl;
    }

    public String getUfi() {
        return ufi;
    }

    public void setUfi(String ufi) {
        this.ufi = ufi;
    }

    @Override
    public String toString() {
        return "BookingCity{" +
                "country='" + country + '\'' +
                ", cityName='" + cityName + '\'' +
                ", hotelsCount=" + hotelsCount +
                ", cityUrl='" + cityUrl + '\'' +
                ", ufi='" + ufi + '\'' +
                '}';
    }
}

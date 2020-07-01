package org.humanhelper.travel.integration.rome2rio.autocomplete;

import org.humanhelper.service.utils.WebHelper;
import org.humanhelper.travel.geo.Location;
import org.humanhelper.travel.place.Place;

/**
 * @author Андрей
 * @since 26.11.14
 */
public class Rome2RioPlace {

    private Rome2RioPlaceKind kind;
    private String longName;
    private String shortName;
    private String canonicalName;
    private Float lat;
    private Float lng;
    private Float rad;
    private String code;
    private String regionName;
    private String regionCode;
    private String countryName;
    private String countryCode;

    public Rome2RioPlaceKind getKind() {
        return kind;
    }

    public void setKind(Rome2RioPlaceKind kind) {
        this.kind = kind;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCanonicalName() {
        return canonicalName;
    }

    public void setCanonicalName(String canonicalName) {
        this.canonicalName = canonicalName;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }

    public Float getRad() {
        return rad;
    }

    public void setRad(Float rad) {
        this.rad = rad;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public String toString() {
        return "Rome2RioPlace{" +
                "kind=" + kind +
                ", longName='" + longName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", canonicalName='" + canonicalName + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", rad=" + rad +
                ", code='" + code + '\'' +
                ", regionName='" + regionName + '\'' +
                ", regionCode='" + regionCode + '\'' +
                ", countryName='" + countryName + '\'' +
                ", countryCode='" + countryCode + '\'' +
                '}';
    }

    public Place createPlace() {
        Place place = kind.createPlace(this);
        place.setLocation(Location.latLng(getLat(), getLng()));
        place.setName(getShortName());
        //place.setCountry(getCountryCode());
        //TODO
        //place.setRegion(getRegionCode());
        return place;
    }

    public String toWebParams(String prefix) {
        return getParam(prefix, "Short", getShortName()) + getParam(prefix, "Long", getLongName()) +
                getParam(prefix, "Lat", getLat()) + getParam(prefix, "Lng", getLng()) + getParam(prefix, "Type", getKind())
                + getParam(prefix, "Canonical", getCanonicalName()) + getParam(prefix, "Radius", getRad());
    }

    private String getParam(String prefix, String name, Object value) {
        if (value == null) {
            throw new IllegalStateException("Param " + name + " is null for prefix " + prefix);
        }
        return prefix + name + "=" + WebHelper.encodeURL(value.toString()) + "&";
    }
}

package org.humanhelper.travel.dataprovider.air.skysales.model;

/**
 * @author Андрей
 * @since 19.05.14
 */
public class StationInfo {

    private String stationCategories;
    private Boolean allowed;
    private String name;
    private String countryCode;
    private String shortName;
    private String code;

    public String getStationCategories() {
        return stationCategories;
    }

    public void setStationCategories(String stationCategories) {
        this.stationCategories = stationCategories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getAllowed() {
        return allowed;
    }

    public void setAllowed(Boolean allowed) {
        this.allowed = allowed;
    }

    @Override
    public String toString() {
        return "StationInfo{" +
                "name='" + name + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", shortName='" + shortName + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}

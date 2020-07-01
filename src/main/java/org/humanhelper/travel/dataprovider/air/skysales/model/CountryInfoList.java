package org.humanhelper.travel.dataprovider.air.skysales.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Андрей
 * @since 19.05.14
 */
public class CountryInfoList {

    private List<CountryInfo> CountryList;

    public List<CountryInfo> getCountryList() {
        return CountryList;
    }

    @JsonProperty("CountryList")
    public void setCountryList(List<CountryInfo> countryList) {
        CountryList = countryList;
    }
}

package org.humanhelper.travel.integration.rome2rio.autocomplete;

import java.util.Collection;

/**
 * @author Андрей
 * @since 26.11.14
 */
public class Rome2RioAutoCompleteResponse {

    private String query;
    private String countryCode;
    private String languageCode;
    private Collection<Rome2RioPlace> places;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public Collection<Rome2RioPlace> getPlaces() {
        return places;
    }

    public void setPlaces(Collection<Rome2RioPlace> places) {
        this.places = places;
    }

}

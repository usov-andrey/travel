package org.humanhelper.travel.country;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.humanhelper.travel.service.translation.TranslationsNameBean;

/**
 * @author Андрей
 * @since 05.09.15
 */
public class BeanWithCountry extends TranslationsNameBean {

    public static final String COUNTRY_FIELD = "country";

    @JsonProperty(COUNTRY_FIELD)
    private Country country;

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

}

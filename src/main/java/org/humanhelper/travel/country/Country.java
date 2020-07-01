package org.humanhelper.travel.country;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.humanhelper.travel.place.type.region.Region;
import org.humanhelper.travel.service.translation.TranslationsNameBean;

import java.util.Currency;
import java.util.List;

/**
 * json взят отсюда: https://raw.githubusercontent.com/mledoze/countries/master/countries.json
 *
 * @author Андрей
 * @since 18.12.14
 */
public class Country extends TranslationsNameBean {

    //id=код cca2
    @JsonIgnore
    private Currency currency;
    private List<Region> regions;

    @Override
    public void setId(String id) {
        super.setId(id.toUpperCase());//Заплатка, чтобы избежать старых багов, потом убрать
    }

    @Override
    public Country id(String id) {
        setId(id);
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @JsonProperty("currency")
    public String getCurrencyCode() {
        return currency != null ? currency.getCurrencyCode() : null;
    }

    @JsonProperty("currency")
    public void setCurrencyCode(String currencyCode) {
        if (currencyCode != null) {
            try {
                this.currency = Currency.getInstance(currencyCode);
            } catch (Exception e) {
                throw new IllegalArgumentException("Error create currency for code " + currencyCode);
            }
        }
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    @Override
    public String toString() {
        return name != null ? name : id;
    }

    public Country name(String name) {
        setName(name);
        return this;
    }

}

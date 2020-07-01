package org.humanhelper.travel.country;

import java.util.Currency;

/**
 * Для ручного создания стран или использования их валюты по стране
 *
 * @author Андрей
 * @since 21.01.15
 */
public enum CountryBuilder {
    PH("Philippines", "PHP"),
    RU("Russia", "RUB"),
    TH("Thailand", "THB"),
    HK("Hong Kong", "HKD"),
    US("USA", "USD"),
    ID("Indonesia", "IDR");

    private String caption;
    private Currency currency;

    CountryBuilder(String caption, String currency) {
        this.caption = caption;
        this.currency = Currency.getInstance(currency);
    }

    public Currency getCurrency() {
        return currency;
    }

    public Country createForTest() {
        Country country = new Country();
        country.setCurrency(currency);
        country.setName(caption);
        country.setId(name());
        return country;
    }

    public String getCode() {
        return toString();
    }

}

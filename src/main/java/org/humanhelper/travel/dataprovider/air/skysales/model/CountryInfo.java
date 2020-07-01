package org.humanhelper.travel.dataprovider.air.skysales.model;

/**
 * {"currency":"AFN","InternationalDialCode":"93","name":"Afghanistan","code":"AF"}
 *
 * @author Андрей
 * @since 19.05.14
 */
public class CountryInfo {
    private String currency;
    private String name;
    private String code;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

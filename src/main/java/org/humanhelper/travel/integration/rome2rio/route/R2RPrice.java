package org.humanhelper.travel.integration.rome2rio.route;

import java.util.Currency;

/**
 * @author Андрей
 * @since 09.10.15
 */
public class R2RPrice {

    private Double price;
    private Currency currency;

    public R2RPrice(Double price, String currency) {
        this.price = price;
        this.currency = Currency.getInstance(currency);
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}

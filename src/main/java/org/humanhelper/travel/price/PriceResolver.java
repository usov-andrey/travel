package org.humanhelper.travel.price;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Currency;

/**
 * Определение цены этапа маршрута
 *
 * @author Андрей
 * @since 11.05.14
 */
public class PriceResolver {

    public static final PriceResolver EMPTY = new PriceResolver(0f, Currency.getInstance("USD"), SimplePriceAgent.INSTANCE);

    public static final String PRICE_FIELD = "price";
    public static final String CURRENCY_FIELD = "currency";
    public static final String AGENT_FIELD = "agentId";

    private Float price;
    private Currency currency;
    private PriceAgent agent;

    public PriceResolver() {
    }

    public PriceResolver(float price, Currency currency, PriceAgent agent) {
        this.price = price;
        this.currency = currency;
        this.agent = agent;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @JsonIgnore
    public PriceAgent getPriceAgent() {
        return agent;
    }

    public void changeCurrency(Currency currency) {
        price = agent.getPrice(price, this.currency, currency);
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PriceResolver)) return false;

        PriceResolver that = (PriceResolver) o;

        if (!price.equals(that.price)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return price.hashCode();
    }

    @Override
    public String toString() {
        return price + " " + currency;
    }

}

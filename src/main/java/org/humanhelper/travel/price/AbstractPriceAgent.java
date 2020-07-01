package org.humanhelper.travel.price;

import org.humanhelper.service.singleton.Singleton;
import org.humanhelper.service.singleton.SingletonLocator;
import org.humanhelper.travel.price.currency.CurrencyConverter;

import java.util.Currency;

/**
 * @author Андрей
 * @since 20.10.15
 */
public abstract class AbstractPriceAgent implements PriceAgent {

    private String name;
    private Singleton<CurrencyConverter> currencyConverter = new Singleton<>(() -> SingletonLocator.get(CurrencyConverter.class));

    public AbstractPriceAgent(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getPrice(float price, Currency from, Currency to) {
        return currencyConverter.get().convert(price, from, to);
    }

}

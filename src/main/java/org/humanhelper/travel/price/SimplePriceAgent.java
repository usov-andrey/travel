package org.humanhelper.travel.price;

import org.humanhelper.travel.route.type.TimeRoute;

import java.util.Currency;

/**
 * @author Андрей
 * @since 08.01.15
 */
public class SimplePriceAgent implements PriceAgent {

    public static SimplePriceAgent INSTANCE = new SimplePriceAgent();

    @Override
    public String getName() {
        return "simple";
    }

    @Override
    public float getPrice(float price, Currency from, Currency to) {
        return price;
    }

    @Override
    public String getUrl(TimeRoute route) {
        return "";
    }
}

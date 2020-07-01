package org.humanhelper.travel.price;

import org.humanhelper.travel.route.type.TimeRoute;

import java.util.Currency;

/**
 * Определяет логику покупки билета на перемещение
 *
 * @author Андрей
 * @since 26.12.14
 */
public interface PriceAgent {

    String getName();

    float getPrice(float price, Currency from, Currency to);

    String getUrl(TimeRoute route);
}

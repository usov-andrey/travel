package org.humanhelper.travel.route.waydiscount;

import org.humanhelper.travel.route.type.Route;

/**
 * Пока сделано для тестов
 *
 * @author Андрей
 * @since 22.12.14
 */
public class FixedPriceTwoWaysDiscountStrategy extends TwoWaysDiscountStrategy {

    private Float priceTogether;

    public FixedPriceTwoWaysDiscountStrategy(Float priceForOne) {
        super(0f);
        this.priceTogether = priceForOne;
    }

    @Override
    public Float getPriceForStartWay(Route way) {
        return 0f;
    }

    @Override
    public Float getPriceForEndWay(Route way) {
        return priceTogether * 2;
    }
}

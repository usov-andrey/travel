package org.humanhelper.travel.route.discount;

import org.humanhelper.travel.route.Activity;
import org.humanhelper.travel.route.type.ActivityBase;

/**
 * @author Андрей
 * @since 30.10.15
 */
public class DiscountResolver extends java.util.HashMap<Activity, Float> {

    private float minPriceWithDiscount = Float.MAX_VALUE;

    public void addDiscountPrice(ActivityBase previousActivityWithDiscount, float price) {
        put(previousActivityWithDiscount, price);
        if (minPriceWithDiscount > price) {
            minPriceWithDiscount = price;
        }
    }

    /**
     * @return Например, если из Source в Target есть маршрут со стоимостью 10000 и из Target
     * в Source есть маршрут со стоимостью 10000. Без скидок. В итоге мы заплатим 20000
     * Также есть маршруты с ценой в 13000 и обратно тоже 13000. При этом на них распространяется скикда
     * и общая сумма будет со скидкой не 26000, а всего 19000.
     * Поэтому при рассмотрении маршрутов из Source в Target нам также нужно рассматировать маршрут со скидкой
     * Получается цена со скидкой в 9500
     */
    public float getMinPossiblePriceWithDiscount() {
        return minPriceWithDiscount;
    }

    public Float getDiscountPrice(Activity previousActivityWithDiscount) {
        return get(previousActivityWithDiscount);
    }
}

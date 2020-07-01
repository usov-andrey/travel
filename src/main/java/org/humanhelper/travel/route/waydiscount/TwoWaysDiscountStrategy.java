package org.humanhelper.travel.route.waydiscount;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.type.Route;

/**
 * Скидка работает только в том случае, если мы покупаем обратный билет:
 * 1.Если билет туда обратно: то N1.startPlace=N2.endPlace и N1.endPlace=N2.startPlace
 *
 * @author Андрей
 * @since 22.12.14
 */
public class TwoWaysDiscountStrategy implements DiscountStrategy {

    private float discountFactor;

    public TwoWaysDiscountStrategy(float discountFactor) {
        this.discountFactor = discountFactor;
    }

    @Override
    public int getEndCodeForStartWay(Route way) {
        //1) startPlace + endPlace + transport + discount1
        return getHashCode(way.getSource(), way.getTarget());
    }

    @Override
    public int getEndCodeForEndWay(Route way) {
        //1) endPlace + startPlace + transport + discount1
        return getHashCode(way.getTarget(), way.getSource());
    }

    @Override
    public Float getPriceForStartWay(Route way) {
        return way.getPriceResolver().getPrice() * discountFactor;
    }

    @Override
    public Float getPriceForEndWay(Route way) {
        return way.getPriceResolver().getPrice() * discountFactor;
    }

    private int getHashCode(Place place1, Place place2) {
        int result = getClass().hashCode();
        result = 31 * result + place1.hashCode();
        result = 31 * result + place2.hashCode();
        return result;
    }
}

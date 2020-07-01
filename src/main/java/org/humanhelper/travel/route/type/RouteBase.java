package org.humanhelper.travel.route.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.humanhelper.travel.route.waydiscount.DiscountStrategy;

/**
 * @author Андрей
 * @since 21.01.15
 */
public abstract class RouteBase extends ActivityBase implements Route {

    @JsonIgnore
    protected DiscountStrategy discountStrategy;

    @Override
    public DiscountStrategy getDiscountStrategy() {
        return discountStrategy;
    }

    public void setDiscountStrategy(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }
}

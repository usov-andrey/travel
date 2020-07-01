package org.humanhelper.travel.route.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.price.PriceResolver;
import org.humanhelper.travel.route.Activity;
import org.humanhelper.travel.route.discount.DiscountResolver;

/**
 * Это какое-то время провождение за определенную цену в каких-то местах
 * <p>
 * Для сохранения
 *
 * @author Андрей
 * @since 22.12.14
 */
public abstract class ActivityBase implements Activity {

    public static final String ANY_KIND = "any";
    public static final String SCHEDULE_KIND = "sched";
    public static final String FIXED_KIND = "fixed";
    public static final String KIND_FIELD = "kind";
    public static final String PRICE_RESOLVER_FIELD = "priceResolver";
    public static final String SOURCE_ID_FIELD = "sourceId";

    protected Place source;
    protected PriceResolver priceResolver;
    protected DiscountResolver discountResolver;

    public ActivityBase() {
    }

    public ActivityBase(Place source) {
        this.source = source;
    }

    @JsonIgnore
    @Override
    public Place getSource() {
        return source;
    }

    @JsonIgnore
    public void setSource(Place source) {
        this.source = source;
    }

    @Override
    public PriceResolver getPriceResolver() {
        return priceResolver;
    }

    @Override
    public void setPriceResolver(PriceResolver priceResolver) {
        this.priceResolver = priceResolver;
    }

    public String getId() {
        return Integer.toString(hashCode());
    }

    public void setId(String id) {
        //Ничего не делаем, id генерируется автоматически
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActivityBase)) return false;

        ActivityBase routeBase = (ActivityBase) o;

        if (!source.equals(routeBase.source)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return source.hashCode();
    }

    @Override
    public String toString() {
        return source.nameOrId() + (priceResolver != null ? ", price=" + priceResolver : "");
    }

    @Override
    public DiscountResolver getDiscountResolver() {
        return discountResolver;
    }

    public void setDiscountResolver(DiscountResolver discountResolver) {
        this.discountResolver = discountResolver;
    }
}

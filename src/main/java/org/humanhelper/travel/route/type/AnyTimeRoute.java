package org.humanhelper.travel.route.type;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.price.PriceResolver;
import org.humanhelper.travel.route.routesearch.newsearch.ActivityVisitor;
import org.humanhelper.travel.transport.Transport;

/**
 * Перемещение из одного места в другое в любое время
 * Есть дополнительная характеристика - это продолжительность перемещения
 *
 * @author Андрей
 * @since 20.01.15
 */
public class AnyTimeRoute extends SingleRoute {

    private int durationInMinutes;

    public AnyTimeRoute() {
    }

    public AnyTimeRoute(PriceResolver priceResolver, Place source, Place target, int durationInMinutes, Transport transport) {
        this.priceResolver = priceResolver;
        this.transport = transport;
        this.source = source;
        this.target = target;
        this.durationInMinutes = durationInMinutes;
    }

    public AnyTimeRoute duration(int durationInMinutes) {
        setDurationInMinutes(durationInMinutes);
        return this;
    }

    public AnyTimeRoute sourceTarget(Place source, Place target) {
        super.sourceTarget(source, target);
        return this;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnyTimeRoute)) return false;
        if (!super.equals(o)) return false;

        AnyTimeRoute that = (AnyTimeRoute) o;

        if (durationInMinutes != that.durationInMinutes) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + durationInMinutes;
        return result;
    }

    @Override
    public String toString() {
        return "AnyTimeWayRoute{" +
                super.toString() +
                ",durationInMinutes=" + durationInMinutes +
                '}';
    }

    public AnyTimeRoute price(PriceResolver priceResolver) {
        setPriceResolver(priceResolver);
        return this;
    }

    @Override
    public void visit(ActivityVisitor visitor) {
        visitor.anyRoute(this);
    }
}

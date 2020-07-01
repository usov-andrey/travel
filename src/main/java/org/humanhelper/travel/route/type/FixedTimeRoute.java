package org.humanhelper.travel.route.type;

import org.humanhelper.service.utils.DateHelper;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.price.PriceResolver;
import org.humanhelper.travel.route.routesearch.newsearch.ActivityVisitor;
import org.humanhelper.travel.route.waydiscount.DiscountStrategy;
import org.humanhelper.travel.transport.Transport;

import java.util.Date;

/**
 * Путь из одного места в другой в определенное время
 *
 * @author Андрей
 * @since 08.05.14
 */
public class FixedTimeRoute extends SingleRoute implements TimeRoute {

    protected Date startTime;
    protected Date endTime;

    public FixedTimeRoute() {
    }

    public FixedTimeRoute(Place source, PriceResolver priceResolver, Place target, Transport transport, Date startTime, Date endTime) {
        this.source = source;
        this.priceResolver = priceResolver;
        this.target = target;
        this.transport = transport;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public TimeRoute copy() {
        return new FixedTimeRoute(source, priceResolver, target, transport, startTime, endTime);
    }

    @Override
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Override
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public FixedTimeRoute time(Date startTime, Date endTime) {
        setStartTime(startTime);
        setEndTime(endTime);
        return this;
    }

    @Override
    public FixedTimeRoute sourceTarget(Place source, Place target) {
        setSource(source);
        setTarget(target);
        return this;
    }

    public FixedTimeRoute price(PriceResolver priceResolver) {
        setPriceResolver(priceResolver);
        return this;
    }


    @Override
    public String toString() {
        return "From " + source.nameOrId() +
                " to " + target.nameOrId() +
                " at (" + DateHelper.getDateTime(startTime) +
                ", " + DateHelper.getDateTime(endTime) + ")" +
                (transport != null ?
                        " with " + transport : "") +
                (getPriceResolver() != null ?
                        " for " + getPriceResolver().getPrice() + getPriceResolver().getCurrency() +
                                " " + getPriceResolver().getPriceAgent().getUrl(this)
                        : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FixedTimeRoute)) return false;
        if (!super.equals(o)) return false;

        FixedTimeRoute that = (FixedTimeRoute) o;

        if (!endTime.equals(that.endTime)) return false;
        if (!startTime.equals(that.startTime)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + startTime.hashCode();
        result = 31 * result + endTime.hashCode();
        return result;
    }

    public FixedTimeRoute discount(DiscountStrategy discountStrategy) {
        setDiscountStrategy(discountStrategy);
        return this;
    }

    @Override
    public void visit(ActivityVisitor visitor) {
        visitor.timeRoute(this);
    }
}

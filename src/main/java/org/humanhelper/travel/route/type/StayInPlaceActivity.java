package org.humanhelper.travel.route.type;

import org.humanhelper.service.utils.DateHelper;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.price.PriceResolver;
import org.humanhelper.travel.route.routesearch.newsearch.ActivityVisitor;

import java.util.Date;

/**
 * Нахождение в каком-то месте несколько ночей
 *
 * @author Андрей
 * @since 08.05.14
 */
public class StayInPlaceActivity extends StopActivity {

    private Integer nights;

    public StayInPlaceActivity(Place place) {
        super(place);
    }

    public StayInPlaceActivity(Place place, PriceResolver priceResolver, Date startDate, Integer nights) {
        super(place);
        this.source = place;
        this.priceResolver = priceResolver;
        this.startTime = startDate;
        this.endTime = DateHelper.incDays(startDate, nights);
        this.nights = nights;
    }

    public Integer getNights() {
        return nights;
    }

    public void setNights(Integer nights) {
        this.nights = nights;
    }

    @Override
    public String toString() {
        return "Stay in " + source.nameOrId() +
                (startTime != null ?
                        " at " + startTime +
                                " for " + nights + " nights " : "") +
                (priceResolver != null ? " with price:" + getPriceResolver() : "");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StayInPlaceActivity)) return false;

        StayInPlaceActivity routeItem = (StayInPlaceActivity) o;

        if (!endTime.equals(routeItem.endTime)) return false;
        if (!source.equals(routeItem.source)) return false;
        if (!startTime.equals(routeItem.startTime)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + startTime.hashCode();
        result = 31 * result + endTime.hashCode();
        return result;
    }

    @Override
    public void visit(ActivityVisitor visitor) {
        visitor.stop(this);
    }
}

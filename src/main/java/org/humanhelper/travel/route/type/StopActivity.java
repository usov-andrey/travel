package org.humanhelper.travel.route.type;

import org.humanhelper.travel.place.Place;

import java.util.Date;

/**
 * @author Андрей
 * @since 31.10.15
 */
public abstract class StopActivity extends ActivityBase implements TimeActivity {

    protected Date startTime;
    protected Date endTime;

    public StopActivity(Place place) {
        super(place);
    }

    @Override
    public String toString() {
        return "Stop at" + source.nameOrId() +
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
        if (!(o instanceof StopActivity)) return false;

        StopActivity routeItem = (StopActivity) o;

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

}

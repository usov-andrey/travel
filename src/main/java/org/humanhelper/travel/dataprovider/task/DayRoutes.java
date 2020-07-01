package org.humanhelper.travel.dataprovider.task;

import org.humanhelper.service.utils.DateHelper;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.service.period.DatePeriod;

import java.util.Date;

/**
 * @author Андрей
 * @since 19.12.14
 */
public class DayRoutes extends FixedPriceRoutes {

    private Date day;

    public DayRoutes() {
    }

    public DayRoutes(Place source, Place target, Date day) {
        this(source.getId(), target.getId(), day);
    }


    public DayRoutes(String source, String target, Date day) {
        super(source, target);
        this.day = day;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public DatePeriod calculateDayDatePeriod() {
        DatePeriod period = new DatePeriod(day, day);
        period.clearTime();
        return period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DayRoutes)) return false;

        DayRoutes dayRoutes = (DayRoutes) o;

        if (!day.equals(dayRoutes.day)) return false;
        if (!source.equals(dayRoutes.source)) return false;
        if (!target.equals(dayRoutes.target)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + target.hashCode();
        result = 31 * result + day.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DayRoutes{"
                + source +
                ", " + target +
                " at " + DateHelper.getDate(day) +
                '}';
    }
}

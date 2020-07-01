package org.humanhelper.travel.place.sourcetarget;

import java.util.Date;

/**
 * @author Андрей
 * @since 18.12.15
 */
public class DaySourceTarget extends SourceTarget {

    private Date day;
    private Date updateTime;

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}

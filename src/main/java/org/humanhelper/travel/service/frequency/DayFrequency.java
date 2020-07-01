package org.humanhelper.travel.service.frequency;

import org.humanhelper.service.utils.DateHelper;

import java.util.Date;
import java.util.Set;

/**
 * @author Андрей
 * @since 21.01.15
 */
public class DayFrequency extends Frequency {

    private Set<Integer> days;

    @Override
    public boolean inDay(Date day) {
        return days.contains(DateHelper.getDayOfWeek(day));
    }

    public Set<Integer> getDays() {
        return days;
    }

    public void setDays(Set<Integer> days) {
        this.days = days;
    }
}

package org.humanhelper.travel.service.period;

import org.humanhelper.service.utils.DateHelper;

import java.util.Date;
import java.util.Iterator;

/**
 * @author Андрей
 * @since 08.05.14
 */
public class DatePeriod extends Period<Date> {

    public DatePeriod() {
    }

    public DatePeriod(Date startAndEnd) {
        super(startAndEnd);
    }

    public DatePeriod(Date start, Date end) {
        super(start, end);
    }

    public DatePeriod(DatePeriod period) {
        this(period.start, period.end);
    }

    /**
     * Только сегодняшний день
     */
    public static DatePeriod today() {
        return fromToday(1);
    }

    public static DatePeriod fromToday(int days) {
        return from(DateHelper.clearTime(new Date()), days);
    }

    public static DatePeriod day(Date day) {
        return from(day, 1);
    }

    public static DatePeriod from(Date startDate, int days) {
        return new DatePeriod(startDate, DateHelper.incDays(startDate, days));
    }

    public static Iterable<Date> dayIterator(Date start, Date end) {
        return new DatePeriod(start, end).dayIterator();
    }

    /**
     * Заполняет период значениями с максимальным охватом между текущими и period
     */
    public void setMax(DatePeriod period) {
        if (period.start.before(start)) {
            start = period.start;
        }
        if (period.end.after(end)) {
            end = period.end;
        }
    }

    /**
     * @return Находится дата внутри периода или нет
     */
    public boolean inPeriodDate(Date date) {
        //Предполагается, что у start уже установлено начальное время дня, а у окончания конечное время дня
        return date.compareTo(start) >= 0 && date.compareTo(end) <= 0;
    }

    /**
     * У даты начала и окончания очищаем время
     */
    public void clearTime() {
        DateHelper.setTime(start, 0, 0);
        DateHelper.setTime(end, 23, 59);
    }

    public boolean inPeriodDateTime(Date dateTime) {
        return dateTime.after(start) && dateTime.before(end);
    }

    public Iterable<Date> dayIterator() {
        return new Iterable<Date>() {
            @Override
            public Iterator<Date> iterator() {
                return new Iterator<Date>() {

                    private Date currentDate = start;

                    @Override
                    public boolean hasNext() {
                        return !currentDate.after(end);
                    }

                    @Override
                    public Date next() {
                        Date currentDay = currentDate;
                        currentDate = DateHelper.incDays(currentDate, 1);
                        return currentDay;
                    }

                    @Override
                    public void remove() {

                    }
                };
            }
        };

    }

    public DatePeriod copy() {
        return new DatePeriod(start, end);
    }
}

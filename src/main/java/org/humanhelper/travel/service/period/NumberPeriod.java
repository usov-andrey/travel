package org.humanhelper.travel.service.period;

/**
 * @author Андрей
 * @since 08.05.14
 */
public class NumberPeriod extends Period<Integer> {

    public NumberPeriod() {
    }

    public NumberPeriod(Integer startAndEnd) {
        super(startAndEnd);
    }

    public NumberPeriod(Integer start, Integer end) {
        super(start, end);
    }

}

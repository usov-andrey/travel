package org.humanhelper.travel.service.period;

/**
 * @author Андрей
 * @since 08.05.14
 */
public class PricePeriod extends Period<Float> {

    public PricePeriod() {
    }

    public PricePeriod(Float startAndEnd) {
        super(startAndEnd);
    }

    public PricePeriod(Float start, Float end) {
        super(start, end);
    }
}

package org.humanhelper.travel.service.frequency;

import java.util.Date;

/**
 * Каждый день
 *
 * @author Андрей
 * @since 21.01.15
 */
public class DailyFrequency extends Frequency {

    @Override
    public boolean inDay(Date day) {
        return true;
    }
}

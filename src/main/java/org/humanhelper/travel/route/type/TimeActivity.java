package org.humanhelper.travel.route.type;

import org.humanhelper.travel.route.Activity;

import java.util.Date;

/**
 * Нахождение в каком-то месте(Source) в определенное время(StartTime-EndTime)
 *
 * @author Андрей
 * @since 21.01.15
 */
public interface TimeActivity extends Activity {

    String START_TIME_FIELD = "startTime";
    String END_TIME_FIELD = "endTime";

    Date getStartTime();

    void setStartTime(Date startTime);

    Date getEndTime();

    void setEndTime(Date endTime);

}

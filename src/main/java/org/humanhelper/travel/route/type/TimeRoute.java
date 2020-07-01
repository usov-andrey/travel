package org.humanhelper.travel.route.type;

/**
 * Перемещение из Source в Target по времени StartTime - EndTime
 *
 * @author Андрей
 * @since 21.01.15
 */
public interface TimeRoute extends Route, TimeActivity {

    TimeRoute copy();
}

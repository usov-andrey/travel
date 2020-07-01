package org.humanhelper.travel.route.resolver;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.service.period.DatePeriod;

/**
 * @author Андрей
 * @since 22.12.15
 */
@FunctionalInterface
public interface RoutesRequestConsumer {

    void accept(Place source, Place target, DatePeriod period);
}

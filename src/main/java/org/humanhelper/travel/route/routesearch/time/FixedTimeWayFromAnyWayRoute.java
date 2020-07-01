package org.humanhelper.travel.route.routesearch.time;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.price.PriceResolver;
import org.humanhelper.travel.route.type.FixedTimeRoute;
import org.humanhelper.travel.transport.Transport;

import java.util.Date;

/**
 * Необходимо разделять перемещения добавленные из AnyTimeWayRoute
 *
 * @author Андрей
 * @since 20.01.15
 */
public class FixedTimeWayFromAnyWayRoute extends FixedTimeRoute {

    public FixedTimeWayFromAnyWayRoute(Place source, PriceResolver priceResolver, Place target, Transport transport, Date startTime, Date endTime) {
        super(source, priceResolver, target, transport, startTime, endTime);
    }

}

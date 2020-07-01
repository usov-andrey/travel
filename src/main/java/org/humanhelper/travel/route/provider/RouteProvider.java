package org.humanhelper.travel.route.provider;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.resolver.RoutesRequest;
import org.humanhelper.travel.route.type.AnyTimeRoute;
import org.humanhelper.travel.route.type.TimeRoute;

import java.util.Collection;
import java.util.Set;

/**
 * Определяет логику поиска маршрутов между местами
 *
 * @author Андрей
 * @since 05.10.15
 */
public interface RouteProvider {

    /**
     * @return Маршруты по расписанию возможные из source в target в день day
     */
    Collection<TimeRoute> getTimeRoutes(RoutesRequest request);

    /**
     * @return Маршруты в любом время(не по расписанию) между source и target
     */
    Collection<AnyTimeRoute> getAnyTimeRoutes(RoutesRequest request);

    /**
     * @return Список мест, куда можно попасть из source
     */
    Set<? extends Place> getTargets(Place source);

}

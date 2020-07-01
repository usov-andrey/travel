package org.humanhelper.travel.route.provider;

import org.humanhelper.data.dao.Dao;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.dao.RouteDao;
import org.humanhelper.travel.route.resolver.RoutesRequest;
import org.humanhelper.travel.route.type.AnyTimeRoute;
import org.humanhelper.travel.route.type.TimeRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

/**
 * Перенаправляем в Dao
 *
 * @author Андрей
 * @since 22.12.15
 */
@Service(DaoRouteProvider.NAME)
@Profile(Dao.PROFILE)
public class DaoRouteProvider implements RouteProvider {

    public static final String NAME = "DaoRouteProvider";

    @Autowired
    private RouteDao dao;

    @Override
    public Collection<TimeRoute> getTimeRoutes(RoutesRequest request) {
        return dao.getTimeRoutes(request);
    }

    @Override
    public Collection<AnyTimeRoute> getAnyTimeRoutes(RoutesRequest request) {
        return dao.getAnyTimeRoutes(request);
    }

    @Override
    public Set<? extends Place> getTargets(Place source) {
        return dao.getTargets(source);
    }
}

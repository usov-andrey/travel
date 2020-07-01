package org.humanhelper.travel.route.provider;

import org.humanhelper.data.dao.Dao;
import org.humanhelper.travel.route.provider.external.TPRouteProvider;
import org.humanhelper.travel.route.resolver.RoutesRequest;
import org.humanhelper.travel.route.type.TimeRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * AnyTime и targets должны быть расчитаны заранее
 *
 * @author Андрей
 * @since 18.12.15
 */
@Service(PreCalcDaoRouteProvider.NAME)
@Profile(Dao.PROFILE)
public class PreCalcDaoRouteProvider extends DaoRouteProvider {

    public static final String NAME = "PreCalcDaoRouteProvider";

    @Autowired
    private TPRouteProvider tpRouteProvider;

    @Override
    public Collection<TimeRoute> getTimeRoutes(RoutesRequest request) {
        //Нам нужно узнать, какие маршруты уже есть в базе
        //Для недостающих маршрутов необходимо их получить
        //TODO
        return tpRouteProvider.getTimeRoutes(request);
    }

}

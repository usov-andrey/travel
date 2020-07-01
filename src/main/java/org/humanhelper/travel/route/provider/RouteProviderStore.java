package org.humanhelper.travel.route.provider;

import org.humanhelper.service.singleton.SingletonLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * ВХодная точка всех поставщиков маршрутов:
 * Для тестов в качестве routeProvider подставляется MemoryPlaceRouteProvider
 * Для рабочей конфигурации используется:
 * DaoRouteProvider в котором заложена логика:
 * Если в базе значения еще не получены, то нужно их получить из дочернего RouteProvider
 *
 * @author Андрей
 * @since 05.10.15
 */
@Service
public class RouteProviderStore {

    @Autowired(required = false)
    @Qualifier(PreCalcDaoRouteProvider.NAME)
    private RouteProvider routeProvider;

    public static RouteProvider getMainRouteProvider() {
        return SingletonLocator.get(RouteProviderStore.class).routeProvider;
    }

    public void setRouteProvider(RouteProvider routeProvider) {
        this.routeProvider = routeProvider;
    }
}

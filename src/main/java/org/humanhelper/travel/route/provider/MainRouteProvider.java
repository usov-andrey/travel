package org.humanhelper.travel.route.provider;

import org.humanhelper.travel.route.provider.external.LocationRouteProvider;
import org.humanhelper.travel.route.provider.external.TPRouteProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author Андрей
 * @since 15.10.15
 */
@Service
public class MainRouteProvider extends CompositeRouteProvider<RouteProvider> implements InitializingBean {

    @Autowired
    private TPRouteProvider tpRouteProvider;
    @Autowired
    private LocationRouteProvider locationRouteProvider;

    @Override
    public void afterPropertiesSet() throws Exception {
        setRouteProviders(tpRouteProvider, locationRouteProvider);
    }

    public void setRouteProviders(RouteProvider... routeProviders) {
        clear();
        Collections.addAll(this, routeProviders);
    }

}

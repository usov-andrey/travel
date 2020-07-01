package org.humanhelper.travel.route.provider.air.id;

import org.humanhelper.service.utils.DateHelper;
import org.humanhelper.travel.place.PlaceService;
import org.humanhelper.travel.place.type.transport.Airport;
import org.humanhelper.travel.route.provider.RouteProviderTest;
import org.humanhelper.travel.route.provider.air.id.EnTiket.TiketAirRouteProvider;
import org.humanhelper.travel.route.type.TimeRoute;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * @author Андрей
 * @since 08.10.15
 */
public class TiketAirRouteProviderTest extends RouteProviderTest<TiketAirRouteProvider> {

    @Autowired
    private PlaceService placeResolver;

    @Override
    protected TiketAirRouteProvider createRouteProvider() {
        return new TiketAirRouteProvider();
    }

    //@Test
    public void testGetTargets() throws Exception {
        Airport bjd = Airport.build("BDJ");
        Airport sub = Airport.build("SUB");
        placeResolver.add(bjd, sub);
        assertTrue(routeProvider.support(bjd, sub));
    }

    //@Test
    public void testGetRoutes() throws Exception {
        Airport bjd = Airport.build("BDJ");
        Airport sub = Airport.build("SUB");
        placeResolver.add(bjd, sub);
        Collection<TimeRoute> routes = routeProvider.getRoutes(bjd, sub, DateHelper.incDays(new Date(), 20));
        log.debug("Routes:" + routes);
        assertTrue(routes.size() > 0);
    }

    @Test
    public void testGetRoutes2() throws Exception {
        Airport bjd = Airport.build("KUL");
        Airport sub = Airport.build("KLO");
        placeResolver.add(bjd, sub);
        Collection<TimeRoute> routes = routeProvider.getRoutes(bjd, sub, DateHelper.incDays(new Date(), 34));
        log.debug("Routes:" + routes);
        assertTrue(routes.size() > 0);
    }
}
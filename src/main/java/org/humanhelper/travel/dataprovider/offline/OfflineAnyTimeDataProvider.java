package org.humanhelper.travel.dataprovider.offline;

import org.humanhelper.travel.country.CountryBuilder;
import org.humanhelper.travel.dataprovider.AnyTimeDataProvider;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.type.AnyTimeRoute;
import org.humanhelper.travel.route.type.sourcetarget.SourceTargetRouteList;

/**
 * Задание маршрутов с помощью Java кода
 *
 * @author Андрей
 * @since 02.09.15
 */
public abstract class OfflineAnyTimeDataProvider extends AnyTimeDataProvider {

    private static final String COMPANY = "offline";
    private RouteMap priceMap = new RouteMap();
    private CountryBuilder country;

    public OfflineAnyTimeDataProvider(CountryBuilder country) {
        this.country = country;
        fillRouteMap(priceMap);
    }

    abstract protected void fillRouteMap(RouteMap priceMap);

    protected final RouteMapEntry build() {
        return RouteMapEntry.create(country).agent(this);
    }

    @Override
    public SourceTargetRouteList<AnyTimeRoute> getWays(Place source, Place target) {
        SourceTargetRouteList<AnyTimeRoute> result = new SourceTargetRouteList<>(source, target, this);
        RouteMapEntry route = priceMap.getRoute(source, target, placeResolver);
        result.addRoute(new AnyTimeRoute(route.getPriceResolver(),
                source, target, getDuration(source, target), route.getTransport()));
        return result;
    }

    @Override
    public void updatePlaces() {
        for (RouteMapEntry route : priceMap.getRoutes()) {
            addOneWayRoute(route.getSource(placeResolver), route.getTarget(placeResolver));
        }
    }

    @Override
    public String getName() {
        return COMPANY;
    }
}

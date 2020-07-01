package org.humanhelper.travel.route.provider.external;

import org.humanhelper.travel.geo.GeoUtils;
import org.humanhelper.travel.integration.graphhopper.GraphHopperRouteService;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.PlaceService;
import org.humanhelper.travel.place.type.region.Region;
import org.humanhelper.travel.place.type.transport.TransportPlace;
import org.humanhelper.travel.price.PriceResolver;
import org.humanhelper.travel.price.SimplePriceAgent;
import org.humanhelper.travel.route.provider.AbstractRouteProvider;
import org.humanhelper.travel.route.resolver.RoutesRequest;
import org.humanhelper.travel.route.type.AnyTimeRoute;
import org.humanhelper.travel.route.type.AnyTimeRouteMap;
import org.humanhelper.travel.route.type.TimeRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Определяет ближайщие места и ищет до них маршруты
 *
 * @author Андрей
 * @since 04.10.15
 */
@Service
public class LocationRouteProvider extends AbstractRouteProvider {

    private static final Currency cur = Currency.getInstance("USD");

    @Autowired
    private PlaceService placeResolver;
    //@Autowired
    //private Rome2RioRouteService rome2RioRouteService;
    @Autowired
    private GraphHopperRouteService graphHopperRouteService;

    private AnyTimeRouteMap routeMap = new AnyTimeRouteMap() {
        @Override
        protected List<AnyTimeRoute> createRoutes(Place place) {
            List<AnyTimeRoute> routes = new ArrayList<>();
            Region region = place.getRegion();
            Collection<Place> nearestPlaces = placeResolver.getNearestPlaces(place);
            Set<Place> visitedPlaces = new HashSet<>();
            boolean isSourceTransportPlace = TransportPlace.isTransportPlace(place);
            for (Place target : nearestPlaces) {
                //Пока что рассматриваем пересечение границы только самолетами или автобусами, не машиной
                if (!target.getCountry().equals(place.getCountry())) {
                    continue;
                }
                //Регион для source уже был добавлен
                if (region != null && region.equals(target)) {
                    continue;
                }
                //Перемещаться можно только если одно из двух мест транспортное
                if (!isSourceTransportPlace && !TransportPlace.isTransportPlace(target)) {
                    continue;
                }
                //Если среди ближайщих мест есть группа мест, то ее не рассматриваем
                if (!Region.isRegion(target) || !Region.get(target).regionGroup()) {
                    AnyTimeRoute route = graphHopperRouteService.createAnyTimeRoute(place, target, visitedPlaces);
                    if (route != null) {
                        if (target instanceof TransportPlace) {
                            visitedPlaces.add(target);
                        }
                        routes.add(route);
                    }
                }
            }
            return routes;
        }
    };

    private AnyTimeRoute createRoute(Place source, Place target) {
        double distance = source.getLocation().getDistance(target.getLocation());
        return new AnyTimeRoute().sourceTarget(source, target)
                .duration(//Считаем, что мы идем по прямой со скоростью 25 км в час
                        GeoUtils.minutesWithVelocity(distance, 25))
                //стоимость 1 км = 5000 IDR, считаем, что проехать нам нужно будет в полтора раза больше
                .price(new PriceResolver(Math.round(distance * 5000 * 1.5), cur, SimplePriceAgent.INSTANCE));
    }

    @Override
    public Collection<TimeRoute> getTimeRoutes(RoutesRequest request) {
        return Collections.emptyList();
    }

    @Override
    public Collection<AnyTimeRoute> getAnyTimeRoutes(RoutesRequest request) {
        return routeMap.getRoutes(request);
    }

    @Override
    public Set<Place> getTargets(Place source) {
        return routeMap.getTargets(source);
    }

}

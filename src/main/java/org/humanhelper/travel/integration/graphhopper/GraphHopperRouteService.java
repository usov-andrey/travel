package org.humanhelper.travel.integration.graphhopper;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.PointList;
import com.graphhopper.util.shapes.GHPoint;
import org.humanhelper.service.singleton.Singleton;
import org.humanhelper.travel.geo.GeoUtils;
import org.humanhelper.travel.geo.Location;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.price.PriceResolver;
import org.humanhelper.travel.price.SimplePriceAgent;
import org.humanhelper.travel.route.type.AnyTimeRoute;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Поиск маршрутов через https://github.com/graphhopper
 *
 * @author Андрей
 * @since 10.10.15
 */
@Service
public class GraphHopperRouteService {

    private Singleton<GraphHopper> hopper = new Singleton<>(() -> {
        GraphHopper hopper1 = new GraphHopper().forServer();
        //hopper.setOSMFile("/Users/admin/IdeaWorkspace/myselfn/malaysia-singapore-brunei-latest.osm.pbf");
        hopper1.setGraphHopperLocation("/Users/admin/IdeaWorkspace/myselfn/GraphHooperData");
        hopper1.setEncodingManager(new EncodingManager("car"));
        hopper1.importOrLoad();
        return hopper1;
    });

    private GHPoint point(Place place) {
        return new GHPoint(place.getLocation().getLat(), place.getLocation().getLng());
    }

    /**
     * Есть прямой путь между source и target или нет
     */
    public AnyTimeRoute createAnyTimeRoute(Place source, Place target, Set<Place> visitedPlaces) {
        // simple configuration of the request object, see the GraphHopperServlet classs for more possibilities.

        GHRequest req = new GHRequest(point(source), point(target)).
                setWeighting("fastest").
                setVehicle("car").
                setLocale(Locale.US);
        GHResponse rsp = hopper.get().route(req);

        // first check for errors
        if (rsp.hasErrors()) {
            //Значит текущая загруженная карта не поддерживает эту локацию(например, загружена только карта индонезии,
            //а место ждля поиска находится в малазийи
            return null;
            //throw new IllegalStateException("Errors on request to GraphHopper:"+rsp);
        }

        // points, distance in meters and time in millis of the full path
        PointList pointList = rsp.getPoints();
        //Если путь проходит в радиусе 5 километров от какой-то ранее посещенной точки, то считаем,
        //что прямого пути к текущему target нет и он лежит через предыдуще посещенное место
        for (Place visitedPlace : visitedPlaces) {
            Location placeLocation = visitedPlace.getLocation();
            for (GHPoint point : pointList) {
                if (placeLocation.inRadius(Location.latLng(point.getLat(), point.getLon()), 5)) {
                    return null;
                }
            }
        }

        double distanceInKm = rsp.getDistance() / 1000;
        long timeInMs = rsp.getTime();
        AnyTimeRoute route = createRoute(source, target, distanceInKm, (int) TimeUnit.MILLISECONDS.toMinutes(timeInMs));
        return route;
    }

    private AnyTimeRoute createRoute(Place source, Place target, double distance, int duration) {
        if (duration == 0) {
            //Считаем, что мы идем по прямой со скоростью 25 км в час
            duration = GeoUtils.minutesWithVelocity(distance, 25);
        }
        return new AnyTimeRoute().sourceTarget(source, target)
                .duration(duration)
                //стоимость 1 км = 5000 IDR, считаем, что проехать нам нужно будет в полтора раза больше
                .price(new PriceResolver(Math.round(distance * 5000 * 1.5), Currency.getInstance("USD"), SimplePriceAgent.INSTANCE));
    }

}

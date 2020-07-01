package org.humanhelper.travel.route.routesearch.road;

import org.humanhelper.service.utils.TimerHelper;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.routesearch.NotFoundRouteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Находит оптимальные маршруты между набором мест без учета времени
 *
 * @author Андрей
 * @since 11.12.14
 */
@Service
public class RoadRouteSearchService {

    private static final Logger log = LoggerFactory.getLogger(RoadRouteSearchService.class);

    private static final int routeMinPrice = 1;

    @Autowired
    private RoadRouteQueueFactory queueFactory;

    /**
     * @return Находит дополнительный список мест(транзитные), через которые проходят маршруты для посещения places
     * В этот дополнительный список мест могут входить только города
     */
    public Collection<Place> getRouteTransitPlaces(Collection<Place> places) {
        Set<Place> transitPlaces = new HashSet<>();
        for (Place place : getRouteMap(places).getAllPlaces()) {
            if (!places.contains(place)) {
                transitPlaces.add(place);
            }
        }
        log.debug("Found transit places:" + transitPlaces);
        return transitPlaces;
    }

    public PlaceRouteNetwork getRouteMap(Collection<Place> places) {
        PlaceRouteNetwork result = new PlaceRouteNetwork();
        log.info("RouteNetwork calculated in ms:" + TimerHelper.run(() -> fillRouteMap(result, places)));
        return result;
    }

    private void fillRouteMap(PlaceRouteNetwork result, Collection<Place> places) {
        Map<Place, TransportPlaceSet> transportPlaces = getTransportPlaces(places);
        for (Place source : places) {
            TransportPlaceSet sourceTransportPlaces = transportPlaces.get(source);
            for (Place target : places)
                if (!source.equals(target)) {
                    TransportPlaceSet targetTransportPlaces = transportPlaces.get(target);
                    DijkstraRouteMap routeMap = dijkstra(sourceTransportPlaces.getTransportPlaces(), targetTransportPlaces.getTransportPlaces());
                    if (!routeMap.isPathFounded()) {
                        throw new NotFoundRouteException(source, target);
                    }
                    routeMap.fillPlaceSetRouteMap(result, sourceTransportPlaces, targetTransportPlaces);
                }
        }
    }

    private Map<Place, TransportPlaceSet> getTransportPlaces(Collection<Place> places) {
        Map<Place, TransportPlaceSet> transportPlaces = new HashMap<>();
        for (Place place : places) {
            transportPlaces.put(place, new TransportPlaceSet(place.transportPlaces(), place));
        }
        return transportPlaces;
    }

    private Collection<? extends Place> getTargets(Place source) {
        return source.targets();
    }

    private DijkstraRouteMap dijkstra(Set<Place> sources, Set<Place> targets) {
        //Вначале обрабатываем точки, которые находятся ближе к центру от targets
        Queue<Place> vertexQueue = queueFactory.apply(targets);
        DijkstraRouteMap routes = new DijkstraRouteMap(targets);
        for (Place source : sources) {
            vertexQueue.add(source);
            //стоимость до исходной точки равна нулю
            routes.addRoute(null, source, 0f);
        }
        //Ищем
        dijkstra(vertexQueue, routes, targets);
        return routes;
    }


    private void dijkstra(Queue<Place> vertexQueue, DijkstraRouteMap routes, Set<Place> endPlaces) {
        float minPriceToEndPlace = Float.MAX_VALUE;
        while (!vertexQueue.isEmpty()) {
            Place source = vertexQueue.poll();
            float priceToSource = routes.get(source).getBestPrice();
            //нет смысла смотреть targets, если мы уже имеем цену, хуже чем найденный вариант
            if (isMove(priceToSource + routeMinPrice, minPriceToEndPlace)) {
                Collection<? extends Place> targets = getTargets(source);
                for (Place target : targets) {
                    float newPrice = priceToSource + getPrice(source, target);
                    if (isMove(newPrice, minPriceToEndPlace)) {
                        DijkstraRouteList oldRoutes = routes.get(target);
                        if (oldRoutes == null || (isMove(newPrice, oldRoutes.getBestPrice()) &&
                                !oldRoutes.containsSource(source))) {//Если мы сюда еще не приходили этим же путем
                            if (log.isDebugEnabled()) {
                                log.debug("Move:" + source.nameOrId() + " " + target.nameOrId() + " " + newPrice);
                            }
                            routes.addRoute(source, target, newPrice);
                            if (endPlaces.contains(target)) {
                                if (log.isDebugEnabled()) {
                                    log.debug("Found route with price:" + newPrice);
                                }
                                //Пришли в нужное место
                                if (minPriceToEndPlace > newPrice) {
                                    minPriceToEndPlace = newPrice;
                                }
                            } else {
                                //Перемещаем target на другое место в очереди
                                vertexQueue.remove(target);
                                vertexQueue.add(target);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Расматриваем продвижение дальше или нет
     */
    private boolean isMove(float newPrice, float oldPrice) {
        //Ищем маршруты с +1 пересадкой от оптимального
        return newPrice <= oldPrice;// + routeMinPrice;
    }

    private float getPrice(Place source, Place target) {
        return routeMinPrice;
    }


}

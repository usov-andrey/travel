package org.humanhelper.travel.integration.rome2rio.route;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.humanhelper.service.conversion.ConverterService;
import org.humanhelper.service.http.Http;
import org.humanhelper.service.utils.UserAgentHelper;
import org.humanhelper.travel.geo.GeoUtils;
import org.humanhelper.travel.integration.rome2rio.Rome2RioVersionResolver;
import org.humanhelper.travel.integration.rome2rio.autocomplete.Rome2RioAutoCompleteService;
import org.humanhelper.travel.integration.rome2rio.autocomplete.Rome2RioPlace;
import org.humanhelper.travel.integration.rome2rio.autocomplete.Rome2RioPlaceKind;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.PlaceService;
import org.humanhelper.travel.place.type.transport.Airport;
import org.humanhelper.travel.price.PriceResolver;
import org.humanhelper.travel.price.SimplePriceAgent;
import org.humanhelper.travel.route.type.AnyTimeRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Set;

/**
 * Вначале нужно получить номер версии
 * global_buildNumber = "2015011504";
 *
 * @author Андрей
 * @since 17.01.15
 */
@Service
public class Rome2RioRouteService {

    private static final Logger log = LoggerFactory.getLogger(Rome2RioRouteService.class);

    private String url = "http://www.rome2rio.com/api/json/GetRoutes?%sflags=&currency=USD&version=%s&";

    @Autowired
    private ConverterService converterService;
    @Autowired
    private PlaceService placeResolver;
    @Autowired
    private Rome2RioVersionResolver versionResolver;
    @Autowired
    private Rome2RioAutoCompleteService autoCompleteService;

    private String version;

    /**
     * Список маршрутов между source и target
     */
    public List<List<R2RRoute>> search(Place source, Place target) {
        String version = getVersion();
        Rome2RioPlace rome2RioSource = getPlace(source);
        Rome2RioPlace rome2RioTarget = getPlace(target);
        String fullURL = String.format(url, rome2RioSource.toWebParams("o") + rome2RioTarget.toWebParams("d"), version);
        Http http = Http.get(fullURL);
        http.setUserAgent(UserAgentHelper.getRandomMobileUserAgent());
        ArrayNode array = converterService.getJSONArray(http.responseBody());
        List<List<R2RRoute>> result = new ArrayList<>();
        for (JsonNode node : array.get(2).get(1)) {
            List<R2RRoute> rome2RioRoutes = new ArrayList<>();
            double maxPrice = node.get(5).asDouble();
            double minPrice = node.get(6).asDouble();
            JsonNode routes = node.get(8);
            for (JsonNode route : routes) {
                R2RRoute rome2RioRoute = getRoute(route);
                if (rome2RioRoute != null) {
                    rome2RioRoutes.add(rome2RioRoute);
                }
            }
            if (rome2RioRoutes.size() > 0) {
                //Заменяем исходную и конечную точку на исходные source и target
                rome2RioRoutes.get(0).setSource(source);
                rome2RioRoutes.get(rome2RioRoutes.size() - 1).setTarget(target);
                result.add(rome2RioRoutes);
            }
        }
        return result;
    }

    private R2RRoute getRoute(JsonNode json) {
        R2RRouteKind kind = getKind(json);
        try {
            return kind.parseRoute(json);
        } catch (Exception e) {
            throw new IllegalStateException("Error on parse json:" + json + " with kind:" + kind, e);
        }
    }

    private R2RRouteKind getKind(JsonNode json) {
        String type = json.get(0).textValue();
        switch (type) {
            case "transit":
                return R2RRouteKind.valueOf(json.get(1).textValue());
            case "flight":
                return R2RRouteKind.flight;
            case "car":
                return R2RRouteKind.car;
            case "walk":
                return R2RRouteKind.walk;
            case "hotel":
                return R2RRouteKind.hotel;
            case "final":
                return R2RRouteKind.finalKind;
        }
        throw new IllegalStateException("Not found kind in json:" + json);
    }

    private Rome2RioPlace getPlace(Place place) {
        Rome2RioPlace result = place.getName() != null ? autoCompleteService.search(place) : null;
        if (result == null) {
            result = Rome2RioPlaceKind.address.createPlace(place);
        }
        return result;
    }

    private String getVersion() {
        if (version == null) {
            version = versionResolver.getVersion();
        }
        return version;
    }


    /**
     * Есть прямой путь между source и target или нет
     */
    public AnyTimeRoute createAnyTimeRoute(Place source, Place target, Set<Place> visitedPlaces) {
        List<List<R2RRoute>> compositeRoutes = search(source, target);
        for (List<R2RRoute> routes : compositeRoutes) {
            if (routes.size() == 1) {
                //Прямой путь
                R2RRoute directRoute = routes.get(0);
                if (directRoute.getKind() == R2RRouteKind.car) {
                    //Это значит, что маршрут rome2rio не построил и дает расстояние напрямую
                    //Добраться как-то можно, но мы не знаем как, поэтому даем примерную возможность
                    //Стоимость преодоления расстояния считаем сами
                    return createRoute(source, target, directRoute);
                } else {
                    if (directRoute.getKind() != R2RRouteKind.flight) {
                        //throw new IllegalStateException();
                        //TODO Возможно нужно взять расписание от Rome2Rio и загнать его к нам
                    }
                    //Если это прямой перелет, то это явно не AnyTimeRoute
                }
            } else {
                for (R2RRoute route : routes) {
                    if (route.getKind() == R2RRouteKind.flight) {
                        //Если мы куда-то полетели, то перемещения в target с помощью этого маршрута не AnyTimeRoute
                        break;
                    }
                    Place routeTarget = route.getTarget();
                    //Если мы перемещаемся в аэропорт, то переводим routeTarget в аэропорт из существующей модели в placeResolver
                    Airport airport = null;
                    if (Airport.isAirport(routeTarget)) {
                        airport = placeResolver.getAirportByCode(Airport.get(routeTarget).getCode());
                        if (airport != null) {
                            routeTarget = airport;
                        }
                    }
                    //Если мы перемещяемся в какое-то место, где уже были, то значит нету прямого пути в target
                    if (visitedPlaces.contains(routeTarget)) {
                        break;
                    }

                    if (airport != null) {
                        //Значит путь в текущий таргет лежит через аэропорт
                        return createRoute(source, airport, route);
                    }
                    if (route.getKind() != R2RRouteKind.car) {
                        //TODO Возможно нужно взять расписание от Rome2Rio и загнать его к нам
                    }
                    //Мы прибыли в какое-то неизвестное нам место, продолжать не стоит
                    break;
                }
            }
        }
        return null;
    }

    private AnyTimeRoute createRoute(Place source, Place target, R2RRoute route) {
        //TODO Необходимо если задана стоимость, то расчитывать ее
        double distance = route.getKm();
        int duration = route.getDurationInMinutes();
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

package org.humanhelper.travel.route.provider.external;

import org.humanhelper.service.singleton.Singleton;
import org.humanhelper.service.spring.DependencyInjector;
import org.humanhelper.service.utils.CollectionHelper;
import org.humanhelper.service.utils.DateHelper;
import org.humanhelper.travel.integration.travelpayouts.TPDataProvider;
import org.humanhelper.travel.place.type.transport.Airport;
import org.humanhelper.travel.route.provider.CompositeRouteProvider;
import org.humanhelper.travel.route.provider.air.AirportRouteProvider;
import org.humanhelper.travel.route.provider.air.id.EnTiket.TiketAirRouteProvider;
import org.humanhelper.travel.route.type.TimeRoute;
import org.humanhelper.travel.service.period.DatePeriod;
import org.humanhelper.travel.transport.air.AirlineList;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Андрей
 * @since 05.10.15
 */
@Service
public class TPRouteProvider extends AirportRouteProvider implements InitializingBean {

    private CompositeRouteProvider<AirportRouteProvider> compositeRouteProvider = new CompositeRouteProvider<>();

    @Autowired
    private TPDataProvider tpDataProvider;
    @Autowired
    private DependencyInjector dependencyInjector;
    private Singleton<Map<String, Map<String, AirlineList>>> airportTargets = new Singleton<>(this::createTargets);

    @Override
    public void afterPropertiesSet() throws Exception {
        register(
                new TiketAirRouteProvider()
        );
    }

    private void register(AirportRouteProvider... routeProviders) {
        for (AirportRouteProvider routeProvider : routeProviders) {
            dependencyInjector.inject(routeProvider);
            compositeRouteProvider.add(routeProvider);
        }
    }

    @Override
    public Collection<TimeRoute> getRoutes(Airport source, Airport target, DatePeriod period) {
        List<TimeRoute> result = new ArrayList<>();
        compositeRouteProvider.stream()
                .filter(provider -> provider.support(source, target))
                .forEach(provider -> {
                    result.addAll(provider.getRoutes(source, target, period));

                });
        List<TimeRoute> filteredResult = filter(result);
        if (log.isDebugEnabled()) {
            log.debug(DateHelper.getDate(period.getStart()) + "-" + DateHelper.getDate(period.getEnd()) +
                    " route:" + source.nameOrId() + " to " + target.nameOrId() +
                    " with:" + getAirlineList(source, target) + " routes:" + filteredResult);
        }
        return filteredResult;
    }

    private List<TimeRoute> filter(List<TimeRoute> routes) {
        if (routes.isEmpty()) {
            return routes;
        }

        Collections.sort(routes, (o1, o2) -> {
            //Сортируем сначала по стоимости, потом по времени начала, потом по времени окончания
            int diff = Float.compare(o1.getPriceResolver().getPrice(), o2.getPriceResolver().getPrice());
            if (diff != 0) {
                return diff;
            }
            diff = o1.getStartTime().compareTo(o2.getStartTime());
            if (diff != 0) {
                return diff;
            }
            return o1.getEndTime().compareTo(o2.getEndTime());
        });
        List<TimeRoute> result = new ArrayList<>();
        for (TimeRoute newRoute : routes) {
            if (!haveBestRoute(result, newRoute)) {
                result.add(newRoute);
            }
        }
        return result;
    }

    private boolean haveBestRoute(List<TimeRoute> routes, TimeRoute newRoute) {
        for (TimeRoute oldRoute : routes) {
            //Если одинакова цена и время вылета одинаково, то уже есть в списке рейс, который прибывает или во столько же или раньше
            if ((newRoute.getPriceResolver().getPrice() == oldRoute.getPriceResolver().getPrice())
                    && newRoute.getStartTime().equals(oldRoute.getStartTime())) {
                return true;
            }
            //Также не стоит платить дороже, и вылетать раньше, а прилетать позже
            //Если рейс начинается раньше, имеет большую цену и заканчивается позже, то такой рейс добавлять не нужно
            if (newRoute.getStartTime().before(oldRoute.getStartTime()) &&
                    newRoute.getEndTime().after(oldRoute.getEndTime())) {
                return true;
            }
        }
        return false;
    }

    private Map<String, Map<String, AirlineList>> createTargets() {
        return tpDataProvider.getAirportRoutes();
    }

    private AirlineList getAirlineList(Airport source, Airport target) {
        Map<String, AirlineList> targets = CollectionHelper.getSafeHashMap(airportTargets.get(), source.getCode());
        return targets.get(target.getCode());
    }

    @Override
    protected Set<Airport> getTargets(Airport airport) {
        Set<Airport> airports = new HashSet<>();
        Map<String, AirlineList> targets = CollectionHelper.getSafeHashMap(airportTargets.get(), airport.getCode());
        for (String code : targets.keySet()) {
            Airport target = placeResolver.getAirportByCode(code);
            if (target != null) {
                airports.add(target);
            }
        }
        return airports;
    }

}

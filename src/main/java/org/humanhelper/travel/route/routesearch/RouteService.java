package org.humanhelper.travel.route.routesearch;

import org.humanhelper.service.spring.DependencyInjector;
import org.humanhelper.service.utils.DateHelper;
import org.humanhelper.travel.place.PlaceList;
import org.humanhelper.travel.place.dao.PlaceDao;
import org.humanhelper.travel.route.dao.RouteDao;
import org.humanhelper.travel.route.routesearch.newsearch.NewTimeRouteSearchService;
import org.humanhelper.travel.route.routesearch.query.RouteQuery;
import org.humanhelper.travel.route.routesearch.road.PlaceRouteNetwork;
import org.humanhelper.travel.route.routesearch.road.RoadRouteSearchService;
import org.humanhelper.travel.route.type.CompositeActivity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Неплохая статья здесь: http://habrahabr.ru/post/171707/
 * <p>
 * Неплохой алгоритм описан здесь: http://www.lifl.fr/~clautiau/biblio/ber06caor.pdf
 *
 * @author Андрей
 * @since 27.05.14
 */
@Service
@RequestMapping("routes")
public class RouteService {
    private static final Logger log = LoggerFactory.getLogger(RouteService.class);

    @Autowired(required = false)//Для простых тестов dao не подключен
    private PlaceDao placeDao;
    @Autowired(required = false)//Для простых тестов dao не подключен
    private RouteDao routeDao;
    @Autowired
    private RoadRouteSearchService roadSearchService;
    @Autowired
    private DependencyInjector dependencyInjector;

    @RequestMapping(value = "info", method = RequestMethod.GET)
    public
    @ResponseBody
    String getInfo() {
        StringBuilder sb = new StringBuilder();
        Map<Date, Integer> byDates = routeDao.getFixedTimeRoutesCount();
        for (Date date : byDates.keySet()) {
            sb.append(DateHelper.getDate(date)).append(":").append(byDates.get(date)).append(", ");
        }
        return "Places:" + placeDao.getCount() + " ByDates:" + sb.toString();

    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public
    @ResponseBody
    List<CompositeActivity> getRoute(@RequestBody Query query) {
        log.debug("Route search by query:" + query.getQuery());
        List<CompositeActivity> route = search(query.getQuery()).getRouteList();
        log.debug("Roure search result:" + route);
        return route;
    }

    public RouteSearchResult search(RouteQuery query) {
        log.info("Calculate Road network for places:" + PlaceList.simple(query.getPlaces()));
        PlaceRouteNetwork places = roadSearchService.getRouteMap(query.getPlaces());
        log.info("Road network:" + PlaceList.simple(places.getAllPlaces()));
        //TimeRouteSearchService searchService = dependencyInjector.create(TimeRouteSearchService.class);
        NewTimeRouteSearchService searchService = dependencyInjector.create(NewTimeRouteSearchService.class);
        return searchService.getResultRouteList(query, places);
    }

    private static class Query {
        private RouteQuery query;

        public RouteQuery getQuery() {
            return query;
        }

        public void setQuery(RouteQuery query) {
            this.query = query;
        }
    }

}

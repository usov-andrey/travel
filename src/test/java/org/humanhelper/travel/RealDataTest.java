package org.humanhelper.travel;

import org.humanhelper.data.dao.Dao;
import org.humanhelper.service.task.TaskRunner;
import org.humanhelper.service.utils.DateHelper;
import org.humanhelper.travel.dao.DaoController;
import org.humanhelper.travel.dao.elastic.ElasticDaoFactory;
import org.humanhelper.travel.place.AutoCompleteController;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.PlaceList;
import org.humanhelper.travel.place.type.region.Region;
import org.humanhelper.travel.route.routesearch.RouteSearchResult;
import org.humanhelper.travel.route.routesearch.RouteService;
import org.humanhelper.travel.route.routesearch.query.RouteQuery;
import org.humanhelper.travel.route.routesearch.query.RouteQueryValidator;
import org.humanhelper.travel.service.period.DatePeriod;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

/**
 * Поиск оптимального маршрута на реальных данных
 *
 * @author Андрей
 * @since 08.01.16
 */
@ActiveProfiles(profiles = {TaskRunner.PROFILE, Dao.PROFILE, ElasticDaoFactory.PROFILE})
public class RealDataTest extends ApplicationTest {

    @Autowired
    private DaoController daoController;
    @Autowired
    private AutoCompleteController autoComplete;
    @Autowired
    private RouteService routeService;
    @Autowired
    private RouteQueryValidator routeQueryValidator;

    @Test
    public void bkkToMoscow() {
        daoController.create();
        Region bangkok = getFirstRegion("Bangkok");
        Region moscow = getFirstRegion("Moscow");

        Date date = DateHelper.getDate("2016-05-20");
        RouteQuery query = new RouteQuery().currencyUSD().start(bangkok, DatePeriod.from(date, 10))
                .end(moscow);
        routeQueryValidator.validate(query);
        RouteSearchResult routeSearchResult = routeService.search(query);
        routeSearchResult.printStatistics(log);
    }

    private Region getFirstRegion(String name) {
        PlaceList placeList = autoComplete.getPlacesByNamePrefix(name);
        Place place = placeList.getByName(name);
        if (place == null) {
            throw new IllegalStateException("Not found place by name:" + name + " in placeList:" + placeList);
        }
        return Region.get(place);
    }
}

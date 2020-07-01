package org.humanhelper.travel.route.routesearch;

import org.humanhelper.service.utils.TimerHelper;
import org.humanhelper.travel.MemoryDaoTest;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.PlaceService;
import org.humanhelper.travel.route.dao.MemoryRouteDao;
import org.humanhelper.travel.route.provider.DaoRouteProvider;
import org.humanhelper.travel.route.provider.RouteProviderStore;
import org.humanhelper.travel.route.routesearch.query.RouteQuery;
import org.humanhelper.travel.route.routesearch.query.RouteQueryValidator;
import org.humanhelper.travel.route.routesearch.road.RoadRouteQueueFactory;
import org.humanhelper.travel.route.routesearch.time.Graph;
import org.humanhelper.travel.route.routesearch.time.search.visitor.GraphVisitor;
import org.humanhelper.travel.route.routesearch.time.search.visitor.GraphVisitorFactory;
import org.humanhelper.travel.route.routesearch.time.search.visitor.LoggedBreadFirstGraphVisitor;
import org.humanhelper.travel.route.type.AnyTimeRoute;
import org.humanhelper.travel.route.type.TimeRoute;
import org.humanhelper.travel.route.type.builder.TimeRouteListBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Currency;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Андрей
 * @since 03.12.14
 */
public abstract class AbstractRouteTest extends MemoryDaoTest implements InitializingBean {

    @Autowired
    protected RoadRouteQueueFactory roadRouteQueueFactory;
    @Autowired
    private RouteProviderStore routeProviderStore;
    @Autowired
    private GraphVisitorFactory graphVisitorFactory;
    @Autowired
    private PlaceService placeResolver;
    @Autowired
    @Qualifier(DaoRouteProvider.NAME)
    private DaoRouteProvider routeProvider;
    @Autowired
    private MemoryRouteDao routeDao;
    @Autowired
    private RouteService routeService;
    @Autowired
    private RouteQueryValidator routeQueryValidator;

    @Override
    public void afterPropertiesSet() throws Exception {
        //Поставщик маршрутов будет только один
        //mainRouteProvider.setRouteProviders(routeProvider);
        //Отключаем кэширование и поставщик маршрутов будет только один
        routeProviderStore.setRouteProvider(routeProvider);

        graphVisitorFactory.setDelegate(this::createGraphVisitor);
        //По-умолчанию используем очередь без подсчета расстояния до цели
        roadRouteQueueFactory.setDelegate(places -> new LinkedBlockingQueue<>());

    }

    protected GraphVisitor createGraphVisitor(Graph graph) {
        return new LoggedBreadFirstGraphVisitor(graph);
    }

    protected Currency currency() {
        return Currency.getInstance("USD");
    }

    protected RouteQuery query() {
        return new RouteQuery().currency(currency());
    }

    protected TimeRouteListBuilder routes() {
        return TimeRouteListBuilder.build(currency());
    }

    protected AbstractRouteTest add(Place... places) {
        placeResolver.clear();
        routeDao.clear();
        placeResolver.add(places);
        return this;
    }

    protected AbstractRouteTest add(List<? extends TimeRoute> routes) {
        routes.forEach(routeDao::add);
        return this;
    }

    protected AbstractRouteTest addAnyTime(List<AnyTimeRoute> routes) {
        routes.forEach(routeDao::add);
        return this;
    }

    protected RouteSearchResult search(RouteQuery query) {
        routeQueryValidator.validate(query);
        RouteSearchResult routeSearchResult = TimerHelper.callWithTimeInMS(
                () -> routeService.search(query),
                object -> log.debug("Search processed in " + object + "ms")
        );
        routeSearchResult.printStatistics(log);
        return routeSearchResult;
    }

}

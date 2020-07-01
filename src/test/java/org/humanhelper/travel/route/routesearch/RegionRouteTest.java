package org.humanhelper.travel.route.routesearch;

import org.humanhelper.travel.place.type.region.Region;
import org.humanhelper.travel.place.type.transport.Airport;
import org.humanhelper.travel.route.routesearch.query.RouteQuery;
import org.humanhelper.travel.route.type.CompositeActivity;
import org.humanhelper.travel.service.period.DatePeriod;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Перемещения есть между аэропортами, а во входном запросе мы задаем посещение города
 * В городе может быть несколько аэропортов
 *
 * @author Андрей
 * @since 10.01.15
 */
public class RegionRouteTest extends AbstractRouteTest {

    @Test
    public void testRegionToRegionViaAirports() {
        //Из Бангкока-BKK, из BKK в SVO- Москву, из SVO в BKK-Бангкок
        Region bangkok = Region.build("Bangkok");
        Airport bkk = Airport.build("BKK", bangkok);

        Region moscow = Region.build("Moscow");
        Airport svo = Airport.build("SVO", moscow);

        DatePeriod dayPeriod = DatePeriod.fromToday(10);

        add(bangkok, moscow).add(
                routes()
                        .route(svo, bkk).time(9, 11).price(14000f)
                        .route(bkk, svo).time(10, 12).price(15000f)
                        .buildDaily(dayPeriod)
        );
        RouteQuery query = query().startEnd(bangkok, dayPeriod).stop(moscow, 3);
        List<CompositeActivity> routes = search(query).getRouteList();
        assertEquals(1, routes.size());
    }

    /**
     * Из Москвы в Манилу, через два аэропорта в Бангкоке
     */
    @Test
    public void testSimple() {
        Region moscow = Region.build("Moscow");
        Airport moscowSVO = Airport.build("SVO", moscow);
        Region bangkok = Region.build("Bangkok");
        Airport bangkokBKK = Airport.build("BKK", bangkok);
        Airport bangkokDMK = Airport.build("DMK", bangkok);
        Region manila = Region.build("Manila");
        Airport manilaMNL = Airport.build("MNL", manila);

        DatePeriod period = DatePeriod.fromToday(15);
        add(moscow, bangkok, manila).add(routes()
                .way(moscowSVO, bangkokBKK, 20, 9, 10, 14000f, 15000f, 11000f)
                .way(bangkokDMK, manilaMNL, 11, 17, 4, 2500f, 2500f)
                .buildDaily(period));
        addAnyTime(routes()
                .route(bangkokBKK, bangkokDMK).duration(3 * 60).backRoute()
                .buildAnyTime());

        RouteSearchResult result = search(query().startEnd(moscow, period).stop(manila, 4));
        List<CompositeActivity> routes = result.getRouteList();
        assertEquals(1, routes.size());
        assertEquals(8, routes.get(0).getRouteItems().size());
        assertEquals(32000, routes.get(0).getPrice(), 0);

    }

    /**
     * Регион вложенный в другой регион
     */
    @Test
    public void testRegionGroup() {
        Region moscow = Region.build("Moscow");
        Airport moscowSVO = Airport.build("SVO", moscow);
        Region bangkok = Region.build("Bangkok");
        Airport bangkokBKK = Airport.build("BKK", bangkok);
        Airport bangkokDMK = Airport.build("DMK", bangkok);
        Region bali = Region.build("Bali");
        Region denpasar = Region.build("Denpasar").linkRegion(bali);
        Airport denpasarDPS = Airport.build("DPS", denpasar);

        DatePeriod period = DatePeriod.fromToday(5);
        add(moscow, bangkok, bali).add(routes()
                .way(moscowSVO, bangkokBKK, 20, 9, 10, 14000f, 15000f)
                .way(bangkokDMK, denpasarDPS, 11, 17, 4, 2500f, 2500f)
                .buildDaily(period));
        addAnyTime(routes()
                .route(bangkokBKK, bangkokDMK).duration(3 * 60).backRoute()
                .buildAnyTime());
        RouteSearchResult result = search(query().startEnd(moscow, period).stop(bali, 1));
        List<CompositeActivity> routes = result.getRouteList();
        assertEquals(1, routes.size());
        assertEquals(8, routes.get(0).getRouteItems().size());
        assertEquals(36000, routes.get(0).getPrice(), 0);
    }

    /**
     * Прилетев из Москвы в Бангкок мы не успеваем переместиться из одного аэропорта за три часа
     * В результате нужно две остановки в Бангкоке, а не одна
     */
    @Test
    public void testWait() {
        Region moscow = Region.build("Moscow");
        Airport moscowSVO = Airport.build("SVO", moscow);
        Region bangkok = Region.build("Bangkok");
        Airport bangkokBKK = Airport.build("BKK", bangkok);
        Airport bangkokDMK = Airport.build("DMK", bangkok);
        Region manila = Region.build("Manila");
        Airport manilaMNL = Airport.build("MNL", manila);

        DatePeriod period = DatePeriod.fromToday(30);
        add(moscow, bangkok, manila).add(routes()
                .way(moscowSVO, bangkokBKK, 21, 9, 10, 14000f, 15000f, 11000f)
                .way(bangkokDMK, manilaMNL, 9, 17, 4, 2500f, 2500f)
                .buildDaily(period));
        addAnyTime(routes()
                .route(bangkokBKK, bangkokDMK).duration(3 * 60).backRoute()
                .buildAnyTime());
        RouteSearchResult result = search(query().startEnd(moscow, period).stop(manila, 4));
        List<CompositeActivity> routes = result.getRouteList();
        assertEquals(1, routes.size());
        assertEquals(33000, routes.get(0).getPrice(), 0);
        assertEquals(9, routes.get(0).getRouteItems().size());
    }
}
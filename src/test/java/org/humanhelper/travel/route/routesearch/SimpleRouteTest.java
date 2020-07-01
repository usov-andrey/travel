package org.humanhelper.travel.route.routesearch;

import org.humanhelper.travel.place.type.transport.Airport;
import org.humanhelper.travel.route.type.CompositeActivity;
import org.humanhelper.travel.service.period.DatePeriod;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Тесты, когда места только аэропорта и только time маршруты заданы
 *
 * @author Андрей
 * @since 03.12.14
 */
public class SimpleRouteTest extends AbstractRouteTest {

    @Test
    public void testBorders() {
        //Москва - Бангкок(2 ночи) - Москва
        Airport moscow = Airport.build("Moscow");
        Airport bangkok = Airport.build("Bangkok");
        DatePeriod period = DatePeriod.fromToday(5);

        add(moscow, bangkok).add(routes()
                .route(moscow, bangkok).time(20, 8).price(14000f).backRoute(12500f).time(8, 17).price(15000f)
                .buildDaily(period)
        );

        List<CompositeActivity> routes = search(query().startEnd(moscow, period).stop(bangkok, 2)).getRouteList();
        assertTrue(routes.size() > 0);
        assertEquals(27000, routes.get(0).getPrice(), 0);//25 + 2 ночи по 1000
        assertEquals(3, routes.get(0).getRouteItems().size());
    }

    //Оптимальный путь: Москва-Гонконг(не ночуя)-Бангкок-Гонконг-Москва - 15+3+3+2+2 = 25
    //Обычный путь: цена 26+2+2 = 30
    @Test
    public void testTwoWayPrice() {
        Airport moscow = Airport.build("Moscow");
        Airport bangkok = Airport.build("Bangkok");
        Airport hongkong = Airport.build("Hongkong");
        DatePeriod period = DatePeriod.fromToday(8);
        add(moscow, bangkok, hongkong).add(routes()
                .route(moscow, bangkok).time(20, 8).price(12800f).backRoute().time(8, 17).price(12800f)
                .route(bangkok, hongkong).time(15, 18).price(3000f).backRoute()
                .route(moscow, hongkong).time(21, 7).price(10000f).backRoute(75000f).time(10, 20)
                .buildDaily(period)
        );
        List<CompositeActivity> routes = search(
                query().startEnd(moscow, period).stop(hongkong, 2).stop(bangkok, 2)
        ).getRouteList();
        assertTrue(routes.size() > 0);
        assertEquals(29800, routes.get(0).getPrice(), 0);
        assertEquals(5, routes.get(0).getRouteItems().size());
    }

    @Test
    public void testTransits() {
        DatePeriod period = DatePeriod.fromToday(30);
        Airport moscow = Airport.build("Moscow");
        Airport bangkok = Airport.build("Bangkok");
        Airport hongkong = Airport.build("Hongkong");
        Airport manila = Airport.build("manila");
        Airport cebu = Airport.build("cebu");
        Airport elNido = Airport.build("El-Nido");
        Airport boracay = Airport.build("Boracay");

        add(moscow, bangkok, hongkong, manila, cebu, elNido, boracay);
        add(routes()
                .way(moscow, bangkok, 20, 9, 10, 12800f, 12800f)
                .way(bangkok, hongkong, 15, 15, 3, 3000f, 3000f)
                .way(moscow, hongkong, 21, 10, 10, 10000f, 10000f, 7500f)
                .way(hongkong, manila, 10, 15, 3, 3000f, 3000f)
                .way(hongkong, cebu, 10, 15, 4, 4500f, 4500f)
                .way(manila, elNido, 9, 13, 1, 1000f, 1000f)
                .way(cebu, elNido, 11, 13, 1, 2000f, 1800f)
                .way(cebu, boracay, 10, 13, 1, 1300f, 1500f)
                .way(manila, boracay, 14, 18, 1, 1500f, 1700f)
                .buildDaily(period)
        );

        List<CompositeActivity> routes = search(
                query().startEnd(moscow, period).stop(elNido, 4).stop(boracay, 5).stop(cebu, 1)
        ).getRouteList();
        assertTrue(routes.size() > 0);
        //10000 - остановки
        //assertEquals(42600, routes.get(0).getPrice(), 0);
        CompositeActivity bestRoute = routes.get(0);
        assertEquals(38000, bestRoute.getPrice(), 0);
        assertEquals(12, bestRoute.getRouteItems().size());

        /**
         * 19:56:12.681 [main] DEBUG o.humanhelper.travel.ApplicationTest - ---Route:38000.0 Days:12 Places:[BORACAY, CEBU, EL-NIDO, HONGKONG(T)]
         19:56:12.681 [main] DEBUG o.humanhelper.travel.ApplicationTest - From MOSCOW to HONGKONG at (14.10.2015 21:00:00, 15.10.2015 7:00:00) for 0.0USD
         19:56:12.681 [main] DEBUG o.humanhelper.travel.ApplicationTest - From HONGKONG to MANILA at (15.10.2015 10:00:00, 15.10.2015 13:00:00) for 3000.0USD
         19:56:12.682 [main] DEBUG o.humanhelper.travel.ApplicationTest - From MANILA to BORACAY at (15.10.2015 14:00:00, 15.10.2015 15:00:00) for 1500.0USD
         19:56:12.682 [main] DEBUG o.humanhelper.travel.ApplicationTest - Stay in BORACAY at Thu Oct 15 03:00:00 EAT 2015 for 5 nights with price:5000.0 USD
         19:56:12.683 [main] DEBUG o.humanhelper.travel.ApplicationTest - From BORACAY to CEBU at (20.10.2015 13:00:00, 20.10.2015 14:00:00) for 1500.0USD
         19:56:12.683 [main] DEBUG o.humanhelper.travel.ApplicationTest - Stay in CEBU at Tue Oct 20 03:00:00 EAT 2015 for 1 nights with price:1000.0 USD
         19:56:12.683 [main] DEBUG o.humanhelper.travel.ApplicationTest - From CEBU to EL-NIDO at (21.10.2015 11:00:00, 21.10.2015 12:00:00) for 2000.0USD
         19:56:12.684 [main] DEBUG o.humanhelper.travel.ApplicationTest - Stay in EL-NIDO at Wed Oct 21 03:00:00 EAT 2015 for 4 nights with price:4000.0 USD
         19:56:12.684 [main] DEBUG o.humanhelper.travel.ApplicationTest - From EL-NIDO to MANILA at (25.10.2015 13:00:00, 25.10.2015 14:00:00) for 1000.0USD
         19:56:12.684 [main] DEBUG o.humanhelper.travel.ApplicationTest - From MANILA to HONGKONG at (25.10.2015 15:00:00, 25.10.2015 18:00:00) for 3000.0USD
         19:56:12.684 [main] DEBUG o.humanhelper.travel.ApplicationTest - Transit in HONGKONG at Sun Oct 25 03:00:00 EAT 2015 for 1 nights with price:1000.0 USD
         19:56:12.684 [main] DEBUG o.humanhelper.travel.ApplicationTest - From HONGKONG to MOSCOW at (26.10.2015 10:00:00, 26.10.2015 20:00:00) for 15000.0USD
         */
    }

}

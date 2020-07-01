package org.humanhelper.travel.route.routesearch;

import org.humanhelper.travel.place.type.region.Region;
import org.humanhelper.travel.route.routesearch.time.Graph;
import org.humanhelper.travel.route.routesearch.time.search.visitor.BreadFirstGraphVisitor;
import org.humanhelper.travel.route.routesearch.time.search.visitor.GraphVisitor;
import org.humanhelper.travel.route.type.CompositeActivity;
import org.humanhelper.travel.service.period.DatePeriod;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Андрей
 * @since 31.10.15
 */
public class RealRouteTest extends AbstractRouteTest {

    @Test
    public void testBig() {
        Region moscow = Region.build("Moscow");
        Region bangkok = Region.build("Bangkok");
        Region hongkong = Region.build("Hongkong");
        Region manila = Region.build("Manila");
        Region hanoi = Region.build("Hanoi");
        Region pnompenh = Region.build("Pnompenh");
        Region kualumpur = Region.build("Kualumpur");
        Region seul = Region.build("Seul");

        DatePeriod period = DatePeriod.fromToday(30);//на год
        add(moscow, bangkok, hongkong, manila, hanoi, pnompenh, kualumpur, seul).add(routes()
                .route(moscow, bangkok).timeDuration(20, 10).price(14000f).backRoute(11000f).timeDuration(8, 9).price(15000f)
                .route(moscow, bangkok).timeDuration(19, 13).price(13000f).backRoute(10000f).timeDuration(9, 13).price(12000f)
                .route(moscow, bangkok).timeDuration(20, 12).price(14000f).backRoute(11000f).timeDuration(7, 12).price(14000f)
                .route(moscow, bangkok).timeDuration(21, 14).price(12000f).backRoute(9000f).timeDuration(10, 15).price(11000f)
                .route(moscow, bangkok).timeDuration(22, 13).price(11000f).backRoute(10000f).timeDuration(8, 13).price(16000f)

                .route(moscow, hongkong).timeDuration(22, 11).price(9000f).backRoute(8000f).timeDuration(9, 11).price(11000f)
                .route(moscow, hongkong).timeDuration(19, 9).price(12000f).backRoute(9000f).timeDuration(8, 9).price(11000f)
                .route(moscow, hongkong).timeDuration(21, 10).price(10000f).backRoute(9000f).timeDuration(10, 10).price(10000f)
                .route(moscow, hongkong).timeDuration(20, 8).price(13000f).backRoute(10000f).timeDuration(10, 8).price(12000f)
                .route(moscow, hongkong).timeDuration(19, 13).price(8000f).backRoute(7000f).timeDuration(7, 13).price(9000f)

                .route(moscow, hanoi).timeDuration(19, 9).price(9000f).backRoute(8000f).timeDuration(7, 9).price(9000f)
                .route(moscow, hanoi).timeDuration(20, 12).price(8000f).backRoute(7000f).timeDuration(9, 12).price(8000f)
                .route(moscow, hanoi).timeDuration(21, 15).price(7000f).backRoute(6000f).timeDuration(10, 15).price(8000f)
                .route(moscow, hanoi).timeDuration(22, 10).price(8000f).backRoute().timeDuration(8, 10).price(7000f)

                .route(moscow, kualumpur).timeDuration(20, 10).price(16000f).backRoute(13000f).timeDuration(8, 11).price(17000f)
                .route(moscow, kualumpur).timeDuration(19, 13).price(15000f).backRoute(12000f).timeDuration(9, 14).price(14000f)
                .route(moscow, kualumpur).timeDuration(20, 12).price(17000f).backRoute(13000f).timeDuration(7, 13).price(16000f)
                .route(moscow, kualumpur).timeDuration(21, 14).price(14000f).backRoute(1100f).timeDuration(10, 14).price(14000f)//TODO Ошибка
                .route(moscow, kualumpur).timeDuration(22, 13).price(13000f).backRoute(12000f).timeDuration(8, 14).price(14000f)

                .route(hongkong, bangkok).timeDuration(9, 4).price(2000f).backRoute().timeDuration(13, 4).price(1900f)
                .route(hongkong, bangkok).timeDuration(11, 4).price(2200f).backRoute().timeDuration(15, 4).price(2000f)
                .route(hongkong, bangkok).timeDuration(13, 4).price(2100f).backRoute().timeDuration(18, 4).price(1500f)
                .route(hongkong, bangkok).timeDuration(18, 4).price(2300f).backRoute().timeDuration(20, 4).price(1300f)
                .route(hongkong, bangkok).timeDuration(21, 4).price(1900f).backRoute().timeDuration(23, 4).price(1000f)
                .route(hongkong, bangkok).timeDuration(12, 4).price(2000f).backRoute().timeDuration(14, 4).price(1900f)
                .route(hongkong, bangkok).timeDuration(15, 4).price(2200f).backRoute().timeDuration(17, 4).price(2000f)
                .route(hongkong, bangkok).timeDuration(17, 4).price(2100f).backRoute().timeDuration(19, 4).price(1500f)
                .route(hongkong, bangkok).timeDuration(19, 4).price(2300f).backRoute().timeDuration(11, 4).price(1300f)
                .route(hongkong, bangkok).timeDuration(20, 4).price(1900f).backRoute().timeDuration(8, 4).price(1000f)

                .route(bangkok, manila).timeDuration(8, 4).price(3000f).backRoute().timeDuration(13, 4).price(2900f)
                .route(bangkok, manila).timeDuration(11, 4).price(3500f).backRoute().timeDuration(18, 4).price(3400f)
                .route(bangkok, manila).timeDuration(15, 4).price(2900f).backRoute().timeDuration(21, 4).price(2500f)
                .route(bangkok, manila).timeDuration(19, 4).price(2500f).backRoute().timeDuration(7, 4).price(2200f)

                .route(hongkong, manila).timeDuration(8, 3).price(2000f).backRoute().timeDuration(13, 3).price(1900f)
                .route(hongkong, manila).timeDuration(11, 3).price(2500f).backRoute().timeDuration(18, 3).price(2400f)
                .route(hongkong, manila).timeDuration(15, 3).price(1900f).backRoute().timeDuration(21, 3).price(1500f)
                .route(hongkong, manila).timeDuration(19, 3).price(1500f).backRoute().timeDuration(7, 3).price(1200f)
                .route(hongkong, manila).timeDuration(21, 3).price(1400f).backRoute().timeDuration(10, 3).price(2000f)
                .route(hongkong, manila).timeDuration(23, 3).price(1200f).backRoute().timeDuration(6, 3).price(1100f)

                .route(bangkok, hanoi).timeDuration(9, 4).price(2500f).backRoute().timeDuration(11, 4).price(2900f)
                .route(bangkok, hanoi).timeDuration(12, 4).price(3000f).backRoute().timeDuration(14, 4).price(3200f)
                .route(bangkok, hanoi).timeDuration(17, 4).price(2400f).backRoute().timeDuration(20, 4).price(2600f)
                .route(bangkok, hanoi).timeDuration(20, 4).price(2100f).backRoute().timeDuration(8, 4).price(2100f)

                .route(bangkok, seul).timeDuration(10, 5).price(5000f).backRoute().timeDuration(9, 5).price(5900f)
                .route(bangkok, seul).timeDuration(14, 5).price(6000f).backRoute().timeDuration(13, 5).price(6200f)
                .route(bangkok, seul).timeDuration(18, 5).price(4500f).backRoute().timeDuration(17, 5).price(4600f)
                .route(bangkok, seul).timeDuration(21, 5).price(3500f).backRoute().timeDuration(21, 5).price(4100f)

                .route(bangkok, pnompenh).timeDuration(9, 2).price(1500f).backRoute().timeDuration(12, 2).price(1800f)
                .route(bangkok, pnompenh).timeDuration(15, 2).price(1800f).backRoute().timeDuration(17, 2).price(1300f)

                .route(bangkok, kualumpur).timeDuration(9, 2).price(2100f).backRoute().timeDuration(13, 2).price(2100f)
                .route(bangkok, kualumpur).timeDuration(11, 2).price(2300f).backRoute().timeDuration(15, 2).price(2200f)
                .route(bangkok, kualumpur).timeDuration(13, 2).price(2100f).backRoute().timeDuration(18, 2).price(1500f)
                .route(bangkok, kualumpur).timeDuration(18, 2).price(2300f).backRoute().timeDuration(20, 2).price(1400f)
                .route(bangkok, kualumpur).timeDuration(21, 2).price(1900f).backRoute().timeDuration(23, 2).price(1000f)
                .route(bangkok, kualumpur).timeDuration(12, 2).price(2000f).backRoute().timeDuration(6, 2).price(1300f)
                .route(bangkok, kualumpur).timeDuration(15, 2).price(2200f).backRoute().timeDuration(17, 2).price(2100f)
                .route(bangkok, kualumpur).timeDuration(17, 2).price(2100f).backRoute().timeDuration(19, 2).price(1800f)
                .route(bangkok, kualumpur).timeDuration(19, 2).price(2300f).backRoute().timeDuration(11, 2).price(1500f)
                .route(bangkok, kualumpur).timeDuration(20, 2).price(1700f).backRoute().timeDuration(8, 2).price(1500f)

                .route(hongkong, kualumpur).timeDuration(9, 3).price(3100f).backRoute().timeDuration(13, 3).price(3100f)
                .route(hongkong, kualumpur).timeDuration(11, 3).price(3300f).backRoute().timeDuration(15, 3).price(3200f)
                .route(hongkong, kualumpur).timeDuration(13, 3).price(3100f).backRoute().timeDuration(18, 3).price(2500f)
                .route(hongkong, kualumpur).timeDuration(18, 3).price(3300f).backRoute().timeDuration(20, 3).price(2400f)
                .route(hongkong, kualumpur).timeDuration(21, 3).price(2900f).backRoute().timeDuration(23, 3).price(2100f)
                .route(hongkong, kualumpur).timeDuration(12, 3).price(3000f).backRoute().timeDuration(6, 3).price(2300f)
                .route(hongkong, kualumpur).timeDuration(15, 3).price(3200f).backRoute().timeDuration(17, 3).price(3100f)
                .route(hongkong, kualumpur).timeDuration(17, 3).price(3100f).backRoute().timeDuration(19, 3).price(2800f)
                .route(hongkong, kualumpur).timeDuration(19, 3).price(3300f).backRoute().timeDuration(11, 3).price(2500f)
                .route(hongkong, kualumpur).timeDuration(20, 3).price(2700f).backRoute().timeDuration(8, 3).price(2500f)

                .route(hongkong, hanoi).timeDuration(8, 1).price(1500f).backRoute().timeDuration(11, 1).price(1900f)
                .route(hongkong, hanoi).timeDuration(11, 1).price(2000f).backRoute().timeDuration(15, 1).price(1800f)
                .route(hongkong, hanoi).timeDuration(15, 1).price(1400f).backRoute().timeDuration(19, 1).price(1600f)
                .route(hongkong, hanoi).timeDuration(19, 1).price(1100f).backRoute().timeDuration(23, 1).price(1000f)

                .route(kualumpur, seul).timeDuration(10, 5).price(4800f).backRoute().timeDuration(9, 5).price(4900f)
                .route(kualumpur, seul).timeDuration(14, 5).price(5400f).backRoute().timeDuration(13, 5).price(5200f)
                .route(kualumpur, seul).timeDuration(18, 5).price(4300f).backRoute().timeDuration(17, 5).price(5600f)
                .route(kualumpur, seul).timeDuration(21, 5).price(3300f).backRoute().timeDuration(21, 5).price(3100f)

                .buildDaily(period));

        RouteSearchResult result = search(query().startEnd(moscow, period)
                .stop(hongkong, 2, 4)
                .stop(bangkok, 4, 6)
                .stop(pnompenh, 2, 4)
                .stop(manila, 4)
                .stop(seul, 3, 6)
                .stop(hanoi, 2, 4));
        List<CompositeActivity> routes = result.getRouteList();
        assertTrue(routes.size() > 0);
        assertEquals(37200, routes.get(0).getPrice(), 0);
        assertEquals(18, routes.get(0).getRouteItems().size());


		/*
		11:51:42.286 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Route:38500.0 Days:20 Places:[Seul, Bangkok, Pnompenh, Hanoi, Hongkong, Manila, Kualumpur(T)]
11:51:42.286 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Route:38800.0 Days:20 Places:[Seul, Bangkok, Pnompenh, Manila, Hanoi, Hongkong, Kualumpur(T)]
11:51:42.287 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Route:39100.0 Days:21 Places:[Seul, Bangkok, Pnompenh, Hongkong, Manila, Hanoi, Kualumpur(T)]
11:51:42.287 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Route:39100.0 Days:21 Places:[Seul, Bangkok, Pnompenh, Hongkong, Hanoi, Manila, Kualumpur(T)]
11:51:42.287 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Route:39100.0 Days:20 Places:[Seul, Bangkok, Pnompenh, Hanoi, Manila, Hongkong, Kualumpur(T)]
11:51:42.288 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Route:39400.0 Days:20 Places:[Seul, Bangkok, Pnompenh, Manila, Hongkong, Hanoi, Kualumpur(T)]
11:51:42.288 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Route:39500.0 Days:21 Places:[Seul, Bangkok(T), Pnompenh, Bangkok, Manila, Hanoi, Hongkong, Kualumpur(T)]
11:51:42.288 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Route:39500.0 Days:21 Places:[Seul, Bangkok(T), Pnompenh, Bangkok, Hanoi, Hongkong, Manila, Kualumpur(T)]

11:51:42.328 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Route:38500.0 Days:20 Places:[Seul, Bangkok, Pnompenh, Hanoi, Hongkong, Manila, Kualumpur(T)]
11:51:42.329 [main] INFO  o.humanhelper.travel.ApplicationTest - From Moscow to Kualumpur at (31.10.2015 19:00:00, 01.11.2015 8:00:00) for 0.0USD
11:51:42.330 [main] INFO  o.humanhelper.travel.ApplicationTest - From Kualumpur to Seul at (01.11.2015 21:00:00, 02.11.2015 2:00:00) for 3300.0USD
11:51:42.332 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Seul at Sun Nov 01 03:00:00 EAT 2015 for 3 nights  with price:3000.0 USD
11:51:42.334 [main] INFO  o.humanhelper.travel.ApplicationTest - From Seul to Bangkok at (04.11.2015 21:00:00, 05.11.2015 2:00:00) for 4100.0USD
11:51:42.334 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Bangkok at Wed Nov 04 03:00:00 EAT 2015 for 4 nights  with price:4000.0 USD
11:51:42.335 [main] INFO  o.humanhelper.travel.ApplicationTest - From Bangkok to Pnompenh at (08.11.2015 9:00:00, 08.11.2015 11:00:00) for 1500.0USD
11:51:42.337 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Pnompenh at Sun Nov 08 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
11:51:42.338 [main] INFO  o.humanhelper.travel.ApplicationTest - From Pnompenh to Bangkok at (10.11.2015 17:00:00, 10.11.2015 19:00:00) for 1300.0USD
11:51:42.338 [main] INFO  o.humanhelper.travel.ApplicationTest - From Bangkok to Hanoi at (10.11.2015 20:00:00, 11.11.2015 0:00:00) for 2100.0USD
11:51:42.339 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Hanoi at Tue Nov 10 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
11:51:42.339 [main] INFO  o.humanhelper.travel.ApplicationTest - From Hanoi to Hongkong at (12.11.2015 23:00:00, 13.11.2015 0:00:00) for 1000.0USD
11:51:42.340 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Hongkong at Thu Nov 12 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
11:51:42.340 [main] INFO  o.humanhelper.travel.ApplicationTest - From Hongkong to Manila at (14.11.2015 23:00:00, 15.11.2015 2:00:00) for 1200.0USD
11:51:42.340 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Manila at Sat Nov 14 03:00:00 EAT 2015 for 4 nights  with price:4000.0 USD
11:51:42.340 [main] INFO  o.humanhelper.travel.ApplicationTest - From Manila to Hongkong at (18.11.2015 6:00:00, 18.11.2015 9:00:00) for 1100.0USD
11:51:42.341 [main] INFO  o.humanhelper.travel.ApplicationTest - From Hongkong to Kualumpur at (18.11.2015 20:00:00, 18.11.2015 23:00:00) for 2700.0USD
11:51:42.341 [main] INFO  o.humanhelper.travel.ApplicationTest - Transit in Kualumpur at Wed Nov 18 03:00:00 EAT 2015 with price:1000.0 USD
11:51:42.341 [main] INFO  o.humanhelper.travel.ApplicationTest - From Kualumpur to Moscow at (19.11.2015 10:00:00, 20.11.2015 0:00:00) for 2200.0USD
11:51:42.342 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Route:38800.0 Days:20 Places:[Seul, Bangkok, Pnompenh, Manila, Hanoi, Hongkong, Kualumpur(T)]
11:51:42.342 [main] INFO  o.humanhelper.travel.ApplicationTest - From Moscow to Kualumpur at (31.10.2015 19:00:00, 01.11.2015 8:00:00) for 0.0USD
11:51:42.343 [main] INFO  o.humanhelper.travel.ApplicationTest - From Kualumpur to Seul at (01.11.2015 21:00:00, 02.11.2015 2:00:00) for 3300.0USD
11:51:42.343 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Seul at Sun Nov 01 03:00:00 EAT 2015 for 3 nights  with price:3000.0 USD
11:51:42.343 [main] INFO  o.humanhelper.travel.ApplicationTest - From Seul to Bangkok at (04.11.2015 21:00:00, 05.11.2015 2:00:00) for 4100.0USD
11:51:42.344 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Bangkok at Wed Nov 04 03:00:00 EAT 2015 for 4 nights  with price:4000.0 USD
11:51:42.344 [main] INFO  o.humanhelper.travel.ApplicationTest - From Bangkok to Pnompenh at (08.11.2015 9:00:00, 08.11.2015 11:00:00) for 1500.0USD
11:51:42.344 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Pnompenh at Sun Nov 08 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
11:51:42.345 [main] INFO  o.humanhelper.travel.ApplicationTest - From Pnompenh to Bangkok at (10.11.2015 17:00:00, 10.11.2015 19:00:00) for 1300.0USD
11:51:42.345 [main] INFO  o.humanhelper.travel.ApplicationTest - From Bangkok to Manila at (10.11.2015 19:00:00, 10.11.2015 23:00:00) for 2500.0USD
11:51:42.346 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Manila at Tue Nov 10 03:00:00 EAT 2015 for 4 nights  with price:4000.0 USD
11:51:42.347 [main] INFO  o.humanhelper.travel.ApplicationTest - From Manila to Hongkong at (14.11.2015 6:00:00, 14.11.2015 9:00:00) for 1100.0USD
11:51:42.347 [main] INFO  o.humanhelper.travel.ApplicationTest - From Hongkong to Hanoi at (14.11.2015 19:00:00, 14.11.2015 20:00:00) for 1100.0USD
11:51:42.348 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Hanoi at Sat Nov 14 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
11:51:42.348 [main] INFO  o.humanhelper.travel.ApplicationTest - From Hanoi to Hongkong at (16.11.2015 23:00:00, 17.11.2015 0:00:00) for 1000.0USD
11:51:42.348 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Hongkong at Mon Nov 16 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
11:51:42.349 [main] INFO  o.humanhelper.travel.ApplicationTest - From Hongkong to Kualumpur at (18.11.2015 20:00:00, 18.11.2015 23:00:00) for 2700.0USD
11:51:42.349 [main] INFO  o.humanhelper.travel.ApplicationTest - Transit in Kualumpur at Wed Nov 18 03:00:00 EAT 2015 with price:1000.0 USD
11:51:42.350 [main] INFO  o.humanhelper.travel.ApplicationTest - From Kualumpur to Moscow at (19.11.2015 10:00:00, 20.11.2015 0:00:00) for 2200.0USD
11:51:42.350 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Route:39100.0 Days:21 Places:[Seul, Bangkok, Pnompenh, Hongkong, Manila, Hanoi, Kualumpur(T)]
11:51:42.351 [main] INFO  o.humanhelper.travel.ApplicationTest - From Moscow to Kualumpur at (31.10.2015 19:00:00, 01.11.2015 8:00:00) for 0.0USD
11:51:42.351 [main] INFO  o.humanhelper.travel.ApplicationTest - From Kualumpur to Seul at (01.11.2015 21:00:00, 02.11.2015 2:00:00) for 3300.0USD
11:51:42.352 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Seul at Sun Nov 01 03:00:00 EAT 2015 for 3 nights  with price:3000.0 USD
11:51:42.352 [main] INFO  o.humanhelper.travel.ApplicationTest - From Seul to Bangkok at (04.11.2015 21:00:00, 05.11.2015 2:00:00) for 4100.0USD
11:51:42.353 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Bangkok at Wed Nov 04 03:00:00 EAT 2015 for 4 nights  with price:4000.0 USD
11:51:42.353 [main] INFO  o.humanhelper.travel.ApplicationTest - From Bangkok to Pnompenh at (08.11.2015 9:00:00, 08.11.2015 11:00:00) for 1500.0USD
11:51:42.354 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Pnompenh at Sun Nov 08 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
11:51:42.354 [main] INFO  o.humanhelper.travel.ApplicationTest - From Pnompenh to Bangkok at (10.11.2015 17:00:00, 10.11.2015 19:00:00) for 1300.0USD
11:51:42.354 [main] INFO  o.humanhelper.travel.ApplicationTest - From Bangkok to Hongkong at (10.11.2015 23:00:00, 11.11.2015 3:00:00) for 1000.0USD
11:51:42.355 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Hongkong at Wed Nov 11 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
11:51:42.355 [main] INFO  o.humanhelper.travel.ApplicationTest - From Hongkong to Manila at (13.11.2015 23:00:00, 14.11.2015 2:00:00) for 1200.0USD
11:51:42.356 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Manila at Fri Nov 13 03:00:00 EAT 2015 for 4 nights  with price:4000.0 USD
11:51:42.356 [main] INFO  o.humanhelper.travel.ApplicationTest - From Manila to Hongkong at (17.11.2015 6:00:00, 17.11.2015 9:00:00) for 1100.0USD
11:51:42.357 [main] INFO  o.humanhelper.travel.ApplicationTest - From Hongkong to Hanoi at (17.11.2015 19:00:00, 17.11.2015 20:00:00) for 1100.0USD
11:51:42.357 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Hanoi at Tue Nov 17 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
11:51:42.357 [main] INFO  o.humanhelper.travel.ApplicationTest - From Hanoi to Hongkong at (19.11.2015 19:00:00, 19.11.2015 20:00:00) for 1600.0USD
11:51:42.358 [main] INFO  o.humanhelper.travel.ApplicationTest - From Hongkong to Kualumpur at (19.11.2015 20:00:00, 19.11.2015 23:00:00) for 2700.0USD
11:51:42.359 [main] INFO  o.humanhelper.travel.ApplicationTest - Transit in Kualumpur at Thu Nov 19 03:00:00 EAT 2015 with price:1000.0 USD
11:51:42.359 [main] INFO  o.humanhelper.travel.ApplicationTest - From Kualumpur to Moscow at (20.11.2015 10:00:00, 21.11.2015 0:00:00) for 2200.0USD



15:33:33.522 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Route:48000.0 Days:18 Places:[Manila, Pnompenh, Seul, Bangkok, Hanoi, Hongkong]
15:33:33.522 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Route:48700.0 Days:19 Places:[Hanoi, Bangkok, Pnompenh, Seul, Bangkok(T), Manila, Hongkong]
15:33:33.523 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Route:48700.0 Days:18 Places:[Hanoi, Pnompenh, Seul, Bangkok, Manila, Hongkong]
15:33:33.523 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Route:48700.0 Days:19 Places:[Bangkok, Pnompenh, Seul, Bangkok(T), Manila, Hongkong, Hanoi]
15:33:33.523 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Route:49000.0 Days:19 Places:[Hongkong, Manila, Pnompenh, Seul, Bangkok, Hanoi, Hongkong(T)]
15:33:33.564 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Route:48000.0 Days:18 Places:[Manila, Pnompenh, Seul, Bangkok, Hanoi, Hongkong]
15:33:33.568 [main] INFO  o.humanhelper.travel.ApplicationTest - From Moscow to Hongkong at (31.10.2015 22:00:00, 01.11.2015 6:00:00) for 9000.0USD
15:33:33.569 [main] INFO  o.humanhelper.travel.ApplicationTest - From Hongkong to Manila at (01.11.2015 9:00:00, 01.11.2015 12:00:00) for 1500.0USD
15:33:33.569 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Manila at Sun Nov 01 03:00:00 EAT 2015 for 4 nights  with price:4000.0 USD
15:33:33.570 [main] INFO  o.humanhelper.travel.ApplicationTest - From Manila to Bangkok at (05.11.2015 17:00:00, 05.11.2015 21:00:00) for 2500.0USD
15:33:33.570 [main] INFO  o.humanhelper.travel.ApplicationTest - From Bangkok to Pnompenh at (05.11.2015 23:00:00, 06.11.2015 1:00:00) for 1000.0USD
15:33:33.571 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Pnompenh at Thu Nov 05 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
15:33:33.571 [main] INFO  o.humanhelper.travel.ApplicationTest - From Pnompenh to Bangkok at (07.11.2015 10:00:00, 07.11.2015 12:00:00) for 3000.0USD
15:33:33.572 [main] INFO  o.humanhelper.travel.ApplicationTest - From Bangkok to Seul at (07.11.2015 19:00:00, 08.11.2015 0:00:00) for 4000.0USD
15:33:33.572 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Seul at Sat Nov 07 03:00:00 EAT 2015 for 3 nights  with price:3000.0 USD
15:33:33.572 [main] INFO  o.humanhelper.travel.ApplicationTest - From Seul to Bangkok at (10.11.2015 21:00:00, 11.11.2015 2:00:00) for 5000.0USD
15:33:33.574 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Bangkok at Tue Nov 10 03:00:00 EAT 2015 for 4 nights  with price:4000.0 USD
15:33:33.575 [main] INFO  o.humanhelper.travel.ApplicationTest - From Bangkok to Hanoi at (14.11.2015 20:00:00, 15.11.2015 0:00:00) for 2000.0USD
15:33:33.575 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Hanoi at Sat Nov 14 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
15:33:33.576 [main] INFO  o.humanhelper.travel.ApplicationTest - From Hanoi to Hongkong at (16.11.2015 13:00:00, 16.11.2015 14:00:00) for 2000.0USD
15:33:33.576 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Hongkong at Mon Nov 16 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
15:33:33.577 [main] INFO  o.humanhelper.travel.ApplicationTest - From Hongkong to Moscow at (18.11.2015 11:00:00, 18.11.2015 19:00:00) for 10000.0USD
15:33:33.578 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Route:48700.0 Days:19 Places:[Hanoi, Bangkok, Pnompenh, Seul, Bangkok(T), Manila, Hongkong]
15:33:33.578 [main] INFO  o.humanhelper.travel.ApplicationTest - From Moscow to Hanoi at (31.10.2015 21:00:00, 01.11.2015 6:00:00) for 10000.0USD
15:33:33.578 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Hanoi at Sun Nov 01 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
15:33:33.579 [main] INFO  o.humanhelper.travel.ApplicationTest - From Hanoi to Bangkok at (03.11.2015 22:00:00, 04.11.2015 2:00:00) for 3000.0USD
15:33:33.579 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Bangkok at Tue Nov 03 03:00:00 EAT 2015 for 4 nights  with price:4000.0 USD
15:33:33.580 [main] INFO  o.humanhelper.travel.ApplicationTest - From Bangkok to Pnompenh at (07.11.2015 23:00:00, 08.11.2015 1:00:00) for 1000.0USD
15:33:33.580 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Pnompenh at Sat Nov 07 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
15:33:33.581 [main] INFO  o.humanhelper.travel.ApplicationTest - From Pnompenh to Bangkok at (09.11.2015 10:00:00, 09.11.2015 12:00:00) for 3000.0USD
15:33:33.581 [main] INFO  o.humanhelper.travel.ApplicationTest - From Bangkok to Seul at (09.11.2015 19:00:00, 10.11.2015 0:00:00) for 4000.0USD
15:33:33.581 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Seul at Mon Nov 09 03:00:00 EAT 2015 for 3 nights  with price:3000.0 USD
15:33:33.584 [main] INFO  o.humanhelper.travel.ApplicationTest - From Seul to Bangkok at (12.11.2015 21:00:00, 13.11.2015 2:00:00) for 5000.0USD
15:33:33.584 [main] INFO  o.humanhelper.travel.ApplicationTest - Transit in Bangkok at Thu Nov 12 03:00:00 EAT 2015 with price:1000.0 USD
15:33:33.585 [main] INFO  o.humanhelper.travel.ApplicationTest - From Bangkok to Manila at (13.11.2015 11:00:00, 13.11.2015 15:00:00) for 2500.0USD
15:33:33.586 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Manila at Fri Nov 13 03:00:00 EAT 2015 for 4 nights  with price:4000.0 USD
15:33:33.586 [main] INFO  o.humanhelper.travel.ApplicationTest - From Manila to Hongkong at (17.11.2015 18:00:00, 17.11.2015 21:00:00) for 1200.0USD
15:33:33.587 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Hongkong at Tue Nov 17 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
15:33:33.590 [main] INFO  o.humanhelper.travel.ApplicationTest - From Hongkong to Moscow at (19.11.2015 11:00:00, 19.11.2015 19:00:00) for 10000.0USD
15:33:33.590 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Route:48700.0 Days:18 Places:[Hanoi, Pnompenh, Seul, Bangkok, Manila, Hongkong]
15:33:33.592 [main] INFO  o.humanhelper.travel.ApplicationTest - From Moscow to Hanoi at (31.10.2015 21:00:00, 01.11.2015 6:00:00) for 10000.0USD
15:33:33.592 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Hanoi at Sun Nov 01 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
15:33:33.594 [main] INFO  o.humanhelper.travel.ApplicationTest - From Hanoi to Hongkong at (03.11.2015 13:00:00, 03.11.2015 14:00:00) for 2000.0USD
15:33:33.599 [main] INFO  o.humanhelper.travel.ApplicationTest - From Hongkong to Bangkok at (03.11.2015 14:00:00, 03.11.2015 18:00:00) for 2000.0USD
15:33:33.599 [main] INFO  o.humanhelper.travel.ApplicationTest - From Bangkok to Pnompenh at (03.11.2015 23:00:00, 04.11.2015 1:00:00) for 1000.0USD
15:33:33.599 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Pnompenh at Tue Nov 03 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
15:33:33.600 [main] INFO  o.humanhelper.travel.ApplicationTest - From Pnompenh to Bangkok at (05.11.2015 10:00:00, 05.11.2015 12:00:00) for 3000.0USD
15:33:33.601 [main] INFO  o.humanhelper.travel.ApplicationTest - From Bangkok to Seul at (05.11.2015 19:00:00, 06.11.2015 0:00:00) for 4000.0USD
15:33:33.601 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Seul at Thu Nov 05 03:00:00 EAT 2015 for 3 nights  with price:3000.0 USD
15:33:33.602 [main] INFO  o.humanhelper.travel.ApplicationTest - From Seul to Bangkok at (08.11.2015 21:00:00, 09.11.2015 2:00:00) for 5000.0USD
15:33:33.602 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Bangkok at Sun Nov 08 03:00:00 EAT 2015 for 4 nights  with price:4000.0 USD
15:33:33.603 [main] INFO  o.humanhelper.travel.ApplicationTest - From Bangkok to Manila at (12.11.2015 11:00:00, 12.11.2015 15:00:00) for 2500.0USD
15:33:33.603 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Manila at Thu Nov 12 03:00:00 EAT 2015 for 4 nights  with price:4000.0 USD
15:33:33.604 [main] INFO  o.humanhelper.travel.ApplicationTest - From Manila to Hongkong at (16.11.2015 18:00:00, 16.11.2015 21:00:00) for 1200.0USD
15:33:33.605 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Hongkong at Mon Nov 16 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
15:33:33.605 [main] INFO  o.humanhelper.travel.ApplicationTest - From Hongkong to Moscow at (18.11.2015 11:00:00, 18.11.2015 19:00:00) for 10000.0USD
15:33:33.606 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Route:48700.0 Days:19 Places:[Bangkok, Pnompenh, Seul, Bangkok(T), Manila, Hongkong, Hanoi]
15:33:33.606 [main] INFO  o.humanhelper.travel.ApplicationTest - From Moscow to Hongkong at (31.10.2015 22:00:00, 01.11.2015 6:00:00) for 9000.0USD
15:33:33.607 [main] INFO  o.humanhelper.travel.ApplicationTest - From Hongkong to Bangkok at (01.11.2015 14:00:00, 01.11.2015 18:00:00) for 2000.0USD
15:33:33.607 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Bangkok at Sun Nov 01 03:00:00 EAT 2015 for 4 nights  with price:4000.0 USD
15:33:33.607 [main] INFO  o.humanhelper.travel.ApplicationTest - From Bangkok to Pnompenh at (05.11.2015 23:00:00, 06.11.2015 1:00:00) for 1000.0USD
15:33:33.608 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Pnompenh at Thu Nov 05 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
15:33:33.608 [main] INFO  o.humanhelper.travel.ApplicationTest - From Pnompenh to Bangkok at (07.11.2015 10:00:00, 07.11.2015 12:00:00) for 3000.0USD
15:33:33.609 [main] INFO  o.humanhelper.travel.ApplicationTest - From Bangkok to Seul at (07.11.2015 19:00:00, 08.11.2015 0:00:00) for 4000.0USD
15:33:33.610 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Seul at Sat Nov 07 03:00:00 EAT 2015 for 3 nights  with price:3000.0 USD
15:33:33.610 [main] INFO  o.humanhelper.travel.ApplicationTest - From Seul to Bangkok at (10.11.2015 21:00:00, 11.11.2015 2:00:00) for 5000.0USD
15:33:33.611 [main] INFO  o.humanhelper.travel.ApplicationTest - Transit in Bangkok at Tue Nov 10 03:00:00 EAT 2015 with price:1000.0 USD
15:33:33.611 [main] INFO  o.humanhelper.travel.ApplicationTest - From Bangkok to Manila at (11.11.2015 11:00:00, 11.11.2015 15:00:00) for 2500.0USD
15:33:33.611 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Manila at Wed Nov 11 03:00:00 EAT 2015 for 4 nights  with price:4000.0 USD
15:33:33.613 [main] INFO  o.humanhelper.travel.ApplicationTest - From Manila to Hongkong at (15.11.2015 18:00:00, 15.11.2015 21:00:00) for 1200.0USD
15:33:33.613 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Hongkong at Sun Nov 15 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
15:33:33.614 [main] INFO  o.humanhelper.travel.ApplicationTest - From Hongkong to Hanoi at (17.11.2015 10:00:00, 17.11.2015 11:00:00) for 1000.0USD
15:33:33.614 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Hanoi at Tue Nov 17 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
15:33:33.614 [main] INFO  o.humanhelper.travel.ApplicationTest - From Hanoi to Moscow at (19.11.2015 10:00:00, 19.11.2015 19:00:00) for 10000.0USD


		 */

    }

    protected GraphVisitor createGraphVisitor(Graph graph) {
        return new BreadFirstGraphVisitor(graph);
    }

}

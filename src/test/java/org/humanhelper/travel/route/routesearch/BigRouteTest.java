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
 * @since 03.12.14
 */
public class BigRouteTest extends AbstractRouteTest {

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

        DatePeriod period = DatePeriod.fromToday(30 * 12);//на год
        add(moscow, bangkok, hongkong, manila, hanoi, pnompenh, kualumpur, seul).add(routes()
                .way(moscow, bangkok, 20, 9, 10, 14000f, 15000f, 11000f)
                .way(moscow, hongkong, 22, 11, 8, 9000f, 10000f, 7000f)
                .way(moscow, hanoi, 21, 10, 9, 10000f, 10000f, 8000f)
                .way(moscow, kualumpur, 19, 8, 11, 16000f, 17000f, 13000f)
                .way(hongkong, bangkok, 9, 14, 4, 2000f, 1500f)
                .way(bangkok, hongkong, 9, 14, 4, 1500f, 2000f)
                .way(bangkok, hongkong, 19, 23, 4, 2000f, 3000f)
                .way(bangkok, manila, 11, 17, 4, 2500f, 2500f)
                .way(hongkong, manila, 9, 18, 3, 1500f, 1200f)
                .way(bangkok, hanoi, 20, 22, 4, 2000f, 3000f)
                .way(bangkok, seul, 19, 21, 5, 4000f, 5000f)
                .way(bangkok, pnompenh, 23, 10, 2, 1000f, 3000f)
                .way(bangkok, kualumpur, 14, 17, 2, 1000f, 3000f)
                .way(bangkok, kualumpur, 10, 13, 2, 1000f, 3000f)
                .way(hongkong, kualumpur, 10, 13, 3, 2000f, 4000f)
                .way(hongkong, hanoi, 10, 13, 1, 1000f, 2000f)
                .way(seul, kualumpur, 9, 18, 5, 3000f, 6000f)
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
        assertEquals(52000, routes.get(0).getPrice(), 0);
        assertEquals(16, routes.get(0).getRouteItems().size());


		/*
		Лучший маршрут
		21:48:54.318 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Route:52000.0 Days:18 Places:[MANILA, PNOMPENH, SEUL, BANGKOK, HANOI, HONGKONG]
21:48:54.319 [main] INFO  o.humanhelper.travel.ApplicationTest - From MOSCOW to HONGKONG at (30.10.2015 22:00:00, 31.10.2015 6:00:00) for 0.0USD
21:48:54.319 [main] INFO  o.humanhelper.travel.ApplicationTest - From HONGKONG to MANILA at (31.10.2015 9:00:00, 31.10.2015 12:00:00) for 1500.0USD
21:48:54.320 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in MANILA at Sat Oct 31 03:00:00 EAT 2015 for 4 nights  with price:4000.0 USD
21:48:54.320 [main] INFO  o.humanhelper.travel.ApplicationTest - From MANILA to BANGKOK at (04.11.2015 17:00:00, 04.11.2015 21:00:00) for 2500.0USD
21:48:54.320 [main] INFO  o.humanhelper.travel.ApplicationTest - From BANGKOK to PNOMPENH at (04.11.2015 23:00:00, 05.11.2015 1:00:00) for 1000.0USD
21:48:54.320 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in PNOMPENH at Wed Nov 04 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
21:48:54.321 [main] INFO  o.humanhelper.travel.ApplicationTest - From PNOMPENH to BANGKOK at (06.11.2015 10:00:00, 06.11.2015 12:00:00) for 3000.0USD
21:48:54.321 [main] INFO  o.humanhelper.travel.ApplicationTest - From BANGKOK to SEUL at (06.11.2015 19:00:00, 07.11.2015 0:00:00) for 4000.0USD
21:48:54.321 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in SEUL at Fri Nov 06 03:00:00 EAT 2015 for 3 nights  with price:3000.0 USD
21:48:54.322 [main] INFO  o.humanhelper.travel.ApplicationTest - From SEUL to BANGKOK at (09.11.2015 21:00:00, 10.11.2015 2:00:00) for 5000.0USD
21:48:54.322 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in BANGKOK at Mon Nov 09 03:00:00 EAT 2015 for 4 nights  with price:4000.0 USD
21:48:54.323 [main] INFO  o.humanhelper.travel.ApplicationTest - From BANGKOK to HANOI at (13.11.2015 20:00:00, 14.11.2015 0:00:00) for 2000.0USD
21:48:54.323 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in HANOI at Fri Nov 13 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
21:48:54.323 [main] INFO  o.humanhelper.travel.ApplicationTest - From HANOI to HONGKONG at (15.11.2015 13:00:00, 15.11.2015 14:00:00) for 2000.0USD
21:48:54.324 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in HONGKONG at Sun Nov 15 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
21:48:54.324 [main] INFO  o.humanhelper.travel.ApplicationTest - From HONGKONG to MOSCOW at (17.11.2015 11:00:00, 17.11.2015 19:00:00) for 14000.0USD
21:48:54.324 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Route:53000.0 Days:19 Places:[HONGKONG, MANILA, PNOMPENH, SEUL, BANGKOK, HANOI, HONGKONG(T)]
21:48:54.325 [main] INFO  o.humanhelper.travel.ApplicationTest - From MOSCOW to HONGKONG at (30.10.2015 22:00:00, 31.10.2015 6:00:00) for 0.0USD
21:48:54.325 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in HONGKONG at Sat Oct 31 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
21:48:54.325 [main] INFO  o.humanhelper.travel.ApplicationTest - From HONGKONG to MANILA at (02.11.2015 9:00:00, 02.11.2015 12:00:00) for 1500.0USD
21:48:54.325 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in MANILA at Mon Nov 02 03:00:00 EAT 2015 for 4 nights  with price:4000.0 USD
21:48:54.326 [main] INFO  o.humanhelper.travel.ApplicationTest - From MANILA to BANGKOK at (06.11.2015 17:00:00, 06.11.2015 21:00:00) for 2500.0USD
21:48:54.326 [main] INFO  o.humanhelper.travel.ApplicationTest - From BANGKOK to PNOMPENH at (06.11.2015 23:00:00, 07.11.2015 1:00:00) for 1000.0USD
21:48:54.326 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in PNOMPENH at Fri Nov 06 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
21:48:54.326 [main] INFO  o.humanhelper.travel.ApplicationTest - From PNOMPENH to BANGKOK at (08.11.2015 10:00:00, 08.11.2015 12:00:00) for 3000.0USD
21:48:54.327 [main] INFO  o.humanhelper.travel.ApplicationTest - From BANGKOK to SEUL at (08.11.2015 19:00:00, 09.11.2015 0:00:00) for 4000.0USD
21:48:54.327 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in SEUL at Sun Nov 08 03:00:00 EAT 2015 for 3 nights  with price:3000.0 USD
21:48:54.328 [main] INFO  o.humanhelper.travel.ApplicationTest - From SEUL to BANGKOK at (11.11.2015 21:00:00, 12.11.2015 2:00:00) for 5000.0USD
21:48:54.328 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in BANGKOK at Wed Nov 11 03:00:00 EAT 2015 for 4 nights  with price:4000.0 USD
21:48:54.328 [main] INFO  o.humanhelper.travel.ApplicationTest - From BANGKOK to HANOI at (15.11.2015 20:00:00, 16.11.2015 0:00:00) for 2000.0USD
21:48:54.328 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in HANOI at Sun Nov 15 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
21:48:54.329 [main] INFO  o.humanhelper.travel.ApplicationTest - From HANOI to HONGKONG at (17.11.2015 13:00:00, 17.11.2015 14:00:00) for 2000.0USD
21:48:54.329 [main] INFO  o.humanhelper.travel.ApplicationTest - Transit in HONGKONG at Tue Nov 17 03:00:00 EAT 2015 with price:1000.0 USD
21:48:54.330 [main] INFO  o.humanhelper.travel.ApplicationTest - From HONGKONG to MOSCOW at (18.11.2015 11:00:00, 18.11.2015 19:00:00) for 14000.0USD
21:48:54.330 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Route:53000.0 Days:19 Places:[MANILA, BANGKOK, SEUL, BANGKOK(T), PNOMPENH, HANOI, HONGKONG]
21:48:54.330 [main] INFO  o.humanhelper.travel.ApplicationTest - From MOSCOW to HONGKONG at (30.10.2015 22:00:00, 31.10.2015 6:00:00) for 0.0USD
21:48:54.331 [main] INFO  o.humanhelper.travel.ApplicationTest - From HONGKONG to MANILA at (31.10.2015 9:00:00, 31.10.2015 12:00:00) for 1500.0USD
21:48:54.331 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in MANILA at Sat Oct 31 03:00:00 EAT 2015 for 4 nights  with price:4000.0 USD
21:48:54.332 [main] INFO  o.humanhelper.travel.ApplicationTest - From MANILA to BANGKOK at (04.11.2015 17:00:00, 04.11.2015 21:00:00) for 2500.0USD
21:48:54.332 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in BANGKOK at Wed Nov 04 03:00:00 EAT 2015 for 4 nights  with price:4000.0 USD
21:48:54.332 [main] INFO  o.humanhelper.travel.ApplicationTest - From BANGKOK to SEUL at (08.11.2015 19:00:00, 09.11.2015 0:00:00) for 4000.0USD
21:48:54.333 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in SEUL at Sun Nov 08 03:00:00 EAT 2015 for 3 nights  with price:3000.0 USD
21:48:54.333 [main] INFO  o.humanhelper.travel.ApplicationTest - From SEUL to BANGKOK at (11.11.2015 21:00:00, 12.11.2015 2:00:00) for 5000.0USD
21:48:54.333 [main] INFO  o.humanhelper.travel.ApplicationTest - Transit in BANGKOK at Wed Nov 11 03:00:00 EAT 2015 with price:1000.0 USD
21:48:54.334 [main] INFO  o.humanhelper.travel.ApplicationTest - From BANGKOK to PNOMPENH at (12.11.2015 23:00:00, 13.11.2015 1:00:00) for 1000.0USD
21:48:54.334 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in PNOMPENH at Thu Nov 12 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
21:48:54.334 [main] INFO  o.humanhelper.travel.ApplicationTest - From PNOMPENH to BANGKOK at (14.11.2015 10:00:00, 14.11.2015 12:00:00) for 3000.0USD
21:48:54.335 [main] INFO  o.humanhelper.travel.ApplicationTest - From BANGKOK to HANOI at (14.11.2015 20:00:00, 15.11.2015 0:00:00) for 2000.0USD
21:48:54.335 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in HANOI at Sat Nov 14 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
21:48:54.335 [main] INFO  o.humanhelper.travel.ApplicationTest - From HANOI to HONGKONG at (16.11.2015 13:00:00, 16.11.2015 14:00:00) for 2000.0USD
21:48:54.336 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in HONGKONG at Mon Nov 16 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
21:48:54.336 [main] INFO  o.humanhelper.travel.ApplicationTest - From HONGKONG to MOSCOW at (18.11.2015 11:00:00, 18.11.2015 19:00:00) for 14000.0USD
21:48:54.337 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Route:53000.0 Days:19 Places:[MANILA, BANGKOK, PNOMPENH, SEUL, BANGKOK(T), HANOI, HONGKONG]
21:48:54.337 [main] INFO  o.humanhelper.travel.ApplicationTest - From MOSCOW to HONGKONG at (30.10.2015 22:00:00, 31.10.2015 6:00:00) for 0.0USD
21:48:54.337 [main] INFO  o.humanhelper.travel.ApplicationTest - From HONGKONG to MANILA at (31.10.2015 9:00:00, 31.10.2015 12:00:00) for 1500.0USD
21:48:54.337 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in MANILA at Sat Oct 31 03:00:00 EAT 2015 for 4 nights  with price:4000.0 USD
21:48:54.338 [main] INFO  o.humanhelper.travel.ApplicationTest - From MANILA to BANGKOK at (04.11.2015 17:00:00, 04.11.2015 21:00:00) for 2500.0USD
21:48:54.338 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in BANGKOK at Wed Nov 04 03:00:00 EAT 2015 for 4 nights  with price:4000.0 USD
21:48:54.338 [main] INFO  o.humanhelper.travel.ApplicationTest - From BANGKOK to PNOMPENH at (08.11.2015 23:00:00, 09.11.2015 1:00:00) for 1000.0USD
21:48:54.338 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in PNOMPENH at Sun Nov 08 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
21:48:54.339 [main] INFO  o.humanhelper.travel.ApplicationTest - From PNOMPENH to BANGKOK at (10.11.2015 10:00:00, 10.11.2015 12:00:00) for 3000.0USD
21:48:54.339 [main] INFO  o.humanhelper.travel.ApplicationTest - From BANGKOK to SEUL at (10.11.2015 19:00:00, 11.11.2015 0:00:00) for 4000.0USD
21:48:54.339 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in SEUL at Tue Nov 10 03:00:00 EAT 2015 for 3 nights  with price:3000.0 USD
21:48:54.340 [main] INFO  o.humanhelper.travel.ApplicationTest - From SEUL to BANGKOK at (13.11.2015 21:00:00, 14.11.2015 2:00:00) for 5000.0USD
21:48:54.340 [main] INFO  o.humanhelper.travel.ApplicationTest - Transit in BANGKOK at Fri Nov 13 03:00:00 EAT 2015 with price:1000.0 USD
21:48:54.340 [main] INFO  o.humanhelper.travel.ApplicationTest - From BANGKOK to HANOI at (14.11.2015 20:00:00, 15.11.2015 0:00:00) for 2000.0USD
21:48:54.340 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in HANOI at Sat Nov 14 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
21:48:54.341 [main] INFO  o.humanhelper.travel.ApplicationTest - From HANOI to HONGKONG at (16.11.2015 13:00:00, 16.11.2015 14:00:00) for 2000.0USD
21:48:54.341 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in HONGKONG at Mon Nov 16 03:00:00 EAT 2015 for 2 nights  with price:2000.0 USD
21:48:54.342 [main] INFO  o.humanhelper.travel.ApplicationTest - From HONGKONG to MOSCOW at (18.11.2015 11:00:00, 18.11.2015 19:00:00) for 14000.0USD
21:48:54.342 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Route:53000.0 Days:19 Places:[MANILA, BANGKOK(T), SEUL, BANGKOK, PNOMPENH, HANOI, HONGKONG]

		 */
    }

    protected GraphVisitor createGraphVisitor(Graph graph) {
        return new BreadFirstGraphVisitor(graph);
    }

}

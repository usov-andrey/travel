package org.humanhelper.travel.route.routesearch;

import org.humanhelper.travel.place.type.region.Region;
import org.humanhelper.travel.place.type.transport.Airport;
import org.humanhelper.travel.route.type.CompositeActivity;
import org.humanhelper.travel.service.period.DatePeriod;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Андрей
 * @since 20.01.15
 */
public class AnyTimeRouteTest extends AbstractRouteTest {


    /**
     * Из Москвы в Манилу, через два аэропорта в Бангкоке на автобусе
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
        Region pattaya = Region.build("Pattaya");

        DatePeriod period = DatePeriod.fromToday(10);
        add(moscow, bangkok, manila).add(routes()
                .way(moscowSVO, bangkokBKK, 20, 9, 10, 14000f, 15000f, 11000f)
                .way(bangkokDMK, manilaMNL, 11, 17, 4, 2500f, 2500f)
                .buildDaily(period));
        addAnyTime(routes()
                .route(bangkokBKK, pattaya).price(1500f).duration(2 * 60).backRoute()
                .route(bangkokBKK, bangkokDMK).price(300f).duration(3 * 60).backRoute()
                .buildAnyTime());
        RouteSearchResult result = search(query().startEnd(moscow, period).stop(pattaya, 1).stop(manila, 1));
        List<CompositeActivity> routes = result.getRouteList();
        assertTrue(routes.size() > 0);
        assertEquals(33600, routes.get(0).getPrice(), 0);
        assertEquals(11, routes.get(0).getRouteItems().size());
		/*
		21:47:32.901 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Price with discount:33600.0 Price without discount:33600.0 Days:4 Places:[Manila, BKK(T), Pattaya]
21:47:32.903 [main] INFO  o.humanhelper.travel.ApplicationTest - From SVO to BKK at (07.11.2015 20:00:00, 08.11.2015 6:00:00) for 0.0USD
21:47:32.907 [main] INFO  o.humanhelper.travel.ApplicationTest - From BKK to DMK at (08.11.2015 7:00:00, 08.11.2015 10:00:00) for 300.0USD
21:47:32.910 [main] INFO  o.humanhelper.travel.ApplicationTest - From DMK to MNL at (08.11.2015 11:00:00, 08.11.2015 15:00:00) for 2500.0USD
21:47:32.911 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Manila at Sun Nov 08 03:00:00 EAT 2015 for 1 nights  with price:1000.0 USD
21:47:32.916 [main] INFO  o.humanhelper.travel.ApplicationTest - From MNL to DMK at (09.11.2015 17:00:00, 09.11.2015 21:00:00) for 2500.0USD
21:47:32.920 [main] INFO  o.humanhelper.travel.ApplicationTest - From DMK to BKK at (09.11.2015 22:00:00, 10.11.2015 1:00:00) for 300.0USD
21:47:32.923 [main] INFO  o.humanhelper.travel.ApplicationTest - Transit in BKK at Mon Nov 09 03:00:00 EAT 2015 with price:1000.0 USD
21:47:32.926 [main] INFO  o.humanhelper.travel.ApplicationTest - From BKK to Pattaya at (10.11.2015 7:00:00, 10.11.2015 9:00:00) for 1500.0USD
21:47:32.928 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Pattaya at Tue Nov 10 03:00:00 EAT 2015 for 1 nights  with price:1000.0 USD
21:47:32.931 [main] INFO  o.humanhelper.travel.ApplicationTest - From Pattaya to BKK at (11.11.2015 6:00:00, 11.11.2015 8:00:00) for 1500.0USD
21:47:32.934 [main] INFO  o.humanhelper.travel.ApplicationTest - From BKK to SVO at (11.11.2015 9:00:00, 11.11.2015 19:00:00) for 22000.0USD
21:47:32.936 [main] INFO  o.humanhelper.travel.ApplicationTest - ---Price with discount:34600.0 Price without discount:34600.0 Days:5 Places:[Pattaya, BKK(T), Manila, DMK(T)]
21:47:32.939 [main] INFO  o.humanhelper.travel.ApplicationTest - From SVO to BKK at (07.11.2015 20:00:00, 08.11.2015 6:00:00) for 0.0USD
21:47:32.943 [main] INFO  o.humanhelper.travel.ApplicationTest - From BKK to Pattaya at (08.11.2015 7:00:00, 08.11.2015 9:00:00) for 1500.0USD
21:47:32.945 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Pattaya at Sun Nov 08 03:00:00 EAT 2015 for 1 nights  with price:1000.0 USD
21:47:32.949 [main] INFO  o.humanhelper.travel.ApplicationTest - From Pattaya to BKK at (09.11.2015 6:00:00, 09.11.2015 8:00:00) for 1500.0USD
21:47:32.952 [main] INFO  o.humanhelper.travel.ApplicationTest - Transit in BKK at Mon Nov 09 03:00:00 EAT 2015 with price:1000.0 USD
21:47:32.955 [main] INFO  o.humanhelper.travel.ApplicationTest - From BKK to DMK at (10.11.2015 7:00:00, 10.11.2015 10:00:00) for 300.0USD
21:47:32.959 [main] INFO  o.humanhelper.travel.ApplicationTest - From DMK to MNL at (10.11.2015 11:00:00, 10.11.2015 15:00:00) for 2500.0USD
21:47:32.961 [main] INFO  o.humanhelper.travel.ApplicationTest - Stay in Manila at Tue Nov 10 03:00:00 EAT 2015 for 1 nights  with price:1000.0 USD
21:47:32.964 [main] INFO  o.humanhelper.travel.ApplicationTest - From MNL to DMK at (11.11.2015 17:00:00, 11.11.2015 21:00:00) for 2500.0USD
21:47:32.966 [main] INFO  o.humanhelper.travel.ApplicationTest - Transit in DMK at Wed Nov 11 03:00:00 EAT 2015 with price:1000.0 USD
21:47:32.970 [main] INFO  o.humanhelper.travel.ApplicationTest - From DMK to BKK at (12.11.2015 5:00:00, 12.11.2015 8:00:00) for 300.0USD
21:47:32.973 [main] INFO  o.humanhelper.travel.ApplicationTest - From BKK to SVO at (12.11.2015 9:00:00, 12.11.2015 19:00:00) for 22000.0USD
		 */
    }

}

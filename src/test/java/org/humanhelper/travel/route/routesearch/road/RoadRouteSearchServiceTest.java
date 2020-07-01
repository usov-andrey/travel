package org.humanhelper.travel.route.routesearch.road;

import org.humanhelper.travel.geo.Location;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.type.region.Region;
import org.humanhelper.travel.place.type.transport.Airport;
import org.humanhelper.travel.route.routesearch.AbstractRouteTest;
import org.humanhelper.travel.service.period.DatePeriod;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Андрей
 * @since 04.09.15
 */
public class RoadRouteSearchServiceTest extends AbstractRouteTest {

    @Autowired
    private RoadRouteSearchService service;

    @Test
    public void testSimple() {
        DatePeriod period = DatePeriod.fromToday(10);
        Region moscow = Region.build("Moscow");
        Airport moscowSVO = Airport.build("SVO", moscow);
        Region bangkok = Region.build("Bangkok");
        Airport bangkokBKK = Airport.build("BKK", bangkok);
        Airport bangkokDMK = Airport.build("DMK", bangkok);
        Region manila = Region.build("Manila");
        Airport manilaMNL = Airport.build("MNL", manila);
        Region bali = Region.build("Bali");
        Region denpasar = Region.build("Denpasar").linkRegion(bali);
        Airport denpasarDPS = Airport.build("DPS", denpasar);
        add(moscow, moscowSVO, bangkok, bangkokBKK, bangkokDMK, manila, manilaMNL, denpasar, denpasarDPS, bali);

        add(routes()
                .route(moscowSVO, bangkokBKK).backRoute()
                .route(bangkokDMK, manilaMNL).backRoute()
                .route(bangkokBKK, denpasarDPS).backRoute()
                .buildDaily(period));
        addAnyTime(routes()
                .route(bangkokBKK, bangkokDMK).backRoute()
                .buildAnyTime());

        Collection<Place> transitPlaces = getTransitPlaces(moscow, manilaMNL);
        assertEquals(2, transitPlaces.size());
        assertTrue(transitPlaces.contains(bangkokBKK));
        assertTrue(transitPlaces.contains(bangkokDMK));

        transitPlaces = getTransitPlaces(moscow, bali);
        assertEquals(1, transitPlaces.size());
        assertTrue(transitPlaces.contains(bangkokBKK));
    }

    private void findPlacesWithLocationQueue() {
        //Есть маршрут до dps через BKK, DMK и через FAR1, FAR2
        //BKK находится ближе к dps, поэтому он должен рассматириваться раньше чем FAR1
        //В результате если мы используем поиск по местоположению, а не поиск в ширину,
        // то не должно быть запроса на получение targets у FAR2
        Airport bkk = Airport.build("BKK").location(Location.latLng(800, 800));
        Airport dmk = Airport.build("DMK").location(Location.latLng(950, 950));
        Airport dps = Airport.build("DPS").location(Location.latLng(1000, 1000));
        Airport far1 = Airport.build("far1").location(Location.latLng(300, 300));
        Airport far2 = new Airport().code("far2").location(Location.latLng(900, 900));

        Airport svo = new Airport() {

            @Override
            public Set<Place> getTargets() {
                Set<Place> targets = new LinkedHashSet<>();
                //Выдаем места куда можно попасть из svo в четком порядке
                targets.add(far1);
                targets.add(bkk);
                return targets;
            }

        }.code("SVO").location(Location.latLng(100, 100));

        add(svo, bkk, dps, far1, far2);

        addAnyTime(routes()
                .route(dps, svo)
                //.route(svo, bkk) //Задается внутри svo
                //.route(svo, far1)
                .route(far1, far2).route(far2, dps)
                .route(bkk, dmk).route(dmk, dps)
                .buildAnyTime());

        Collection<Place> transitPlaces = getTransitPlaces(svo, dps);
        assertEquals(2, transitPlaces.size());
        assertTrue(transitPlaces.contains(bkk));
        assertTrue(transitPlaces.contains(dmk));
    }

    @Test
    public void testFindPlacesWithLocationQueue() {
        //ДОлжен вывалиться Exception
        try {
            findPlacesWithLocationQueue();
        } catch (IllegalStateException e) {
            if (!e.getMessage().equals("Can't get targets of far2")) {
                throw e;
            }
        }
        //Искать оптимальный маршрут до цели будем используя location
        roadRouteQueueFactory.setDelegate(roadRouteQueueFactory.createDefaultImplementation());
        try {
            findPlacesWithLocationQueue();
        } finally {
            roadRouteQueueFactory.setDelegate(places -> new LinkedBlockingQueue<>());
        }
    }

    protected Collection<Place> getTransitPlaces(Place... placeArray) {
        Collection<Place> places = Arrays.asList(placeArray);
        log.debug("SourcePlaces:" + places);
        Collection<Place> transitPlaces = service.getRouteTransitPlaces(places);
        log.debug("TransitPlaces:" + transitPlaces);
        return transitPlaces;
    }

}
package org.humanhelper.travel.integration.rome2rio.route;

import org.humanhelper.travel.ApplicationTest;
import org.humanhelper.travel.geo.Location;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.type.region.Region;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertTrue;

//@ActiveProfiles(MapDBHttpGetCache.PROFILE)
public class Rome2RioRouteServiceTest extends ApplicationTest {

    @Autowired
    private Rome2RioRouteService rome2RioRouteService;

    @Test
    public void testSearch() throws Exception {
        Region source = Place.build(Region.class, "Manila", "Manila");
        source.setLocation(Location.latLng(14.59f, 120.9f));
        Region target = Place.build(Region.class, "Panglao", "Panglao");
        target.setLocation(Location.latLng(9.58f, 123.7f));
        List<List<R2RRoute>> routes = rome2RioRouteService.search(source, target);
        assertTrue(routes.size() > 4);
    }

    @Test
    public void testSearchWithoutName() throws Exception {
        Place source = new Place().location(-8.7378, 121.824);
        Place target = new Place().location(-8.5539, 115.1862);
        List<List<R2RRoute>> routes = rome2RioRouteService.search(source, target);
        assertTrue(routes.size() > 3);
    }
}
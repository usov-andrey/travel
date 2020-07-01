package org.humanhelper.travel.route;

import org.geojson.*;
import org.humanhelper.travel.country.Country;
import org.humanhelper.travel.country.CountryService;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.dao.PlaceDao;
import org.humanhelper.travel.place.type.region.Region;
import org.humanhelper.travel.route.dao.RouteDao;
import org.humanhelper.travel.route.type.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author Андрей
 * @since 26.11.14
 */
@RestController
@RequestMapping(value = "map")
public class MapController {

    @Autowired
    private PlaceDao placeDao;
    @Autowired
    private RouteDao routeDao;
    @Autowired
    private CountryService countryService;

    @RequestMapping(value = "countries", method = RequestMethod.GET)
    public Collection<Country> getCountries() {
        return countryService.getCountries();
    }

    @RequestMapping(value = "routes", method = RequestMethod.GET)
    public FeatureCollection getRoutes() {
        List<Feature> routes = new ArrayList<>();
        Map<String, Place> placeMap = getPlaceMap();
        Set<Place> selectedPlaces = new HashSet<>();
        for (Route wayRoute : routeDao.getAll()) {
            Place source = placeMap.get(wayRoute.getSource().getId());
            Place target = placeMap.get(wayRoute.getTarget().getId());
            selectedPlaces.add(source);
            selectedPlaces.add(target);
            routes.add(getRouteFeature(source, target));
        }
        FeatureCollection collection = new FeatureCollection();
        collection.addAll(getPlaceFeatures(selectedPlaces));
        collection.addAll(routes);
        return collection;
    }

    private Feature getRouteFeature(Place source, Place target) {
        Feature line = new Feature();
        line.setGeometry(new LineString(getPosition(source), getPosition(target)));
        return line;
    }

    private List<Feature> getPlaceFeatures(Collection<Place> places) {
        List<Feature> result = new ArrayList<>();
        for (Place place : places) {
            result.add(createPlace(place));
        }
        return result;
    }

    private Map<String, Place> getPlaceMap() {
        Map<String, Place> placeMap = new HashMap<>();
        for (Place place : placeDao.getAll()) {
            placeMap.put(place.getId(), place);
        }
        return placeMap;
    }

    @RequestMapping(value = "places", method = RequestMethod.GET)
    public FeatureCollection getPlaces() {
        FeatureCollection collection = new FeatureCollection();
        Map<String, Place> placeMap = getPlaceMap();
        for (Place place : placeMap.values()) {
			/*
			if (place instanceof Airport) {
				Feature point = new Feature();
				LngLatAlt placePosition = getPosition(place);
				point.setGeometry(new Point(placePosition));
				point.setProperty("code", place.getId());
				point.setProperty("name", place.getName());
				collection.add(point);


			}*/
            collection.add(createPlace(place));
			/*
				LngLatAlt placePosition = getPosition(place);
				//Используем targetId, чтобы не делать кучу запросов в базу
				for (String targetId : place.getTargetIds()) {
					Place target = placeMap.get(targetId);
					if (target.getCountry().equals(Countries.PH.toString())) {
						if (place instanceof Airport && target instanceof Airport) {
							continue;//Авиаперемещений слишком много и они захломляют карту
						}
						Feature line = new Feature();
						line.setGeometry(new LineString(placePosition, getPosition(target)));
						collection.add(line);
					}
				}*/
        }
        return collection;
    }

    private Feature createPlace(Place place) {
        Feature point = new Feature();
        try {
            LngLatAlt placePosition = getPosition(place);
            point.setGeometry(new Point(placePosition));
            point.setProperty("id", place.getId());
            point.setProperty("name", place.getName());
            point.setProperty("icon", !Region.isRegion(place) ?
                    "/assets/images/marker.png" : "/assets/images/marker-red.png");
        } catch (Exception e) {
            throw new IllegalStateException("Error on create point by place " + place);
        }
        return point;
    }

    private LngLatAlt getPosition(Place place) {
        return new LngLatAlt(place.getLocation().getLng(), place.getLocation().getLat());
    }
}

package org.humanhelper.travel.place;

import org.apache.commons.lang3.ObjectUtils;
import org.humanhelper.travel.country.Country;
import org.humanhelper.travel.country.dao.CountryDao;
import org.humanhelper.travel.place.dao.PlaceDao;
import org.humanhelper.travel.place.type.region.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Андрей
 * @since 26.12.14
 */
@RestController
@RequestMapping("autoComplete")
public class AutoCompleteController {

    @Autowired
    private PlaceDao placeDao;
    @Autowired
    private CountryDao countryDao;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public PlaceList getPlacesByNamePrefix(@RequestParam("value") @NotNull String namePrefix) {
        Collection<Place> places = placeDao.getByNamePrefix(namePrefix, 20);
        if (places.isEmpty()) {
            places = getPlacesByCountry(namePrefix);
        }
		/*
		for (Place place : places) {
			if (place instanceof Airport) {
				if (!place.getName().contains("Airport")) {
					place.setName(place.getName() + " Airport");
				}
				Region city = place.getRegion();
				place.setName(place.getName() + ", " + city.getName());
			}
			place.setName(place.getName() + ", " + place.getCountry().getName());
		}*/
        PlaceList list = new PlaceList();
        list.addAll(places);
        Collections.sort(list, (o1, o2) -> {
            //Вначале выводим регионы, потом аэропорты
            //Регионы сортируем по количеству отелей
            if (Region.isRegion(o1)) {
                if (Region.isRegion(o2)) {
                    return ObjectUtils.compare(Region.get(o2).getHotels(), Region.get(o1).getHotels());
                } else {
                    return -1;
                }
            } else if (Region.isRegion(o2)) {
                return 1;
            } else {
                //Два аэропорта
                return 0;
            }
        });
        return list;
    }

    private Collection<Place> getPlacesByCountry(String countryPrefix) {
        Collection<Place> places = new ArrayList<>();
        //Для простоты пока что смотрим одну страну и получаем наиболее популярные места для нее
        Collection<Country> countries = countryDao.getByNamePrefix(countryPrefix, 1);
        for (Country country : countries) {
            places.addAll(placeDao.getPopularRegions(country, 10));
        }
        return places;
    }
}

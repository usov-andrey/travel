package org.humanhelper.travel.integration.rome2rio.autocomplete;

import org.humanhelper.service.conversion.ConverterService;
import org.humanhelper.service.http.Http;
import org.humanhelper.service.utils.WebHelper;
import org.humanhelper.travel.geo.Location;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.type.region.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author Андрей
 * @since 26.11.14
 */
@Service
public class Rome2RioAutoCompleteService {

    private static final String URL = "http://www.rome2rio.com/api/1.2/json/autocomplete?query=";

    @Autowired
    private ConverterService converterService;

    public Collection<Rome2RioPlace> search(String query) {
        Rome2RioAutoCompleteResponse response = converterService.getJSONObject(Http.get(URL + WebHelper.encodeURL(query)).responseBody(), Rome2RioAutoCompleteResponse.class);
        return response.getPlaces();
    }

    public Rome2RioPlace search(Region city) {
        for (Rome2RioPlace rome2RioPlace : search(city.getName())) {
            System.out.print(rome2RioPlace);
        }
        return null;
    }

    /**
     * Ищем по имени место ближайщее к координатам place, если не находим, то возвращаем null
     */
    public Rome2RioPlace search(Place place) {
        if (place.getName() == null) {
            throw new IllegalArgumentException("Place.name is null");
        }
        Rome2RioPlace result = null;
        Collection<Rome2RioPlace> rome2RioPlaces = search(place.getName());
        double resultDistance = Integer.MAX_VALUE;
        for (Rome2RioPlace rome2RioPlace : rome2RioPlaces) {
            double distance = place.getLocation().getDistance(Location.latLng(rome2RioPlace.getLat(), rome2RioPlace.getLng()));
            if (distance < 100 && resultDistance > distance) {//Если найденное место дальше от исходного чем 100 км, то мы нашли что-то не то
                resultDistance = distance;
                result = rome2RioPlace;
            }
        }
        return result;
    }
}

package org.humanhelper.travel.integration.osm;

import org.apache.commons.lang3.StringUtils;
import org.humanhelper.service.http.Http;
import org.humanhelper.service.utils.ProcessHelper;
import org.humanhelper.service.utils.WebHelper;
import org.humanhelper.travel.geo.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author Андрей
 * @since 19.01.15
 */
@Service
public class OpenStreetMapGeoCoder {

    private static final Logger log = LoggerFactory.getLogger(OpenStreetMapGeoCoder.class);
    private String url = "https://nominatim.openstreetmap.org/search?format=json&exclude_place_ids=43388554&accept-language=en&q=%s";

    /**
     * @return null, если ничего по этому типу не найдно
     */
    public Location getLocation(String name, List<OpenStreetMapPlaceType> types) {
        return getLocation(name, types, new ArrayList<String>());
    }

    private Location getLocation(String name, List<OpenStreetMapPlaceType> types, List<String> excludeIds) {
        OpenStreetMapPlaceList places = getPlaces(name, excludeIds);
        if (places.isEmpty()) {
            return null;
        }
        for (OpenStreetMapPlace place : places) {
            log.info("Found place:" + place);
            for (OpenStreetMapPlaceType type : types) {
                if (place.getType().equals(type)) {
                    return new Location(place.getLat(), place.getLon());
                }
            }
            excludeIds.add(place.getId());
        }
        return getLocation(name, types, excludeIds);
    }


    private OpenStreetMapPlaceList getPlaces(String name, List<String> excludeIds) {
        String fullUrl = String.format(url, WebHelper.encodeURL(name));
        if (!excludeIds.isEmpty()) {
            fullUrl = fullUrl + "&exclude_place_ids=" + StringUtils.join(excludeIds, ",");
        }
        final String finalUrl = fullUrl;
        return ProcessHelper.repeatOnException(new Callable<OpenStreetMapPlaceList>() {
            @Override
            public OpenStreetMapPlaceList call() {
                return Http.get(finalUrl).responseInputStream(OpenStreetMapPlaceList.class);
            }
        }, 5, IOException.class);

    }

    public void setUrl(String url) {
        this.url = url;
    }
}

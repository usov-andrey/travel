package org.humanhelper.travel.integration.google;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import org.humanhelper.travel.geo.Location;
import org.humanhelper.travel.place.Place;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Андрей
 * @since 21.01.15
 */
@Service
public class GoogleService {

    private static final Logger log = LoggerFactory.getLogger(GoogleService.class);

    private GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyD9sBEwkFCAnQqfwVywKvkDPGR3-_Gd3_I");

    public Location getLocation(Place place) {
        GeocodingResult[] results = geoCoding(place.getName());
        Location resultLocation = null;
        double resultDistance = 500;
        for (GeocodingResult result : results) {
            LatLng latLng = result.geometry.location;
            Location location = new Location(latLng.lat, latLng.lng);

            double distance = location.getDistance(place.getLocation());
            if (distance < resultDistance) {
                resultDistance = distance;
                resultLocation = location;
            }
        }
        if (resultLocation == null) {
            throw new IllegalStateException("Not found location by name:" + place);
        }

        return resultLocation;
    }


    public GeocodingResult[] geoCoding(String place) {
        log.debug("Geo coding: " + place);
        try {
            return GeocodingApi.geocode(context, place).await();
        } catch (Exception e) {
            throw new IllegalStateException("Error on request to google", e);
        }
    }

    public DirectionsRoute[] directions(Location source, Location target) {
        log.debug("Get directions from: " + source + " to:" + target);
        try {
            return DirectionsApi.getDirections(context, source.toString(), target.toString()).await();
        } catch (Exception e) {
            throw new IllegalStateException("Error on request to google", e);
        }
    }

    /**
     * @return Длительность маршрута из source в target в минутах,
     * null, если не найдено
     */
    public Integer getDurationRoute(Location source, Location target) {
        int duration = Integer.MAX_VALUE;
        for (DirectionsRoute route : directions(source, target)) {
            int localDuration = getDurationRoute(route);
            if (localDuration < duration) {
                duration = localDuration;
            }
        }
        if (duration == Integer.MAX_VALUE) {
            return null;
        }
        return duration;
    }

    private int getDurationRoute(DirectionsRoute route) {
        int duration = 0;
        for (DirectionsLeg leg : route.legs) {
            duration += leg.duration.inSeconds / 60;
        }
        return duration;
    }

}

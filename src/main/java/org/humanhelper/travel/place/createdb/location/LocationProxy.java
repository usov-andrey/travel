package org.humanhelper.travel.place.createdb.location;

import org.humanhelper.travel.geo.Location;

/**
 * @author Андрей
 * @since 18.09.15
 */
public class LocationProxy extends Location {

    private PlaceLocationResolver locationResolver;

    public LocationProxy(PlaceLocationResolver locationResolver) {
        this.locationResolver = locationResolver;
    }

    @Override
    public float getLat() {
        lazyLoad();
        return super.getLat();
    }

    @Override
    public float getLng() {
        lazyLoad();
        return super.getLng();
    }

    private void lazyLoad() {
        if (locationResolver != null) {
            Location location = locationResolver.resolve();
            setLat(location.getLat());
            setLng(location.getLng());
        }
    }

}

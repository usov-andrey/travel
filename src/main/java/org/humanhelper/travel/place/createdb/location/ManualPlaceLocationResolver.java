package org.humanhelper.travel.place.createdb.location;

import org.humanhelper.travel.geo.Location;

/**
 * Пример: 11.274039904671213, 124.06388282775879
 * или (11.274039904671213, 124.06388282775879)
 *
 * @author Андрей
 * @since 27.01.15
 */
public class ManualPlaceLocationResolver extends PlaceLocationResolver {

    private String value;

    public void setValue(String value) {
        this.value = value.replace(")", "").replace("(", "").replace(" ", "");
    }

    @Override
    public Location resolve() {
        return Location.latLng(value);
    }
}

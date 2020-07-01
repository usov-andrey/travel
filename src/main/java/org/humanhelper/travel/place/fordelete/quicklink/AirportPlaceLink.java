package org.humanhelper.travel.place.fordelete.quicklink;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.fordelete.OldPlaceService;

/**
 * Ищем аэропорт по коду
 *
 * @author Андрей
 * @since 28.01.15
 */
public class AirportPlaceLink implements PlaceLink {

    private String code;

    public AirportPlaceLink(String code) {
        this.code = code;
    }

    public static AirportPlaceLink create(String code) {
        return new AirportPlaceLink(code);
    }

    @Override
    public Place getPlace(OldPlaceService placeResolver) {
        return placeResolver.getAirport(code);
    }
}

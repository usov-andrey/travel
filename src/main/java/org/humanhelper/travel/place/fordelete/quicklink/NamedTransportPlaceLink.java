package org.humanhelper.travel.place.fordelete.quicklink;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.fordelete.OldPlaceService;
import org.humanhelper.travel.place.type.transport.TransportPlace;

/**
 * Ищем место по названию + регион
 *
 * @author Андрей
 * @since 28.01.15
 */
public class NamedTransportPlaceLink extends TransportPlaceLink {

    private String placeName;

    public NamedTransportPlaceLink(RegionPlaceLink regionPlace, Class<? extends TransportPlace> placeClass, String placeName) {
        super(regionPlace, placeClass);
        this.placeName = placeName;
    }

    @Override
    public Place getPlace(OldPlaceService placeResolver) {
        return placeResolver.getPlace(placeName, regionPlace.getRegion(placeResolver), placeClass);
    }
}

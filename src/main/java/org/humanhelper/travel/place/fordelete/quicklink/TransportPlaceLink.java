package org.humanhelper.travel.place.fordelete.quicklink;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.fordelete.OldPlaceService;
import org.humanhelper.travel.place.type.transport.TransportPlace;

/**
 * Ищет место по региону и типу транспортного места
 *
 * @author Андрей
 * @since 28.01.15
 */
public class TransportPlaceLink implements PlaceLink {

    protected RegionPlaceLink regionPlace;
    protected Class<? extends TransportPlace> placeClass;

    public TransportPlaceLink(RegionPlaceLink regionPlace, Class<? extends TransportPlace> placeClass) {
        this.regionPlace = regionPlace;
        this.placeClass = placeClass;
    }

    @Override
    public Place getPlace(OldPlaceService placeResolver) {
        return placeResolver.getTransportPlace(regionPlace.getRegion(placeResolver), placeClass);
    }


}

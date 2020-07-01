package org.humanhelper.travel.place.fordelete.quicklink;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.fordelete.OldPlaceService;

/**
 * Задает логику поиска места по короткой ссылке на него
 *
 * @author Андрей
 * @since 28.01.15
 */
public interface PlaceLink {

    Place getPlace(OldPlaceService placeResolver);
}

package org.humanhelper.travel.place.fordelete.quicklink;

import org.humanhelper.travel.country.CountryBuilder;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.fordelete.OldPlaceService;
import org.humanhelper.travel.place.type.region.Region;
import org.humanhelper.travel.place.type.transport.BusStation;
import org.humanhelper.travel.place.type.transport.SeaPort;

/**
 * Позволяет задать вручную место для поиска его через placeResolver
 * Текущий класс позволяет задать регион(например, для такси или van из аэропорта в город)
 *
 * @author Андрей
 * @since 28.01.15
 */
public class RegionPlaceLink implements PlaceLink {

    protected String region;
    protected CountryBuilder country;

    public RegionPlaceLink(String region, CountryBuilder country) {
        this.region = region;
        this.country = country;
    }

    public static RegionPlaceLink create(String region, CountryBuilder country) {
        return new RegionPlaceLink(region, country);
    }

    public Place getPlace(OldPlaceService placeResolver) {
        return getRegion(placeResolver);
    }

    protected Region getRegion(OldPlaceService placeResolver) {
        return placeResolver.getRegion(region, placeResolver.getCountry(country.getCode()));
    }

    public TransportPlaceLink sea() {
        return new TransportPlaceLink(this, SeaPort.class);
    }

    public TransportPlaceLink bus() {
        return new TransportPlaceLink(this, BusStation.class);
    }

    public NamedTransportPlaceLink bus(String placeName) {
        return new NamedTransportPlaceLink(this, BusStation.class, placeName);
    }

}

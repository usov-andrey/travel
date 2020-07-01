package org.humanhelper.travel.place.fordelete.quicklink;

import org.humanhelper.travel.country.CountryBuilder;
import org.humanhelper.travel.place.fordelete.OldPlaceService;
import org.humanhelper.travel.place.type.region.Region;

/**
 * @author Андрей
 * @since 28.01.15
 */
public class RegionWithSuffixPlaceLink extends RegionPlaceLink {

    public RegionWithSuffixPlaceLink(String region, CountryBuilder country) {
        super(region, country);
    }

    public static RegionWithSuffixPlaceLink create(String region, CountryBuilder country) {
        return new RegionWithSuffixPlaceLink(region, country);
    }

    @Override
    protected Region getRegion(OldPlaceService placeResolver) {
        return placeResolver.getRegionWithSuffixCity(region, placeResolver.getCountry(country.getCode()));
    }

}

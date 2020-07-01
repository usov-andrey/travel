package org.humanhelper.travel.country;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.PlaceList;
import org.humanhelper.travel.service.translation.TranslationsNameBeanList;

/**
 * @author Андрей
 * @since 05.09.15
 */
public class CountryList extends TranslationsNameBeanList<Country> {

    public void loadFromJson() {
        for (Country country : this) {
            PlaceList regions = new PlaceList();
            regions.addAll(country.getRegions());
            //При чтении из Json, нужно для всех регионов, которые входят в страны, проставить ссылки на эти страны
            for (Place region : regions) {
                region.setCountry(country);
            }
            //Также нужно для каждого региона вызвать чтение из json
            regions.loadFromJson();
        }
    }
}

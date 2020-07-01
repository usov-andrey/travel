package org.humanhelper.travel.dataprovider.domestic.ph;

import org.humanhelper.travel.country.CountryBuilder;
import org.humanhelper.travel.dataprovider.offline.OfflineAnyTimeDataProvider;
import org.humanhelper.travel.dataprovider.offline.RouteMap;

/**
 * Смотреть цены здесь:
 * http://dumagueteunitown.com/transportation/
 * <p>
 * http://www.aabana.de/Travel_infos/Travel_information/travel_information.html
 * <p>
 * Место можно задать как и по имени + регион, так и по региону(автоматический выбор места по транспортной компании)
 * Place.class=BusStation, Region.name=Dumaguete,
 * BusStation.name = ..., Region.nam
 *
 * @author Андрей
 * @since 27.01.15
 */
//@Service
public class OfflinePHDataProvider extends OfflineAnyTimeDataProvider {


    public OfflinePHDataProvider() {
        super(CountryBuilder.PH);
    }

    @Override
    protected void fillRouteMap(RouteMap priceMap) {
        priceMap.addRound(
                build().sourceBus("Dumaguete").targetBus("Bayawan").price(110),
                build().sourceSea("Maya").targetSea("Malapascua Island").price(80),
                build().sourceBus("Cebu City", "North Bus Terminal").targetBus("Maya").price(170).transport("Ceres Bus"),
                build().sourceAir("USU").targetRegion("Coron").price(150),
                build().sourceBus("Cebu City", "South Bus Terminal").targetRegion("Moalboal").price(116).transport("Ceres Liner")
        );
    }

}

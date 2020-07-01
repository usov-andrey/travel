package org.humanhelper.travel.dataprovider;

import org.humanhelper.travel.dataprovider.air.tiger.TigerAirDataProvider;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.type.transport.Airport;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Андрей
 * @since 29.05.14
 */
public class TigerAirDataProviderTest extends AbstractFloatPriceDataProviderTest {
    @Autowired
    private TigerAirDataProvider tigerAirDataProvider;

    @Override
    protected TigerAirDataProvider getDataProvider() {
        return tigerAirDataProvider;
    }

    @Override
    protected Airport getSource() {
        return Place.build(Airport.class, "BKK", "Bangkok");
    }

    @Override
    protected Airport getTarget() {
        return Place.build(Airport.class, "SIN", "Singapur");
    }

}

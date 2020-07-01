package org.humanhelper.travel.dataprovider;

import org.humanhelper.travel.dataprovider.air.airasia.AirAsiaDataProvider;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.type.transport.Airport;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Андрей
 * @since 05.05.14
 */

public class AirAsiaDataProviderTest extends AbstractFloatPriceDataProviderTest {


    @Autowired
    private AirAsiaDataProvider airAsiaDataProvider;

    @Override
    protected AirAsiaDataProvider getDataProvider() {
        return airAsiaDataProvider;
    }

    @Override
    protected Airport getSource() {
        return Place.build(Airport.class, "DMK", "Bangkok");
    }

    @Override
    protected Airport getTarget() {
        return Place.build(Airport.class, "HKG", "Hong Kong");
    }

}

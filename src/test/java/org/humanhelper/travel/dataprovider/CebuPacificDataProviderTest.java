package org.humanhelper.travel.dataprovider;

import org.humanhelper.travel.dataprovider.air.cebupacific.CebuPacificDataProvider;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.type.transport.Airport;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author Андрей
 * @since 05.05.14
 */
public class CebuPacificDataProviderTest extends AbstractFloatPriceDataProviderTest {

    @Autowired
    private CebuPacificDataProvider cebuPacificDataProvider;

    @Override
    protected CebuPacificDataProvider getDataProvider() {
        return cebuPacificDataProvider;
    }

    @Override
    protected Airport getSource() {
        return Place.build(Airport.class, "MNL", "Manila");
    }

    @Override
    protected Airport getTarget() {
        return Place.build(Airport.class, "CEB", "Cebu");
    }

}

package org.humanhelper.travel.dataprovider.domestic.ph;

import org.humanhelper.travel.dataprovider.AbstractFixedPriceDataProviderTest;
import org.springframework.beans.factory.annotation.Autowired;


public class SouthWestBoracayPHDataProviderTest extends AbstractFixedPriceDataProviderTest {

    @Autowired
    private SouthWestBoracayPHDataProvider dataProvider;

    @Override
    protected SouthWestBoracayPHDataProvider getDataProvider() {
        return dataProvider;
    }

}
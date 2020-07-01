package org.humanhelper.travel.dataprovider.domestic.ph;

import org.humanhelper.travel.dataprovider.AbstractFixedPriceDataProviderTest;
import org.springframework.beans.factory.annotation.Autowired;

public class SuperCatPHDataProviderTest extends AbstractFixedPriceDataProviderTest {

    @Autowired
    private SuperCatPHDataProvider dataProvider;

    @Override
    protected SuperCatPHDataProvider getDataProvider() {
        return dataProvider;
    }

}
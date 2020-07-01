package org.humanhelper.travel.dataprovider.domestic.ph;

import org.humanhelper.travel.dataprovider.AbstractFixedPriceDataProviderTest;
import org.springframework.beans.factory.annotation.Autowired;

public class OfflineDataPHProvider2Test extends AbstractFixedPriceDataProviderTest {

    @Autowired
    private OfflinePHDataProvider dataProvider;

    @Override
    protected OfflinePHDataProvider getDataProvider() {
        return dataProvider;
    }

}
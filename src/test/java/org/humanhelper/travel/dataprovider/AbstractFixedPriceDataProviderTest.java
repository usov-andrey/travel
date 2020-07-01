package org.humanhelper.travel.dataprovider;

import org.humanhelper.travel.ApplicationTest;
import org.humanhelper.travel.place.dao.PlaceDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Андрей
 * @since 20.01.15
 */
@ActiveProfiles(profiles = {"memoryDao"})
public abstract class AbstractFixedPriceDataProviderTest extends ApplicationTest {

    @Autowired
    protected PlaceDao placeDao;

    abstract protected AbstractFixedPriceDataProvider getDataProvider();

    @Test
    public void testUpdateRoutesSourceToTarget() {
        System.out.println(getDataProvider().getRoutes());
    }

}

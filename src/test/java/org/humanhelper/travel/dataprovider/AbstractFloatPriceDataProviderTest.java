package org.humanhelper.travel.dataprovider;

import org.humanhelper.service.utils.DateHelper;
import org.humanhelper.travel.ApplicationTest;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.dao.PlaceDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

/**
 * @author Андрей
 * @since 08.11.14
 */
@ActiveProfiles(profiles = {"memoryDao"})
public abstract class AbstractFloatPriceDataProviderTest extends ApplicationTest {

    @Autowired
    protected PlaceDao placeDao;

    abstract protected FixedTimeDataProvider getDataProvider();

    abstract protected Place getSource();

    abstract protected Place getTarget();

    @Test
    public void testUpdateRoutesSourceToTarget() {
        System.out.println(getDataProvider().getPlaces());
        Date date = DateHelper.incDays(new Date(), 90);//Прибавляем 60 дней
        System.out.println(getDataProvider().getWays(getSource(), getTarget(), date));
    }


}

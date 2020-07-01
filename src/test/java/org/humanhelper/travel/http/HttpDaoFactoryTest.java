package org.humanhelper.travel.http;

import org.humanhelper.travel.ApplicationTest;
import org.humanhelper.travel.place.dao.PlaceDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Для работоспособности этого теста необходимо написать какую-то прослойку, которая не будет рубиться
 * по http, а вызывать какие-то методы у другого класса
 *
 * @author Андрей
 * @since 20.05.14
 */
public class HttpDaoFactoryTest extends ApplicationTest {

    @Autowired
    private PlaceDao placeDao;

    @Test
    public void testCreatePlaceDao() throws Exception {
        System.out.println("Place Dao created:" + placeDao);
    }
}

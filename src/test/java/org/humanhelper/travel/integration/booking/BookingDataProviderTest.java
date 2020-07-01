package org.humanhelper.travel.integration.booking;

import org.humanhelper.data.dao.Dao;
import org.humanhelper.service.marshaling.jdk.JdkMarshaller;
import org.humanhelper.service.task.rmi.LocalTaskServer;
import org.humanhelper.service.task.rmi.RemoteTaskRunner;
import org.humanhelper.service.task.rmi.classloader.TestTaskServerClassLoader;
import org.humanhelper.service.task.rmi.http.HttpRemoteTaskServer;
import org.humanhelper.travel.TestConfig;
import org.humanhelper.travel.country.Country;
import org.humanhelper.travel.country.CountryList;
import org.humanhelper.travel.dao.memory.MemoryDaoFactory;
import org.humanhelper.travel.place.PlaceList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Андрей
 * @since 09.11.15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@ActiveProfiles({
        //локальный запуск
        //TaskRunner.PROFILE,
        //запуск через heroku сервер

        RemoteTaskRunner.PROFILE,
        JdkMarshaller.PROFILE,
        HttpRemoteTaskServer.PROFILE,

        //LocalTaskServer.PROFILE,

        Dao.PROFILE, MemoryDaoFactory.PROFILE})
public class BookingDataProviderTest {

    protected static final Logger log = LoggerFactory.getLogger(BookingDataProviderTest.class);

    @Autowired
    private BookingDataProvider bookingDataProvider;
    @Autowired(required = false)
    private LocalTaskServer localTaskServer;
    @Autowired(required = false)
    private HttpRemoteTaskServer remoteTaskServer;

    @Test
    public void testGetPlaces() throws Exception {
        prepare();
        CountryList countries = bookingDataProvider.getCountries();
        int index = 0;
        for (Country country : countries) {
            //Country country = new Country();
            //country.setId("DE");
            log.debug("Process " + (index++) + " of " + countries.size() + " country");
            bookingDataProvider.savePlacesToFile(country);
        }
    }

    private void prepare() {
        //remoteTaskServer.setServerUrl("http://localhost:8080/task");
        if (remoteTaskServer != null) {
            remoteTaskServer.clearClasses();
        }
        if (localTaskServer != null) {
            localTaskServer.setClassLoader(new TestTaskServerClassLoader(getClass().getClassLoader()));
        }
    }

    //@Test
    public void testCountry() {
        prepare();

        Country country = new Country().id("RU");
        PlaceList places = bookingDataProvider.loadPlaces(country);
        log.debug(places + "");
    }
}
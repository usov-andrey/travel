package org.humanhelper.travel.country;

import org.humanhelper.travel.ApplicationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Андрей
 * @since 09.11.15
 */
public class CountryServiceTest extends ApplicationTest {

    @Autowired
    private CountryDaoCreator service;

    @Test
    public void testCreateCountries() throws Exception {
        service.createCountriesInDao();
    }
}
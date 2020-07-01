package org.humanhelper.travel.country;

import org.humanhelper.travel.country.dao.CountryDao;
import org.humanhelper.travel.integration.booking.BookingDataProvider;
import org.humanhelper.travel.integration.travelpayouts.TPDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Создает в dao страны
 *
 * @author Андрей
 * @since 06.09.15
 */
@Service
public class CountryDaoCreator {

    @Autowired
    private CountryDao dao;
    @Autowired
    private BookingDataProvider bookingDataProvider;
    @Autowired
    private TPDataProvider tpDataProvider;

    /**
     * Добавляем в dao страны, если их еще там нет
     */
    public void createCountriesInDao() {
        if (dao.getCount() == 0) {
            //Значит в базе еще нет стран и нужно наполнять базу данными с самого начала
            CountryList countries = loadCountries();
            for (Country country : countries) {
                Country countryInDB = dao.get(country.getId());
                if (countryInDB == null) {
                    dao.insert(country);
                } else {
                    dao.update(country.getId(), country);
                }
            }
        }
    }

    /**
     * @return список стран полученных от внешних систем
     */
    private CountryList loadCountries() {
        CountryList countries = bookingDataProvider.getCountries();
        CountryList tpCountries = tpDataProvider.getCountries();
        for (Country tpCountry : tpCountries) {
            if (countries.getById(tpCountry.getId()) == null) {
                countries.add(tpCountry);
            }
        }
        return countries;
    }

}

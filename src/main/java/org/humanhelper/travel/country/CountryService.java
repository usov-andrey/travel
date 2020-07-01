package org.humanhelper.travel.country;

import org.humanhelper.travel.country.dao.CountryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Андрей
 * @since 02.04.16
 */
@Service
public class CountryService {

    @Autowired
    private CountryDao dao;

    /**
     * @return Список стран отсортированных по названию
     */
    public List<Country> getCountries() {
        Collection<Country> countries = dao.getAll();
        List<Country> result = new ArrayList<>();
        result.addAll(countries);
        Collections.sort(result, (o1, o2) -> o1.getName().compareTo(o2.getName()));
        return result;
    }
}

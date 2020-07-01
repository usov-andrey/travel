package org.humanhelper.travel.country.dao;

import org.humanhelper.data.dao.CRUDDao;
import org.humanhelper.travel.country.Country;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

/**
 * @author Андрей
 * @since 18.12.14
 */
@RequestMapping("countryDao")
public interface CountryDao extends CRUDDao<Country, String> {

    Collection<Country> getByNamePrefix(String namePrefix, int limitCount);

}

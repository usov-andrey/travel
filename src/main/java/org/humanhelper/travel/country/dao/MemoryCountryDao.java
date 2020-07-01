package org.humanhelper.travel.country.dao;

import org.humanhelper.travel.country.Country;
import org.humanhelper.travel.dao.memory.MemoryCRUDDao;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Андрей
 * @since 08.01.15
 */
public class MemoryCountryDao extends MemoryCRUDDao<Country> implements CountryDao {

    @Override
    public Collection<Country> getByNamePrefix(String namePrefix, int limitCount) {
        return new ArrayList<>();
    }

}

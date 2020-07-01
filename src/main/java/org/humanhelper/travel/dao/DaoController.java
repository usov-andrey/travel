package org.humanhelper.travel.dao;

import org.humanhelper.data.dao.Dao;
import org.humanhelper.travel.country.CountryDaoCreator;
import org.humanhelper.travel.country.dao.CountryDao;
import org.humanhelper.travel.place.createdb.PlaceDaoCreator;
import org.humanhelper.travel.place.dao.PlaceDao;
import org.humanhelper.travel.route.dao.RouteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Общие функции по работе с базой
 *
 * @author Андрей
 * @since 16.01.15
 */
@RestController
@RequestMapping("dao")
@Profile(Dao.PROFILE)
public class DaoController {

    @Autowired
    private CountryDao countryDao;
    @Autowired
    private CountryDaoCreator countryDaoCreator;
    @Autowired
    private PlaceDao placeDao;
    @Autowired
    private PlaceDaoCreator placeDaoCreator;
    @Autowired
    private RouteDao routeDao;

    @RequestMapping(value = "clear", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void clear() {
        routeDao.delete();
        placeDao.delete();
        countryDao.delete();
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public
    @ResponseBody
    String status() {
        return "Routes:" + routeDao.getCount() + "\nPlaces:" + placeDao.getCount() + "\nCountries:" + countryDao.getCount();
    }

    /**
     * Создаем данные в базе
     */
    @RequestMapping(value = "create", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void create() {
        countryDaoCreator.createCountriesInDao();
        placeDaoCreator.createPlacesInDao();
    }

}

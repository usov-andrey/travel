package org.humanhelper.travel.dao.remote;

import org.humanhelper.extservice.rmi.RMIService;
import org.humanhelper.travel.country.dao.CountryDao;
import org.humanhelper.travel.hotel.HotelDao;
import org.humanhelper.travel.place.dao.PlaceDao;
import org.humanhelper.travel.route.dao.RouteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Задает реализацию DAO как обращение к Http сервису через RestClientServiceFactory
 *
 * @author Андрей
 * @since 20.05.14
 */
@Service
@Profile("remoteDao")
public class RemoteDaoFactory {

    @Autowired
    protected RMIService rmiService;

    @Bean
    public PlaceDao createPlaceDao() {
        return rmiService.createProxy(PlaceDao.class);
    }

    @Bean
    public HotelDao createHotelDao() {
        return rmiService.createProxy(HotelDao.class);
    }

    @Bean
    public RouteDao createRouteDao() {
        return rmiService.createProxy(RouteDao.class);
    }

    @Bean
    public CountryDao countryDao() {
        return rmiService.createProxy(CountryDao.class);
    }
}

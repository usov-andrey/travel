package org.humanhelper.travel.dao.mongodb;

/**
 * @author Андрей
 * @since 29.12.14
 */

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.humanhelper.travel.hotel.HotelDao;
import org.humanhelper.travel.hotel.MemoryHotelDao;
import org.humanhelper.travel.route.dao.RouteDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("mongoDBDao")
public class MongoDBDaoFactory {

    private String url = "mongodb://traveluser:qazwsxedc@ds047050.mongolab.com:47050/travel";

    public void setUrl(String url) {
        this.url = url;
    }

    @Bean(destroyMethod = "close")
    public MongoClient mongoClient() {
        MongoClientURI uri = new MongoClientURI(url);
        try {
            return new MongoClient(uri);
        } catch (Exception e) {
            throw new RuntimeException("Error on connect to mongo db:" + url, e);
        }
    }


    @Bean
    public HotelDao hotelDao() {
        return new MemoryHotelDao();
    }

    @Bean
    public RouteDao routeDao() {
        return null;
    }

}

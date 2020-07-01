package org.humanhelper.travel.dao.elastic.httpclient;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.humanhelper.travel.hotel.HotelDao;
import org.humanhelper.travel.hotel.MemoryHotelDao;
import org.humanhelper.travel.route.dao.RouteDao;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * @author Андрей
 * @since 29.12.14
 */
@Service
@Profile("httpElasticDao")
public class HttpElasticDaoFactory implements DisposableBean {

    private JestClient client;

    @Bean
    public JestClient client() {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder("https://cy1czb6d1d:v2zap4ort9@travel-8444281760.us-east-1.bonsai.io")
                .multiThreaded(true)
                .build());
        client = factory.getObject();
        return client;
    }

    @Override
    public void destroy() throws Exception {
        client.shutdownClient();
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

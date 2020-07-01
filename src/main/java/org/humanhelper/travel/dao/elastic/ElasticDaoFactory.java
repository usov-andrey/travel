package org.humanhelper.travel.dao.elastic;

/**
 * @author Андрей
 * @since 29.12.14
 */

import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.humanhelper.travel.country.dao.CountryDao;
import org.humanhelper.travel.country.dao.ElasticCountryDao;
import org.humanhelper.travel.hotel.HotelDao;
import org.humanhelper.travel.hotel.MemoryHotelDao;
import org.humanhelper.travel.place.dao.ElasticPlaceDao;
import org.humanhelper.travel.place.dao.PlaceDao;
import org.humanhelper.travel.route.dao.ElasticRouteDao;
import org.humanhelper.travel.route.dao.RouteDao;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile(ElasticDaoFactory.PROFILE)
public class ElasticDaoFactory implements DisposableBean {

    public static final String PROFILE = "elasticDao";

    private Node node;

    @Bean
    public PlaceDao placeDao() {
        return new ElasticPlaceDao(getClient());
    }

    @Bean
    public CountryDao countryDao() {
        return new ElasticCountryDao(getClient());
    }

    private synchronized Client getClient() {
        if (node == null) {
            node = NodeBuilder.nodeBuilder().local(true).node();
        }
        return node.client();
    }

    @Override
    public void destroy() throws Exception {
        node.close();
    }

    @Bean
    public HotelDao hotelDao() {
        return new MemoryHotelDao();
    }

    @Bean
    public RouteDao routeDao() {
        return new ElasticRouteDao(getClient());
    }

    //Для ff
	/*
	@Bean
	public ItemDao itemDao() {
		return new ElasticItemDao(getClient());
	}

	@Bean
	public RecipeDao recipeDao() {
		return new ElasticRecipeDao(getClient());
	}

	@Bean
	public InventoryDao inventoryDao() {
		return new ElasticInventoryDao(getClient());
	} */
}

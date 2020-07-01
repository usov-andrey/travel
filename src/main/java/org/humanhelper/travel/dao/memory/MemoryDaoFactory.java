package org.humanhelper.travel.dao.memory;

import org.humanhelper.travel.country.dao.CountryDao;
import org.humanhelper.travel.country.dao.MemoryCountryDao;
import org.humanhelper.travel.hotel.HotelDao;
import org.humanhelper.travel.hotel.MemoryHotelDao;
import org.humanhelper.travel.place.dao.MemoryPlaceDao;
import org.humanhelper.travel.place.dao.PlaceDao;
import org.humanhelper.travel.route.dao.MemoryRouteDao;
import org.humanhelper.travel.route.dao.RouteDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * @author Андрей
 * @since 28.11.14
 */
@Service
@Profile(MemoryDaoFactory.PROFILE)
public class MemoryDaoFactory {

    public static final String PROFILE = "memoryDao";

    @Bean
    public CountryDao countryDao() {
        return new MemoryCountryDao();
    }

    @Bean
    public PlaceDao placeDao() {
        return new MemoryPlaceDao();
    }

    @Bean
    public HotelDao hotelDao() {
        return new MemoryHotelDao();
    }

    @Bean
    public RouteDao routeDao() {
        return new MemoryRouteDao();
    }

    //Для ff
	/*
	@Bean
	public ItemDao itemDao() {
		return new MemoryItemDao();
	}

	@Bean
	public RecipeDao recipeDao() {
		return new MemoryRecipeDao();
	}

	@Bean
	public InventoryDao inventoryDao() {
		return new MemoryInventoryDao();
	}*/
}

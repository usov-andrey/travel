package org.humanhelper.travel.hotel;

import org.humanhelper.travel.country.CountryBuilder;
import org.humanhelper.travel.price.PriceAgent;
import org.humanhelper.travel.price.PriceResolver;
import org.humanhelper.travel.price.SimplePriceAgent;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * @author Андрей
 * @since 13.05.14
 */
public class MemoryHotelDao implements HotelDao {

    private PriceAgent agent = new SimplePriceAgent();

    @Override
    public PriceResolver getPlaceItemPriceResolver(@PathVariable String placeId, @RequestParam(value = "arriveDate") Date arriveDate, @RequestParam(value = "nights") Integer nights) {
        return new PriceResolver(1000f * nights, CountryBuilder.US.getCurrency(), agent);
    }

}

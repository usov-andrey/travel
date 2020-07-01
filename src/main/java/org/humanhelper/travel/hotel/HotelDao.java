package org.humanhelper.travel.hotel;

import org.humanhelper.travel.price.PriceResolver;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * @author Андрей
 * @since 12.05.14
 */
@RequestMapping("hotels")
public interface HotelDao {

    @RequestMapping(value = "{placeId}/price", method = RequestMethod.GET)
    PriceResolver getPlaceItemPriceResolver(@PathVariable String placeId,
                                            @RequestParam(value = "arriveDate") Date arriveDate,
                                            @RequestParam(value = "nights") Integer nights);
}

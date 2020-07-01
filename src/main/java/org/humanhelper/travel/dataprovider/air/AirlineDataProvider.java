package org.humanhelper.travel.dataprovider.air;

import org.apache.commons.lang3.time.FastDateFormat;
import org.humanhelper.service.utils.DateHelper;
import org.humanhelper.travel.dataprovider.FixedTimeDataProvider;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.type.transport.Airport;
import org.humanhelper.travel.route.type.TimeRoute;
import org.humanhelper.travel.route.type.sourcetarget.SourceTargetRouteList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Поставщик информации от авиакомпании
 *
 * @author Human Helper
 * @since 30.10.13
 */
public abstract class AirlineDataProvider extends FixedTimeDataProvider {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    protected static Logger log = LoggerFactory.getLogger(AirlineDataProvider.class);

	/*
	@Override
	public Place createPlace(String placeId) {
		Collection<Rome2RioPlace> places = rome2RioAutoCompleteService.search(WebHelper.encodeURL(placeId + " airport"));
		if (places.isEmpty()) {
			throw new IllegalArgumentException("Not found place by query:" + placeId);
		}

		for (Rome2RioPlace r2rPlace : places) {
			if (r2rPlace.getKind().equals(Rome2RioPlaceKind.airport)) {
				return r2rPlace.createPlace();
			}
		}
		throw new IllegalStateException("Not found airport by query:" + placeId);
	}*/

    @Override
    public SourceTargetRouteList<TimeRoute> getWays(Place source, Place target, Date day) {
        return getWays((Airport) source, (Airport) target, day);
    }

    abstract protected SourceTargetRouteList<TimeRoute> getWays(Airport source, Airport target, Date day);

    protected String getDate(Date date) {
        FastDateFormat format = FastDateFormat.getInstance(DATE_FORMAT);
        return format.format(date);
    }

    /**
     * Берет первые два символа из value и подставляет их как количество часов, вторые два символа повставляет как минуты
     */
    protected Date createTime(Date day, String value) {
        try {
            value = value.replace(":", "");
            int hours = Integer.parseInt(value.substring(0, 2));
            int minutes = Integer.parseInt(value.substring(2, 4));
            return DateHelper.setTime(day, hours, minutes);
        } catch (Exception e) {
            throw new IllegalStateException("Error on parse time:" + value, e);
        }
    }

}

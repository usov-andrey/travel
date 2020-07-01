package org.humanhelper.travel.route.provider;

import org.humanhelper.service.conversion.ConverterService;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.PlaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Общий предок всех поставщиков маршрутов
 *
 * @author Андрей
 * @since 05.10.15
 */
public abstract class AbstractRouteProvider implements RouteProvider {

    protected static Logger log = LoggerFactory.getLogger(RouteProvider.class);
    @Autowired
    protected ConverterService converterService;
    @Autowired
    protected PlaceService placeResolver;

    public PlaceService getPlaceResolver() {
        return placeResolver;
    }

    public final boolean support(Place source, Place target) {
        return getTargets(source).contains(target);
    }
}

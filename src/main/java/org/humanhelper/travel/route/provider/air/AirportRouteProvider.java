package org.humanhelper.travel.route.provider.air;

import org.apache.commons.lang3.time.FastDateFormat;
import org.humanhelper.service.htmlparser.Html;
import org.humanhelper.service.http.Http;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.type.transport.Airport;
import org.humanhelper.travel.price.AbstractPriceAgent;
import org.humanhelper.travel.route.provider.AbstractRouteProvider;
import org.humanhelper.travel.route.resolver.RoutesRequest;
import org.humanhelper.travel.route.type.AnyTimeRoute;
import org.humanhelper.travel.route.type.TimeRoute;
import org.humanhelper.travel.service.period.DatePeriod;

import java.util.*;
import java.util.function.Consumer;

/**
 * ЛОгика работы поиска маршрутов в зависимости от типа Place : Airport
 *
 * @author Андрей
 * @since 05.10.15
 */
public abstract class AirportRouteProvider extends AbstractRouteProvider {

    @Override
    public final Collection<TimeRoute> getTimeRoutes(RoutesRequest request) {
        //Разбиваем на список маршрутов для различной пары source target
        Collection<TimeRoute> routes = new ArrayList<>();
        request.process((source, target, period) -> {
            if (Airport.isAirport(source) && Airport.isAirport(target)) {
                routes.addAll(getRoutes(Airport.get(source), Airport.get(target), period));
            }
        });
        return routes;
    }

    public Collection<TimeRoute> getRoutes(Airport source, Airport target, DatePeriod period) {
        Collection<TimeRoute> routes = new ArrayList<>();
        for (Date day : period.dayIterator()) {
            routes.addAll(getRoutes(source, target, day));
        }
        return routes;
    }


    public Collection<TimeRoute> getRoutes(Airport source, Airport target, Date day) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final Collection<AnyTimeRoute> getAnyTimeRoutes(RoutesRequest request) {
        return Collections.emptyList();
    }

    @Override
    public final Set<Airport> getTargets(Place source) {
        if (!Airport.isAirport(source)) {
            return Collections.emptySet();
        }
        return getTargets(Airport.get(source));
    }

    /**
     * Набор аэропортов, куда можно попасть из airport с помощью этого поставщика
     */
    abstract protected Set<Airport> getTargets(Airport airport);

    protected AbstractPriceAgent createPriceAgent(String webSite, String urlPattern) {
        return new AbstractPriceAgent(webSite) {
            @Override
            public String getUrl(TimeRoute route) {
                return url(urlPattern, route.getSource(), route.getTarget(), route.getStartTime());
            }
        };
    }

    //------------------Функции форматирования
    protected final Object[] format(Object... values) {
        Object[] result = new Object[values.length];
        int index = 0;
        for (Object value : values) {
            result[index++] = format(value);
        }
        return result;
    }

    private Object format(Object value) {
        if (value instanceof Airport) {
            return ((Airport) value).getCode();
        } else if (value instanceof Date) {
            return formatDay((Date) value);
        } else if (value instanceof String || value instanceof Number) {
            return value.toString();
        }
        throw new IllegalArgumentException("Unknown format of object:" + value);
    }

    private String formatDay(Date day) {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        return format.format(day);
    }

    protected final String url(String url, Object... params) {
        return String.format(url, format(params));
    }

    protected final void processHtml(String url, Consumer<Html> consumer) {
        processHtml(Http.get(url), consumer);
    }

    protected final void processHtml(Http http, Consumer<Html> consumer) {
        Html html = Html.get(http);
        try {
            consumer.accept(html);
        } catch (Exception e) {
            throw new IllegalStateException("Error on process html:" + html, e);
        }
    }

}

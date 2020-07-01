package org.humanhelper.travel.route.provider.air.airasia;

import org.apache.commons.lang3.time.FastDateFormat;
import org.humanhelper.service.htmlparser.Html;
import org.humanhelper.travel.country.Country;
import org.humanhelper.travel.place.type.transport.Airport;
import org.humanhelper.travel.route.provider.air.AirlineRouteProvider;
import org.humanhelper.travel.route.type.TimeRoute;

import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Андрей
 * @since 05.10.15
 */
public class AirAsiaRouteProvider extends AirlineRouteProvider {

    @Override
    public List<TimeRoute> getRoutes(Airport source, Airport target, Date day) {
        Html html = getHtml(source, target, day);
        log.debug(html.toString());
        return null;
    }

    @Override
    protected Set<Airport> getTargets(Airport airport) {
        return null;  //TODO
    }

    private Html getHtml(Airport source, Airport target, Date day) {
        Country country = source.getCountry();
        Currency currency = country.getCurrency();
        String origin = source.getCode();
        String arrival = target.getCode();
        String url = String.format("http://booking11.airasia.com/Page/SkySalesRedirectHandler.aspx?IsReturn=false&OriginStation=%s&ArrivalStation=%s&DepartureDate=%s&ReturnDate=&NoAdult=1&NoChild=0&NoInfant=0&Currency=" + currency + "&Culture=en-GB&pc=&respURL=http://booking.airasia.com/",
                origin, arrival, formatDay(day));
        return Html.get(url);
    }

    private String formatDay(Date day) {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        return format.format(day);
    }
}

package org.humanhelper.travel.dataprovider.domestic.ph;

import org.humanhelper.service.http.Http;
import org.humanhelper.service.utils.StringHelper;
import org.humanhelper.travel.country.CountryBuilder;
import org.humanhelper.travel.dataprovider.AnyTimeDataProvider;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.fordelete.quicklink.AirportPlaceLink;
import org.humanhelper.travel.place.fordelete.quicklink.RegionPlaceLink;
import org.humanhelper.travel.price.PriceResolver;
import org.humanhelper.travel.route.type.AnyTimeRoute;
import org.humanhelper.travel.route.type.sourcetarget.SourceTargetRouteList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Currency;

/**
 * http://www.southwesttoursboracay.com/boracay-transportation/caticlan-airport/one-way-trip
 * Нужно доделывать парсинг, они переделали сайт
 *
 * @author Андрей
 * @since 17.01.15
 */
//@Service
public class SouthWestBoracayPHDataProvider extends AnyTimeDataProvider {

    private static final Currency CURRENCY = Currency.getInstance("PHP");
    private static final String COMPANY = "southwesttoursboracay";

    public SouthWestBoracayPHDataProvider() {
        super();
    }

    @Override
    public void updatePlaces() {
        Place boracayPort = getBoracayPort();
        addRoundRoute(boracayPort, getCaticlan());
        addRoundRoute(boracayPort, getKalibo());
    }

    protected Place getCaticlan() {
        return AirportPlaceLink.create("MPH").getPlace(placeResolver);
    }

    protected Place getKalibo() {
        return AirportPlaceLink.create("KLO").getPlace(placeResolver);
    }

    protected Place getBoracayPort() {
        return RegionPlaceLink.create("Boracay", CountryBuilder.PH).sea().getPlace(placeResolver);
    }

    @Override
    public SourceTargetRouteList<AnyTimeRoute> getWays(Place source, Place target) {
        SourceTargetRouteList<AnyTimeRoute> routeList = new SourceTargetRouteList<>(source, target, this);
        try {
            routeList.addRoute(new AnyTimeRoute(new PriceResolver(getPrice(source, target), CURRENCY, this), source,
                    target, getDuration(source, target), createTransport(COMPANY)));
        } catch (Exception e) {
            throw new IllegalStateException("Error on update routes from " + source + " to " + target, e);
        }
        return routeList;
    }

    private Float getPrice(Place source, Place target) {
        //Из боракая
        if (source.equals(getBoracayPort())) {
            //В калибо
            if (target.equals(getKalibo())) {
                return getPrice("http://www.southwesttoursboracay.com/transfer/resort-kalibo-airport",
                        "(Cagban Port to Kalibo Airport)");
            } else {
                //В катиклан
                return getPrice("http://www.southwesttoursboracay.com/transfer/resort-caticlan-airport",
                        "(Cagban Port to Caticlan Airport)");
            }
        } else {
            if (source.equals(getKalibo())) {
                return getPrice("http://www.southwesttoursboracay.com/boracay-transportation/kalibo-airport/one-way-trip",
                        "One way Bus and Boat with fees (Kalibo)");
            } else {
                return getPrice("http://www.southwesttoursboracay.com/boracay-transportation/caticlan-airport/one-way-trip",
                        "One way Van and Boat with fees (Caticlan)");
            }
        }
    }

    /**
     * получить цену находящуюся между шаблоном и точкой(.)
     */
    private Float getPrice(String url, String template) {
        //Image Name Price Add to booking Oneway Van/Coaster with fees (Caticlan Jetty Port to Caticlan Airport) Php200.00 Oneway Boat and Van/Coaster with Fees (Cagban Port to Caticlan Airport) Php275.00 Oneway Door to Door with Fees (Resort to Caticlan Airport) Php350.00 Oneway Door to Door with Fees (Yapak and Diniwid Resorts to Caticlan Airport) Php400.00
        //Берем эту часть (Cagban Port to Caticlan Airport) Php275.00
        String pricesString = getPrices(url);
        try {
            return getPriceFromString(pricesString, template);
        } catch (Exception e) {
            throw new IllegalStateException("Error on parse prices:" + pricesString, e);
        }
    }

    private Float getPriceFromString(String prices, String template) {
        // Php275
        String price = StringHelper.getStringBetween(prices, template, ".");
        price = StringHelper.getStringAfter(price, "Php");
        return Float.parseFloat(price);
    }

    private String getPrices(String url) {
        String content = Http.get(url).responseBody();
        Document document = Jsoup.parse(content);
        Elements elements = document.select("table.category-products");
        if (elements.size() == 0) {
            throw new IllegalStateException("Error on parse:" + content);
        }
        return elements.get(0).text();
    }

    @Override
    public String getName() {
        return "www.southwesttoursboracay.com";
    }
}

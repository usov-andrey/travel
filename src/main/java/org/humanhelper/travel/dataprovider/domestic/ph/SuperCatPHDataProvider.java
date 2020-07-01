package org.humanhelper.travel.dataprovider.domestic.ph;

import org.humanhelper.service.http.Http;
import org.humanhelper.service.utils.DateHelper;
import org.humanhelper.service.utils.StringHelper;
import org.humanhelper.travel.country.CountryBuilder;
import org.humanhelper.travel.dataprovider.ScheduleTimeDataProvider;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.fordelete.quicklink.RegionWithSuffixPlaceLink;
import org.humanhelper.travel.price.PriceResolver;
import org.humanhelper.travel.route.type.ScheduleTimeRoute;
import org.humanhelper.travel.route.type.sourcetarget.SourceTargetRouteList;
import org.humanhelper.travel.service.frequency.DailyFrequency;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * http://www.supercat.com.ph/sched.asp?route=68#dtn
 *
 * @author Андрей
 * @since 19.01.15
 */
//@Service
public class SuperCatPHDataProvider extends ScheduleTimeDataProvider {

    private static final String COMPANY = "supercat";
    private String url = "http://www.supercat.com.ph/sched.asp";

    @Override
    public SourceTargetRouteList<ScheduleTimeRoute> getWays(Place source, Place target) {
        Map<String, String> fares = getFares();
        Map<String, String> routes = getRouteMap();
        String routeKey = null;
        String price = null;
        for (String key : routes.keySet()) {
            String value = routes.get(key);
            String[] places = value.split("-");
            Place sourceSeaPort = getPlace(places[0]);
            Place targetSeaPort = getPlace(places[1]);
            if (sourceSeaPort.equals(source) && targetSeaPort.equals(target)) {
                routeKey = key;
                price = fares.get(value);
                if (price == null) {
                    price = fares.get(places[1] + "-" + places[0]);
                }
            }
        }
        if (routeKey == null) {
            throw new IllegalStateException("Not found route key by source " + source + " and target " + target + " in routes " + routes);
        }
        if (price == null) {
            throw new IllegalStateException("Not found price by source " + source + " and target " + target + " in fares " + fares);
        }
        return getSchedule(source, target, routeKey, Float.parseFloat(price));
    }

    @Override
    public void updatePlaces() {
        Map<String, String> routes = getRouteMap();
        for (String id : routes.keySet()) {
            String value = routes.get(id);
            String[] places = value.split("-");
            Place source = getPlace(places[0]);
            Place target = getPlace(places[1]);
            addOneWayRoute(source, target);
        }
    }

    private Place getPlace(String regionName) {
        return RegionWithSuffixPlaceLink.create(regionName, CountryBuilder.PH).sea().getPlace(placeResolver);
    }

    @Override
    public String getName() {
        return "http://www.supercat.com.ph";
    }

    private Map<String, String> getFares() {
        Map<String, String> result = new HashMap<>();
        String url = "http://www.supercat.com.ph/Fare.asp";
        String content = Http.get(url).responseBody();
        Document document = Jsoup.parse(content);
        Elements table = document.select("table.bgtable").select("table").select("tr.text_black");
        for (Element tr : table) {
            //BACOLOD-ILOILO PhP 380.00 275.00 308.00 326.00 200.00
            String value = tr.getAllElements().get(0).text().trim().replace("\u00A0", "");//Вначале идет символ с кодом 160
            String[] values = value.split(" ");
            String route = values[0].trim();
            String price = values[2];
            result.put(route, price);
        }
        return result;
    }

    private SourceTargetRouteList<ScheduleTimeRoute> getSchedule(Place source, Place target, String routeKey, Float price) {
        SourceTargetRouteList<ScheduleTimeRoute> routes = new SourceTargetRouteList<>(source, target, this);
        String url = this.url + "?route=" + routeKey;
        String content = Http.get(url).responseBody();
        Document document = Jsoup.parse(content);
        Elements table = document.select("table.bgtable").select("table");
        //    BACOLOD-ILOILO BATANGAS-CALAPAN CALAPAN-BATANGAS CEBU-ORMOC CEBU-TAGBILARAN ILOILO-BACOLOD ORMOC-CEBU TAGBILARAN-CEBU     BACOLOD-ILOILO Schedules Trip Departure Arrival Frequency * Book Now! 1 6:00 AM 7:00 AM DAILY 2 9:00 AM 10:00 AM DAILY 3 12:40 PM 1:40 PM DAILY 4 3:40 PM 4:40 PM DAILY BACOLOD-ILOILO Schedules Trip Departure Arrival Frequency * Book Now! 1 6:00 AM 7:00 AM DAILY 2 9:00 AM 10:00 AM DAILY 3 12:40 PM 1:40 PM DAILY 4 3:40 PM 4:40 PM DAILY
        String text = StringHelper.getStringBetween(table.text(), "Book Now!", "Schedules").trim();
        //Имеем:1 6:00 AM 7:00 AM DAILY 2 9:00 AM 10:00 AM DAILY 3 12:40 PM 1:40 PM DAILY 4 3:40 PM 4:40 PM DAILY BACOLOD-ILOILO
        String[] values = text.split(" ");
        int i = 0;
        int index = 1;
        while (values[i].equals(Integer.toString(index))) {
            String startTime = values[i + 1] + values[i + 2];
            String endTime = values[i + 3] + values[i + 4];
            String frequency = values[i + 5];
            if (!frequency.equals("DAILY")) {
                throw new IllegalStateException("Found illegal frequency " + frequency + " in text:" + text);
            }
            Date startT = DateHelper.getDateTime(startTime, "h:mma");
            Date endT = DateHelper.getDateTime(endTime, "h:mma");
            ScheduleTimeRoute route = new ScheduleTimeRoute(source, new PriceResolver(price, CountryBuilder.PH.getCurrency(), this),
                    target, createTransport(COMPANY), startT, endT);
            route.setFrequency(new DailyFrequency());
            routes.addRoute(route);
            i = i + 6;
            index++;
        }
        return routes;
    }

    /**
     * @return Доступные маршруты
     */
    private Map<String, String> getRouteMap() {
        Map<String, String> routes = new HashMap<>();
        String content = Http.get(url).responseBody();
        Document document = Jsoup.parse(content);
        Element element = document.select("select[name=route]").get(0);
        for (Element option : element.select("option")) {
            String id = option.attr("value");
            String name = option.text();
            routes.put(id, name);
        }
		/*
		<select name="route" onchange="javascript:toSched(this.value);" class="form_content">

										    <option value="74" selected="">BACOLOD-ILOILO
										    </option><option value="25">BATANGAS-CALAPAN
										    </option><option value="4">CALAPAN-BATANGAS
										    </option><option value="7">CEBU-ORMOC
										    </option><option value="68">CEBU-TAGBILARAN
										    </option><option value="75">ILOILO-BACOLOD
										    </option><option value="8">ORMOC-CEBU
										    </option><option value="69">TAGBILARAN-CEBU
                                        </option></select> */
        return routes;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

package org.humanhelper.travel.dataprovider.air.airasia;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.humanhelper.service.http.HttpGet;
import org.humanhelper.service.http.HttpPost;
import org.humanhelper.service.utils.StringHelper;
import org.humanhelper.travel.country.Country;
import org.humanhelper.travel.dataprovider.air.skysales.AbstractSkySalesDataProvider;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.type.transport.Airport;
import org.humanhelper.travel.price.PriceResolver;
import org.humanhelper.travel.route.type.FixedTimeRoute;
import org.humanhelper.travel.route.type.TimeRoute;
import org.humanhelper.travel.route.type.sourcetarget.SourceTargetRouteList;
import org.humanhelper.travel.transport.Transport;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * airasia.com
 * Спарсить расписание можно вот отсюда:
 * http://www.airasia.com/my/en/where-we-fly/flight-schedule.page
 *
 * @author Human Helper
 * @since 30.10.13
 */
//@Service
public class AirAsiaDataProvider extends AbstractSkySalesDataProvider {

    public AirAsiaDataProvider() {
        super("https://booking.airasia.com/");
    }

    @Override
    protected String getCompanyName() {
        return "AirAsia";
    }

    @Override
    public String getName() {
        return "airasia.com";
    }

    @Override
    protected boolean isPlaceProcessed(String placeCode) {
        //Места содержащие 1, 2, 3 - это сложные маршруты airasia включающие трансфер, напримре KhaoLak
        return !placeCode.contains("1") && !placeCode.contains("2") && !placeCode.contains("3");
    }

    public SourceTargetRouteList<TimeRoute> getWays(Airport source, Airport target, Date day) {
        Country country = source.getCountry();
        Currency currency = country.getCurrency();
        //Вначале парсим обычный сайт, так как он по http. Если есть какие-то ошибки, то пробуем по https
        try {
            String content = getFlightContent(source.getCode(), target.getCode(), getDate(day), currency.getCurrencyCode());
            Document document = Jsoup.parse(content);
            return parseContent(document, source, target, day, currency);
        } catch (Exception e) {
            log.warn("Error on parse airasia, try to parsing mobile", e);
            String content = getMobileFlightContent("https://mobile.airasia.com/en/search", source.getCode(), target.getCode(), getDate(day), currency);
            Document document = Jsoup.parse(content);
            return parseMobileContent(document, source, target, day, currency);
        }

    }


    private SourceTargetRouteList parseMobileContent(Document document, Place source, Place target, Date day, Currency currency) {
        SourceTargetRouteList result = new SourceTargetRouteList(source, target, this);
        Map<String, Float> flightMap = new HashMap<>();
        Elements fareList = document.select("div.farelist");
        for (Element element : fareList) {
            Elements elements = element.getAllElements();
            String flight = elements.get(1).text();
            String time = elements.get(2).text();
            String price = elements.get(4).getAllElements().get(0).getAllElements().get(0).text();
            price = StringHelper.getStringBetween(price, " ", " ").replace(",", "");
            Float priceF = getPrice(price);
            String key = flight + "_" + time;
            Float oldPrice = flightMap.get(key);
            if (oldPrice == null || oldPrice > priceF) {
                flightMap.put(key, priceF);
            }
        }
        for (String key : flightMap.keySet()) {
            Transport transport = createTransport(StringHelper.getStringBefore(key, "_"));
            //Parse time
            String timeString = StringHelper.getStringAfter(key, "_");
            String[] strings = timeString.split(" ");
            String time1 = strings[0].replace(":", "");
            String time2 = strings[1].replace(":", "");
            Date startTime = createTime(day, time1);
            Date endTime = createTime(day, time2);
            if (startTime.after(endTime)) {
                endTime = DateUtils.addDays(endTime, 1);
            }
            Float price = flightMap.get(key);
            FixedTimeRoute way = new FixedTimeRoute(source, new PriceResolver(price, currency, this),
                    target, transport, startTime, endTime);
            result.addRoute(way);
        }
        return result;
    }

    private SourceTargetRouteList parseContent(Document document, Place source, Place target, Date day, Currency currency) {
        SourceTargetRouteList result = new SourceTargetRouteList(source, target, this);
        boolean isError = true;
        Elements table = document.select("table.rgMasterTable");
        Elements trList = table.select("tr");
        for (Element tr : trList) {
            Element a = tr.select("a").first();
            String flightInfo = a.attr("title");
            if (StringHelper.isNotEmpty(flightInfo)) {
                isError = false;
            }
            //log.debug("FlightInfo:" + flightInfo);
            Elements priceSpanList = tr.select("span.price");
            if (!priceSpanList.isEmpty()) {
                //Пример flighInfo = FlightInfo:<b>FD 2790</b><br />0745 (DMK) - 0915 (SGN)<br>Estimated flight time: 1 hrs 30 Mins<br>Plane Type : 320A

                //Parse time
                String timeString = StringUtils.substringBetween(flightInfo, "<br />", "<br>");
                String[] strings = timeString.split(" ");
                String time1 = strings[0];
                String time2 = strings[3];
                Date startTime = createTime(day, time1);
                Date endTime = createTime(day, time2);
                if (startTime.after(endTime)) {
                    endTime = DateUtils.addDays(endTime, 1);
                }
                PriceResolver priceResolver = null;
                for (Element priceSpan : priceSpanList) {
                    String price = priceSpan.text();
                    //log.debug("Price:" + price);
                    //Пример price = 85.05 USD
                    String[] values = price.split(" ");
                    priceResolver = new PriceResolver(getPrice(values[0]), currency, this);
                    break;
                }
                FixedTimeRoute way = new FixedTimeRoute(source, priceResolver,
                        target, createTransport(StringUtils.substringBetween(flightInfo, "<b>", "</b>")), startTime, endTime);
                result.addRoute(way);
            }
        }
        if (isError) {
            throw new IllegalStateException("Error on parse flight from " + source + " to " + target + " in " + day + " with currency " + currency + " with content:" + document.toString());
        }
        return result;
    }

    /**
     * @return html билетов на дату date из origin в arrival
     */
    private String getFlightContent(String origin, String arrival, String date, String currency) {
        String url = String.format("http://booking11.airasia.com/Page/SkySalesRedirectHandler.aspx?IsReturn=false&OriginStation=%s&ArrivalStation=%s&DepartureDate=%s&ReturnDate=&NoAdult=1&NoChild=0&NoInfant=0&Currency=" + currency + "&Culture=en-GB&pc=&respURL=http://booking.airasia.com/",
                origin, arrival, date);
        return HttpGet.get(url).responseBody();
    }

    protected String getMobileFlightContent(String url, String origin, String arrival, String date, Currency currency) {
        String day = getDay(date);
        String yearMonth = getYearMonth(date);
        HttpPost post = createHttpPostWithRandomMobileUserAgent(url);
        post.form()
                //hash:822821e4e4cf909dcbe38a739b0bc06b
                .param("trip-type", "one-way")
                .param("origin", origin)
                .param("destination", arrival)
                .param("date-depart-d", day)
                .param("date-depart-my", yearMonth)
                .param("date-return-d", day)
                .param("date-return-my", yearMonth)
                .param("passenger-count", "1")
                .param("child-count", "0")
                .param("infant-count", "0")
                .param("currency", currency.getCurrencyCode())
                .param("depart-sellkey")
                .param("return-sellkey")
                .param("depart-details-index")
                .param("return-details-index")
                .param("depart-faretype")
                .param("action", "search")
                .param("btnSearch", "Search")

        ;

        return post.responseBody();
    }

}

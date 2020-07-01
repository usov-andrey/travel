package org.humanhelper.travel.dataprovider.air.tiger;

import org.humanhelper.service.http.Http;
import org.humanhelper.service.http.HttpGet;
import org.humanhelper.service.http.HttpPost;
import org.humanhelper.service.utils.DateHelper;
import org.humanhelper.service.utils.StringHelper;
import org.humanhelper.travel.country.Country;
import org.humanhelper.travel.dataprovider.air.skysales.AbstractSkySalesDataProvider;
import org.humanhelper.travel.dataprovider.air.skysales.model.AirportsData;
import org.humanhelper.travel.dataprovider.air.skysales.model.StationInfoList;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.type.transport.Airport;
import org.humanhelper.travel.price.PriceResolver;
import org.humanhelper.travel.route.type.FixedTimeRoute;
import org.humanhelper.travel.route.type.TimeRoute;
import org.humanhelper.travel.route.type.sourcetarget.SourceTargetRouteList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Currency;
import java.util.Date;

/**
 * http://www.tigerair.com/
 * Интересный вариант для Ajax данных:
 * curl 'http://booking.tigerair.com/AjaxFlightSearch.aspx?date=2014-07-13&segment=1'
 * <p>
 * Мобильный вариант:
 * https://m.tigerair.com/booking/select
 *
 * @author Андрей
 * @since 29.05.14
 */
//@Service
public class TigerAirDataProvider extends AbstractSkySalesDataProvider {

    public TigerAirDataProvider() {
        super("http://www.tigerair.com/sg/en/");
    }

    @Override
    protected String getCompanyName() {
        return "TigerAir";
    }

    @Override
    public String getName() {
        return "tigerair";
    }

    @Override
    public SourceTargetRouteList<TimeRoute> getWays(Airport source, Airport target, Date day) {
        Country country = source.getCountry();
        Currency currency = country.getCurrency();
        SourceTargetRouteList<TimeRoute> result = new SourceTargetRouteList<>(source, target, this);
        String content = getMobileFlightContent("https://m.tigerair.com/booking", "search", "select", source.getCode(), target.getCode(), getDate(day), currency);
        if (content.contains("no available flights") || content.contains("flyscoot.com")) {
            return result;
        }

        boolean isError = true;
        Document document = Jsoup.parse(content);
        Elements divList = document.select("div.withprice");
        for (Element div : divList) {
            isError = false;
            try {
                String text = div.text();
                if (!text.startsWith("sold out")) {
                    FixedTimeRoute way = readFlight(div.text(), source, target, day, currency);
                    result.addRoute(way);
                }
            } catch (Exception e) {
                throw new IllegalStateException("Error on parse flight from " + source + " to " + target + " in " + day + " with td content:", e);
            }
        }
        if (isError) {
            throw new IllegalStateException("Error on parse flight from " + source + " to " + target + " in " + day + " with content:" + content);
        }
        return result;
    }

    protected String getMobileFlightContent(String url, String searchContext, String selectContext, String origin, String arrival, String date, Currency currency) {
        HttpPost post = Http.post(url, searchContext);
        post.form()
                .param("roundtrip", "false")
                .param("departureStation", origin)
                .param("arrivalStation", arrival)
                .param("departureDate", date)
                .param("returnDate", "")
                .param("adults", "1")
                .param("children", "0")
                .param("infants", "0")
                .param("currency", currency.getCurrencyCode());

        //Делаем запрос, получаем кукисы в ответе
        post.exec();
        HttpGet get = Http.get(post, url, selectContext);
        //Теперь делаем запрос с полученными кукисами, только на другой адрес и запрос GET, а не Post
        return get.responseBody();
    }

    private FixedTimeRoute readFlight(String text, Place source, Place target, Date day, Currency currency) {
        //text = SGD 94.23 BKK 08:45 - SIN 12:10 flight: TR 2103      close flight details flight: TR 2103 date: Monday, April 13, 2015 departing: BKK 08:45 arriving: SIN 12:10 travel time: 3:25
        if (!text.startsWith(currency.getCurrencyCode())) {
            throw new IllegalStateException("Error on parse flight:" + text);
        }
        String[] values = text.split(" ");
        String sPrice = values[1];
        String startTime = values[3];
        String endTime = values[6];
        String flightNo = values[8] + values[9];
        String date = StringHelper.getStringBetween(text, "date:", "departing");// Monday, April 13, 2015
        date = StringHelper.getStringBetween(date, ",", ",");
        date = date.trim().split(" ")[1];
        //Получили номер дня
        day = DateHelper.setDay(day, Integer.parseInt(date));

        //Создаем маршрут
        FixedTimeRoute way = new FixedTimeRoute(source, new PriceResolver(getPrice(sPrice), currency, this),
                target, createTransport(flightNo), createTime(day, startTime), createTime(day, endTime));
        return way;
    }


    @Override
    protected AirportsData readAirportsData() {
        //У них на сайте все данные не обернуты в виде AirportsData, поэтому их нужно специально собирать
        String response = HttpGet.get(airportDataUrl).responseBody();
        StationInfoList stationInfoList = readData(response, StationInfoList.class, "stationInfo");
        TigerAirMarketInfoList marketInfoList = readData(response, TigerAirMarketInfoList.class, "marketInfo");
        TigerAirAirportsData data = new TigerAirAirportsData();
        data.setStationInfo(stationInfoList);
        data.setMarketInfoList(marketInfoList);
        return data;
    }

    private <T> T readData(String response, Class<T> tClass, String varName) {
        String value = StringHelper.getStringBetween(response, "var " + varName + "=", ";");
        return converterService.getJSONObject(value, tClass);
    }
}

package org.humanhelper.travel.dataprovider.air.skysales;

import org.humanhelper.service.conversion.ConverterService;
import org.humanhelper.service.http.Http;
import org.humanhelper.service.http.HttpGet;
import org.humanhelper.service.http.HttpPost;
import org.humanhelper.service.utils.StringHelper;
import org.humanhelper.service.utils.UserAgentHelper;
import org.humanhelper.travel.dataprovider.air.AirlineDataProvider;
import org.humanhelper.travel.dataprovider.air.skysales.model.AirportsData;
import org.humanhelper.travel.place.type.transport.Airport;
import org.humanhelper.travel.transport.Transport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * airasia и cebupacific, tigerairways используют одну и ту же модель
 *
 * @author Андрей
 * @since 19.05.14
 */
public abstract class AbstractSkySalesDataProvider extends AirlineDataProvider {

    @Autowired
    protected ConverterService converterService;

    protected String airportDataUrl;

    protected AbstractSkySalesDataProvider(String airportDataUrl) {
        this.airportDataUrl = airportDataUrl;
    }

    protected boolean isPlaceProcessed(String placeCode) {
        return true;
    }

    @Override
    public void updatePlaces() {
        AirportsData airportsData = readAirportsData();
        Set<String> airportCodes = airportsData.getAirportCodes();
        for (String sourceCode : airportCodes) {
            if (!isPlaceProcessed(sourceCode)) {
                continue;
            }
            List<Map<String, String>> maps = airportsData.getTargetList(sourceCode);
            if (maps != null) {
                Airport source = placeResolver.getAirport(sourceCode);
                for (Map<String, String> map : maps) {
                    String targetCode = map.get("code");
                    if (isPlaceProcessed(targetCode) && airportCodes.contains(targetCode)) {
                        try {
                            Airport target = placeResolver.getAirport(targetCode);
                            addOneWayRoute(source, target);
                        } catch (Exception e) {
                            throw new IllegalStateException("Error on processing airport:" + airportsData.getStationInfo().getByCode(targetCode), e);
                        }
                    }
                }
            }
        }
    }

    protected AirportsData readAirportsData() {
        String response = HttpGet.get(airportDataUrl).responseBody();
        String json = StringHelper.getStringBetween(response, "SKYSALES.Resource.init(", ");");
        AirportsData airportsData = converterService.getJSONObject(json, AirportsData.class);
        //Считываем ограничения
		/*
		"restrictedStationCategories":
        [

          'C'

        ]
      ,
		 */
        String restrictions = StringHelper.getStringBetween(response, "\"restrictedStationCategories\":", ",");
        if (StringHelper.isNotEmpty(restrictions)) {
            Set<String> r = converterService.getJSONObject(restrictions.replace("'", "\""), HashSet.class);
            airportsData.setRestrictedStationInfoCategories(r);
        }
        return airportsData;
    }

    protected String getFlightContent(String url, String selectContext, String origin, String arrival, String date) {
        String day = getDay(date);
        String yearMonth = getYearMonth(date);
        HttpPost post = Http.post(url, "Search.aspx");
        post.form().param("__EVENTTARGET", "ControlGroupSearchView$AvailabilitySearchInputSearchView$LinkButtonNewSearch")
                .param("__EVENTARGUMENT", "")
                .param("__VIEWSTATE", "")
                .param("ControlGroupSearchView$AvailabilitySearchInputSearchView$RadioButtonMarketStructure", "OneWay")
                .param("ControlGroupSearchView$AvailabilitySearchInputSearchView$TextBoxMarketOrigin1", origin)
                .param("ControlGroupSearchView$AvailabilitySearchInputSearchView$TextBoxMarketDestination1", arrival)
                .param("ControlGroupSearchView$AvailabilitySearchInputSearchView$TextBoxMarketOrigin2", "undefined")
                .param("ControlGroupSearchView$AvailabilitySearchInputSearchView$TextBoxMarketDestination2", "undefined")
                .param("ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketDay1", day)
                .param("ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketMonth1", yearMonth)
                .param("ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketDay2", day)
                .param("ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketMonth2", yearMonth)
                .param("ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListPassengerType_ADT", "1")
                .param("ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListPassengerType_CHD", "0")
                .param("ControlGroupSearchView$AvailabilitySearchInputSearchView$promoCodeID", "");

        //Делаем запрос, получаем кукисы в ответе
        post.exec();
        HttpGet get = Http.get(post, url, selectContext);
        //Теперь делаем запрос с полученными кукисами, только на другой адрес и запрос GET, а не Post
        return get.responseBody();
    }

    protected String getDay(String date) {
        return date.substring(date.lastIndexOf("-") + 1);
    }

    protected String getYearMonth(String date) {
        return date.substring(0, date.lastIndexOf("-"));
    }

    protected float getPrice(String value) {
        return Float.parseFloat(value.replace(",", ""));
    }

    protected HttpPost createHttpPostWithRandomMobileUserAgent(String url) {
        HttpPost post = Http.post(url);
        post.setUserAgent(UserAgentHelper.getRandomMobileUserAgent());
        return post;
    }

    abstract protected String getCompanyName();

    protected Transport createTransport(String name) {
        return new Transport(name, getCompanyName());
    }

}
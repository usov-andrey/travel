package org.humanhelper.travel.dataprovider.air.cebupacific;

import org.humanhelper.service.utils.StringHelper;
import org.humanhelper.travel.dataprovider.air.skysales.AbstractSkySalesDataProvider;
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
 * cebupacific.com
 * Цены возвращаются в местной валюте PHP
 * <p>
 * Страница выдачи весит 750 килобайт, возможно стоит подумать, как получать цену по другому, нужно исследовать мобильное приложение
 *
 * @author Human Helper
 * @since 13.11.13
 */
//@Service
public class CebuPacificDataProvider extends AbstractSkySalesDataProvider {

    private static final String URL = "https://book.cebupacificair.com";

    private static final Currency CURRENCY = Currency.getInstance("PHP");

    public CebuPacificDataProvider() {
        super(URL);
    }

    @Override
    protected String getCompanyName() {
        return "Cebu Pacific";
    }

    @Override
    public String getName() {
        return "cebupacific.com";
    }

    @Override
    public SourceTargetRouteList<TimeRoute> getWays(Airport source, Airport target, Date day) {
        SourceTargetRouteList<TimeRoute> result = new SourceTargetRouteList<>(source, target, this);
        String content = getFlightContent(URL, "Select.aspx", source.getCode(), target.getCode(), getDate(day));
        if (content.contains("Sorry, flights for this day are either sold-out or unavailable")) {
            return result;
        }
        boolean isError = true;
        Document document = Jsoup.parse(content);
        Element table = document.getElementById("availabilityTable");
        if (table != null) {
            Elements trList = table.select("tr");
            for (Element tr : trList) {
                isError = false;
                Elements tdList = tr.select("td");
                if (tdList.isEmpty()) {
                    //Пропускаем заголовки
                    continue;
                }
                //Считываем время перелета
                Element td = tdList.get(0);
                Date startTime = readTime(day, td);
                td = tdList.get(1);
                Date endTime = readTime(day, td);
                //Считываем рейс
                td = tdList.get(2);
                String planeType = td.select("strong").text();
                String planeNumber = td.select("span.flightInfoLink").text();
                String transport = planeType + " " + planeNumber;
                //Считываем Year Round Fares
                td = tdList.get(3);
                Float price = readPrice(td);
                if (price == null) {
                    throw new IllegalStateException("Error on parse flight from " + source + " to " + target + " in " + day + " with content td:" + td.toString());
                }
                //Считываем Promo Fares
                td = tdList.get(4);
                Float priceWithDiscount = readPrice(td);
                //Создаем маршрут
                FixedTimeRoute way = new FixedTimeRoute(source, new PriceResolver(priceWithDiscount != null ? priceWithDiscount : price, CURRENCY, this),
                        target, createTransport(transport), startTime, endTime);
                result.addRoute(way);
            }
        }
        if (isError) {
            throw new IllegalStateException("Error on parse flight from " + source + " to " + target + " in " + day + " with content:" + content);
        }
        return result;
    }

    private Date readTime(Date day, Element td) {
		/*
		<td class="alignleft"><b>0325 H 

              </b>Manila<br></td>
                <td class="alignleft"><b>0435 H 
              </b>Cebu<br></td>
		 */

        String value = td.text();
        //value = 0325 H Manila
        return createTime(day, value);
    }

    private Float readPrice(Element td) {
        String columnText = td.select("span.ADTprice").text();
        columnText = StringHelper.getStringBetween(columnText, "PHP", ".");
        String priceText = StringHelper.getOnlyNumbers(columnText);
        if (priceText.isEmpty()) {
            return null;
        }
        return Float.parseFloat(priceText);
    }


}

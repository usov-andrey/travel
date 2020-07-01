package org.humanhelper.travel.route.provider.air.id.EnTiket;

import com.fasterxml.jackson.databind.JsonNode;
import org.humanhelper.service.htmlparser.Img;
import org.humanhelper.service.htmlparser.Selector;
import org.humanhelper.service.http.Http;
import org.humanhelper.travel.country.CountryBuilder;
import org.humanhelper.travel.place.type.transport.Airport;
import org.humanhelper.travel.route.provider.air.AirportRouteProvider;
import org.humanhelper.travel.route.provider.air.AirportTargetMap;
import org.humanhelper.travel.route.type.TimeRoute;
import org.humanhelper.travel.route.type.builder.TimeRouteListBuilder;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * http://en.tiket.com/pesawat/cari?d=SUB&a=CGK&date=2015-11-23&ret_date=&flexDeparting=2015-10&adult=1&child=0&infant=0
 *
 * @author Андрей
 * @since 07.10.15
 */
public class TiketAirRouteProvider extends AirportRouteProvider {

    private static final String URL = "http://en.tiket.com/pesawat/cari?d=%s&a=%s&date=%s&ret_date=&flexDeparting=&adult=1&child=0&infant=0";
    private static final String AJAX_URL = "https://en.tiket.com/ajax/pingFlightSearch?d=%s&a=%s&date=%s&adult=1&child=0&infant=0&airlines=%s&subsidy=true";

    private AirportTargetMap targetMap = new AirportTargetMap(this::getPlaceResolver) {
        @Override
        protected void createTargets() {
            String json = Http.get("http://en.tiket.com/ajax/dropdown_list").responseBody();
            JsonNode node = converterService.getJSONObject(json, JsonNode.class);
            for (JsonNode airport : node.get("all_flight")) {
                String code = airport.get("airport_code").textValue();
                try {
                    addTarget(code);
                } catch (IllegalArgumentException e) {
                    log.warn("Not found airport:" + airport);
                }
            }
        }
    };

    @Override
    protected Set<Airport> getTargets(Airport airport) {
        return targetMap.get(airport);
    }

    private void fillRoutes(TimeRouteListBuilder builder, Airport source, Airport target, Date day) {
        Http request = Http.get(url(URL, source, target, day));
        processHtml(request,
                html -> {
                    //находим все tr для tbody id=tbody_depart
                    html.one(Selector.id("tbody_depart")).children().process(tag -> {
                        //извлекаем из аттрибута tr цену
                        builder.route(source, target).price(tag.attr("data-price"));
                        tag.children().process(
                                //обрабатываем первые четыре td в каждом tr
                                td1 -> builder.company(td1.first(Img.selector()).title()),
                                td2 -> builder.transport(td2.text()),
                                td3 -> builder.startHHmm(td3.one(Selector.tag("h4")).text()),
                                td4 -> builder.endHHmm(td4.one(Selector.tag("h4")).text())
                        );
                    });

                    //Сайт выдает информацию в виде html, если она у него в кэше
                    //Если в кэше нет, то нужно сделать ajax запросы
                    String json = html.html("'search_result' : {\"depart\":", ",\"return\"");
                    //{"need_crawl":true,"airlines_need":{"KUL_KLO":["TIGER","AIRASIA"]},"airlinesCB":[],"airports":{"KUL":"Kuala Lumpur","KLO":"Kalibo"},"from_airports":["KUL"],"to_airports":["KLO"]}
                    JsonNode node = converterService.getJSONNode(json);
                    if (node.get("need_crawl").asBoolean()) {
                        JsonNode airLines_need = node.get("airlines_need").get(source.getCode() + "_" + target.getCode());
                        for (JsonNode airline : airLines_need) {
                            String airlineName = airline.asText();
                            addRoutesWithAjax(builder, source, target, day, airlineName);
                        }
						/*
						if (airLines_need.size() > 0) {
							//Получаем заново html, чтобы удостовериться, что все данные были получены
							fillRoutes(builder, source, target, day);
						} */
                    }
                });
    }

    @Override
    public Set<TimeRoute> getRoutes(Airport source, Airport target, Date day) {
        TimeRouteListBuilder builder = TimeRouteListBuilder.build(CountryBuilder.ID.getCurrency(),
                createPriceAgent("en.tiket.com", URL));
        fillRoutes(builder, source, target, day);
        return builder.buildDay(day);
    }

    private void addRoutesWithAjax(TimeRouteListBuilder builder, Airport source, Airport target, Date day, String airlineName) {
        Http http = Http.get(url(AJAX_URL, source, target, day, "%5B%22" + airlineName + "%22%5D")).ajaxRequestHeader();
        List<EnTiket> json = converterService.getJSONArray(http.responseBody(), EnTiket.class);
        json.forEach(enTiket -> enTiket.getData().forEach(data ->
                builder.route(source, target).price(data.getPriceAdultIDR())
                        .company(data.getAirlinesRealName()).transport(data.getFlightNumber())
                        //Формат даты на входе "2015-12-18 14:45:00"
                        .startDateTime(data.getDepartureTime()).endDateTime(data.getArrivalTime())
        ));

    }


}

package org.humanhelper.travel.route.provider.air.cebu;

import org.humanhelper.service.http.HttpGet;
import org.humanhelper.service.utils.StringHelper;
import org.humanhelper.travel.dataprovider.air.skysales.model.AirportsData;
import org.humanhelper.travel.place.type.transport.Airport;
import org.humanhelper.travel.route.provider.air.AirlineRouteProvider;
import org.humanhelper.travel.route.provider.air.AirportTargetMap;
import org.humanhelper.travel.route.type.TimeRoute;
import org.humanhelper.travel.service.period.DatePeriod;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * cebupacificair.com
 *
 * @author Андрей
 * @since 05.10.15
 */
public class CebuPacificRouteProvider extends AirlineRouteProvider {

    private static final String URL = "https://book.cebupacificair.com";

    private AirportTargetMap targetMap = new AirportTargetMap(this::getPlaceResolver) {
        @Override
        protected void createTargets() {
            AirportsData airportsData = readAirportsData();
            Set<String> airportCodes = airportsData.getAirportCodes();
            for (String sourceCode : airportCodes) {
                if (!isAirportValid(sourceCode)) {
                    continue;
                }
                List<Map<String, String>> maps = airportsData.getTargetList(sourceCode);
                if (maps != null) {
                    for (Map<String, String> map : maps) {
                        String targetCode = map.get("code");
                        if (isAirportValid(targetCode) && airportCodes.contains(targetCode)) {
                            try {
                                addTarget(sourceCode, targetCode);
                            } catch (Exception e) {
                                throw new IllegalStateException("Error on processing airport:" + airportsData.getStationInfo().getByCode(targetCode), e);
                            }
                        }
                    }
                }
            }
        }
    };

    @Override
    public List<TimeRoute> getRoutes(Airport source, Airport target, DatePeriod period) {
        return null;  //TODO
    }

    @Override
    protected Set<Airport> getTargets(Airport airport) {
        return targetMap.get(airport);
    }

    private boolean isAirportValid(String code) {
        return !code.equals("Invalid");
    }

    protected AirportsData readAirportsData() {
        String response = HttpGet.get(URL).responseBody();
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
}

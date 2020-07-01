package org.humanhelper.travel.integration.travelpayouts;

import org.humanhelper.service.conversion.ConverterService;
import org.humanhelper.service.http.Http;
import org.humanhelper.service.utils.CollectionHelper;
import org.humanhelper.service.utils.StringHelper;
import org.humanhelper.travel.country.Country;
import org.humanhelper.travel.country.CountryList;
import org.humanhelper.travel.geo.Location;
import org.humanhelper.travel.place.type.region.Region;
import org.humanhelper.travel.place.type.region.RegionList;
import org.humanhelper.travel.place.type.transport.Airport;
import org.humanhelper.travel.place.type.transport.AirportList;
import org.humanhelper.travel.service.translation.Translations;
import org.humanhelper.travel.transport.air.Airline;
import org.humanhelper.travel.transport.air.AirlineList;
import org.humanhelper.travel.transport.air.Airplane;
import org.humanhelper.travel.transport.air.AirplaneList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Загружаем из файлов данные от системы travelpayouts.com
 * <p>
 * Используем данные с сайта http://api.travelpayouts.com/
 * <p>
 * Другие ссылки по api:
 * http://www.airport-data.com/api/ap_info.json?iata=CEB
 * http://www.gcmap.com/airport/
 *
 * @author Андрей
 * @since 08.01.15
 */
@Service
//@Profile(WebApplicationConfig.PROFILE)
@RequestMapping("tp")
public class TPDataProvider {

    private static final String API_URL = "http://api.travelpayouts.com/";
    private static final String DATA_URL = API_URL + "data/";
    private static final String COUNTRIES = "countries.json";
    private static final String CITIES = "cities.json";
    private static final String AIRPORTS = "airports.json";
    private static final String AIRPLANES = "planes.json";
    private static final String AIRLINES = "airlines.json";
    private static final String ROUTES = "routes.json";

    private static final Logger log = LoggerFactory.getLogger(TPDataProvider.class);

    @Autowired
    private ConverterService converterService;

    @RequestMapping(value = "countries", method = RequestMethod.GET)
    public
    @ResponseBody
    CountryList getCountries() {
        CountryList countries = new CountryList();

        List<TPCountry> tpCountries = readFromFile(COUNTRIES, TPCountryList.class);
        for (TPCountry tpCountry : tpCountries) {
            tpCountry.deleteDuplicates();

            countries.add(create(tpCountry));
        }
        return countries;
    }

    private Country create(TPCountry tpCountry) {
        Country country = new Country();
        country.setName(tpCountry.getName());
        country.setId(tpCountry.getCode());
        country.setCurrencyCode(tpCountry.getCurrency());
        country.createTranslations(tpCountry.getName_translations());
        return country;
    }

    @RequestMapping(value = "regions", method = RequestMethod.GET)
    public
    @ResponseBody
    RegionList getRegions() {
        RegionList regions = new RegionList();
        CountryList countries = getCountries();
        List<TPCity> tpCities = readFromFile(CITIES, TPCityList.class);
        for (TPCity tpCity : tpCities) {
            Country country = countries.getById(tpCity.getCountry_code());
            if (country == null) {
                throw new IllegalStateException("Not found country by code:" + tpCity.getCountry_code());
            }
            tpCity.deleteDuplicates();
            regions.add(create(tpCity, country));
        }
        return regions;
    }

    private Region create(TPCity tpCity, Country country) {
        Region city = new Region();
        city.setId(tpCity.getCode());
        city.setCountry(country);
        city.setName(tpCity.getName());
        if (tpCity.getCoordinates() != null) {
            Location location = new Location(tpCity.getCoordinates().getLat(), tpCity.getCoordinates().getLon());
            city.setLocation(location);
        }
        city.setTranslations(new Translations().set(tpCity.getName_translations()));
        return city;
    }


    @RequestMapping(value = "airports", method = RequestMethod.GET)
    public
    @ResponseBody
    AirportList getAirports() {
        AirportList airports = new AirportList();
        RegionList regions = getRegions();
        List<TPAirport> tpAirports = readFromFile(AIRPORTS, TPAirportList.class);
        for (TPAirport tpAirport : tpAirports) {
            Region region = regions.getById(tpAirport.getCity_code());
            if (region == null) {
                throw new IllegalStateException("Not found region by city code:" + tpAirport.getCity_code());
            }
            tpAirport.deleteDuplicates();
            Airport airport = create(tpAirport, region);
            if (airport.getLocation() != null) {
                airports.add(airport);
            }
        }
        return airports;
    }

    private Airport create(TPAirport tpAirport, Region city) {
        Airport airport = new Airport();
        airport.setCode(tpAirport.getCode());
        airport.setName(tpAirport.getName());
        airport.setRegion(city);
        airport.setCountry(city.getCountry());
        airport.setTranslations(new Translations().set(tpAirport.getName_translations()));
        if (tpAirport.getCoordinates() != null) {
            airport.setLocation(Location.latLng(tpAirport.getCoordinates().getLat(), tpAirport.getCoordinates().getLon()));
        } else {
            //log.warn("don't have coordinates:"+tpAirport);
        }
        airport.fillId();
        return airport;
    }

    @RequestMapping(value = "airplanes", method = RequestMethod.GET)
    public
    @ResponseBody
    AirplaneList getAirplaes() {
        List<Airplane> airplaneList = readFromFile(AIRPLANES, AirplaneList.class);
        AirplaneList airplanes = new AirplaneList();
        airplanes.addAll(airplaneList);
        return airplanes;
    }

    @RequestMapping(value = "airlines", method = RequestMethod.GET)
    public
    @ResponseBody
    AirlineList getAirlines() {
        List<TPAirline> airlineList = readFromFile(AIRLINES, TPAirlineList.class);
        AirlineList airlines = new AirlineList();
        for (TPAirline tpAirline : airlineList) {
            if (Boolean.TRUE.equals(tpAirline.getIs_active())) {
                Airline airline = new Airline();
                //String countryName = tpAirline.getCountry();//Там очень странное имя страны, не ее код
                airline.setName(tpAirline.getName());
                airline.setIcao(tpAirline.getIcao());
                airline.setIata(tpAirline.getIata());
                airline.setCallSign(tpAirline.getCallSign());
                airlines.add(airline);
            }
        }
        return airlines;
    }

    public Map<String, Airline> getAirlineMap() {
        Map<String, Airline> result = new HashMap<>();
        for (Airline airline : getAirlines()) {
            if (StringHelper.isNotEmpty(airline.getIata())) {
                result.put(airline.getIata(), airline);
            }
        }
        return result;
    }

    public List<TPRoute> getRoutesByAirport(List<TPRoute> routes, String airportCode) {
        List<TPRoute> result = new ArrayList<>();
        for (TPRoute route : routes) {
            if (route.getDeparture_airport_iata().equals(airportCode) || route.getArrival_airport_iata().equals(airportCode)) {
                result.add(route);
            }
        }
        return result;
    }

    /**
     * код аэропорта - список кодов аэропортов, куда можно попасть с привязкой через какие авиакомпании
     */
    public Map<String, Map<String, AirlineList>> getAirportRoutes() {
        Map<String, Map<String, AirlineList>> airportTargets = new HashMap<>();
        Map<String, Airline> airlineMap = getAirlineMap();
        List<TPRoute> routes = getRoutes();
        for (TPRoute route : routes) {
            String sourceAirportCode = route.getDeparture_airport_iata();
            String targetAirportCode = route.getArrival_airport_iata();
            String airlineCode = route.getAirline_iata();
            Map<String, AirlineList> targetMap = CollectionHelper.getHashMapOrCreate(airportTargets, sourceAirportCode);
            AirlineList airlines = targetMap.computeIfAbsent(targetAirportCode, s -> new AirlineList());
            Airline airline = airlineMap.get(airlineCode);
            if (airline != null) {//Авиакомпания может быть не активна
                airlines.add(airline);
            }
        }
        return airportTargets;
    }


    @RequestMapping(value = "routes", method = RequestMethod.GET)
    public
    @ResponseBody
    List<TPRoute> getRoutes() {
        List<TPRoute> routes = readFromFile(ROUTES, TPRouteList.class);
        return routes;
    }


    private <T> List<T> readFromURL(String fileName, Class<? extends ArrayList<T>> listClass) {
        return Http.get(DATA_URL + fileName).responseInputStream(listClass);
    }

    private <T> List<T> readFromFile(String fileName, Class<? extends List<T>> listClass) {
        log.debug("Load data from file:" + fileName);
        try (InputStream is = getClass().getResourceAsStream(fileName)) {
            return converterService.getJSONObject(is, listClass);
        } catch (Exception e) {
            throw new IllegalStateException("Error on file:" + fileName, e);
        }
    }

}

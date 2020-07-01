package org.humanhelper.travel.place.createdb;

import org.humanhelper.service.utils.CollectionHelper;
import org.humanhelper.travel.country.Country;
import org.humanhelper.travel.country.CountryService;
import org.humanhelper.travel.integration.booking.BookingDataProvider;
import org.humanhelper.travel.integration.travelpayouts.TPDataProvider;
import org.humanhelper.travel.integration.travelpayouts.TPRoute;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.PlaceList;
import org.humanhelper.travel.place.PlaceService;
import org.humanhelper.travel.place.type.region.Region;
import org.humanhelper.travel.place.type.transport.Airport;
import org.humanhelper.travel.place.type.transport.AirportList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Создает все места в базе
 *
 * @author Андрей
 * @since 08.01.16
 */
@Service
public class PlaceDaoCreator {

    private static final Logger log = LoggerFactory.getLogger(PlaceDaoCreator.class);

    private static Set<String> notSupportedAirportCodes = CollectionHelper.newHashSet(
			/*
			"AMV"//Amderma, Russia
			,"CKH"//Chokurdah, Russia
			,"CYX"//Russia
			,"DYR"//Анадырь, Russia
			,"GDZ"//геленджик Russia
			,"IAA"//Игарка Russia
			,"LDG"*/
    );
    //Некоторые регионы в tp называются по-другому нежели на booking, необходимо соответствие
    private static Map<String, String> tpRegionNameToBookingRegionName = new HashMap<String, String>() {{
        put("St Martin", "Saint Martin");
        put("St Pierre", "Saint-Pierre");
        //put("Groznyj", "Groznyy");
        //put("", "Naberezhnyye Chelny");
    }};

    private static Set<String> notExistsCountriesOnBooking = CollectionHelper.newHashSet("Cuba", "Jersey", "Nauru",
            "Antarctica", "Yemen", "Iran", "Isle of Man", "Saint Helena", "South Georgia and the South Sandwich Islands",
            "Comoros", "Sudan", "French Southern Territories", "South Sudan", "United States Minor Outlying Islands",
            "Tokelau", "Somalia", "British Indian Ocean Territory", "Svalbard and Jan Mayen", "Pitcairn", "Aland Islands",
            "Vatican", "Heard Island and McDonald Islands", "Syria", "Western Sahara", "Turkmenistan", "Guernsey",
            "Bouvet Island", "Christmas Island", "Afghanistan", "North Korea", "Liberia");

    @Autowired
    private BookingDataProvider bookingDataProvider;
    @Autowired
    private TPDataProvider tpDataProvider;
    @Autowired
    private CountryService countryService;
    @Autowired
    private PlaceService placeService;

    //Кэшируем данные, чтобы не делать лишние запросы
    private AirportList airports;
    private List<TPRoute> routes;

    public void createPlacesInDao() {
        placeService.clear();
        Collection<Country> countries = countryService.getCountries();
		/*
		List<Throwable> exceptions = new ArrayList<>();
		for (Country country : countries) {
			try {
				createPlaces(country);
			} catch (Throwable e) {
				exceptions.add(e);
			}
		}
		for (Throwable e : exceptions) {
			e.printStackTrace();
		}*/
        //Вначале создаем места в базе
        countries.forEach(this::createPlaces);
        //Для каждого места необходимо заполнить targets

        //Очищаем кэш
        airports = null;
        routes = null;
    }

    private void createPlaces(Country country) {
        PlaceList places = getPlaces(country);
        for (Place place : places) {
            placeService.add(place);
        }
    }

    private AirportList getAirports() {
        if (airports == null) {
            airports = tpDataProvider.getAirports();
        }
        return airports;
    }

    private List<TPRoute> getRoutes() {
        if (routes == null) {
            routes = tpDataProvider.getRoutes();
        }
        return routes;
    }

    /**
     * Загружаем данные из booking и tp и обьединяем их
     */
    private PlaceList getPlaces(Country country) {
        PlaceList result = new PlaceList();
        //Кубы нет на booking
        PlaceList places = notExistsCountriesOnBooking.contains(country.getName()) ? new PlaceList() : bookingDataProvider.getPlaces(country);
        AirportList airports = getAirports();
        //Добавляем в результат все кроме аэропортов без регионов(аэропорт с регионом будет внутри childs региона
        result.addAll(places.stream().filter(place -> !Airport.isAirport(place)).collect(Collectors.toList()));
        //От booking к нам приходит также информация об аэропортах(возможно там более качественный перевод)
        //Вначале дополняем эту информацию информацией от tp
        fillPlacesAirports(result, airports);
        //Теперь нам нужно добавить оставшиеся аэропорты в result
        addTpAirports(country, result, airports, getRoutes());
        return result;
    }

    private void addTpAirports(Country country, PlaceList places, AirportList airports, List<TPRoute> allRoutes) {
        for (Airport airport : airports) {
            if (airport.getCountry().equals(country)) {
                Region region = airport.getRegion();
                Region bookingRegion = getBookingRegion(places, region.getName());
                if (bookingRegion == null) {
                    //В tp есть аэропорт, в booking для него мы не нашли регион
                    List<TPRoute> routes = tpDataProvider.getRoutesByAirport(allRoutes, airport.getCode());
                    if (routes.isEmpty()) {
                        //Так как маршрутов из этого аэропорта никуда нет, то и добавлять к нам в базу его не будем
                        continue;
                    }
                    if (region.getLocation() != null) {
                        //Маршруты в этот аэропорт в tp есть, возможно регион аэропорта в booking называется по-другому, ищем
                        Place similarPlace = places.findWithChilds(place -> Region.isRegion(place) && place.isSimilar(region));
                        if (similarPlace != null) {
                            linkAirport(airport, Region.get(similarPlace));
                            continue;
                        }
                        //Региона с похожим названием и местоположением не нашли
                        //Возможно нужно добавить это место в качестве дочернего в какой-то регион
                        Region parentRegion = places.getParentRegionByBBox(region);
                        if (parentRegion != null) {
                            //Нашли в какую группу регионов добавить текущий из tp, добавляем
                            parentRegion.linkRegion(region);
                            continue;
                        }
                        //Такого места в booking нет, добавляем его в корневой список мест
                        places.add(region);
                    } else {
                        throw new IllegalStateException("Not found in booking places tpRegion with null location:" + region + " but have routes:" + routes);
                    }
                } else {
                    //Регион нашли, нужно добавить в него аэропорт(аэропорт добавится как child в текущий регион)
                    linkAirport(airport, bookingRegion);
                }
            }
        }
    }

    private void linkAirport(Airport airport, Region region) {
        airport.linkRegion(region);
        //TODO нужно добавить обьединение переводов из airport.getRegion в region
    }

    private Region getBookingRegion(PlaceList places, String regionName) {
        Region region = places.findRegionByName(regionName);
        if (region == null) {
            String bookingRegionName = tpRegionNameToBookingRegionName.get(regionName);
            if (bookingRegionName != null) {
                region = places.findRegionByName(bookingRegionName);
            }
        }
        return region;
    }
	/*
	Старый код, возможно он и правильный

	//Нам нужно обработать все дерево полученных мест начиная с самого родительского
		Place rootPlace = place.rootPlace();
		//В регионы проставляем аэропорты
		fillRegionWithTransportPlaces(rootPlace);

	private void fillRegionWithTransportPlaces(Place place) {
		if (!Region.isRegion(place)) return;
		Region region = Region.get(place);
		for (Place child : region.getChilds()) {
			fillRegionWithTransportPlaces(child);
		}
		fillAirports(region);
	}

	private void fillAirports(Region region) {
		for (Airport airport : airports.get().values()) {
			Airport childAirport = Airport.getAirportInRegion(region, airport.getCode());
			if (childAirport != null) {
				//Удаляем аэропорт из booking
				if (!region.removeChild(childAirport)) {
					throw new IllegalStateException("Can't find " + childAirport + " in region:" + region);
				}
				airport.linkRegion(region);
			} else if (airport.getRegion().getName().equals(region.getName())) {
				airport.linkRegion(region);
			}
		}
	} */

    private void fillPlacesAirports(Collection<Place> places, AirportList airports) {
        //Так как в дальнейшем поддерживается удаление, то итерироваться будем по новой коллекции
        Collection<Place> placesForIterate = new ArrayList<>();
        placesForIterate.addAll(places);
        for (Place place : placesForIterate) {
            if (Airport.isAirport(place)) {
                //Найденный аэропорт уже привязан к региону, так что нам нужно лишь дополнитить информацией из tp
                Airport bookingAirport = Airport.get(place);
                Airport tpAirport = airports.getByCode(bookingAirport.getCode());
                if (tpAirport == null) {
                    //В tp аэропорт не найден, а значит никаких маршрутов из него мы не найдем
                    //удаляем этот аэропорт из региона
                    place.getRegion().removeChild(place);
                    //throw new IllegalStateException("Not found booking airport in tp:" + bookingAirport);
                } else {
                    //Добавляем слово Airport в имя получаемое из tp
                    String tpAirportName = tpAirport.getName();
                    if (!tpAirportName.endsWith(" Airport")) {
                        tpAirport.setName(tpAirportName + " Airport");
                    }
                    if (!bookingAirport.getName().equals(tpAirport.getName())) {
                        if (!bookingAirport.isNameSimilar(tpAirport)) { //если есть большое различие в названии аэропорта
                            log.warn("Airports info difference, booking:" + bookingAirport + " tp:" + tpAirport);
                        }
                    }
                    bookingAirport.setLocation(tpAirport.getLocation());
                    //Исключаем аэропорт из дальнейшего рассмотрения
                    airports.remove(tpAirport);
                }
            } else if (Region.isRegion(place)) {
                fillPlacesAirports(Region.get(place).getChilds(), airports);
            }
        }
    }

}

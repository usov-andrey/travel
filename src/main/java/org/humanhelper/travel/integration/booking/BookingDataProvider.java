package org.humanhelper.travel.integration.booking;

import org.apache.commons.io.FileUtils;
import org.humanhelper.service.conversion.ConverterService;
import org.humanhelper.service.htmlparser.ATag;
import org.humanhelper.service.htmlparser.Html;
import org.humanhelper.service.htmlparser.Selector;
import org.humanhelper.service.task.TaskRunner;
import org.humanhelper.travel.country.Country;
import org.humanhelper.travel.country.CountryList;
import org.humanhelper.travel.geo.Location;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.PlaceList;
import org.humanhelper.travel.place.type.transport.Airport;
import org.humanhelper.travel.service.translation.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Создание городов на основе booking.com
 *
 * @author Андрей
 * @since 16.01.15
 */
@Service
@RequestMapping("booking")
public class BookingDataProvider {

    private static final Logger log = LoggerFactory.getLogger(BookingDataProvider.class);
    private static final String SITE_URL = "http://www.booking.com";
    private static final String COUNTRIES_URL = SITE_URL + "/destination.%s.html";//Подставляется язык

    private static final String AUTO_COMPLETE_URL = SITE_URL + "/autocomplete?lang=%s&aid=304142&stype=1&force_ufi=&should_split=1&eb=0&add_themes=1&sort_nr_destinations=1&themes_match_start=1&include_synonyms=1&e_obj_labels=1&exclude_some_hotels=&term=%s";
    @Autowired
    private ConverterService converterService;
    @Autowired
    private TaskRunner taskRunner;

    public static void main(String[] values) {
        String value = "sd  df";
        System.out.println(value.split("\\s+").length);
        int[] ar = new int[3];
        ar[0] = 1;
        ar[1] = 2;
        ar[2] = 1;
        ar = Arrays.stream(ar).distinct().toArray();
        for (int el : ar) {
            System.out.println(el);
        }
    }
	/*
	private Collection<PlaceList> loadPlaces(Collection<Country> countries) {
		Collection<TaskCallable<PlaceList>> tasks = new ArrayList<>();
		for (Country country : countries) {
			BookingGetCountryPlacesTask task = new BookingGetCountryPlacesTask(country);
			tasks.add(task);
		}
		return taskRunner.call(tasks);
	}

	private Place getPlace(String name) {
		String lang = Language.en.getLocaleName();
		String url = String.format(AUTO_COMPLETE_URL, lang, name);
		BookingAutoCompleteResponse response = Http.get(url).responseInputStream(BookingAutoCompleteResponse.class);
		if (response.get__did_you_mean__() != null) {
			return null;
		}
		for (BookingAutoCompleteEntry city : response.getCity()) {
			if (city.getDest_type().equals("city") || city.getDest_type().equals("region")) {
				//String regionId = city.getDest_id();
				String cityName = city.getLabels().get(0).getText();
				Region region = null;//getFilePlaces().findRegionByName(cityName);
				if (region == null) {
					region = new Region();
					region.setName(cityName);
					region.setCountry(new Country().id(city.getCc1().toUpperCase()));
				}
				return region;
				//Что-то нифига нормального CITY_TOP_DESTINATIONS_URL не выдает
				//PlaceList regionChilds = getTopDestinations(CITY_TOP_DESTINATIONS_URL, regionId, lang);
			} else {
				log.error("Found place with another type:" + city);
			}

		}
		return null;
	}*/

    @RequestMapping(value = "countries", method = RequestMethod.GET)
    public
    @ResponseBody
    CountryList getCountries() {
        CountryList countries = new CountryList();
        for (Language language : Language.values()) {
            String lang = language.getLocaleName();
            String url = String.format(COUNTRIES_URL, lang);
            //Если не найден <div class="header-404"> (например для немецкого нет такой страницы)
            //Для блока class=flatListContainer найти все ссылки
            //<a href="/destination/country/xa.ru.html?sid=cd39502c0eb3fa8af2a3e745ca85094d;dcid=4">Абхазия</a>
            Html.get(url).one(Selector.css("flatListContainer")).list(ATag.selectorWithHref()).process(a -> {
                String id = a.hrefPart("country/", "." + lang).toUpperCase();
                Country country = countries.getByIdOrCreate(id);
                country.setName(language, a.text());
            });
        }
        return countries;
    }

    /**
     * Загружаем данные из локальных файлов
     */
    public PlaceList getPlaces(Country country) {
        PlaceList filePlaces = getPlacesFromFile(country);
        if (filePlaces.isEmpty()) {
            throw new IllegalStateException("File for country " + country + " is empty");
        }
        filePlaces.loadFromJson();
        //Нужно очистить id из данных, а то там и дубликаты бывают
        filePlaces.processWithChilds(Place::fillId);
        return removeDuplicates(filePlaces);
    }

    public void savePlacesToFile(Country country) {
        if (getFile(country).exists() && !getPlacesFromFile(country).isEmpty()) {
            log.debug("file already exists for country:" + country);
            return;
        }
        //Так как потом это все будем сохранять в файл, то в файле не нужна полная информация о стране, очищаем
        Country cleanCountry = new Country().id(country.getId());
        PlaceList places = loadPlaces(cleanCountry);

        PlaceList result = new PlaceList();
        places.saveForJson(result);
        result = removeNoLocation(result);
        result = removeDuplicates(result);
        converterService.objectToFile(getFile(country), result);
    }

    private PlaceList removeDuplicates(PlaceList places) {
		/*
		 На корневом уровне могут быть места, которые как дублируются на этом же корневом уровне, так и находятся внутри какого-то региона.
		 В случае, если дубликат внутри региона, то id у них будут разные.
		 Находим дубликаты по одинаковому location
		 В этом случае необходимо убрать это место из корневого списка. Алгоритм убирания:
		 1. Находим такие дубликаты(при поиске, если мы нашли дубликат, то рассматривать его дочерние элементы не стоит,
		 так как там тоже будут дубликаты
		 2. Формируем корневой список мест без этих дубликаток
		 Также в данных могут быть на корневом уровне дубликаты, поэтому при добавлении элементов в результирующий корневой список
		 мы проверяем, чтобы там уже такого элемента на первом уровне не было
		 */

        //В случае такого дублирования, у мест на корневом уровне нету childs
        Map<Location, Place> uniqueLocations = new HashMap<>();
        Map<Location, Place> dupLocations = new HashMap<>();
        places.processWithChilds(new Consumer<Place>() {
            @Override
            public void accept(Place place) {
                if (!Airport.isAirport(place)) {//От booking поступает информация об аэропортах без координат
                    Location location = place.getLocation();
                    if (location == null) {
                        throw new IllegalStateException("Place have null location:" + place);
                    }
                    if (uniqueLocations.containsKey(location)) {
                        dupLocations.put(location, place);
                    } else {
                        uniqueLocations.put(location, place);
                    }
                }
            }
        }, new Predicate<Place>() {
            @Override
            public boolean test(Place place) {
                return !Airport.isAirport(place) && !dupLocations.containsKey(place.getLocation());
            }
        });
        PlaceList result = new PlaceList();
        for (Place place : places) {
            if (dupLocations.containsKey(place.getLocation())) {
                dupLocations.remove(place.getLocation());
            } else {
                if (!result.contains(place)) {
                    result.add(place);
                } else {
                    log.warn("Duplicate on root level:" + place);
                }
            }
        }
        if (!dupLocations.isEmpty()) {
            throw new IllegalStateException("dupLocations is not empty:" + dupLocations);
        }
        return result;
    }

    private PlaceList removeNoLocation(PlaceList places) {
        return places.stream().filter(place -> place.getLocation() != null).collect(Collectors.toCollection(PlaceList::new));
    }

    protected PlaceList loadPlaces(Country country) {
        log.debug("Get places by country:" + country + " with id:" + country.getId());
        List<BookingLink> links = taskRunner.call(new BookingGetCountryLinksTask(country));
        PlaceList places = new PlaceList();
        for (BookingLink link : links) {
            if (link.getPlace() != null) {
                places.add(link.getPlace());
            }
        }
        return places;
    }

    private File getFile(Country country) {
        return new File("booking" + File.separator + "places_" + country.getId() + ".json");
    }

    private PlaceList getPlacesFromFile(Country country) {
        File file = getFile(country);
        if (!file.exists()) {
            throw new IllegalStateException("json file " + file + " does not exist for country:" + country);
        }
        try (InputStream is = FileUtils.openInputStream(getFile(country))) {
            return converterService.getJSONObject(is, PlaceList.class);
        } catch (Exception e) {
            throw new IllegalStateException("Error on create object from json file:" + file, e);
        }
    }
}

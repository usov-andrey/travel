package org.humanhelper.travel.integration.booking;

import org.humanhelper.service.htmlparser.ATag;
import org.humanhelper.service.htmlparser.Html;
import org.humanhelper.service.htmlparser.Selector;
import org.humanhelper.service.http.Http;
import org.humanhelper.service.task.TaskRunnable;
import org.humanhelper.travel.geo.Location;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.type.region.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Андрей
 * @since 02.12.15
 */
public class BookingLinkFillTask implements TaskRunnable {

    private static final Logger log = LoggerFactory.getLogger(BookingLinkFillTask.class);

    private int index;
    private int count;
    private BookingLink link;
    private List<BookingLink> links;
    private String countryId;
    private String lang;

    public BookingLinkFillTask(int index, int count, BookingLink link, List<BookingLink> links, String countryId, String lang) {
        this.index = index;
        this.count = count;
        this.link = link;
        this.links = links;
        this.countryId = countryId;
        this.lang = lang;
    }

    private void doRun() {
        Place place = link.getPlace();
        Html html = getHtmlWithAttempts(BookingGetCountryLinksTask.SITE_URL + link.getUrl(), 3);
        //Иногда booking выдает неправильные ссылки:
        if (html.html().contains("<body id=\"b2error404Page\"")) {
            //Иногда в списке ссылок какой-то страны есть дубликаты мест и первая ссылка неправильная,
            //а вторая правильная. При этом у них в точности такое же название
            log.warn("Page not found on process place:" + place);
            link.setPlace(null);
            return;
        }

        //По хлебным крошкам вычисляем родителя
        //Находим div с id=breadcrumb, находим послледнюю ссылку в этом div и если там ссылка
        ATag a = html.one(Selector.id("breadcrumb")).last(ATag.selectorWithHref());
        BookingLink parentLink = new BookingLink(a, countryId, lang);
        if (!parentLink.isCountry()) {
            //Проставляем родителя
            String regionName = parentLink.getName();
            if (regionName != null && !place.equalsNameWithTranslations(regionName)) {
                Region region = //Для аэропорта ищем среди городов, для городов среди регионов
                        link.isAirport() ? getCity(regionName) : getRegion(regionName);
                if (region == null) {
                    //Для аэропорта бывают родительские города, в которых нет отелей
                    //В этом случае в link такого города нет, исключаем тогда этот аэропорт из списка мест
                    //Также бывают в базе лапы типо, что город относится к одной стране, но входит в регион из другой страны
                    //Например, город во франции
                    //http://www.booking.com/city/fr/bois-damont.en-gb.html?label=gen173nr-15EhJkZXN0aW5hdGlvbmNvdW50cnkoTUICZnJILmIFbm9yZWZyBXVzX3ZhiAEBmAExuAEEyAEE2AED6AEB;sid=c58ea63dd96806177a6417f57a640084;dcid=1;inac=0&
                    //А родительский регион в швейцарии
                    //http://www.booking.com/region/ch/vaud-canton.en-gb.html?label=gen173nr-15EhJkZXN0aW5hdGlvbmNvdW50cnkoTUICZnJILmIFbm9yZWZyBXVzX3ZhiAEBmAExuAEEyAEE2AED6AEB;sid=c58ea63dd96806177a6417f57a640084;dcid=1
                    //Для таких случаев мы исключаем это место из расмотрения
                    link.setPlace(null);
                    return;
                } else {
                    place.linkRegion(region);
                }
            }
        }

        //Нужно проставить для этого места координаты, если это не аэропорт
        //Для аэропортов берем координаты от tp
        if (!link.isAirport()) {
            //b_dest_id: '-2670474',
            String bookingPlaceId = html.html("b_dest_id: '", "'");
            String url = String.format(BookingGetCountryLinksTask.CITY_TOP_DESTINATIONS_URL, bookingPlaceId, lang);
            BookingCities cities = Http.get(url).responseInputStream(BookingCities.class);
            for (BookingCity city : cities) {
                if (city.getUfi().equals(bookingPlaceId)) {
                    place.setLocation(Location.latLng(city.getLat(), city.getLon()));
                    if (place instanceof Region) {
                        Region.get(place).setHotels(city.getHotelsCount());
                    }
                }
            }
            if (place.getLocation() == null) {
                throw new IllegalStateException(index + "-Error on find location for " + place.withTranslations() + " in cities:" + cities);
            }
        }
    }

    @Override
    public void run() {
        log.debug("Process " + (index++) + " of " + count + ":" + link.getPlace());
        try {
            doRun();
        } catch (Exception e) {
            throw new IllegalStateException("Error on process " + (index++) + " of " + count + ":" + link.getPlace() + " with url:" + link.getUrl(), e);
        }
    }

    private Region getRegion(String regionName) {
        for (BookingLink link : links) {
            if (link.isRegion()) {
                Region region = Region.get(link.getPlace());
                if (region.equalsNameWithTranslations(regionName)) {
                    return region;
                }
            }
        }
        return null;
    }

    private Region getCity(String regionName) {
        for (BookingLink link : links) {
            if (!link.isRegion() && link.getPlace() != null) {
                if (link.getPlace().equalsNameWithTranslations(regionName)) {
                    return Region.get(link.getPlace());
                }
            }
        }
        return null;
    }


    /**
     * На вход приходит link или аэропорт или город, необходимо определить родителя и координаты
     */
	/*
	private PlaceList getPlacesOfMap(BookingLink link, Holder<String> cityNameHolder, String countryId, String lang) {
		Html html = getHtmlWithAttempts(BookingGetCountryPlacesTask.SITE_URL + link.getUrl(), 1);
		//Иногда booking выдает неправильные ссылки:
		if (html.html().contains("Page not found")) {
			return new PlaceList();
		}
		//По хлебным крошкам вычисляем родителя
		//Находим div с id=breadcrumb, находим послледнюю ссылку в этом div и если там ссылка
		ATag a = html.one(Selector.id("breadcrumb")).last(ATag.selectorWithHref());
		BookingLink parentLink = new BookingLink(a, countryId);
		if (!parentLink.isCountry()) {
			cityNameHolder.set(parentLink.getName());
		}
		//Для аэропорта делаем запрос на markersOnMap
		if (link.isAirport()) {
			//<div id="b_map_container" data-bbox="104.598637289059,-2.99982356613519,104.803362710941,-2.79573643386481" class="b_map_inline b_map_no_padding_lp b_map_lp_non_property_markers map_iw_compact   free_spacing">
			Tag mapContainer = html.one(Selector.id("b_map_container"));
			String bBox = mapContainer.attr("data-bbox");//Формат в обратном порядке lng, Lat
			if (StringHelper.isEmpty(bBox)) {
				//Если не задан аттрибут data-bbox, то задан аттрибут data-latlng
				String lngLat = mapContainer.attr("data-latlng");//Только он идет в обратном порядке, например 115.3499984741211,-8.449999809265137
				bBox = BoundingBox.fromCenter(Location.lngLat(lngLat), 0.07726574f, 0.092185974f).latLng();
			}

			// Есть такой текст, по нему определяем ссылку
			// markersOnMapURL: '/markers_on_map_immutable?sid=cd39502c0eb3fa8af2a3e745ca85094d;dcid=1;aid=304142;dest_id=504;dest_type=airport;sr_id=;ref=airport;limit=50;stype=1;label=;lang=en-gb;get_hotel_details=0;cs=0;u=1;pgp=1;pd=1;dbc=1;b_group
			String markersOnMapUrl = html.html("markersOnMapURL: '", "b_group");
			if (StringHelper.isEmpty(markersOnMapUrl)) {
				throw new IllegalStateException("Not found markersOnMapURL in html:" + html);
			}
			markersOnMapUrl = markersOnMapUrl.replace("limit=50", "limit=250") + "BBOX=" + bBox + ";";
			MarkersOnMap markersOnMap = Http.get(BookingGetCountryPlacesTask.SITE_URL + markersOnMapUrl).responseInputStream(MarkersOnMap.class);
			//System.out.println(markersOnMap);
			PlaceList places = new PlaceList();
			createPlaces(Airport.class, markersOnMap.getAirports(), places);
			return places;
		} else { //City
			//b_dest_id: '-2670474',
			String bookingPlaceId = html.html("b_dest_id: '", "'");
			return getTopDestinations(BookingGetCountryPlacesTask.CITY_TOP_DESTINATIONS_URL, bookingPlaceId, lang);
		}
	}

	private void createPlaces(Class<? extends Place> placeClass, List<? extends BookingPlace> bookingPlaceList, PlaceList places) {
		if (bookingPlaceList == null) {
			return;
		}
		for (BookingPlace bookingPlace : bookingPlaceList) {
			Place place = Place.create(placeClass);
			if (bookingPlace.getName() != null) {
				place.setName(bookingPlace.getName().trim());
			} else {
				place.setName(((BookingCity) bookingPlace).getCityName().trim());
				((Region) place).setHotels(((BookingCity) bookingPlace).getHotelsCount());
			}
			place.setLocation(Location.latLng(bookingPlace.getLat(), bookingPlace.getLon()));
			places.add(place);
		}
	}

	private PlaceList getTopDestinations(String url, String bookingPlaceId, String lang) {
		PlaceList places = new PlaceList();
		url = String.format(url, bookingPlaceId, lang);
		BookingCities cities = Http.get(url).responseInputStream(BookingCities.class);
		createPlaces(Region.class, cities, places);
		return places;
	}*/
    private Html getHtmlWithAttempts(String url, int attemptsCount) {
        Html html = Html.get(url);
        if (html.html().contains("502 Bad Gateway") && attemptsCount > 0) {
            log.debug("Bag html, try again");
            return getHtmlWithAttempts(url, attemptsCount - 1);
        }
        return html;
    }

}

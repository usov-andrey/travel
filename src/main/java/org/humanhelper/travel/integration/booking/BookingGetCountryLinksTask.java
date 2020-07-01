package org.humanhelper.travel.integration.booking;

import org.humanhelper.service.htmlparser.ATag;
import org.humanhelper.service.htmlparser.Html;
import org.humanhelper.service.htmlparser.Selector;
import org.humanhelper.service.htmlparser.TagList;
import org.humanhelper.service.singleton.SingletonLocator;
import org.humanhelper.service.task.TaskCallable;
import org.humanhelper.service.task.TaskRunnable;
import org.humanhelper.service.task.TaskRunner;
import org.humanhelper.service.task.rmi.RemoteTaskRunner;
import org.humanhelper.travel.country.Country;
import org.humanhelper.travel.geo.BoundingBox;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.PlaceList;
import org.humanhelper.travel.place.type.region.Region;
import org.humanhelper.travel.place.type.transport.Airport;
import org.humanhelper.travel.service.translation.Language;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Андрей
 * @since 13.01.16
 */
public class BookingGetCountryLinksTask implements TaskCallable<List<BookingLink>>, Serializable {

    protected static final String SITE_URL = "http://www.booking.com";
    protected static final String COUNTRY_PLACES_URL = SITE_URL + "/destination/country/%s.%s.html";
    protected static final String CITY_TOP_DESTINATIONS_URL = SITE_URL + "/dsf/top_destinations_in_boundingbox?cities=%s;lang=%s;";
    private Country country;

    public BookingGetCountryLinksTask(Country country) {
        this.country = country;
    }

    @Override
    public List<BookingLink> call() throws Exception {
        PlaceList places = new PlaceList();
        List<BookingLink> links = new ArrayList<>();
        for (Language language : Language.values()) {
            fillPlaces(links, places, country, language);
        }
        //Заполняем location и связь регионов с дочерними
        fillLocations(links, country, Language.en);
        return links;
    }

    private void fillPlaces(List<BookingLink> links, PlaceList places, Country country, Language language) {
        String id = country.getId().toLowerCase();
        String lang = language.getLocaleName();
        String url = String.format(COUNTRY_PLACES_URL, id, lang);
        //Внутри div с классом destLists ищем таблицы, а в них ищем все ссылки
        TagList<ATag> aTags = Html.get(url).one(Selector.css("destLists"))
                .list(Selector.tagCss("table", "general")).list(ATag.selectorWithHref());
        aTags.process(a -> {
            //Примеры:
            //a.text() + " : " + a.href()
            //Undisan : /destination/city/id/undisan.en-gb.html?sid=5edc2daba0daeb9e89e8a9c770fb1a56;dcid=1
            //Ketapang Airport : /airport/id/ktg.en-gb.html?sid=5edc2daba0daeb9e89e8a9c770fb1a56;dcid=1
            //West Sumatra : /region/id/sumatra-barat.en-gb.html?sid=5edc2daba0daeb9e89e8a9c770fb1a56;dcid=1
            BookingLink link = new BookingLink(a, id, lang);
            if (link.isValid()) {
                if (language.isMain()) {

                    links.add(link);
                    Place place = link.isAirport() ? new Airport().code(link.getId()) : new Region();
                    place.setId(link.getId());
                    place.setName(language, link.getName());
                    place.setCountry(country);
                    places.add(place);//Добавляем место в список, после будем для него проставлять координаты, регион и дочерние регионы
                    link.setPlace(place);
                } else {
                    //Значит английское имя и локацию для этого места мы уже прочитали и это место есть уже в places
                    //Проставляем только перевод
                    Place place = places.getById(link.getId());
                    if (place != null) {//Регион, у которого нет городов мы не рассматриваем
                        place.setName(language, link.getName());
                    }
                }
            }
        });
    }

    private void fillLocations(List<BookingLink> links, Country country, Language language) {
        String countryId = country.getId().toLowerCase();
        String lang = language.getLocaleName();
        int count = links.size();
        int index = 1;
        int offset = 0;
        List<TaskRunnable> linkFillTasks = new ArrayList<>();
        for (BookingLink link : links) {
            //Для регионов координаты будут вычисляться позже, на основе добавленных в них дочерних городов
            if (link.isRegion()) {
                continue;
            }
            if (index >= offset) {
                linkFillTasks.add(new BookingLinkFillTask(index, count, link, links, countryId, lang));
            }
            index++;
        }
        //Выполняем задачи асинхронно, но не удаленно
        TaskRunner taskRunner = SingletonLocator.get(TaskRunner.class);
        if (taskRunner instanceof RemoteTaskRunner) {
            throw new IllegalStateException("Can't thread on remote task runner");
        }
        taskRunner.runAndWait(linkFillTasks);
        //Теперь нужно для регионов вычислять boundingBox и location - центр этого boundingBox
        //На booking много административных регионов, у которых нет дочерних городов, исключаем такие
        links.stream().filter(BookingLink::isRegion).forEach(link -> {
            Region region = Region.get(link.getPlace());
            BoundingBox boundingBox = new BoundingBox();
            region.getChilds().stream().filter(childPlace -> childPlace.getLocation() != null).forEach(childPlace -> {
                boundingBox.addLocation(childPlace.getLocation());
            });
            if (!boundingBox.empty()) {
                region.setbBox(boundingBox);
                region.setLocation(boundingBox.center());
            } else {
                if (region.getChilds().isEmpty()) {
                    //На booking много административных регионов, у которых нет дочерних городов, исключаем такие
                    link.setPlace(null);
                } else {
                    throw new IllegalStateException("Error on find location for " + region);
                }
            }
        });
    }

}

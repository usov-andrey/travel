package org.humanhelper.travel.integration.booking;

import org.humanhelper.service.htmlparser.ATag;
import org.humanhelper.service.utils.StringHelper;
import org.humanhelper.travel.place.Place;

import java.io.Serializable;

/**
 * Важно - на booking может быть город и регион с одинаковым id, например:
 * http://www.booking.com/region/ru/yaroslavl.en-gb.html?label=gen173nr-15CAooggJCAlhYSC5iBW5vcmVmaN0BiAEBmAEuuAEEyAEE2AED6AEB;sid=c58ea63dd96806177a6417f57a640084;dcid=1
 * http://www.booking.com/destination/city/ru/yaroslavl.en-gb.html?label=gen173nr-15CAooggJCAlhYSC5iBW5vcmVmaN0BiAEBmAEuuAEEyAEE2AED6AEB;sid=c58ea63dd96806177a6417f57a640084;dcid=1
 *
 * @author Андрей
 * @since 07.09.15
 */
public class BookingLink implements Serializable {

    private String id;
    private String url;
    private String name;
    private Place place;

    public BookingLink(ATag a, String countryId, String lang) {
        this.url = a.href().replace("destination/", "");
        String urlBeforeHtml = StringHelper.getStringBefore(url, ".html");
        if (!urlBeforeHtml.contains(".")) {
            //Добавляем в ссылку локаль
            url = urlBeforeHtml + "." + lang + ".html" + StringHelper.getStringAfter(url, ".html");
        }
        this.id = isCountry() ? a.hrefPart("/country/", ".") :
                //Иногда встречаются невалидные ссылки типа:
                //airport/cn/.en-gb.html?label=gen173nr-15CAooMUIKY291bnRyeV9jbkguYgVub3JlZmjdAYgBAZgBLrgBBMgBBNgBA-gBAQ;sid=d09e206ac264fa6e2fcead8653e42695;dcid=1
                //В этом случае id будет пустой и метод isValid вернет false
                (isRegion() ? "R." : "") +//Добавляем префикс R, для регионов(замечание в описании класса)
                        StringHelper.getStringBetween(a.href(), countryId + "/", ".");
        this.name = a.text().trim();//иногда встречаются пробелы в конце
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public boolean isAirport() {
        return url.contains("/airport/");
    }

    public boolean isRegion() {
        return url.contains("/region/");
    }

    public boolean isCountry() {
        return url.contains("/country/");
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public boolean isValid() {
        return !id.isEmpty();
    }

    @Override
    public String toString() {
        return "BookingLink{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", place=" + place +
                '}';
    }
}

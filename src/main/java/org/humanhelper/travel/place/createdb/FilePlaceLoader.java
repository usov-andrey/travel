package org.humanhelper.travel.place.createdb;

import org.humanhelper.service.conversion.ConverterService;
import org.humanhelper.travel.country.Country;
import org.humanhelper.travel.country.CountryList;
import org.humanhelper.travel.place.PlaceList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.InputStream;

/**
 * Загрузка информации и местах из файла places.json
 *
 * @author Андрей
 * @since 02.09.15
 */
@Service
@RequestMapping("filePlaces")
public class FilePlaceLoader {

    @Autowired
    private ConverterService converterService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public
    @ResponseBody
    CountryList getData() {
        CountryList countries = readFromFile("places.json", CountryList.class);
        countries.loadFromJson();
        return countries;
    }

    /**
     * @return Все места из файла в плоском списке
     */
    public PlaceList getPlaceList() {
        PlaceList places = new PlaceList();
        for (Country country : getData()) {
            country.getRegions().forEach(places::addWithChilds);
        }
        return places;
    }

    private <T> T readFromFile(String fileName, Class<T> objectClass) {
        try (InputStream is = getClass().getResourceAsStream(fileName)) {
            return converterService.getJSONObject(is, objectClass);
        } catch (Exception e) {
            throw new IllegalStateException("Error on file:" + fileName, e);
        }
    }


}

package org.humanhelper.travel.integration.rome2rio;

import org.humanhelper.travel.ApplicationTest;
import org.humanhelper.travel.integration.rome2rio.autocomplete.Rome2RioAutoCompleteService;
import org.humanhelper.travel.integration.rome2rio.autocomplete.Rome2RioPlace;
import org.humanhelper.travel.integration.rome2rio.autocomplete.Rome2RioPlaceKind;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class Rome2RioAutoCompleteServiceTest extends ApplicationTest {

    @Autowired
    private Rome2RioAutoCompleteService autoCompleteService;

    @Test
    public void testSearch() throws Exception {
        for (Rome2RioPlace place : autoCompleteService.search("DMK")) {
            if (place.getKind().equals(Rome2RioPlaceKind.airport) && place.getCode().equals("DMK")) {
                return;
            }
        }

        throw new IllegalStateException("Error on search in rome2rio");
    }
}
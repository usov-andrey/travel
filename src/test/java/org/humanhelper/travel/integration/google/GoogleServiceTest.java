package org.humanhelper.travel.integration.google;

import com.google.maps.model.GeocodingResult;
import org.humanhelper.travel.ApplicationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Андрей
 * @since 18.09.15
 */
@ActiveProfiles("memoryDao")
public class GoogleServiceTest extends ApplicationTest {

    @Autowired
    private GoogleService service;

    @Test
    public void test() {
        for (GeocodingResult result : service.geoCoding("Pulau Derawan")) {
            log.debug(result.formattedAddress + " location:" + result.geometry.location);
        }
    }

}